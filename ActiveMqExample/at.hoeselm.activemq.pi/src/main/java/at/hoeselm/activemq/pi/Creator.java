package at.hoeselm.activemq.pi;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Creator implements Runnable {

	// variable definitions
	private final String messageBrokerUrl = "tcp://localhost:61616"; // URL of
																		// message
																		// broker
	private ActiveMQConnectionFactory connectionFactory; // factory / connection
															// handler
	private Connection connection; // connection object
	private Session session; // session object
	private Destination destination; // destination object
	private MessageProducer producer; // producer object

	private int start_value;
	private int end_value;
	private int message_count;

	public Creator(int start_value, int end_value, int message_count) throws Exception {

		// create a ActiveMQConnection Factory instance
		connectionFactory = new ActiveMQConnectionFactory(messageBrokerUrl);
		// create connection to the message broker
		connection = connectionFactory.createConnection();
		// create a session based on the connection, with transaction handling
		// false and auto acknowledged
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// messages are send to the Work queue
		destination = session.createQueue("Queue.Work");

		// create a message producer using the session object
		producer = session.createProducer(destination);

		this.start_value = start_value;
		this.end_value = end_value;
		this.message_count = message_count;
	}

	// termination method, run after execution
	protected void finalize() throws Exception {

		// close the message producer
		producer.close();

		// close an active connection
		if (connection != null) {
			connection.close();
		}
	}

	// execution method
	public void run() {

		try {

			// calculate the number of elements being calculated by each
			// consumer
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

			}
			
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
	}

}
