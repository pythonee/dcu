package common;

public class Queue {
	protected int queuingPacket; 		// how many packets in queue
	protected int lossPacket;			// how many packets loss
	protected int departedPacket;		// how many packets depart
	protected int offeredPacket;		// the total arrive packet
	protected int maxQueueLength;		// the max length of queue
	private double serviceTime;		// the service time for current depart packet
	
	public boolean enqueue() {
		offeredPacket++;
		if (queuingPacket == maxQueueLength) {
			lossPacket++;
			return false;
		}
		else {
			queuingPacket++;
			return true;
		}
	}
	
	public void dequeue(){
		queuingPacket--;
		departedPacket++;
	}
	
	public int size(){
		return queuingPacket;
	}
	
	public int getLossPacket() {
		return lossPacket;
	}

	public int getDepartedPacket() {
		return departedPacket;
	}

	public int getOfferedPacket() {
		return offeredPacket;
	}

	public double getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(double serviceTime) {
		this.serviceTime = serviceTime;
	}

	public void setMaxQueueLength(int maxQueueLength) {
		this.maxQueueLength = maxQueueLength;
	}
}
