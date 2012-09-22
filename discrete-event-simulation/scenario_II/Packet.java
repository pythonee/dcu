package scenario_II;

public class Packet {
	private int path;				// indicate the packet transmission path,it is fixed when it generate
	private int currentRouter;		// which router do the packet reach
	private double arriveClock;	// when the packet arrive the core network
	private double serviceTime;	// when the packet generate,the service time is fixed
	
	public int getPath() {
		return path;
	}
	
	public void setPath(int path) {
		this.path = path;
	}
	
	public int getCurrentRouter() {		// return the router index in the path array
		return currentRouter;
	}

	public void setCurrentRouter(int currentRouter) {
		this.currentRouter = currentRouter;
	}

	public int getSource(){			// return the first router on its path
		return Configure.PATHS[path][0];
	}
	
	public int getDestination(){		// return the last router on its path
		return Configure.PATHS[path][Configure.PATHS[path].length-1];
	}
	
	public double getServiceTime() {		
		return serviceTime;
	}

	public void setServiceTime(double serviceTime) {
		this.serviceTime = serviceTime;
	}

	public double getArriveClock() {
		return arriveClock;
	}

	public void setArriveClock(double arriveClock) {
		this.arriveClock = arriveClock;
	}
}
