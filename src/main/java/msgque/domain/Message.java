package msgque.domain;

import lombok.Data;
import lombok.Builder;
import msgque.domain.MessageType;

@Data
@Builder
public class Message {
	// message source name
	private String source;

	// topic category of the message
	private MessageType type;

	// message content
	private String text;

	// a unique url to get to the original message
	// it can also be used as a globally unique message identifier
	private String url;
}
