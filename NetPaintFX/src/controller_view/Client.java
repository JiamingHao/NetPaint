package controller_view;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import java.awt.Color;
import javafx.stage.Stage;
import model.ColorTypeConverter;
import model.Line;
import model.Oval;
import model.PaintObject;
import model.Picture;
import model.Rectangle;

/**
 * A GUI for Netpaint that has all paint objects drawn upon a Canvas. This file
 * also represents the controller as it controls how paint objects are drawn and
 * sends new paint objects to the server. All Client objects also listen to the
 * server to read the Vector of PaintObjects and repaint every time any client
 * adds a new one.
 * 
 * @author Jiaming Hao
 * 
 */
public class Client extends Application {

	public static void main(String[] args) {
		
		launch(args);
	}
	
	private BorderPane all;
	private Canvas canvas;
	private static GraphicsContext gc;
	private HBox hbox;
	//four radio buttons provide different shape choices
	private RadioButton LineChoice;
	private RadioButton RectChoice;
	private RadioButton OvalChoice;
	private RadioButton PicChoice;
	private ToggleGroup group;
	private PaintObject po;
	private String shape;
	private int startX;
	private int startY;
	// a vector records all the paintObject drawn on the canvas
	private static Vector<PaintObject> allPaintObjects;
	private ColorPicker colorPicker;
	//indicate the current using color
	private Color color;
	private int numOfClick;
	private Vector<PaintObject> fromServer;

	//------------------------------netWork Part------------------------------------------
		private ObjectOutputStream output;
		private ObjectInputStream input;
		private Socket server;
	//-----------------------------------------------------------------------------------
	/*---------------------------------------------------------------------
	  |  Method: start
	  |
	  |  Purpose:    A method needs to be override from Application, mainly set 
	  |              up the main components of the controller GUI
	  |  
	  |  Parameters: Stage primaryStage
	  |
	  |  Returns:    None
	  *-------------------------------------------------------------------*/
	@Override
	public void start(Stage primaryStage) throws Exception {
		all = new BorderPane();
		canvas = new Canvas(800, 550);
		gc = canvas.getGraphicsContext2D();
		hbox = new HBox();//line up the radio buttons and color picker in horizontal 
		allPaintObjects = new Vector<>();
		colorPicker = new ColorPicker(ColorTypeConverter.Awt2Fx(Color.RED));
		color = Color.RED;//set the default of color picker to red
		colorPicker.setOnAction(new ColorHandler());
		numOfClick = 0;
		LineChoice = new RadioButton("Line");
		RectChoice = new RadioButton("Rectangle");
		OvalChoice = new RadioButton("Oval");
		PicChoice = new RadioButton("Picture");
		group = new ToggleGroup();
		setRadioButtons();
		drawing();
		hbox.setPadding(new Insets(15, 50, 15, 50));
		hbox.setSpacing(50);
		hbox.getChildren().addAll(LineChoice, RectChoice, OvalChoice, PicChoice, colorPicker);
		all.setCenter(canvas);
		all.setBottom(hbox);
		Scene scene = new Scene(all, 800, 650);
		primaryStage.setScene(scene);
		primaryStage.show();
		makeConnection();
		
	}

