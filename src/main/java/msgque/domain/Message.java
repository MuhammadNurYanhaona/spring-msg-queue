package msgque.domain;

import lombok.Data;
import msgque.domain.MessageType;

@Data
public class Message {
	private String source;
	private MessageType type;
	private String text;
}
