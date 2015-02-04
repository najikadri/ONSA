import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

/**The main JFrame for all the program
 * all functions can be found here.
 * Why we call this software ONSA?
 * ONSA means Orthonormal System Application
 * This software allows to do basics of the orthonormal system
 * which can be used by student to visualize their work on screen
 * or find the right answers and equation
 * thus helping students to understand how the orthonormal system
 * works. It can be used by teacher too.
 * 
 * @author Naji Kadri
 *
 */


public class CoordinatesFrame extends JFrame{
	
	private ArrayList<Plot> points; //default plots
	private ArrayList<Line> lines; //default lines
	private CoordinatesPanel panel;//the Coordnate's panel
	private JMenuBar bar; //the tool-bar
	private JMenu fileMenu; // the file menu
	private JMenu editMenu; // the edit menu
	private JMenu viewMenu; // the view menu
	private JMenu resMenu; // the resolution menu
	private JMenu extraMenu; // the extra menu
	private JMenuItem New; // the new action
	private JMenuItem open; // the open action
	private JMenuItem save; // the save action
	private JMenuItem saveAs; // the save as action
	private JMenuItem about; // the about action
	private JMenuItem addCoo; // add coordinates action
	private JMenuItem addLine; //add line action
	private JMenuItem deleteCoo; //delete a coordinate action
	private JMenuItem deleteLine; //delete a line action
	private JMenuItem setScale; //set scale for drawing
	private JMenuItem viewPlot; // view specific plot action
	private JMenuItem viewLine; //view specific line action
	private JMenuItem clear; //clear panel action
	private JMenuItem smallRes; //small resolution action
	private JMenuItem mediumRes;// medium resolution action
	private JMenuItem largeRes; //large resolution action
	private JMenuItem scaleView; //view scale action
	private JMenuItem parallel;//check if parallel action
	private JMenuItem midpoint;//draw midpoint action
	private JMenuItem plotsLine;//draw line of two plots action
	private File currFile; //current folder (Project) name
	private File dir; //the default directory of the software
	private ObjectInputStream input; //the input stream
	private ObjectOutputStream output; //the output stream
	
