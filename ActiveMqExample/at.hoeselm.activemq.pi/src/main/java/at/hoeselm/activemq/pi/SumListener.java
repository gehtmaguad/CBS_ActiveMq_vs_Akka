package at.hoeselm.activemq.pi;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class SumListener implements MessageListener {
	
	private double pi = 0.0;

	public void onMessage(Message message) {
		try {
			// parse worker message and calculate result
			ObjectMessage object_message = (ObjectMessage) message;
			SumMessage sum_message = (SumMessage) object_message.getObject();
			int message_id = object_message.getIntProperty("message_id");
			
			// calculation
			pi += sum_message.getSum();

			// printout info statement
			System.out.println("sum received message number: " + message_id );
			System.out.println("pi is now : " + pi );
			
		} catch (Exception e) {
			// printout statement
			System.out.println("exception occured: " + e.getMessage());
		}
	}

}
