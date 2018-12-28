package model;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Picture
 * 
 * Picture is the sub class extends from PaintObject, which draws a picture   
 * 
 * @author Jiaming Hao
 *
 */
@SuppressWarnings("serial")
public class Picture extends PaintObject implements Serializable{

	
	protected String picName;
	
	public Picture(Point a, Point b, String picName) {
		super(Color.WHITE, a, b);
		this.picName = picName;
	
	}
	/*---------------------------------------------------------------------
	  |  Method:     draw
	  |
	  |  Purpose:    draw a Doge picture on the canvas 
	  |                         
	  |  Parameters: GraphicsContext gc
	  |
	  |  Returns:    None
	  *-------------------------------------------------------------------*/
	@Override
	public void draw(GraphicsContext gc) {
		
		if(start.getX() > end.getX() && start.getY() > end.getY())
		{
			gc.drawImage(new Image("file:NetPaintFx/images/" + picName,false), end.getX(), end.getY(), start.getX()-end.getX(), start.getY()-end.getY());
		}	
		else if(start.getX() < end.getX() && start.getY() < end.getY())
		{
			gc.drawImage(new Image("file:NetPaintFx/images/" + picName,false), start.getX(), start.getY(), end.getX() - start.getX(), end.getY() - start.getY());
			
		}
		else if (start.getY() < end.getY())
		{
		    gc.drawImage(new Image("file:NetPaintFx/images/" + picName,false), end.getX(), start.getY(), start.getX()-end.getX(), end.getY() - start.getY());
		    
		}
		else if(start.getY() > end.getY())
		{
			gc.drawImage(new Image("file:NetPaintFx/images/" + picName,false), start.getX(), end.getY(), end.getX()-start.getX(),start.getY()-end.getY());
			
		}
	}
	
	/*---------------------------------------------------------------------
	  |  Method:     compareTo
	  |
	  |  Purpose:    return 0 if two pictures are "equal" (have exactly the start 
	  |              and end point), otherwise return 1 
	  |                         
	  |  Parameters: PaintObject po
	  
	  |  Returns:    int 
	  *-------------------------------------------------------------------*/
	@Override
	public int compareTo(PaintObject po) {
		if(po.start.equals(this.start)&&po.end.equals(this.end))
		{
			return 0;
		}
		return 1;
	}

}
