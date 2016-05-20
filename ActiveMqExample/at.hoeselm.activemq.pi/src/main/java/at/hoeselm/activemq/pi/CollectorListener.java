package at.hoeselm.activemq.pi;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class CollectorListener implements MessageListener {
	
	private double pi = 0.0;

	public void onMessage(Message message) {
		try {
			// parse worker message and calculate result
			ObjectMessage object_message = (ObjectMessage) message;
			CollectorMessage sum_message = (CollectorMessage) object_message.getObject();
			int message_id = object_message.getIntProperty("message_id");
			
			// calculation
			pi += sum_message.getSum();

			// printout info statement
			System.out.println("New calculation of pi occured because of message id " + message_id + " ;Pi is now " + pi );
			
		} catch (Exception e) {
			// printout statement
			System.out.println("exception occured: " + e.getMessage());
		}
	}

}
