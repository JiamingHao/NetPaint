package model;

import java.awt.Point;
import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;
import java.awt.Color;
/**
 * Rectangle
 * 
 * Rectangle is the sub class extends from PaintObject, which draws a Rectangle   
 * 
 * @author Jiaming Hao
 *
 */
@SuppressWarnings("serial")
public class Rectangle extends PaintObject implements Serializable{

	
	protected int width;
	protected int height;
	protected Rectangle rc;

	public Rectangle(Color c, Point a, Point b) {
		super(c, a, b);
		this.color = c;
	}

	/*---------------------------------------------------------------------
	  |  Method:     draw
	  |
	  |  Purpose:    draw a Rectangle on the canvas 
	  |                         
	  |  Parameters: GraphicsContext gc
	  |
	  |  Returns:    None
	  *-------------------------------------------------------------------*/
	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(ColorTypeConverter.Awt2Fx(color));
		if (start.getX() > end.getX() && start.getY() > end.getY()) {
			gc.fillRect(end.getX(), end.getY(), start.getX() - end.getX(), start.getY() - end.getY());

		} else if (start.getX() < end.getX() && start.getY() < end.getY()) {
			gc.fillRect(start.getX(), start.getY(), end.getX() - start.getX(), end.getY() - start.getY());

		} else if (start.getY() < end.getY()) {
			gc.fillRect(end.getX(), start.getY(), start.getX() - end.getX(), end.getY() - start.getY());

		} else if (start.getY() > end.getY()) {
			gc.fillRect(start.getX(), end.getY(), end.getX() - start.getX(), start.getY() - end.getY());
		}

	}

	/*---------------------------------------------------------------------
	  |  Method:     compareTo
	  |
	  |  Purpose:    return 0 if two Rectangles are "equal" (have exactly the start 
	  |              and end point and the color), otherwise return 1 
	  |                         
	  |  Parameters: PaintObject po
	  
	  |  Returns:    int 
	  *-------------------------------------------------------------------*/
	@Override
	public int compareTo(PaintObject po) {
		if(po.color.equals(this.color)&& po.start.equals(this.start)&&po.end.equals(this.end))
		{
			return 0;
		}
		return 1;
	}


}
