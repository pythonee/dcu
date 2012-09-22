package scenario_I;

import common.ConfidenceInterval;
import common.Event;
import common.EventListManager;
import common.ExpRndGenerator;
import common.Queue;

public class Simulator {
	double sumOfArriveClock = 0.0;		// the sum of arrive event clock
	double sumOfDepartClock = 0.0;		// the sum of depart event clock
	
	double lastDepartClock  = 0.0;		// the time of the last packet depart the queue
	
	double[] sumOfBatchArriveClock = new double[Configure.NUMBER_OF_ARRIVATE/Configure.BATCH_SIZE];
	double[] sumOfBatchDepartClock = new double[Configure.NUMBER_OF_ARRIVATE/Configure.BATCH_SIZE];

	double[] batchLoss  = new double[Configure.NUMBER_OF_ARRIVATE/Configure.BATCH_SIZE];
	double[] batchDelay = new double[Configure.NUMBER_OF_ARRIVATE/Configure.BATCH_SIZE];
	
	Queue queue = null;
	EventListManager eventList = null;
	
	public void init() {
	
		eventList = EventListManager.getInstance();
		
		Event event = new Event();
		event.setEventOccurClock(0);
		event.setEventType(0);
		eventList.insertEvent(event);
		
		queue = new Queue();
		queue.setMaxQueueLength(Configure.MAX_QUEUE_LENGTH);
	}

	public void event_loop() {

		while (queue.getOfferedPacket() < Configure.NUMBER_OF_ARRIVATE) {
			
			// fetch the event information
			Event event = eventList.removeEvent();
			int eventType = event.getEventType();
			double eventOccurClock = event.getEventOccurClock();
			
			double serviceTime = ExpRndGenerator.random(Configure.MEAN_SERVICE_TIME);
			
			switch (eventType) {
			
			case 0:		// arrive event
				
				Event nextArrivalEvent = event.nextArriveEvent();		
				eventList.insertEvent(nextArrivalEvent);	

				if (queue.enqueue()) {	
					sumOfArriveClock += eventOccurClock;
					if ((queue.getDepartedPacket() + queue.size()) >= Configure.BATCH_SIZE &&
						(queue.getDepartedPacket() + queue.size()) %  Configure.BATCH_SIZE == 0) {
					
							int batch = (queue.getDepartedPacket()+queue.size())/Configure.BATCH_SIZE-1;
							sumOfBatchArriveClock[batch] = sumOfArriveClock;
					
					}
				}
				
				if (queue.getOfferedPacket() >= Configure.BATCH_SIZE &&
					queue.getOfferedPacket() % Configure.BATCH_SIZE == 0) {
					
					int batch = queue.getOfferedPacket()/Configure.BATCH_SIZE-1;
					batchLoss[batch] = queue.getLossPacket();
				
				}
				
				// test the server is idle?
				if (queue.size() == 1) {			// if idle. without wait
					queue.setServiceTime(serviceTime);		// setting the service time
					Event departEvent = new Event();
					departEvent.setEventType(1);
					departEvent.setEventOccurClock(eventOccurClock + serviceTime);
					eventList.insertEvent(departEvent);
				}
				
				break;
				
			case 1:		// depart event
				
				queue.dequeue();
				
				// test the queue is empty?
				if (queue.size() > 0) {
					
					queue.setServiceTime(serviceTime);		// update the next event service time
					
					Event nextDepartEvent = new Event();	// generate next depart event
					nextDepartEvent.setEventType(1);
					nextDepartEvent.setEventOccurClock(eventOccurClock + serviceTime);
					eventList.insertEvent(nextDepartEvent);
				}
				
				sumOfDepartClock += eventOccurClock;

				if (queue.getDepartedPacket() >= Configure.BATCH_SIZE &&
					queue.getDepartedPacket() %  Configure.BATCH_SIZE == 0) {
					
					int batch = queue.getDepartedPacket()/Configure.BATCH_SIZE-1;
					sumOfBatchDepartClock[batch] = sumOfDepartClock;
					
				}

				lastDepartClock = eventOccurClock;
				break;
			}
		}
		
		if (queue.size() > 0) {		// depart the rest packet in queue
			
			sumOfDepartClock += lastDepartClock * queue.size();
			int lastQueuingSize = queue.size();
			
			for (int i = 0; i < lastQueuingSize; i++) {
				
				double serviceTime = ExpRndGenerator.random(Configure.MEAN_SERVICE_TIME);
				sumOfDepartClock += serviceTime*(lastQueuingSize-1-i);
				
				queue.dequeue();
			}
		}
		
		eventList.clear();			// simulation is over
	}
	
