package model;

import java.awt.Color;
/**
 * ColorTypeConverter
 * 
 * A helper class provides methods to transform between awt color and JavaFx sence color
 * 
 * @author Rick Mercer
 *
 */
public class ColorTypeConverter {
	/*---------------------------------------------------------------------
	  |  Method:    Fx2Awt
	  |
	  |  Purpose:    transform from FX color to AWT color
	  |                         
	  |  Parameters: javafx.scene.paint.Color
	  |
	  |  Returns:    java.awt.Color
	  *-------------------------------------------------------------------*/
	public static java.awt.Color Fx2Awt(javafx.scene.paint.Color fxColor) {
	    int r = (int) (255 * fxColor.getRed());
	    int g = (int) (255 * fxColor.getGreen());
	    int b = (int) (255 * fxColor.getBlue());
	    java.awt.Color awtColor = new java.awt.Color(r, g, b);
	    return awtColor;
	  } 

	/*---------------------------------------------------------------------
	  |  Method:   Awt2Fx
	  |
	  |  Purpose:    transform from AWT color to FX color
	  |                         
	  |  Parameters: java.awt.Color
	  |
	  |  Returns:    javafx.scene.paint.Color
	  *-------------------------------------------------------------------*/
	  public static javafx.scene.paint.Color Awt2Fx(Color awtColor) {
	    int r = awtColor.getRed();
	    int g = awtColor.getGreen();
	    int b = awtColor.getBlue();
	    javafx.scene.paint.Color fxColor = javafx.scene.paint.Color.rgb(r, g, b);
	    return fxColor;
	  }
}
