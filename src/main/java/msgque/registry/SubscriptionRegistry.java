package msgque.registry;

import msgque.domain.Subscription;
import msgque.domain.MessageType;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/* Registry class for keeping track of all active subscriptions */
@Component
@Scope("singleton")
public class SubscriptionRegistry {

	private Map<String, Subscription> subscriptions;

	public SubscriptionRegistry() {
		subscriptions = new HashMap<String, Subscription>();
	}

	public boolean addSubscription(Subscription subscription) {

		if (subscriptions.get(subscription.getId()) != null) {
			return false;
		}
		subscriptions.put(subscription.getId(), subscription);
		return true;
	}

	public Subscription removeSubscription(String subscriptionId) {
		return subscriptions.remove(subscriptionId);
	}

	public Subscription getSubscription(String subscriptionId) {
		return subscriptions.get(subscriptionId);
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