	public CoordinatesFrame () {
		super("ONSA"); //name the frame
		
		try { //set the Look&Feel to Nimbus
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		//initialize the private variables
		
		points = new ArrayList<Plot>();
		lines = new ArrayList<Line>();
		panel = new CoordinatesPanel(points,lines,this, 20);
		//create new directory if not found
		dir = new File(System.getenv("APPDATA")+"\\ONSA\\");
		if (! dir.exists()) {
			dir.mkdirs();
		}
		//create default project if not found
		currFile = new File(dir+"\\default");
	    String title = currFile.getName();
	    if (!currFile.exists()) {
	    	try {
	    		System.out.println("Files Created");
				currFile.mkdirs();
				File plots = new File(currFile+"\\plots.dat");
				File lines = new File(currFile+"\\lines.dat");
				plots.createNewFile();
				lines.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	    }
	    
	    this.setTitle("ONSA - "+title); //rename the frame to ONSA and the project's name
		
	    //add the panel to the JFrame
		add(panel, BorderLayout.CENTER);
		
		//add the tool-bar and its features
		bar = new JMenuBar();
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');
		editMenu = new JMenu("Edit");
		editMenu.setMnemonic('e');
		open = new JMenuItem("Open");
		open.setMnemonic('o');
		save = new JMenuItem("Save");
		save.setMnemonic('s');
		about = new JMenuItem("About");
		about.setMnemonic('a');
		addCoo = new JMenuItem("Add Plot");
		addCoo.setMnemonic('p');
		deleteCoo = new JMenuItem("Delete Plot");
		deleteCoo.setMnemonic('d');
		addLine = new JMenuItem("Add Line");
		addLine.setMnemonic('l');
		deleteLine = new JMenuItem("Delete Line");
		deleteLine.setMnemonic('e');
		setScale = new JMenuItem("Set Scale");
		setScale.setMnemonic('s');
		clear = new JMenuItem("Clear");
		clear.setMnemonic('c');
		saveAs = new JMenuItem("Save as");
		saveAs.setMnemonic('v');
		New = new JMenuItem("New");
		New.setMnemonic('n');
		fileMenu.add(New);
		fileMenu.add(open);
		fileMenu.add(save);
		fileMenu.addSeparator();
		fileMenu.add(saveAs);
		fileMenu.addSeparator();
		fileMenu.add(about);
		editMenu.add(addCoo);
		editMenu.add(deleteCoo);
		editMenu.addSeparator();
		editMenu.add(addLine);
		editMenu.add(deleteLine);
		editMenu.addSeparator();
		editMenu.add(setScale);
		editMenu.add(clear);
		viewMenu = new JMenu ("View");
		viewMenu.setMnemonic('v');
		viewPlot = new JMenuItem("Plot");
		viewPlot.setMnemonic('p');
		viewLine = new JMenuItem ("Line");
		viewLine.setMnemonic('l');
		scaleView = new JMenuItem("Scale");
		scaleView.setMnemonic('c');
		viewMenu.add(viewPlot);
		viewMenu.addSeparator();
		viewMenu.add(viewLine);
		viewMenu.addSeparator();
		viewMenu.add(scaleView);
		resMenu = new JMenu("Resolution");
		resMenu.setMnemonic('r');
		smallRes = new JMenuItem("640x480");
		mediumRes = new JMenuItem("800x600");
		largeRes = new JMenuItem("1024x768");
		resMenu.add(smallRes);
		resMenu.addSeparator();
		resMenu.add(mediumRes);
		resMenu.addSeparator();
		resMenu.add(largeRes);
		extraMenu = new JMenu("Extra");
		extraMenu.setMnemonic('x');
		parallel = new JMenuItem ("Parallel");
		parallel.setMnemonic('p');
		midpoint = new JMenuItem("Midpoint");
		midpoint.setMnemonic('m');
		plotsLine = new JMenuItem("Plots Line");
		plotsLine.setMnemonic('l');
		extraMenu.add(parallel);
		extraMenu.addSeparator();
		extraMenu.add(midpoint);
		extraMenu.add(plotsLine);
		bar.add(fileMenu);
		bar.add(editMenu);
		bar.add(viewMenu);
		bar.add(resMenu);
		bar.add(extraMenu);
		setJMenuBar(bar);
		
		//make default operations for the JFrame & JPanel
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1024, 768);
		this.setResizable(false);
		getPanel().getXOX();
		getPanel().getYOY();
		panel.updateScales(); //set scales & refresh
		
		
		// the actions of the JMenuItems (Tool-bar items)
		addCoo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) throws NumberFormatException {
				String name = JOptionPane.showInputDialog(CoordinatesFrame.this,"Set the plot name:");
				String lineName = JOptionPane.showInputDialog(CoordinatesFrame.this, "Set the line for this coordinates");
		        Line line = getPanel().getLine(lineName);
				int x = Integer.parseInt(JOptionPane.showInputDialog(CoordinatesFrame.this, "Set the abscissa"));
				int y = Integer.parseInt(JOptionPane.showInputDialog(CoordinatesFrame.this, "Set the ordinate"));
				try {
				Plot p = new Plot (line,x,y,name);
				getPanel().addPlot(p);
				getPanel().repaint();
				}
				catch(IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(CoordinatesFrame.this,iae.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
				}
				
			}
			
		});
		
