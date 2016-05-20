package at.hoeselm.akka.pi;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class Worker extends UntypedActor {
	
	private ActorRef collector;
	
	Worker(ActorRef collector) {
		this.collector = collector;
	}

	// message listener
	public void onReceive(Object message) {
		// listen for worker messages
		if (message instanceof WorkerMessage) {

			// parse worker message and calculate result
			WorkerMessage worker_message = (WorkerMessage) message;
			double sum = calculate_pie(worker_message);   			
			
			// create sum message
			CollectorMessage sum_message = new CollectorMessage(sum);
			
			// send sum message
			collector.tell(sum_message, getSelf());			
		
		// listen for other messages and handle them as unhandled
		} else {
			unhandled(message);
		}
	}
	
    // calculate number pie
	private double calculate_pie(WorkerMessage worker_message) {
		
		// initialize variables
        long start_value = worker_message.get_start_value();
        long number_of_elements = worker_message.get_number_of_elements();
		double sum = 0.0;
		
		for(long i = start_value * number_of_elements;
			i<= ((start_value + 1) * number_of_elements - 1); i++ ) {
			
	    	// dividend: (1 - (i % 2) * 2) --> -1 for odd numbers and 1 for even numbers
	    	// divisor: (2 * i + 1)
	    	// result times 4 because we want pi and not pi/4
			sum += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1);
		}
		
		return sum;
	}	
}