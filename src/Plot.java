import java.io.Serializable;


/**
 * Plot is a point or coordinate that is plotted on a line equation
 * the plot must be on a line to make sure no errors goes out
 * @author Naji Kadri
 *
 */
public class Plot implements Serializable {
	
	private String name; // the name of the plot
	private Line line; // its line equation
	private double abscissa; // its abscissa or X
	private double ordinate;// its ordinate or Y
	
	public Plot (Line l, double x, double y, String n) { // the constructor of the plot
		checkEquation(l,x,y);
		name = n;
	}
	
	public void checkEquation(Line l,double x, double y) { //checks if it validate line of equation
		if (y == (l.getSlope())*x+l.getTimeShift()) {
			line = l;
			abscissa = x;
			ordinate = y;
		}
		else {
			throw new IllegalArgumentException("Wrong Line"); // throw new exception when there is wrong values
		}
	}
	
	public double getX () { //get the value of x
		return abscissa;
	}
	
	public double getY () { //get the value of Y
		return ordinate;
	}
	
	public Line getLine() { //get the line equation
		return line;
	}
	
	public String getName () { //get its name
		return name;
	}
}
