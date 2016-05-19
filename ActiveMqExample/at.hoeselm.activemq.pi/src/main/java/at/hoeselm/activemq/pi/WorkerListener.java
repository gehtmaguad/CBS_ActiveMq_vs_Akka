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

	public WorkerListener() {
		try {
			// run initializer method
			this.initialize();
        } catch (Exception e) {
			// printout statement
			System.out.println("exception occured: " + e.getMessage());
		}
	}
	
	protected void finalize () {
		try {
			// run terminate function
			this.terminate();
        } catch (Exception e) {
			// printout statement
			System.out.println("exception occured: " + e.getMessage());
		}
	}
	
	// variable definitions
	private final String messageBrokerUrl = "tcp://localhost:61616"; // URL of message broker
	private ActiveMQConnectionFactory connectionFactory; // factory / connection handler
	private Connection connection; // connection object
	private Session session; // session object
	private Destination destination; // destination object
	private MessageProducer producer;
	
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
		destination = session.createQueue("Queue.Sum");
		
		// create a message producer using the session object
		producer = session.createProducer(destination);
	}	
	
	// termination method, run after execution
	public void terminate() throws Exception {
		
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
            
			// printout info statement
			System.out.println("worker retrieved message number: " + message_id );            
            
    		// create sum message
    		SumMessage sum_message = new SumMessage(sum);
    		ObjectMessage sum_object_message = session.createObjectMessage(sum_message);
    		sum_object_message.setIntProperty("message_id", message_id);

    		// send message
    		producer.send(sum_object_message);	
    		
			// printout info statement
			System.out.println("worker sent message number: " + message_id );
            
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
		
		System.out.println("start_value: " + start_value);
		System.out.println("number_of_elements: " + number_of_elements);
		System.out.println("sum: " + sum);
		System.out.println("\n");
		
		return sum;
	}
}