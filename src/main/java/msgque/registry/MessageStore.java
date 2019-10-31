package msgque.registry;

import msgque.domain.Message;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/* The MessageStore is a buffering facility for messages for all subscriber clients. Once
 * some messages have been delivered to a subscriber. His buffer is emptied. The model is
 * that a subscriber will only receives a single message once. In addition, he/she does not
 * receive old messages of his/her topic of interest that have been published before he/she
 * get the subscription. */
@Component
@Scope("singleton")
public class MessageStore {
	
	private Map<String, ArrayDeque<Message>> pendingMessageQueue;

	public MessageStore() {
		pendingMessageQueue =  new HashMap<String, ArrayDeque<Message>>();
	}

	public void bufferMessageForSubscriber(Message msg, String subscriber) {
		ArrayDeque<Message> pendingMessages =  pendingMessageQueue.get(subscriber);
		if (pendingMessages == null) {
			pendingMessages = new ArrayDeque<Message>();
		}
		pendingMessages.addFirst(msg);
		pendingMessageQueue.put(subscriber, pendingMessages);
	}

	public List<Message> getPendingMessagesOfSubscriber(String subscriber) {
		Queue<Message> pendingMessages =  pendingMessageQueue.get(subscriber);
		if (pendingMessages == null) {
			return new ArrayList<Message>();
		}	
		Message[] messages =  pendingMessages.toArray(new Message[]{});
		
		// clear the message buffer of the subscriber when sending him unread messages
		pendingMessages.clear();

		return Arrays.asList(messages);
	} 
}
