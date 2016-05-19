package at.hoeselm.activemq.pi;

import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class SumComponent {

	// variable definitions
	private final String messageBrokerUrl = "tcp://localhost:61616?jms.prefetchPolicy.queuePrefetch=1"; // URL
																										// of
																										// message
																										// broker
	private ActiveMQConnectionFactory connectionFactory; // factory / connection
															// handler
	private Connection connection; // connection object
	private Session session; // session object
	private Destination destinationSum; // destination object
	private int active_for_minutes = 60; // defines how long the consumer runs

	public void initialize() throws Exception {
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
	}

	// termination method, run after execution
	public void terminate() throws Exception {
		// close an active connection
		if (connection != null) {
			connection.close();
		}
	}

	// execution method
	public void execution() throws Exception {

		// create a message consumer using the session object
		MessageConsumer consumer = session.createConsumer(destinationSum);
		// create a listener
		consumer.setMessageListener(new SumListener());
		TimeUnit.MINUTES.sleep(active_for_minutes);
		connection.stop();
		// close the message producer
		consumer.close();
	}

	// main method
	public static void main(String[] args) {
		// create an instance of this class
		SumComponent sumcomponent = new SumComponent();
		// printout info statement
		System.out.println("sumcomponent component started.");

		// execution wrapped in try/catch block
		try {
			sumcomponent.initialize();
			sumcomponent.execution();
			sumcomponent.terminate();
			// exception handling
		} catch (Exception e) {
			// printout statement
			System.out.println("exception occured: " + e.getMessage());
		}

		// printout statement
		System.out.println("sumcomponent component finished.");
	}
}
