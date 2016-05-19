package at.hoeselm.activemq.pi;

import java.io.Serializable;

public class SumMessage implements Serializable {

	double sum = 0.0;
	
	public SumMessage() {
	}

	public SumMessage(double sum) {
		super();
		this.sum = sum;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}
	
}
