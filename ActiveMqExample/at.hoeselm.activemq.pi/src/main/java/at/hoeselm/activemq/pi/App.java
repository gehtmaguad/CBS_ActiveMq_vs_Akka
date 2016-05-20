package at.hoeselm.activemq.pi;

public class App {

    public static void main(String args[]) throws Exception {

    	long start_time = System.nanoTime();
    	
		try {
			
			/** creator */
			// define parameters for execution
			final int start_value = 0;
			final int end_value = 10000000;
			final int actor_count = 10;

			// create an instance of this class
			(new Thread(new Creator(start_value, end_value, actor_count))).start();
			
			/** worker */
			for (int i = 0; i < actor_count; ++i) {
				(new Thread(new Worker())).start();
			}
			
			/** collector */
			(new Thread(new Collector(start_time, actor_count))).start();

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}    	

        
    }
	
}
