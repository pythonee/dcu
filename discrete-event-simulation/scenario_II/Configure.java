package scenario_II;

public class Configure {
	public final static int MEAN_PACKET_LENGTH = 8000;
	
	public final static int NUMBER_OF_ARRIVATE = 20000000;
	
	public final static double LINK_RATE = 1E11;
	
	public final static int MAX_QUEUE_LENGTH = 5;
	
	public final static double MEAN_SERVICE_TIME = MEAN_PACKET_LENGTH/LINK_RATE;
	
	public final static double[] OFFERED_LOADS = {0.7,0.20,0.30,0.25,0.80,0.20};
	
	public final static double[] MEAN_INTER_ARRIVE_TIME = {
															MEAN_SERVICE_TIME/OFFERED_LOADS[0],
															MEAN_SERVICE_TIME/OFFERED_LOADS[1],
															MEAN_SERVICE_TIME/OFFERED_LOADS[2],
															MEAN_SERVICE_TIME/OFFERED_LOADS[3],
															MEAN_SERVICE_TIME/OFFERED_LOADS[4],
															MEAN_SERVICE_TIME/OFFERED_LOADS[5]
														};
	
	public final static int[] NUMBER_OF_QUEUE = {2,2,3,1};
	
	public final static int[][] PATHS  = {{0,2},{0,2,3},{0,1,3},{1,3},{2,1},{2,3}};
	
	public final static int[][] QUEUES = {{1,2},{1,1,0},{0,1,0},{1,0},{0,0},{1,0}};
	
	public final static int NUMBER_OF_PATH = PATHS.length;
	
	public final static int NUMBER_OF_ROUTER = 4;
	
	public final static int BATCH_SIZE = 5000;
	
}
