package at.hoeselm.akka.pi;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

public class App {

	public static void main(String[] args) {
		
		final long start_time = System.nanoTime();

		// define parameters for execution
		final int actor_count = 100;
		final long message_count = 10000;
		final int number_of_elements_per_message = 1000000;
		
		// Create an Akka system
		ActorSystem system = ActorSystem.create("PiCalculation");
		
		// Create a creator actor
		ActorRef creator = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new Creator(start_time, actor_count, message_count, number_of_elements_per_message);
			}
		}), "creator");

		// Start calculation
		StartMessage start_message = new StartMessage();
		creator.tell(start_message);

	}

}
