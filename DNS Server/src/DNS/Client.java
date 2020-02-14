package DNS;

import java.io.*;
import java.net.*;

class Client {
	
   public static void main(String args[]) throws Exception
   {
	// Creating Input Stream to read input from user
      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      
      DatagramSocket clientSocket = new DatagramSocket();
   // Gets the IP Address
      InetAddress IPAddress = InetAddress.getLocalHost();
      
      byte[] sendData = new byte[1024];
      byte[] receiveData = new byte[1024];
      
      boolean quit = false;
      
      do {
    	  System.out.print("\nClient: ");
	      String sentence = inFromUser.readLine();
	      if (sentence.equalsIgnoreCase("quit")) {
	    	  quit = true;
	    	  continue;
	      }
	      sendData = sentence.getBytes();
	      
	   // Creating a datagram packet that will store the packet we will send
	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
	   // Sending packet to the server
	      clientSocket.send(sendPacket);
	   // Creating a datagram packet that will store the packet we will receive
	      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	   // Getting packet from server
	      clientSocket.receive(receivePacket);
	      
	      String serverData = new String(receivePacket.getData());
	      System.out.println(serverData);
	      
          // This is to reset the buffer each loop
          receiveData = new byte[1024];
          sendData = new byte[1024];
	      
      } while (!quit);
      
      clientSocket.close();
   }
}
