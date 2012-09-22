package scenario_II;

import common.ConfidenceInterval;
import common.EventListManager;
import common.ExpRndGenerator;

public class Simulator {

	EventListManager eventList = null;								// event list manager for the event-advance simulate
	Router[] routers = new Router[Configure.NUMBER_OF_ROUTER];		// the network router use for forward packer 
	
	double[] delay = new double[Configure.NUMBER_OF_PATH];			// store every path total cost to the arrival packet
	
	double[][] batchDelay = new double[Configure.NUMBER_OF_PATH][Configure.NUMBER_OF_ARRIVATE/Configure.BATCH_SIZE];
	
	int[] totalDeparted = new int[Configure.NUMBER_OF_PATH];			// how many packet complete transmission in every path
	
	public void init() {	// initial the simulate mode 
	
		eventList = EventListManager.getInstance();					// create the event list
		
		for (int i = 0; i < Configure.NUMBER_OF_ROUTER; i++) {
			routers[i] = new Router(i);								// create all router
		}
		
		int i = 0;
		while(i < Configure.NUMBER_OF_PATH){	// create the initial packet for every path
			Packet packet = new Packet();
			packet.setPath(i);
			packet.setCurrentRouter(0);
			double serviceTime = ExpRndGenerator.random(Configure.MEAN_SERVICE_TIME);
			packet.setServiceTime(serviceTime);
			
			NetworkEvent event = new NetworkEvent(packet);			
			event.setEventType(0);
			event.setEventOccurClock(0);
			eventList.insertEvent(event);
			i++;
		}
	}
	
	public void event_loop(){
		
		int numberOfDepart = 0;
		
		while (numberOfDepart < Configure.NUMBER_OF_ARRIVATE) {
			

			NetworkEvent event = (NetworkEvent)eventList.removeEvent();
			
			// fetch the packet information
			Packet packet = event.getPacket();
			int path = packet.getPath();					
			int currentRouterIndex = packet.getCurrentRouter();	
			int currentQueueID = Configure.QUEUES[path][currentRouterIndex];
			double serviceTime = packet.getServiceTime();
			
			// fetch the event information
			int eventType = event.getEventType();
			double eventOccurClock = event.getEventOccurClock();

			// locate tha router and queue
			Router router = routers[Configure.PATHS[path][currentRouterIndex]];
			RouterQueue queue = router.getQueue(currentQueueID);
			
			switch (eventType) {
			
			case 0 : 		// arrive event
				
				if (router.getRouterID() == packet.getSource()) {
					// generate next arrive event
					NetworkEvent nextArrivalEvent = event.nextArriveEvent();
					eventList.insertEvent(nextArrivalEvent);
					packet.setArriveClock(eventOccurClock);
					numberOfDepart++;
				}
				
				queue.enqueue(packet);
				
				// test if the server is idle
				if (queue.size() == 1) {				// if idle, without wait
					NetworkEvent departWithoutWaitEvent = new NetworkEvent(packet);
					departWithoutWaitEvent.setEventType(1);
					departWithoutWaitEvent.setEventOccurClock(eventOccurClock + serviceTime);
					eventList.insertEvent(departWithoutWaitEvent);
				}
				
				if (queue.getOfferedPacket() >= Configure.BATCH_SIZE &&
					queue.getOfferedPacket() % Configure.BATCH_SIZE == 0) {
					int batch = queue.getOfferedPacket()/Configure.BATCH_SIZE - 1;
					queue.setBatchLoss(batch, queue.getLossPacket());
				}
				
				break;
				
			case 1 :	// depart event
				// complete packet transmission for a path?
				if (router.getRouterID() == packet.getDestination()) {
					totalDeparted[path]++;
										
					// a packet delay
					double duration = eventOccurClock - queue.front().getArriveClock();
					
					// sum of packet delay of a path
					delay[path] += duration;
					
					if (totalDeparted[path] >= Configure.BATCH_SIZE &&
						totalDeparted[path] % Configure.BATCH_SIZE == 0) {
						int batch = totalDeparted[path]/Configure.BATCH_SIZE-1;
						batchDelay[path][batch] = delay[path];
					}
					
				}
				else {		// no complete transmission? keep forwarding
					packet.setCurrentRouter(currentRouterIndex+1);		// move to next router
					NetworkEvent forwardEvent = new NetworkEvent(packet);
					forwardEvent.setEventType(0);				
					forwardEvent.setEventOccurClock(eventOccurClock);		// it occur on the same time
					eventList.insertEvent(forwardEvent);
				}
				
				queue.dequeue();
				
				// test the queue is empty ?
				if (queue.size() > 0) {  // if no ? generate next depart event in this queue
					Packet nextDepartPacket = queue.front();
					NetworkEvent nextDepartEvent = new NetworkEvent(nextDepartPacket);
					
					nextDepartEvent.setEventOccurClock(eventOccurClock + nextDepartPacket.getServiceTime());

					nextDepartEvent.setEventType(1);
					eventList.insertEvent(nextDepartEvent);
				}
				
				break;
			}
		}
		
		eventList.clear();		// simulation is over
	}
	
