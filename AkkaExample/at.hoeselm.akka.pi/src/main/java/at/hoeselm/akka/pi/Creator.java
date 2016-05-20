package at.hoeselm.akka.pi;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.routing.RoundRobinRouter;

public class Creator extends UntypedActor {

	private int start_value;
	private int end_value;
	private int actor_count;
	private int number_of_elements;

	private ActorRef workers;
	private ActorRef collector;

	public Creator(int start_value, int end_value, int actor_count) {

		// initialize variables
		this.start_value = start_value;
		this.end_value = end_value;
		this.actor_count = actor_count;

		// calculate the number of elements being calculated by each consumer
		this.number_of_elements = this.end_value / this.actor_count;

		collector = this.getContext().actorOf(new Props(Collector.class), "collector");

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
			for (int i = 0; i < actor_count; ++i) {

				// create message
				WorkerMessage worker_message = new WorkerMessage(start_value, number_of_elements);

				// calculate new start value
				start_value += number_of_elements;

				// send message
				workers.tell(worker_message);

			}

		}
	}

}