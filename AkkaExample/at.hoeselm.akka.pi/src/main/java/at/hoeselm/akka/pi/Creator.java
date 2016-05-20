package at.hoeselm.akka.pi;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.routing.RoundRobinRouter;

public class Creator extends UntypedActor {

	private long start_value = 0;
	private int actor_count;
	private long message_count;
	private int number_of_elements_per_message;

	private ActorRef workers;
	private ActorRef collector;

	public Creator(final long start_time, final int actor_count, 
			final long message_count, final int number_of_elements_per_message) {

		// initialize variables
		this.actor_count = actor_count;
		this.message_count = message_count;
		this.number_of_elements_per_message = number_of_elements_per_message;

		// create the collector actor
		collector = this.getContext().actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new Collector(start_time, message_count);
			}
		}),"collector");

		// create the worker actors by using a roundrobinrouter
		workers = this.getContext().actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new Worker(collector);
			}
		}).withRouter(new RoundRobinRouter(actor_count)), "workers");
	}

	// message listener
	@Override
	public void onReceive(Object message) throws Exception {

		// listen for worker messages
		if (message instanceof StartMessage) {

			// send messages
			for (int i = 0; i < message_count; ++i) {

				// create message
				WorkerMessage worker_message = new WorkerMessage(start_value, number_of_elements_per_message);

				// calculate new start value
				start_value += number_of_elements_per_message;

				// send message
				workers.tell(worker_message);

			}

		}
	}

}