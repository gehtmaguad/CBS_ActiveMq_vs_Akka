package at.hoeselm.akka.pi;

import akka.actor.UntypedActor;

public class Collector extends UntypedActor {
	
	private double pi = 0.0;
	private long current_count = 0;
	private long start_time;
	private long message_count = 0;
	
	public Collector(long start_time, long message_count) {
		this.start_time = start_time;
		this.message_count = message_count;
	}

	// message listener
	public void onReceive(Object message) {
		// listen for worker messages
		if (message instanceof CollectorMessage) {

			// parse worker message and calculate result
			CollectorMessage collector_message = (CollectorMessage) message;
			
			// calculation
			pi += collector_message.getSum();
			++current_count;
		
			if (current_count == message_count) {

				// calculate time
				long end_time = System.nanoTime();
				double duration = ((double) (end_time - start_time)) / (1000 * 1000 * 1000);

				// print out information
				System.out.println("The result of the calculation is " + pi + " and the calculation took " + duration + " seconds");
			
			}			

			// listen for other messages and handle them as unhandled
		} else {
			unhandled(message);
		}
	}
	
}