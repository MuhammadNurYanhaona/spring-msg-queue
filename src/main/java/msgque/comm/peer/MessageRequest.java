package msgque.comm.peer;

import lombok.Data;
import lombok.Builder;
import msgque.domain.Peer;
import msgque.domain.Message;

@Data
@Builder
public class MessageRequest {

	private String sourceType;
	private Peer sender;
	private Message message;
}