	public static void main(String[] args) {
		Simulator simulator = new Simulator();
		
		double start = System.currentTimeMillis();
		simulator.init();
		simulator.event_loop();
		double end = System.currentTimeMillis();
		
		// print the loss late of single queueu
		System.out.println("total loss:        	" + simulator.queue.getLossPacket());
		int totalDeparted = simulator.queue.getDepartedPacket();
		double lossRate = (double)simulator.queue.getLossPacket()/simulator.queue.getOfferedPacket();
		System.out.println("total loss rate:   	" + lossRate);
		System.out.println("total departed:    	" + simulator.queue.getDepartedPacket());
		System.out.println("total arrive:      	" + simulator.queue.getOfferedPacket());
		
		// print average end-to-end delay of single queue
		double totalDuration = simulator.sumOfDepartClock - simulator.sumOfArriveClock;
		System.out.println("end-to-end delay:    	" + totalDuration/totalDeparted);
		System.out.println();

		// print each batch means delay
		System.out.println("each batch means delay (batch size = " + Configure.BATCH_SIZE + ") :");
		int batchDelaySampleSize = simulator.queue.getDepartedPacket()/Configure.BATCH_SIZE;
		for (int i = batchDelaySampleSize-1; i >= 0; i--) {
			if (i > 0) {
				simulator.sumOfBatchArriveClock[i] -= simulator.sumOfBatchArriveClock[i-1];
				simulator.sumOfBatchDepartClock[i] -= simulator.sumOfBatchDepartClock[i-1];
			}
			simulator.batchDelay[i] = simulator.sumOfBatchDepartClock[i]
			                         -simulator.sumOfBatchArriveClock[i];
			
			simulator.batchDelay[i] /= Configure.BATCH_SIZE;
			
			System.out.println("batch " + i + " means delay is: " + simulator.batchDelay[i]);
		}
		System.out.println();
		
		// print batch delay confidence interval
		double[] delayCfdInterval =  ConfidenceInterval.cfdInterval(simulator.batchDelay,0,batchDelaySampleSize);
		System.out.println("the delay confidence interval: "+"["+delayCfdInterval[0]+","+delayCfdInterval[1]+"]");
		System.out.println("delay percentage: "+delayCfdInterval[2]);
		System.out.println();
		
		// print each batch means loss rate
		System.out.println("each batch means loss rate (batch size = " + Configure.BATCH_SIZE + ") :");
		int batchLossSampleSize = simulator.queue.getOfferedPacket()/Configure.BATCH_SIZE;
		for (int i = batchLossSampleSize-1; i >= 0 ; i--) {
			if (i>0) {
				simulator.batchLoss[i] -= simulator.batchLoss[i-1];
			}
			simulator.batchLoss[i] /= Configure.BATCH_SIZE;
			System.out.println("batch " + i + " means loss rate " + simulator.batchLoss[i]);
		}
		System.out.println();
		
		// print batch loss rate confidence interval
		double[] lossCfdInterval =ConfidenceInterval.cfdInterval(simulator.batchLoss, 0, batchLossSampleSize);
		System.out.println("the loss confidence interval is: "+"["+lossCfdInterval[0]+","+lossCfdInterval[1]+"]");
		System.out.println("the loss percentage is: " + lossCfdInterval[2]);
		System.out.println();
		
		System.out.println("simulation during : " + (end-start) + "ms");
	}
}