	/*===============================================================
	|Class name:  ColorHandler
	|
	|Description:  Change the paint color when user choose color 
	|              through colorPicker
	*==============================================================*/
	private class ColorHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			color = ColorTypeConverter.Fx2Awt(colorPicker.getValue());
		}
	}
	/*---------------------------------------------------------------------
	  |  Method: setRadioButtons
	  |
	  |  Purpose:    A helper method set up the radio buttons, provide four 
	  |  			 shapes, which are line, rectangle, oval and picture
	  |  Parameters: None
	  |
	  |  Returns:    None
	  *-------------------------------------------------------------------*/
	private void setRadioButtons() {
		LineChoice.setToggleGroup(group);
		LineChoice.setSelected(true);
		shape = "Line"; // the default shape drawn is line
		RectChoice.setToggleGroup(group);
		OvalChoice.setToggleGroup(group);
		PicChoice.setToggleGroup(group);

		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
				RadioButton text = (RadioButton) t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
				//System.out.println("Selected Radio Button - " + text.getText());//debug
				shape = text.getText();
			}
		});
	}
	/*---------------------------------------------------------------------
	  |  Method:     drawing
	  |
	  |  Purpose:    A helper method make the canvas detect mouse event to 
	  |              produce "ghost" image when user first click on
	  |  			 it, and actually add the image when user click 
	  |              the mouse again
	  |           
	  |  Parameters: None
	  |
	  |  Returns:    None
	  *-------------------------------------------------------------------*/
	private void drawing() {
		gc.setFill(ColorTypeConverter.Awt2Fx(Color.WHITE));
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		canvas.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {

			/*---------------------------------------------------------------------
			  |  Method:     handle
			  |
			  |  Purpose:    A helper method handle differently according to different
			  |              mouse event
			  |           
			  |  Parameters: MouseEvent mouseEvent
			  |
			  |  Returns:    None
			  *-------------------------------------------------------------------*/
			@Override
			public synchronized void handle(MouseEvent mouseEvent) {
				//System.out.println("x: " + mouseEvent.getX() + "y: " + mouseEvent.getY());//debug
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
					//System.out.println("Detect mouse click"); // debug
					if(numOfClick == 0)
					{
						//System.out.println("The first click!");//debug
						startX = (int) mouseEvent.getX();
						startY = (int) mouseEvent.getY();
						numOfClick++;
					}
					else if(numOfClick == 1)
					{
						//System.out.println("The second click!");//debug
						gc.setFill(ColorTypeConverter.Awt2Fx(Color.WHITE));
						gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
						if (shape.equals("Rectangle"))
							po = new Rectangle(color, new Point(startX, startY),
									new Point((int) (mouseEvent.getX()), (int) mouseEvent.getY()));
						else if (shape.equals("Line"))
							po = new Line(color, new Point(startX, startY),
									new Point((int) (mouseEvent.getX()), (int) mouseEvent.getY()));
						else if (shape.equals("Oval"))
							po = new Oval(color, new Point(startX, startY),
									new Point((int) (mouseEvent.getX()), (int) mouseEvent.getY()));
						else if (shape.equals("Picture"))
							po = new Picture(new Point(startX, startY),
									new Point((int) (mouseEvent.getX()), (int) mouseEvent.getY()), "doge.jpeg");
						
					   for(int i=0;i<allPaintObjects.size();i++)
					    {
					    	allPaintObjects.get(i).draw(gc);
					    }
					   po.draw(gc);
					   allPaintObjects.add(po);
					   numOfClick = 0;
					   //------------------------------the netWork part--------------------------------------
					   try {
						    //output.reset();
						   if(output!=null)
							output.writeObject(po);
							//System.out.println("client: Write Object to Server!");//debug
						} catch (IOException e) {
						
							//e.printStackTrace();
						}
					   //------------------------------------------------------------------------------------
					}
				}
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
					if (numOfClick == 1) {
						gc.setFill(ColorTypeConverter.Awt2Fx(Color.WHITE));
						gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());//clean the canvas
						for (int i = 0; i < allPaintObjects.size(); i++) {
							allPaintObjects.get(i).draw(gc);//draw everything that has been drawn again
						}
						if (shape.equals("Rectangle"))
							po = new Rectangle(color, new Point(startX, startY),
									new Point((int) (mouseEvent.getX()), (int) mouseEvent.getY()));
						else if (shape.equals("Line"))
							po = new Line(color, new Point(startX, startY),
									new Point((int) (mouseEvent.getX()), (int) mouseEvent.getY()));
						else if (shape.equals("Oval"))
							po = new Oval(color, new Point(startX, startY),
									new Point((int) (mouseEvent.getX()), (int) mouseEvent.getY()));
						else if (shape.equals("Picture"))
							po = new Picture(new Point(startX, startY),
									new Point((int) (mouseEvent.getX()), (int) mouseEvent.getY()), "doge.jpeg");
						po.draw(gc);
					}
				}
			}
		});
	}
	/*---------------------------------------------------------------------
	  |  Method:     makeConnection
	  |
	  |  Purpose:    set up the connection between a server and a client 
	  |              in a new thread
	  |                         
	  |  Parameters: None
	  |
	  |  Returns:    None
	  *-------------------------------------------------------------------*/
	private void makeConnection() {
		try {
			server = new Socket("localhost", 4000);
			input = new ObjectInputStream(server.getInputStream());
			output = new ObjectOutputStream(server.getOutputStream());
		} catch (IOException e) {

		}
		ListenForServerUpdates listener = new ListenForServerUpdates();
		Thread thread = new Thread(listener);
		thread.setDaemon(true);
		thread.start();
	}

	
	/*===============================================================
	|Class name:  ListenForServerUpdates
	|
	|Description:  Listen for the Server to read a modified Vector<PaintObject>
	|			   Because JavaFX is not Thread-safe. This must be started in 
	|			   a new Thread as a Task from the javafx.concurrent Package.
	|
	*==============================================================*/
	private class ListenForServerUpdates extends Task<Object>{
		@SuppressWarnings("unchecked")
		@Override
		public void run()
		{
			while(true)//a infinite loop always check if there is something new written by server
			{
				try {
					if((fromServer = (Vector<PaintObject>) input.readObject())!= null)
					{
						//System.out.println("Reading from server");//debug
						//System.out.println("Client: size is: " + fromServer.size());//debug
						//allPaintObjects = new Vector<>();
						//System.out.println("SIZE: "+ allPaintObjects.size());//debug
						for(int i=0;i<fromServer.size();i++)
						{
							boolean alreadyHas = false;
							for(int j=0;j<allPaintObjects.size();j++)
							{
								//following loop checks whether the local canvas already has the shape
								if(fromServer.get(i).compareTo(allPaintObjects.get(j)) == 0)
								{
									alreadyHas = true;
								}
							}
							if (!alreadyHas) {//only add and draw the new shape
								fromServer.get(i).draw(gc);
								allPaintObjects.add(fromServer.get(i));
							}
						}
					}
				} catch (ClassNotFoundException e) {
					//e.printStackTrace();
				} catch (IOException e) {
					//e.printStackTrace();
				}
			}
		}
		
		/*---------------------------------------------------------------------
		  |  Method:     call
		  |
		  |  Purpose:    A method needed to be overridden in order to pass
		  |              compilation , but it is not used for this project
		  |                         
		  |  Parameters: None
		  |
		  |  Returns:    Object
		  *-------------------------------------------------------------------*/
		@Override
		protected Object call() throws Exception {
			// Put the networking code here that has readObject waiting for server input.
			// This includes a while(true) loop
			return null;
		}
		
		
	}
}

