package at.hoeselm.activemq.pi;

import java.time.LocalDateTime;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class CollectorListener implements MessageListener {
	
	private double pi = 0.0;
	private long message_count;
	private long current_count = 0;
	private long start_time;
	
	public CollectorListener(long start_time, long message_count) {
		this.message_count = message_count;
		this.start_time = start_time;
	}

	public void onMessage(Message message) {
		try {
			// parse worker message and calculate result
			ObjectMessage object_message = (ObjectMessage) message;
			CollectorMessage sum_message = (CollectorMessage) object_message.getObject();
			int message_id = object_message.getIntProperty("message_id");
			
			// calculation
			pi += sum_message.getSum();
			++current_count;
			
			if (current_count == message_count) {

				// calculate time
				long end_time = System.nanoTime();
				double duration = ((double) (end_time - start_time)) / (1000 * 1000 * 1000);

				// print out information
				System.out.println("The result of the calculation is " + pi + " and the calculation took " + duration + " seconds");
			
			}
			
		} catch (Exception e) {
			// printout statement
			System.out.println("exception occured: " + e.getMessage());
		}
	}

}
