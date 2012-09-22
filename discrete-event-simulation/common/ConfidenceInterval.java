package common;

public class ConfidenceInterval {

	public static double[] cfdInterval(double[] dataSet,int start,int length){
		
		double[] interval = new double[3];
		
		double sum = 0.0;
		
		for(int i = start; i<length;i++){
			sum += dataSet[i];
		}
		
		double average = sum/length;
		
		double square = 0.0;
		
		for(int i = start; i < length;i++){
			square += (dataSet[i]-average)*(dataSet[i]-average);
		}
		
		double variance = square/(length-1);
		double stdDev = Math.sqrt(variance/length);
		
		interval[0] = average - 1.96*stdDev;
		interval[1] = average + 1.96*stdDev;
		interval[2] = (1.96*stdDev)/average;
		
		return interval;
	}
}
