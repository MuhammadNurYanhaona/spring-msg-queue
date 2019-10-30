package msgque.domain;

import lombok.Data;
import msgque.domain.MessageType;
import java.util.Date;
import java.util.List;

@Data
public class Subscription {
	private int id;
	private String clientId;
	private List<MessageType> topicsOfInterest;

	public boolean isInterestedIn(MessageType type) {
		for (MessageType interest : topicsOfInterest) {
			if (interest == type) return true;
		}
		return false;
	}
}
