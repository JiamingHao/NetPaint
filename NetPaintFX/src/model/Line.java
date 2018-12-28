package model;

import java.awt.Point;
import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;
import java.awt.Color;
/**
 * Line
 * 
 * Line is the sub class extends from PaintObject, which draws a line   
 * 
 * @author Jiaming Hao
 *
 */
@SuppressWarnings("serial")
public class Line extends PaintObject implements Serializable{

	public Line(Color c, Point a, Point b) {
		super(c, a, b);
	}

	/*---------------------------------------------------------------------
	  |  Method:     draw
	  |
	  |  Purpose:    draw a stroke line on the canvas 
	  |                         
	  |  Parameters: GraphicsContext gc
	  |
	  |  Returns:    None
	  *-------------------------------------------------------------------*/
	@Override
	public void draw(GraphicsContext gc) {
		gc.setLineWidth(1);
		gc.setStroke(ColorTypeConverter.Awt2Fx(color));
		gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
	}
	
	/*---------------------------------------------------------------------
	  |  Method:     compareTo
	  |
	  |  Purpose:    return 0 if two lines are "equal" (have exactly the start 
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
