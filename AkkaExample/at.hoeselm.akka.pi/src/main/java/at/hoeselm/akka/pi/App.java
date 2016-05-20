package at.hoeselm.akka.pi;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

public class App {

	public static void main(String[] args) {

		// define parameters for execution
		final int start_value = 0;
		final int end_value = 10000000;
		final int message_count = 1000;
		
		// Create an Akka system
		ActorSystem system = ActorSystem.create("PiCalculation");
		
		// Create a creator actor
		ActorRef creator = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new Creator(start_value, end_value, message_count);
			}
		}), "creator");

		// Start calculation
		StartMessage start_message = new StartMessage();
		creator.tell(start_message);

	}

}
