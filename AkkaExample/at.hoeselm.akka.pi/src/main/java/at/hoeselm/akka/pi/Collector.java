package at.hoeselm.akka.pi;

import akka.actor.UntypedActor;

public class Collector extends UntypedActor {
	
	private double pi = 0.0;
	private int current_count = 0;
	private long start_time;
	private int actor_count = 0;
	
	public Collector(long start_time, int actor_count) {
		this.start_time = start_time;
		this.actor_count = actor_count;
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
		
			if (current_count == actor_count) {

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