		addLine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) throws NumberFormatException {
				String name = JOptionPane.showInputDialog(CoordinatesFrame.this,"Set the line name:");
				int slp = Integer.parseInt(JOptionPane.showInputDialog(CoordinatesFrame.this, "Set the line's slope"));
				int tm= Integer.parseInt(JOptionPane.showInputDialog(CoordinatesFrame.this, "Set the line's time shift"));
				try {
				  Line l = new Line(slp,tm,name);
				  panel.addLine(l);
				  panel.updateScales();
				  panel.repaint();
				}
				catch(IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(CoordinatesFrame.this,iae.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
				}
				
			}
			
		});
		
		deleteLine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String lineName = JOptionPane.showInputDialog(CoordinatesFrame.this, "Write the name of the line");
				Line l = panel.getLine(lineName);
				panel.deleteLine(l);
				panel.repaint();
			}
			
		});
		
		deleteCoo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String plotName = JOptionPane.showInputDialog(CoordinatesFrame.this, "Write the name of the plot");
			    panel.deletePlot(plotName);
			    panel.repaint();
			}
			
		});
		
		viewPlot.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String plotName = JOptionPane.showInputDialog(CoordinatesFrame.this, "Write the name of the plot");
				Plot pl = panel.getPlot(plotName);
				String info = String.format("Plot Name: %s, Line Name: %s, Coordinates(%.0f,%.0f)", pl.getName(),pl.getLine().getName(),pl.getX(),pl.getY());
				JOptionPane.showMessageDialog(CoordinatesFrame.this, info, "Plot View", JOptionPane.INFORMATION_MESSAGE);
				
				
			}
			
		});
		viewLine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String lineName = JOptionPane.showInputDialog(CoordinatesFrame.this, "Write the name of the line");
				Line l = panel.getLine(lineName);
				String info = String.format("Line name: %s, Slope: %.0f, Time Shift: %.0f",l.getName(),l.getSlope(),l.getTimeShift());
				JOptionPane.showMessageDialog(CoordinatesFrame.this, info, "Line View", JOptionPane.INFORMATION_MESSAGE);
				
				
			}
			
		});
		
		scaleView.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String info = String.format("Current Scale: 1 cm per %d pixels", panel.getScale());
				JOptionPane.showMessageDialog(CoordinatesFrame.this,info , "Scale View", JOptionPane.INFORMATION_MESSAGE);
				
				
			}
			
		});
		
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
             panel.clear();
             panel.repaint();
			}
			
		});
		
		setScale.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
             panel.setScale(Integer.parseInt(JOptionPane.showInputDialog(CoordinatesFrame.this, "Set Scale cm per pixel. Default: 20 pixels")));
             panel.updateScales();
             panel.repaint();
			}
			
		});
		
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(CoordinatesFrame.this, "Orthonormal System software created by Naji Kadri. \nAll Rights Reserved © 2015", "About", JOptionPane.INFORMATION_MESSAGE);
				
				
			}
			
		});
		
		smallRes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
                changeRes(640,480);
			}
			
		});
		
		mediumRes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
                changeRes(800,600);
			}
			
		});
		
		largeRes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
                changeRes(1024,768);
			}
			
		});
		
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
                
			}
			
		});
		
		plotsLine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String lineName = JOptionPane.showInputDialog(CoordinatesFrame.this, "Write the name of the line");
				String fPlot = JOptionPane.showInputDialog(CoordinatesFrame.this, "Write the name of the first plot");
				String sPlot = JOptionPane.showInputDialog(CoordinatesFrame.this, "Write the name of the second plot");
				Plot f = panel.getPlot(fPlot);
				Plot s = panel.getPlot(sPlot);
				panel.createLine(f, s, lineName);
				panel.repaint();
                
			}
			
		});
		
		parallel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String fLine = JOptionPane.showInputDialog(CoordinatesFrame.this, "Write the name of the first line");
				String sLine = JOptionPane.showInputDialog(CoordinatesFrame.this, "Write the name of the second line");
				Line f = panel.getLine(fLine);
				Line s = panel.getLine(sLine);
                if (panel.isParallel(f, s)) {
                	String info = String.format("Line %s is // to line %s", fLine,sLine);
                	JOptionPane.showMessageDialog(CoordinatesFrame.this,info , "Parallel?", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                	String info = String.format("Line %s is not // to line %s", fLine,sLine);
                	JOptionPane.showMessageDialog(CoordinatesFrame.this,info , "Parallel?", JOptionPane.INFORMATION_MESSAGE);
                }
                
			}
			
		});
		
		midpoint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String plotName = JOptionPane.showInputDialog(CoordinatesFrame.this, "Write the name of the new plot");
				String fPlot = JOptionPane.showInputDialog(CoordinatesFrame.this, "Write the name of the first plot");
				String sPlot = JOptionPane.showInputDialog(CoordinatesFrame.this, "Write the name of the second plot");
				Plot f = panel.getPlot(fPlot);
				Plot s = panel.getPlot(sPlot);
				panel.createMid(f, s, plotName);
				panel.repaint();
                
			}
			
		});
		
		New.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String fileName = JOptionPane.showInputDialog(CoordinatesFrame.this, "Name new file.\nIt will overwrite if it exists before.");
				if (! fileName.equals(""))
	
				try {
					currFile = new File(dir+"\\"+fileName);
					currFile.mkdir();
					File plotFile = new File(currFile+"\\plots.dat");
					currFile.mkdirs();
					File plots = new File(currFile+"\\plots.dat");
					File lines = new File(currFile+"\\lines.dat");
					plots.createNewFile();
					lines.createNewFile(); 
					String title = currFile.getName();
					setTitle("ONSA - "+title);
					System.out.println("New Data Created!");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File plots = new File(currFile+"\\plots.dat");
					File lines = new File(currFile+"\\lines.dat");
					output = new ObjectOutputStream(new FileOutputStream(lines));
					output.writeObject(panel.getLines());
					output = new ObjectOutputStream(new FileOutputStream(plots));
					output.writeObject(panel.getPlots());
					System.out.println("Data Saved!");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		saveAs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String fileName = JOptionPane.showInputDialog(CoordinatesFrame.this, "Write new file name to clone this file.");
				if (! fileName.equals(""))
	
				try {
					currFile = new File(dir+"\\"+fileName);
					currFile.mkdirs();
					currFile.mkdirs();
					File plots = new File(currFile+"\\plots.dat");
					File lines = new File(currFile+"\\lines.dat");
					plots.createNewFile();
					lines.createNewFile();
					output = new ObjectOutputStream(new FileOutputStream(lines));
					output.writeObject(panel.getLines());
					output = new ObjectOutputStream(new FileOutputStream(plots));
					output.writeObject(panel.getPlots());
					String title = currFile.getName();
					setTitle("ONSA - "+title);
					System.out.println("Data cloned!");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String fileName = JOptionPane.showInputDialog(CoordinatesFrame.this, "Write the name of a file to open.");
				try {
					currFile = new File(dir+"\\"+fileName);
					String title = currFile.getName();
					File plots = new File(currFile+"\\plots.dat");
					File lines = new File(currFile+"\\lines.dat");
					setTitle("ONSA - "+title);
					input = new ObjectInputStream(new FileInputStream(lines));
					panel.lines = (ArrayList<Line>) input.readObject();
					input = new ObjectInputStream(new FileInputStream(plots));
					panel.plots = (ArrayList<Plot>) input.readObject();
					System.out.println("Data Loaded!");
					panel.repaint();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
		
	}
	
	public CoordinatesPanel getPanel() { //get the panel
		//NOTE: You can use panel. instead of getPanel().
		return panel;
	}
	
	public void changeRes (int width, int height) { //change the resolutions of the screen
		this.setSize(width, height);
		panel.getXOX();
		panel.getYOY();
		panel.repaint();
	}
	
	public void setNewTitle (String s) { //set new title to JFrame
		this.setTitle(s);
	}

}

