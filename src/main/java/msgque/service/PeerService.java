package msgque.service;

import msgque.Host;
import msgque.domain.Peer;
import msgque.domain.Message;
import msgque.domain.Subscription;
import msgque.registry.PeerRegistry;
import msgque.registry.SubscriptionRegistry;
import msgque.registry.MessageStore;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Component
@Scope("singleton")
public class PeerService {

	@Value("${peer.send.message.path}")
	private String peerMsgSendRequestPath;

	@Value("${peer.connect.path}")
	private String peerConnectionReqPath;

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

	public void addPeer(Peer peer) {
		peerRegistry.registerPeer(peer);
	}

	public void removePeer(Peer peer) {
		peerRegistry.removePeer(peer);
	}

	public void processMessage(Peer sender, Message message) {
		
		List<Peer> peersExcludingSender =  peerRegistry.getAllExcept(sender);
		for (Peer peer : peersExcludingSender) {
			sendMessage(peer, message);
		}

		List<Subscription> interestedSubscribers 
			= subscriptionRegistry.getSubscriptionswithInterestInMsg(message.getType());
		for (Subscription subscription : interestedSubscribers) {
			String subscriber =  subscription.getClientId();
			messageStore.bufferMessageForSubscriber(message, subscriber);
		}
	}

	private void sendMessage(Peer peer, Message message) {
		Peer me = new Peer();
		me.setUrl(host.getUrl());
		String peerLocation = peer.getUrl();
		String peerPath = peerLocation  + "/" + peerMsgSendRequestPath;
	}
}
