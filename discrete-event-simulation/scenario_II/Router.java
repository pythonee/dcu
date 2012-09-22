package scenario_II;

import java.util.ArrayList;

public class Router {
	private int routerID;						// the router identifier
	private int numberOfQueue;					// how many queues a router has
	private ArrayList<RouterQueue> queues;	// the queues
	
	public Router(int id) {
		routerID = id;
		numberOfQueue = Configure.NUMBER_OF_QUEUE[id];
		queues = new ArrayList<RouterQueue>(numberOfQueue);

		for (int i = 0; i < numberOfQueue; i++) {
			RouterQueue queue = new RouterQueue(i);
			queue.setMaxQueueLength(Configure.MAX_QUEUE_LENGTH);
			queues.add(queue);
		}
	}

	public int getRouterID() {
		return routerID;
	}

	public RouterQueue getQueue(int index){
		return queues.get(index);
	}

	public int getNumberOfQueue() {
		return numberOfQueue;
	}
}