	public static void main(String[] args) {
		Simulator simulator = new Simulator();
		double start = System.currentTimeMillis();
		simulator.init();
		simulator.event_loop();
		double end = System.currentTimeMillis();
		
		// output each queue loss rate for a run
		for (int i = 0; i < Configure.NUMBER_OF_ROUTER; i++) {
			System.out.println("routers " + i);
			for (int j = 0; j < simulator.routers[i].getNumberOfQueue(); j++) {
				int lossPacket = simulator.routers[i].getQueue(j).getLossPacket();
				int offerPacket = simulator.routers[i].getQueue(j).getOfferedPacket();
				System.out.println(" queue "+ j + " loss rate: " + (double)lossPacket/offerPacket);
			}
			System.out.println();
		}
		System.out.println();

		// output each path end-to-end delay for a run
		for (int i = 0; i <  Configure.PATHS.length; i++) {
			System.out.println("path " + i + " end-to-end delay " + simulator.delay[i]/simulator.totalDeparted[i]);
		}
		System.out.println();
		
		// output batch mean loss rate of each queue
		System.out.println("=== batch means loss rate "+"(bath size = " + Configure.BATCH_SIZE +")===");
		for (int i = 0; i < Configure.NUMBER_OF_ROUTER; i++) {
			Router router = simulator.routers[i];
			System.out.println("routers " + i);
			for (int j = 0; j < router.getNumberOfQueue(); j++) {
				RouterQueue queue = router.getQueue(j);
				double[] lossRate = new double[queue.getOfferedPacket()/Configure.BATCH_SIZE];
				System.out.println(" queue "+ j + " loss rate ( batch number "+lossRate.length+" )");
				for (int k = queue.getOfferedPacket()/Configure.BATCH_SIZE-1; k >= 0; k--) {
					if (k>0) {
						int loss = queue.getBatchLoss(k) - queue.getBatchLoss(k-1);
						queue.setBatchLoss(k, loss);
					}
					lossRate[k]=(double)queue.getBatchLoss(k)/Configure.BATCH_SIZE;
				}
				
				double[] lossCfdInterval = ConfidenceInterval.cfdInterval(lossRate, 0, lossRate.length);
				
				System.out.println("interval: ["+lossCfdInterval[0]+","+lossCfdInterval[1]+"]");
				System.out.println("percentage:"+lossCfdInterval[2]);
			}
			System.out.println();
		}
		System.out.println();
		
		// output batch delay of each path
		System.out.println("=== batch means delay "+"(bath size = " + Configure.BATCH_SIZE +")===");
		for (int i = 0; i < Configure.NUMBER_OF_PATH; i++) {
			int sampleSize = simulator.totalDeparted[i]/Configure.BATCH_SIZE;
			System.out.println("path " + i + " end-to-end delay ( batch number = " + sampleSize + " )");
			for (int j = sampleSize-1; j >=0 ; j--) {
				if (j > 0) {
					simulator.batchDelay[i][j] -= simulator.batchDelay[i][j-1];
				}
				simulator.batchDelay[i][j] /= Configure.BATCH_SIZE;
			}
			double[] delayCfdInterval = ConfidenceInterval.cfdInterval(simulator.batchDelay[i], 0, sampleSize);
			System.out.println("interval: ["+delayCfdInterval[0]+","+delayCfdInterval[1]+"]");
			System.out.println("percentage:"+delayCfdInterval[2]);
			System.out.println();
		}
		System.out.println();
		System.out.println("simulation during " + (end-start) + " ms");
	}
}
