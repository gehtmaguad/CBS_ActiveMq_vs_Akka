package at.hoeselm.activemq.pi;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Creator {

	// variable definitions
	private final String messageBrokerUrl = "tcp://localhost:61616"; // URL of message broker
	private ActiveMQConnectionFactory connectionFactory; // factory / connection handler
	private Connection connection; // connection object
	private Session session; // session object
	private Destination destination; // destination object

	// initialization method, run before execution
	public void initialize() throws Exception {
		// create a ActiveMQConnection Factory instance
		connectionFactory = new ActiveMQConnectionFactory(messageBrokerUrl);
		// create connection to the message broker
		connection = connectionFactory.createConnection();
		// create a session based on the connection, with transaction handling
		// false and auto acknowledged
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// messages are send to the Work queue
		destination = session.createQueue("Queue.Work");
	}

	// termination method, run after execution
	public void terminate() throws Exception {
		// close an active connection
		if (connection != null) {
			connection.close();
		}
	}

	// execution method
	public void execute(int start_value, int end_value, int message_count) throws Exception {

		// create a message producer using the session object
		MessageProducer producer = session.createProducer(destination);

		// calculate the number of elements being calculated by each consumer
		int number_of_elements = end_value / message_count;

		// send messages
		for (int i = 0; i < message_count; ++i) {

			// create message
			WorkerMessage worker_message = new WorkerMessage(start_value, number_of_elements);
			ObjectMessage object_message = session.createObjectMessage(worker_message);
			object_message.setIntProperty("message_id", i);
			
			// send message
			producer.send(object_message);

			// calculate new start value
			start_value += number_of_elements;
			
			// printout info statement
			System.out.println("creator sent message number: " + i );
		}

		// close the message producer
		producer.close();
	}

	// main method
	public static void main(String[] args) {
		// create an instance of this class
		Creator creator = new Creator();
		// printout info statement
		System.out.println("creator component started.");

		// execution wrapped in try/catch block
		try {
			creator.initialize();
			creator.execute(0, 10000, 10);
			creator.terminate();
		// exception handling
		} catch (Exception e) {
			// printout statement
			System.out.println("exception occured: " + e.getMessage());
		}

		// printout statement
		System.out.println("creator component finished.");
	}

}
