package msgque.api;

import msgque.service.PeerService;
import msgque.comm.source.PublicationRequest;
import msgque.comm.CommonResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;


/* The SourceController contains the necessary APIs to support message publications from different sources
 * */
@RestController
public class SourceController {

	@Value("${message.source}")
	private boolean isSource;

	@Value("#{'${accepted.message.sources}'.split(',')}") 
	private List<String> supportedSources;

	@Autowired
	private PeerService peerService;

	// API endpoint for accepting published messages from different sources
	@RequestMapping(value = "${message.publication.path}", 
			method = RequestMethod.POST, 
			produces="application/json", consumes="application/json")
	public CommonResponse postMessage(@RequestBody PublicationRequest request) {

		// this message queue must be a source type for accepting publication requests from 
		// different sources 
		if (!isSource) {
			CommonResponse response =  CommonResponse.builder()
				.status(false)
				.failerReason("This message queue peer cannot be used for message publication.")
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

		peerService.processMsgPublication(request);

		CommonResponse response =  CommonResponse.builder()
			.status(true)
			.build();
		return response; 
	}	
}
