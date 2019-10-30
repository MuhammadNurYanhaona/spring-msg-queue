package msgque.registry;

import msgque.domain.Subscription;
import msgque.domain.MessageType;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class SubscriptionRegistry {

	private Map<Integer, Subscription> subscriptions;

	public SubscriptionRegistry() {
		subscriptions = new HashMap<Integer, Subscription>();
	}

	public void addSubscription(Subscription subscription) {
		subscriptions.put(subscription.getId(), subscription);
	}

	public Subscription removeSubscription(int subscriptionId) {
		return subscriptions.remove(subscriptionId);
	}

	public List<Subscription> getSubscriptionswithInterestInMsg(MessageType type) {
		List<Subscription> filteredList = new ArrayList<Subscription>();
		for (Subscription subscriber : subscriptions.values()) {
			if (subscriber.isInterestedIn(type)) {
				filteredList.add(subscriber);
			}
		}
		return filteredList;
	}
}
