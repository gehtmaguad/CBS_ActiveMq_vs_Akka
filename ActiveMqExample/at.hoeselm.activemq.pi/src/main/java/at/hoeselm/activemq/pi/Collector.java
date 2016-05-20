package at.hoeselm.activemq.pi;

import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Collector implements Runnable {

	// variable definitions

	// URL of message broker
	private final String messageBrokerUrl = "tcp://localhost:61616?jms.prefetchPolicy.queuePrefetch=1";
	// factory / connection handler
	private ActiveMQConnectionFactory connectionFactory;
	private Connection connection; // connection object
	private Session session; // session object
	private Destination destinationSum; // destination object
	private int active_for_minutes = 60; // defines how long the consumer runs
	private MessageConsumer consumer; // consumer object
	private long message_count;
	private long start_time;

	public Collector(long start_time, long message_count) throws Exception {

		// create a ActiveMQConnection Factory instance
		connectionFactory = new ActiveMQConnectionFactory(messageBrokerUrl);
		// create connection to the message broker
		connection = connectionFactory.createConnection();
		connection.start();
		// create a session based on the connection, with transaction handling
		// false and auto acknowledged
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// queue definitions
		destinationSum = session.createQueue("Queue.Sum");

		// create a message consumer using the session object
		consumer = session.createConsumer(destinationSum);
		
		this.message_count = message_count;
		this.start_time = start_time;
	}

	// termination method, run after execution
	public void finalize() throws Exception {

		// close the message producer
		consumer.close();

		// close an active connection
		if (connection != null) {
			connection.close();
		}
	}

	// execution method
	public void run() {
		try {
			// create a listener
			consumer.setMessageListener(new CollectorListener(start_time, message_count));
			TimeUnit.MINUTES.sleep(active_for_minutes);
			connection.stop();
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}

	}

}
