package at.hoeselm.activemq.pi;

import java.io.Serializable;

public class WorkerMessage implements Serializable {

	private int start_value;
	private int number_of_elements;
	
	public WorkerMessage() {
		
	}
	
	public WorkerMessage(int start_value, int number_of_elements) {
		this.start_value = start_value;
		this.number_of_elements = number_of_elements;
	}
	
	public int get_start_value() {
		return start_value;
	}
	public void set_start_value(int start_value) {
		this.start_value = start_value;
	}
	public int get_number_of_elements() {
		return number_of_elements;
	}
	public void set_number_of_elements(int number_of_elements) {
		this.number_of_elements = number_of_elements;
	}
	
	public String toString() {
		return "WorkerMessage: start_value = " + start_value
				+ " ; number_of_elements = " + number_of_elements + " ;";
	}
	
}
