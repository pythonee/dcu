package scenario_I;

public class Configure {
	public final static int MAX_QUEUE_LENGTH = 10;
	
	public final static double OFFERED_LOAD = 0.8;
	
	public final static double LINK_RATE = 1E11;
	
	public final static int MEAN_PACKET_LENTH = 8000;
	
	public final static double MEAN_SERVICE_TIME = MEAN_PACKET_LENTH/LINK_RATE;
	
	public final static double MEAN_INTER_ARRIVE_TIME = MEAN_SERVICE_TIME/OFFERED_LOAD;
	
	public final static int NUMBER_OF_ARRIVATE = 200000;
	
	public final static int BATCH_SIZE = 5000;
}
