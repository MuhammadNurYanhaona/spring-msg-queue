package msgque.service;

import msgque.Host;
import msgque.domain.Message;
import msgque.domain.MessageType;
import msgque.domain.Subscription;
import msgque.registry.SubscriptionRegistry;
import msgque.registry.MessageStore;
import java.util.List;
import java.util.Date;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/* the ClientService contains supporting functions for all APIs that deal with clients */
@Component
@Scope("singleton")
public class ClientService {
	
	@Autowired
	private SubscriptionRegistry subscriptionRegistry;

	@Autowired
	private MessageStore messageStore;

	public Subscription createSubscription(String clientId, List<MessageType> interests) {

		if (interests == null || interests.isEmpty()) return null;

		Date currentTime = new Date();
		long dateValue = currentTime.getTime();
		String id = "dateValue-" + clientId;
		Subscription subscription = Subscription.builder()
			.id(id)
			.clientId(clientId)
			.topicsOfInterest(interests)
			.build();

		boolean success = subscriptionRegistry.addSubscription(subscription);
		if (!success) {
			return null;
		}
		return subscription;
	}

	public boolean removeSubscription(String subscriptionId) {
		return subscriptionRegistry.removeSubscription(subscriptionId) != null;
	}

	public List<Message> getUnreadMessages(String subscriptionId) {
		Subscription subscription = subscriptionRegistry.getSubscription(subscriptionId);
		if (subscription == null) {
			return null;
		}
		return messageStore.getPendingMessagesOfSubscriber(subscription.getClientId());
	}
}
