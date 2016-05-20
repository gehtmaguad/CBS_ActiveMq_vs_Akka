package at.hoeselm.activemq.pi;

public class App {

    public static void main(String args[]) throws Exception {

		try {
			
			/** creator */
			// define parameters for execution
			int start_value = 0;
			int end_value = 1000000000;
			int message_count = 100;

			// create an instance of this class
			(new Thread(new Creator(start_value, end_value, message_count))).start();
			
			/** worker */
			(new Thread(new Worker())).start();
			(new Thread(new Worker())).start();
			(new Thread(new Worker())).start();
			
			/** collector */
			(new Thread(new Collector())).start();

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}    	

        
    }
	
}
