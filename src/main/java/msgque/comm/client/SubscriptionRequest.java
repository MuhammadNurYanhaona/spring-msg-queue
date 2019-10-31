package msgque.comm.client;

import java.util.List;
import lombok.Data;
import msgque.domain.MessageType;

@Data
public class SubscriptionRequest {
	
	private String clientId;
	private List<MessageType> topicsOfInterest;
}
