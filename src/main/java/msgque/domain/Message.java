package msgque.domain;

import lombok.Data;
import lombok.Builder;
import msgque.domain.MessageType;

@Data
@Builder
public class Message {
	private String source;
	private MessageType type;
	private String text;
	private String url;
}
