package at.hoeselm.akka.pi;

import java.io.Serializable;

public class CollectorMessage implements Serializable {

	double sum = 0.0;
	
	public CollectorMessage() {
	}

	public CollectorMessage(double sum) {
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
