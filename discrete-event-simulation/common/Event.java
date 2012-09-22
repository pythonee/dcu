package common;

import ee509.assignment.scenario_I.Configure;

public class Event {
	protected int eventType;				// arrive or depart
	protected double eventOccurClock;		// when the event occur

	public double getEventOccurClock() {
		return eventOccurClock;
	}

	public void setEventOccurClock(double eventOccurClock) {
		this.eventOccurClock = eventOccurClock;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
	
	public Event nextArriveEvent(){		// generate next arrive event
		Event next = new Event();
		double inter_time = ExpRndGenerator.random(Configure.MEAN_INTER_ARRIVE_TIME);
		next.setEventType(0);
		next.setEventOccurClock(inter_time+this.eventOccurClock);
		
		return next;
	}
}
