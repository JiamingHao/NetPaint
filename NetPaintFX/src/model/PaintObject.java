package model;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;

/**
 * PaintObject
 * 
 * PaintObject is the superclass of paint objects Oval, Rectangle, Line, and
 * Picture that can be drawn using a Color, two Points, and some Canvas methods.   
 * 
 * @author Jiaming Hao
 *
 */
@SuppressWarnings("serial")
public abstract class PaintObject implements Serializable{

	protected Point start;//the start x,y coordinates of the shape
	protected Point end;// the end x, y coordinates of the shape
	protected Color color;
	
	public PaintObject(Color c, Point a, Point b)
	{
		this.color = c;
		this.start = a;
		this.end = b;
	}
	abstract public void draw(GraphicsContext gc);
	abstract public int compareTo(PaintObject po);
}