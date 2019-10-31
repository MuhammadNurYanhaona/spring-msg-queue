package msgque.api;

import msgque.domain.Peer;
import msgque.service.PeerService;
import msgque.comm.peer.ConnectRequest;
import msgque.comm.peer.DisconnectRequest;
import msgque.comm.peer.MessageRequest;
import msgque.comm.CommonResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;


/* The PeerController contains all the APIs for accepting communication from other message queue peers
 * */
@RestController
public class PeerController {

	@Value("${message.source}")
	private boolean isSource;

	@Value("#{'${accepted.message.sources}'.split(',')}") 
	private List<String> supportedSources;

	@Autowired
	private PeerService peerService;

	// API endpoint for accepting connection request from other peers
	@RequestMapping(value = "${peer.connect.path}", 
			method = RequestMethod.POST, 
			produces="application/json", consumes="application/json")
	public CommonResponse connect(@RequestBody ConnectRequest request) {
		String peerUrl = request.getUrl();
		Peer peer =  Peer.builder().url(peerUrl).build();
		boolean status = peerService.addPeer(peer);
		if (status == true) {
			CommonResponse response =  CommonResponse.builder()
				.status(status)
				.failerReason("The peer is already connected.")
				.build();
			return response;
		}
		CommonResponse response =  CommonResponse.builder()
			.status(status)
			.build();
		return response;
	}

	// API endpoint for accepting a disconnect request from other peers
	@RequestMapping(value = "${peer.disconnect.path}", 
			method = RequestMethod.POST, 
			produces="application/json", consumes="application/json")
	public CommonResponse disconnect(@RequestBody DisconnectRequest request) {
		String peerUrl = request.getUrl();
		Peer peer =  Peer.builder().url(peerUrl).build();
	        boolean status = peerService.removePeer(peer);	
		if (status == true) {
			CommonResponse response =  CommonResponse.builder()
				.status(status)
				.failerReason("The peer was not connected.")
				.build();
			return response;
		}
		CommonResponse response =  CommonResponse.builder()
			.status(status)
			.build();
		return response;
	}

	// API endpoint for accepting a message received from another peer
	@RequestMapping(value = "${peer.send.message.path}", 
			method = RequestMethod.POST, 
			produces="application/json", consumes="application/json")
	public CommonResponse postMessage(@RequestBody MessageRequest request) {

		// a message publication source queue does not accept messages from other peers
		if (isSource) {
			CommonResponse response =  CommonResponse.builder()
				.status(false)
				.failerReason("This message queue peer is a publication source.")
				.build();
			return response;
		}

		// if the source type does not match the accepted source types then do not accept this message
		if (supportedSources.contains(request.getSourceType())) {
			CommonResponse response =  CommonResponse.builder()
				.status(false)
				.failerReason("This message queue peer do not access messages from "
						+ request.getSourceType() + " sources.")
				.build();
			return response;
		}

		peerService.processPeerMessage(request);

		CommonResponse response =  CommonResponse.builder()
			.status(true)
			.build();
		return response; 
	}	
}
