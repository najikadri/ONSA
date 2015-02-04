import java.io.Serializable;


/**
 * This class form the line equation
 * @author Naji Kadri
 *
 */

public class Line implements Serializable{
	
	private String name; //the name of the line
	private double slope; // the slope or (a)
	private double timeShift; //time shift or (b)
	private int scale;
	
	public Line (double s,double b,String n) { //constructor with a time shift
		slope = s;
		timeShift = b;
		name = n;
	}
	public Line (double s,String n) { //constructor without a time shift
		slope = s;
		timeShift = 0;
		name = n;
	}
	
	public double getSlope () { // get the slope's value
		return slope;
	}
	
	public double getTimeShift() { //get the time's shift value
		return timeShift;
	}
	
	public String getName () { //get the name of the line
		return name;
		
	}
	
	public double getY (double x) { // find the Y value from X
		double y = (slope*x)+(timeShift*scale);
		return y;
		
	}
	
	public double getX (double y) { //finds the X value from Y
		double x = (y-(timeShift*scale))/2;
		return x;
	}
	
	public void setScale (int s) {
		scale = s;
	}
	

}
