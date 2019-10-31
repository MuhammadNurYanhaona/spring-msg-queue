package msgque.comm.source;

import msgque.domain.Message;
import lombok.Data;

@Data
public class PublicationRequest {
	private String sourceType;
	private Message message;
}
