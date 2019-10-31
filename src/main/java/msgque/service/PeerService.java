package msgque.service;

import msgque.Host;
import msgque.domain.Peer;
import msgque.domain.Message;
import msgque.domain.Subscription;
import msgque.comm.peer.ConnectRequest;
import msgque.comm.peer.DisconnectRequest;
import msgque.comm.peer.MessageRequest;
import msgque.comm.source.PublicationRequest;
import msgque.comm.CommonResponse;
import msgque.registry.PeerRegistry;
import msgque.registry.SubscriptionRegistry;
import msgque.registry.MessageStore;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/* This service class provides supporting functions for all peer related interactions
 * */
@Component
@Scope("singleton")
public class PeerService {

	@Value("${peer.send.message.path}")
	private String peerMsgSendRequestPath;

	@Value("${peer.connect.path}")
	private String peerConnectReqPath;

	@Value("${peer.disconnect.path}")
	private String peerDisconnectReqPath;

	@Autowired
	private Host host;

	@Autowired
	private PeerRegistry peerRegistry;

	@Autowired
	private SubscriptionRegistry subscriptionRegistry;

	@Autowired
	private MessageStore messageStore;

	public boolean addPeer(Peer peer) {
		Peer oldPeer = peerRegistry.registerPeer(peer);
		return oldPeer == null;
	}

	public boolean removePeer(Peer peer) {
		Peer removedPeer = peerRegistry.removePeer(peer);
		return removedPeer != null;
	}

	/* function for handling messages received from other message queue peers */
	public void processPeerMessage(MessageRequest msgRequest) {

		Peer sender =  msgRequest.getSender();
		Message message = msgRequest.getMessage(); 
		
		// propagate the message to all peer message queues
		List<Peer> peersExcludingSender =  peerRegistry.getAllExcept(sender);
		for (Peer peer : peersExcludingSender) {
			sendMessage(msgRequest, peer);
		}

		// buffer the message for all subscribers
		List<Subscription> interestedSubscribers 
			= subscriptionRegistry.getSubscriptionswithInterestInMsg(message.getType());
		for (Subscription subscription : interestedSubscribers) {
			String subscriber =  subscription.getClientId();
			messageStore.bufferMessageForSubscriber(message, subscriber);
		}
	}

	/* function for handling messages published by an information source */
	public void processMsgPublication(PublicationRequest msgRequest) {

		Message message = msgRequest.getMessage();

		// buffer the message for all subscribers
		List<Subscription> interestedSubscribers 
			= subscriptionRegistry.getSubscriptionswithInterestInMsg(message.getType());
		for (Subscription subscription : interestedSubscribers) {
			String subscriber =  subscription.getClientId();
			messageStore.bufferMessageForSubscriber(message, subscriber);
		}

		Peer me = Peer.builder().url(host.getUrl()).build();
		MessageRequest peerRequest = MessageRequest
				.builder()
				.sourceType(msgRequest.getSourceType())
				.sender(me)
				.message(message)
				.build();
			
		// propagate the message to all peer message queues
		Collection<Peer> peers =  peerRegistry.getPeerList();
		for (Peer peer : peers) {
			sendMessage(peerRequest, peer);
		}
	}

	public String connectWithPeer(Peer peer) {

		// setup the request object
		String myAddr = host.getUrl();
		ConnectRequest request = ConnectRequest.builder().url(myAddr).build();
		
		// setup the request headers
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        	requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		// send the HTTP request
		String peerLocation = peer.getUrl();
		String peerPath = peerLocation  + "/" + peerConnectReqPath;
		HttpEntity<ConnectRequest> requestEntity 
			= new HttpEntity<ConnectRequest>(request, requestHeaders);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<CommonResponse> responseEntity = restTemplate.exchange(peerPath, 
				HttpMethod.POST, requestEntity, CommonResponse.class);
		
		// process the response
		if(responseEntity.getStatusCode() == HttpStatus.OK) {
           		CommonResponse response = responseEntity.getBody();
			if (response.isStatus() == false) {
				return response.getFailerReason();
			}
			return null;
        	}
		return "Communication Error. Status Response: " + responseEntity.getStatusCode();
		
	}

	/* function for communicate with a peer that the current message queue wants to disconnect */
	public String disconnectFromPeer(Peer peer) {
		
		// setup the request object
		String myAddr = host.getUrl();
		DisconnectRequest request = DisconnectRequest.builder().url(myAddr).build();

		// setup the request headers
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        	requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		// send the HTTP request
		String peerLocation = peer.getUrl();
		String peerPath = peerLocation  + "/" + peerDisconnectReqPath;
		HttpEntity<DisconnectRequest> requestEntity 
			= new HttpEntity<DisconnectRequest>(request, requestHeaders);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<CommonResponse> responseEntity = restTemplate.exchange(peerPath, 
				HttpMethod.POST, requestEntity, CommonResponse.class);
		
		// process the response
		if(responseEntity.getStatusCode() == HttpStatus.OK) {
           		CommonResponse response = responseEntity.getBody();
			if (response.isStatus() == false) {
				return response.getFailerReason();
			}
			return null;
        	}
		return "Communication Error. Status Response: " + responseEntity.getStatusCode();
	}

	private boolean sendMessage(MessageRequest msgRequest, Peer receiver) {
		
		String peerLocation = receiver.getUrl();
		String peerPath = peerLocation  + "/" + peerMsgSendRequestPath;


		// setup the request object
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        	requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<MessageRequest> requestEntity 
			= new HttpEntity<MessageRequest>(msgRequest, requestHeaders);

		// send the HTTP request
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<CommonResponse> responseEntity = restTemplate.exchange(peerPath, 
				HttpMethod.POST, requestEntity, CommonResponse.class);

		// process the response
		if(responseEntity.getStatusCode() == HttpStatus.OK) {
           		CommonResponse response = responseEntity.getBody();
			if (response.isStatus() == false) {
				return false;
			}
			return true;
        	}
		return false;
	}
}
