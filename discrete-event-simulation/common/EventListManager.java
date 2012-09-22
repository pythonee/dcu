package common;

import java.util.LinkedList;
import java.util.ListIterator;


public class EventListManager {
	
	private static EventListManager instance;	// single instance
	
	private static LinkedList<Event> eventList;
	
	private EventListManager(){
		eventList = new LinkedList<Event>();
	}
	
	public static synchronized EventListManager getInstance(){
		if (instance == null) 
			instance = new EventListManager();
		
		return instance;
	}
	
	public void insertEvent(Event e){
		boolean isInserted = false;
		
		if (isEmpty()) {
			eventList.addFirst(e);
			isInserted = true;
		}
		else {
			ListIterator<Event> iterator = eventList.listIterator();
			
			while(iterator.hasNext()){
				Event listEvent = iterator.next();
				if (e.getEventOccurClock() <= listEvent.getEventOccurClock()) {
					iterator.previous();
					iterator.add(e);
					isInserted = true;
					break;
				}
			}
		}
		
		if (!isInserted) {
			eventList.addLast(e);
		}
	}
	
	public Event removeEvent(){
		Event firstEvent = eventList.removeFirst();
		return firstEvent;
	}
	
	public boolean isEmpty(){
		return eventList.isEmpty();
	}
	
	public void clear(){
		eventList.clear();
	}
}
