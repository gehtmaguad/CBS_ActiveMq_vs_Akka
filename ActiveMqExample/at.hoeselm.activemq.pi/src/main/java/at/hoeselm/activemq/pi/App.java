package at.hoeselm.activemq.pi;

public class App {

    public static void main(String args[]) throws Exception {

    	long start_time = System.nanoTime();
    	
		try {
			
			/** creator */
			// define parameters for execution
			final int actor_count = 1;
			final long message_count = 100000;
			final int number_of_elements_per_message = 1000000;

			// create an instance of this class
			(new Thread(new Creator(message_count, number_of_elements_per_message))).start();
			
			/** collector */
			(new Thread(new Collector(start_time, message_count))).start();			
			
			/** worker */
			for (int i = 0; i < actor_count; ++i) {
				(new Thread(new Worker())).start();
			}

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}    	

        
    }
	
}
