package at.hoeselm.akka.pi;

import akka.actor.UntypedActor;

public class Collector extends UntypedActor {
	
	private double pi = 0.0;
	private int count = 0;

	// message listener
	public void onReceive(Object message) {
		// listen for worker messages
		if (message instanceof CollectorMessage) {

			// parse worker message and calculate result
			CollectorMessage collector_message = (CollectorMessage) message;
			
			// calculation
			pi += collector_message.getSum();
			++count;

			// printout info statement
			System.out.println("\n ---------------------------------------");
			System.out.println("collector received message " + count );
			System.out.println("pi is now : " + pi );
			System.out.println("\n ---------------------------------------");

			// listen for other messages and handle them as unhandled
		} else {
			unhandled(message);
		}
	}
	
}