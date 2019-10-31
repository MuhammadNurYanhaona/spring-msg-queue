package msgque.api;

import msgque.domain.Message;
import msgque.domain.MessageType;
import msgque.domain.Subscription;
import msgque.service.ClientService;
import msgque.comm.client.SubscriptionRequest;
import msgque.comm.client.SubscriptionResponse;
import msgque.comm.client.SubscriberRequest;
import msgque.comm.client.MessageCollectionResponse;
import msgque.comm.CommonResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;


/* The client controller has all the APIs for interacting with message subscriber clients 
 * */
@RestController
public class ClientController {

	@Autowired
	private ClientService clientService;

	// API endpoint for subscribing to message topics
	@RequestMapping(value = "${subscriber.subscribe.path}",
                        method = RequestMethod.POST,
                        produces="application/json", consumes="application/json")
	public SubscriptionResponse subscribe(@RequestBody SubscriptionRequest request) {
		String clientId =  request.getClientId();
		List<MessageType> topics =  request.getTopicsOfInterest();
		Subscription subscription = clientService.createSubscription(clientId, topics);
		if (subscription == null) {
			SubscriptionResponse response =  SubscriptionResponse
				.builder()
				.subscription(subscription)
				.status(true)
				.build();
			return response;
		}
		SubscriptionResponse response =  SubscriptionResponse
			.builder()
			.status(false)
			.failerReason("You have already subscribed.")
			.build();
		return response;
	}

	// API endpoint to cancel an existing subscription
	@RequestMapping(value = "${subscriber.unsubscribe.path}",
                        method = RequestMethod.POST,
                        produces="application/json", consumes="application/json")
	public CommonResponse unsubscribe(@RequestBody SubscriberRequest request) { 
		
		String subscriptionId =  request.getSubscriptionId();
		boolean status = clientService.removeSubscription(subscriptionId);
		if (status == false) {
			CommonResponse response = CommonResponse.builder()
				.status(false)
				.failerReason("You did not have a subscription.")
				.build();
			return response;
		}

		CommonResponse response = CommonResponse.builder()
			.status(true)
			.build();
		return response; 
	}

	// API endpoint clients use to periodically retrieve unread messages
	@RequestMapping(value = "${subscriber.collect.message.path}",
                        method = RequestMethod.GET,
                        produces="application/json", consumes="application/json")
	public MessageCollectionResponse getUnreadMessages(@RequestBody SubscriberRequest request) { 
		
		String subscriptionId =  request.getSubscriptionId();
		List<Message> messages = clientService.getUnreadMessages(subscriptionId);
		
		if (messages  == null) {
			MessageCollectionResponse response = MessageCollectionResponse
				.builder()
				.status(false)
				.failerReason("You did not have a subscription.")
				.build();
			return response;
		}

		MessageCollectionResponse response = MessageCollectionResponse.builder()
			.status(true)
			.messages(messages)
			.build();
		return response;
	}
}
