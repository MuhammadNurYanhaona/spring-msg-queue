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


/* The AdminController APIs are being used by another administrative application to setup and teardown
 * peer connections.
 * */
@RestController
public class AdminController {

	@Autowired
	private PeerService peerService;

	// API endpoint for requesting a connection establishment with another peer
	@RequestMapping(value = "${admin.connect.path}", 
			method = RequestMethod.POST, 
			produces="application/json", consumes="application/json")
	public CommonResponse connect(@RequestBody ConnectRequest request) {
		String peerUrl = request.getUrl();
		Peer peer =  Peer.builder().url(peerUrl).build();
		String errorMessage = peerService.connectWithPeer(peer);
		if (errorMessage != null) {
			CommonResponse response =  CommonResponse.builder()
				.status(false)
				.failerReason(errorMessage)
				.build();
			return response;
		}
		CommonResponse response =  CommonResponse.builder()
			.status(true)
			.build();
		return response;
	}

	// API endpoint for requesting a connection teardown with another peer
	@RequestMapping(value = "${admin.disconnect.path}", 
			method = RequestMethod.POST, 
			produces="application/json", consumes="application/json")
	public CommonResponse disconnect(@RequestBody DisconnectRequest request) {
		String peerUrl = request.getUrl();
		Peer peer =  Peer.builder().url(peerUrl).build();
	        String errorMessage = peerService.disconnectFromPeer(peer);	
		if (errorMessage == null) {
			CommonResponse response =  CommonResponse.builder()
				.status(false)
				.failerReason(errorMessage)
				.build();
			return response;
		}
		CommonResponse response =  CommonResponse.builder()
			.status(true)
			.build();
		return response;
	}
}
