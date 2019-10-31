# spring-msg-queue
@author: Muhammad Nur Yanhaona
@email address: mny9md@virginia.edu

This is a fully functional distributed message publication/subscription service developed
using the Spring Framework. The service is made of a network of message queues that connect
with each other. Messages are published from various message sources such as blogs, 
newspapers, and websites to specific message queues that are of the "source" type. These 
front end queues propagate the messages to their peers. Who communicate the messages to
their peers and so on. This way messages are broadcasted to the entire network. Client
applications subscribe to one or more message queues and collect unread messages during
their comfortable time.

To build the project use the following command
	gradle build

To run a message queue in your local machine use the following command (only after you build 
the project :))
	java -jar build/libs/message-queue-boot-0.1.0.jar

To interact with some other message queue whose URL you know, check the admin APIs
	For connect:
		${peer_url}/msgqueue//admin/connect
	For disconnect:
		${peer_url}/msgqueue/admin/disconnect

 	

