package at.hoeselm.activemq.pi;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class WorkerListener implements MessageListener {
	
	// variable definitions
	private final String messageBrokerUrl = "tcp://localhost:61616"; // URL of message broker
	private ActiveMQConnectionFactory connectionFactory; // factory / connection handler
	private Connection connection; // connection object
	private Session session; // session object
	private Destination destination; // destination object
	private MessageProducer producer;	

	public WorkerListener() throws Exception {
		
		// create a ActiveMQConnection Factory instance
		connectionFactory = new ActiveMQConnectionFactory(messageBrokerUrl);
		// create connection to the message broker
		connection = connectionFactory.createConnection();
		// create a session based on the connection, with transaction handling
		// false and auto acknowledged
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// messages are send to the Work queue
		destination = session.createQueue("Queue.Sum");
		
		// create a message producer using the session object
		producer = session.createProducer(destination);
	}
	
	protected void finalize () throws Exception {
		
		// close the message producer
		producer.close();
		
		// close an active connection
		if (connection != null) {
			connection.close();
		}
	}
	
    // message listener
    public void onMessage(Message message) {
        try {
         	// parse worker message and calculate result
        	ObjectMessage object_message = (ObjectMessage) message;
            WorkerMessage worker_message = (WorkerMessage) object_message.getObject();  
    		int message_id = object_message.getIntProperty("message_id");
            double sum = calculate_pie(worker_message);      
            
    		// create sum message
    		CollectorMessage collector_message = new CollectorMessage(sum);
    		ObjectMessage collector_object_message = session.createObjectMessage(collector_message);
    		collector_object_message.setIntProperty("message_id", message_id);

    		// send sum message
    		producer.send(collector_object_message);	
            
        } catch (Exception e) {
			// printout statement
			System.out.println("exception occured: " + e.getMessage());
		}
    }

    // calculate number pie
	private double calculate_pie(WorkerMessage worker_message) {
		
        int start_value = worker_message.get_start_value();
        int number_of_elements = worker_message.get_number_of_elements();
		double sum = 0.0;

		for(int i = start_value * number_of_elements;
			i<= ((start_value + 1) * number_of_elements - 1); i++ ) {
			
	    	// dividend: (1 - (i % 2) * 2) --> -1 for odd numbers and 1 for even numbers
	    	// divisor: (2 * i + 1)
	    	// result times 4 because we want pi and not pi/4
			sum += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1);
		}
		
		return sum;
	}
}