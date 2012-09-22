package scenario_II;

import common.Queue;

import java.util.LinkedList;

public class RouterQueue extends Queue{
	
	private int queueID;					// identify the queue
	private int[] batchLoss = new int[Configure.NUMBER_OF_ARRIVATE/Configure.BATCH_SIZE];
	private LinkedList<Packet> queue;		// the queue to store the NetworkEvent
	
	public RouterQueue(int id) {
		queueID = id;
		queue = new LinkedList<Packet>();
	}
	
	public void enqueue(Packet packet) {
		offeredPacket++;
		if (queue.size() == maxQueueLength){
			lossPacket++;
		}
		else {
			queue.addLast(packet);
			queuingPacket++;
		}
	}
	
	public void dequeue() {
		queue.removeFirst();
		queuingPacket--;
		departedPacket++;
	}

	public Packet front(){
		return queue.getFirst();
	}
	
	public int getQueueID() {
		return  queueID;
	}

	public int getBatchLoss(int batch) {
		return batchLoss[batch];
	}

	public void setBatchLoss(int batch,int loss) {
		this.batchLoss[batch] = loss;
	}
}
