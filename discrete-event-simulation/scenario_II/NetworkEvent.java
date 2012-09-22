package scenario_II;

import common.Event;
import common.ExpRndGenerator;

public class NetworkEvent extends Event {
	
	Packet packet = null;		// the event hold the packet information
	
	public NetworkEvent(Packet packet) {
		this.packet = packet;
	}
	
	
	public Packet getPacket(){
		return packet;
	}
	// the current event can generate next arrive event base on the arrive distribute
	public NetworkEvent nextArriveEvent(){
		int path = this.getPacket().getPath();
		
		Packet packet = new Packet();
		packet.setPath(path);
		packet.setCurrentRouter(0);
		
		double serviceTime = ExpRndGenerator.random(Configure.MEAN_SERVICE_TIME);
		packet.setServiceTime(serviceTime);
		
		NetworkEvent nextArriveEvent = new NetworkEvent(packet);
		nextArriveEvent.setEventType(0);
		double inter_time = ExpRndGenerator.random(Configure.MEAN_INTER_ARRIVE_TIME[path]);
		nextArriveEvent.setEventOccurClock(inter_time + this.eventOccurClock);
		
		return nextArriveEvent;
	}
}
