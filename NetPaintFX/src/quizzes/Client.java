package quizzes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

// Take Home Quiz: Client/Server
public class Client {

	public static void main(String[] args) {
		try {
			// Connect to a Server and get the two streams from the server
			System.out.println("Client started");
			Socket server = new Socket("localhost", 4000);
			System.out.println("This Client found a server on port 4000");
			// Do some IO with the server
			ObjectOutputStream output = new ObjectOutputStream(server.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(server.getInputStream());
			// Do some IO with the server
           System.out.print("Enter a message: ");
           Scanner in = new Scanner(System.in);
           while(in.hasNextLine())
           {
        	   String temp = in.nextLine();
        	   output.writeObject(temp.trim());
        	   String messageFromServer = (String) input.readObject();
        	   System.out.println(messageFromServer);
        	   if(temp.trim().toLowerCase().equals("quit"))
        		   break;
        	   System.out.print("Enter a message: ");
           }
		    System.out.println("You entered the magic word");
		    in.close();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
