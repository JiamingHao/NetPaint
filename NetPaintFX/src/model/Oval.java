package model;

import java.awt.Point;
import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;
import java.awt.Color;
/**
 * Oval
 * 
 * Oval is the sub class extends from PaintObject, which draws a oval   
 * 
 * @author Jiaming Hao
 *
 */
@SuppressWarnings("serial")
public class Oval extends PaintObject implements Serializable{

	public Oval(Color c, Point a, Point b) {
		super(c, a, b);
	}
	/*---------------------------------------------------------------------
	  |  Method:     draw
	  |
	  |  Purpose:    draw a fill Oval on the canvas 
	  |                         
	  |  Parameters: GraphicsContext gc
	  |
	  |  Returns:    None
	  *-------------------------------------------------------------------*/
	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(ColorTypeConverter.Awt2Fx(color));

		if (start.getX() > end.getX() && start.getY() > end.getY()) {
			gc.fillOval(end.getX(), end.getY(), start.getX() - end.getX(), start.getY() - end.getY());

		} else if (start.getX() < end.getX() && start.getY() < end.getY()) {
			gc.fillOval(start.getX(), start.getY(), end.getX() - start.getX(), end.getY() - start.getY());

		} else if (start.getY() < end.getY()) {
			gc.fillOval(end.getX(), start.getY(), start.getX() - end.getX(), end.getY() - start.getY());

		} else if (start.getY() > end.getY()) {
			gc.fillOval(start.getX(), end.getY(), end.getX() - start.getX(), start.getY() - end.getY());
		}

	}
	/*---------------------------------------------------------------------
	  |  Method:     compareTo
	  |
	  |  Purpose:    return 0 if two ovals are "equal" (have exactly the start 
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
