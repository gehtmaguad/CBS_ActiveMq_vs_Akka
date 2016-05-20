package at.hoeselm.akka.pi;

import java.io.Serializable;

public class WorkerMessage implements Serializable {

	private long start_value;
	private long number_of_elements;
	
	public WorkerMessage() {
		
	}
	
	public WorkerMessage(long start_value, long number_of_elements) {
		this.start_value = start_value;
		this.number_of_elements = number_of_elements;
	}
	
	public long get_start_value() {
		return start_value;
	}
	public void set_start_value(long start_value) {
		this.start_value = start_value;
	}
	public long get_number_of_elements() {
		return number_of_elements;
	}
	public void set_number_of_elements(long number_of_elements) {
		this.number_of_elements = number_of_elements;
	}
	
	public String toString() {
		return "WorkerMessage: start_value = " + start_value
				+ " ; number_of_elements = " + number_of_elements + " ;";
	}
	
}
