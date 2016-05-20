package at.hoeselm.akka.pi;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

public class App {

	public static void main(String[] args) {

		// print info
		System.out.println("Starting Akka system");
		
		// Create an Akka system
		ActorSystem system = ActorSystem.create("PiCalculation");

		// print info
		System.out.println("Creating creator actor");		
		
		// Create a creator actor
		ActorRef creator = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new Creator(0, 10000, 10);
			}
		}), "creator");

		// Start calculation
		StartMessage start_message = new StartMessage();
		
		// print info
		System.out.println("sending message to creator actor");	
		
		creator.tell(start_message);
		
		// print info
		System.out.println("done starting app");			

	}

}
