package DNS;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class tldServer {
	
	public static void main(String args[]) throws Exception {
		   
	 	DatagramSocket serverSocket = new DatagramSocket(9878);
	 	
	    byte[] receiveData = new byte[1024];
	    byte[] sendData = new byte[1024];
	    String data = null;
	    
	    while(true) {
	    	// Creating a datagram packet that will store the packet we will receive
	          DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	        // Getting packet from client
	          serverSocket.receive(receivePacket);
	
	          String sentence = new String( receivePacket.getData());
	          System.out.println("Client Requested: " + sentence.trim());
	        // This gets the IP Address of the client
	          InetAddress IPAddress = receivePacket.getAddress();
	        // This gets the port number of the client
	          int port = receivePacket.getPort();
	          
	          
	          boolean found = false;
	          String hostName, ip = null, query = null, canonicalName = null;
	          Scanner file;
	          try {
	        	  file = new Scanner (new File("C:\\Users\\master\\Desktop\\DNS Server\\src\\DNS\\TLD_dns_table.txt"));
	        	  while (file.hasNext()) {
            		  hostName = file.next();
            		  ip = file.next();
            		  canonicalName = file.next();
            		  if (canonicalName.equals("none")) {
            			  query = "A";
            		  } else {
            			  query = "A, CNAME\nCanonical Name = " + canonicalName;
            		  }
            		  if(hostName.equalsIgnoreCase(sentence.trim())) {
            			  data = "Reply from TLD DNS Server:\nURL = " + sentence.trim() + "\nIP Address = " + ip + "\nQuery Type = " + query;
            			  found = true;
            			  break;
            		  }
	        	  }
	        	  if (found == false) {
	        			// Creating Input Stream to read input from user
	        	      DatagramSocket clientSocket = new DatagramSocket();
	        	      
	        	      sendData = sentence.getBytes();
	        	      
	        	   // Creating a datagram packet that will store the packet we will send
	        	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9879);
	        	   // Sending packet to the server
	        	      clientSocket.send(sendPacket);
	
	        	   // Getting packet from server
	        	      clientSocket.receive(receivePacket);
	        	      
	        	      data = new String(receivePacket.getData());
	        	      
	        	      clientSocket.close();
	        	  }
	          } catch (FileNotFoundException e) {
	        	  data = "TLD file not found.";
	          }
	          
	       // Converts string to bytes
	          sendData = data.getBytes();
	        // Creating a datagram packet that will store the packet we will send
	          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	        // Sending packet to the client
	          serverSocket.send(sendPacket);
	          // This is to reset the buffer each loop
	          receiveData = new byte[1024];
	          sendData = new byte[1024];
	          data = null;
	       }
	}
}
