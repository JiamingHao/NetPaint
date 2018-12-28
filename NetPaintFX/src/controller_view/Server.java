package controller_view;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import model.PaintObject;
/**
 * A server waiting for clients to connect, once there is a client 
 * connects to it successfully, then the server will start this 
 * client in a new thread with all the shapes drawn by other clients
 * before
 * 
 * @author Jiaming Hao
 * 
 */
public class Server {

	private static List<ObjectOutputStream> outputStreams = new Vector<>();//store all the outputStreams to clients
	private static Vector<PaintObject> allPaintObjects = new Vector<>();//store all the shapes clients draw
	public static void main(String[] args)
	{
	    System.out.println("Server started");//debug
	    try (ServerSocket s = new ServerSocket(4000)) {
			int i = 1;
			//an infinite loop always wait for client to connect
			while (true) {
				Socket incoming = s.accept();
				
				ObjectOutputStream outputToClient = new ObjectOutputStream(incoming.getOutputStream());
				outputStreams.add(outputToClient);
				
				System.out.println("Spawing" + i);//debug, keep record of how many clients are current connected
				Runnable r = new ThreadedEchoHandler(incoming, allPaintObjects);
				Thread t = new Thread(r);
				t.start();
				//System.out.println("Size of Vector:" + allPaintObjects.size());//debug
				i++;
			}
		}
		catch(IOException e)
		{
			//e.printStackTrace();
		}
	
	}

	/*===============================================================
	|Class name:  ThreadedEchoHandler
	|
	|Description:  Whenever a client connects to the server, an object 
	|			   of this class will be created, dealing with the input
	|			   from clients and also write something to the them
	|
	*==============================================================*/
	private static class ThreadedEchoHandler implements Runnable
	{

		private Socket incoming;
		//private ObjectOutputStream output;
		private ObjectInputStream input;
		private Vector<PaintObject> PaintObjectList;//refer to the vector of all paintObjects in server
		
		public ThreadedEchoHandler(Socket incomingSocket, Vector<PaintObject> list )
		{
			incoming = incomingSocket;
			PaintObjectList = list;
			    try {
					//output = new ObjectOutputStream(incoming.getOutputStream());
					input = new ObjectInputStream(incoming.getInputStream());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			
		}
		/*---------------------------------------------------------------------
		  |  Method:     run
		  |
		  |  Purpose:    a method required by Runnable interface
		  |                         
		  |  Parameters: None
		  |
		  |  Returns:    None
		  *-------------------------------------------------------------------*/
		@Override
		public void run() {
			Object temp;
			writeBackToClients();//show all the paintObjects drawn by other clients before
			try {
				while(true)// whenever there is something new to read
				{// it indicates that a new paintObject has been added by one of the client, so the server
				 // needs to update all the clients to make sure that all clients have this new PaintObject!
					//System.out.println("Thread: Enter the loop");//debug
					temp = input.readObject();
					if(temp!=null)//if there is any new paintObject created by clients
					{
						if(temp instanceof PaintObject)
						{
							//System.out.println("Server: new PaintObject written in detected!");//debug
							PaintObjectList.add((PaintObject) temp);//add the new one to the server list
						}
						
					}
					//System.out.println("Size of Vector:" + PaintObjectList.size());//debug
					writeBackToClients();//update the server in all clients
					}
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		 }
		/*---------------------------------------------------------------------
		  |  Method:     writeBackToClients
		  |
		  |  Purpose:    update the shapes on every clients' canvas
		  |                         
		  |  Parameters: None
		  |
		  |  Returns:    None
		  *-------------------------------------------------------------------*/
		private void writeBackToClients()
		{
			for(int i=0;i<outputStreams.size();i++)
			{
				try {
					outputStreams.get(i).reset();
					outputStreams.get(i).writeObject(PaintObjectList);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
	



