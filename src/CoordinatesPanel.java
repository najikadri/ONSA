import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.util.List;

/** Coordinates Panel is used to draw the lines and the plots
 * it has specific methods to render and update panel
 * 
 * @author Naji Kadri
 *
 */


public class CoordinatesPanel extends JPanel {
	public ArrayList<Plot> plots; // the plots to draw
	public ArrayList<Line> lines; //the lines to draw
	private CoordinatesFrame frame; //the current frame
	private int xox; // the x-axis
	private int yoy; // the y-axis
	private int scale; //the scale : eg. 1 cm per 20 pixels

	
	public CoordinatesPanel () { //basic constructor
		plots = new ArrayList<Plot>();
		lines = new ArrayList<Line>();
		frame = new CoordinatesFrame();
		scale = 1;
		this.setToolTipText("Coordinates");
	}
	
	public CoordinatesPanel (ArrayList<Plot> p,ArrayList<Line> l,CoordinatesFrame f,int s) {
		//advance constructor
		plots = p;
		lines = l;
		frame = f;
		xox = frame.getWidth()/2;
		yoy = frame.getHeight()/2;
		scale = s;
		this.setToolTipText("Coordinates");
	}
	
	public void setLineList (ArrayList<Line> l) { //set the line list
		lines = l;
	}
	
	public void setPlotsList (ArrayList<Plot> p) { //set the plot list
		plots = p;
	}
	
	public void getXOX () { //get the x-axis
	  xox = (int) frame.getWidth()/2;
	}
	
	public void getYOY () { //get the y-axis
		yoy = (int) frame.getHeight()/2;
	}
	
	public void addLine (Line line) { //add a new line
		lines.add(line);
	}
	
	public void deleteLine (Line line) { //delete a line and all its plots
		ArrayList<Plot> plt = new ArrayList<Plot>();
		for (int i=0; i < plots.size();i++) {
			if (plots.get(i).getLine().getName().equals(line.getName())) {
				plt.add(plots.get(i));
			}
		}
		plots.removeAll(plt);
		lines.remove(line);
	}
	
	public void addPlot (Plot p) { //add a new plot
		plots.add(p);
	}
	
	public void deletePlot (String name) { //delete a plot
		Plot p = null;
		for (int i=0; i < plots.size();i++) {
			if (plots.get(i).getName().equals(name)) {
				p = plots.get(i);
			}
		}
		plots.remove(p);
		
	}
	
	public void setScale (int s) { //set the scale
		scale = s;
	}
	
	public Line getLine(String n) { //get a specific line
		Line l = null;
		for (Line line: lines) {
			if (line.getName().equals(n)) {
				l = line;
			}
		}
		return l;
	}
	
	public Plot getPlot (String n) { //get a specific plot
		Plot p = null;
		for (Plot plot: plots) {
			if (plot.getName().equals(n)) {
				p = plot;
			}
		}
		return p;
	}
	
	public int getScale () { //get the scale value
		return scale;
	}
	
	public void updateScales() {
		for (int i=0; i < lines.size();i++) {
			lines.get(i).setScale(scale);
		}
	}
	
	public void clear () { //clear all lines and plots
		plots.clear();
		lines.clear();
	}
	
	public boolean isParallel (Line l1, Line l2) { //checks if two lines parallel
		if (l1.getSlope() == l2.getSlope()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void createLine(Plot a, Plot b, String name) { //creates a new line (BETA)
		int slope = (int) Math.round((b.getY()-a.getY())/(b.getX()-a.getX()));
		System.out.println(slope);
		int ax = (int) (slope*a.getX());
		System.out.println(ax);
		int y = (int) a.getY();
		System.out.println(y);
		int timeShift = y - ax;
		System.out.println(timeShift);
		Line l = new Line(slope,timeShift,name);
		this.addLine(l);
	}
	
	public void createMid (Plot a, Plot b, String name) { //creates midpoint for two plots
		int x = (int) ((a.getX()+b.getX())/2);
		int y = (int) ((a.getY()+b.getY())/2);
		Line d = this.getLine(a.getLine().getName());
		Plot p = new Plot (d, x, y, name);
		this.addPlot(p);
	}
	
	public ArrayList<Plot> getPlots () { //get all plots
		return plots;
	}
	
	public ArrayList<Line> getLines () { //get all plots
		return lines;
	}
	
	@Override //draw all the graphics needed to make a Orthonormal system
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawLine(xox, 0, xox,frame.getHeight()); //draws x-axis
		g.drawLine(0, yoy, frame.getWidth(), yoy); //draws the y-axis
		
		for (int i=1;i <= 20;i++) {
			g.setColor(Color.BLUE);
			g.drawString(String.format("%d", i),xox+((i*scale)+1),yoy);
		}
		
		for (int i=1;i <= 20;i++) {
			g.setColor(Color.BLUE);
			g.drawString(String.format("%d", -i),xox-((i*scale)-1),yoy);
		}
		
		for (int i=1;i <= 20;i++) {
			g.setColor(Color.BLUE);
			g.drawString(String.format("%d", i),xox,yoy-((i*scale)-2));
		}
		
		for (int i=1;i <= 20;i++) {
			g.setColor(Color.BLUE);
			g.drawString(String.format("%d", -i),xox,yoy+((i*scale)+2));
		}
		
		g.setColor(Color.BLACK);
		
		for(Line l: lines) { //draw the lines
			int y1 = 0; //first Y
			int x1 = (int) l.getX(y1); // Get X from Y
			int y2 = (int) (300/l.getSlope());
			int x2 = (int) l.getX(y2);
			int y3 = (int) (-300/l.getSlope());
			int x3  = (int) l.getX(y3);
			
			g.drawLine(xox+x1, yoy-y1, xox+x2, yoy-y2); //draw the upper part
			g.drawLine(xox+x1, yoy-y1, xox+x3, (yoy-y3)); //draw the lower part
			g.drawString("("+l.getName()+")", xox+(x2+5), yoy-y2); //draw the name
			
		}
		
		for (Plot p: plots) { //draw the plots
			g.setColor(Color.red); //set the color to red
			g.fillOval((xox+((int) p.getX()*scale))-3, (yoy-((int) p.getY()*scale))-2, 6, 6); //fill an oval of 6x6 size
			g.drawString(p.getName(), xox+((int) p.getX()*scale+5), yoy-((int) p.getY()*scale)); //draw the name
		}
		
	}

}
