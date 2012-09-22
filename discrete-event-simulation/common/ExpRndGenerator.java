package common;

public class ExpRndGenerator {
	
	public static double random(double mean){
		return 0-mean*Math.log(1-Math.random());
	}
}
