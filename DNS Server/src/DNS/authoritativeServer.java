package DNS;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class authoritativeServer {
	
	public static void main(String args[]) throws Exception {
		   
	 	DatagramSocket serverSocket = new DatagramSocket(9879);
	 	
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
	       // This gets the IP Address of the server
	          InetAddress myIPAddress = InetAddress.getLocalHost();
	        // This gets the port number of the client
	          int port = receivePacket.getPort();
	          
	          
	          boolean found = false;
	          String hostName, ip = null, query = null, canonicalName = null;
	          Scanner file;
	          try {
	        	  file = new Scanner (new File("C:\\Users\\master\\Desktop\\DNS Server\\src\\DNS\\authoritative_dns_table.txt"));
	        	  while (file.hasNext()) {
            		  hostName = file.next();
            		  ip = file.next();
            		  canonicalName = file.next();
            		  if (canonicalName.equals("none")) {
            			  query = "A, NS";
            		  } else {
            			  query = "A, NS, CNAME\nCanonical Name = " + canonicalName;
            		  }
            		  if(hostName.equalsIgnoreCase(sentence.trim())) {
            			  data = "Reply from Authoritative DNS Server:\nURL = " + sentence.trim() + "\nIP Address = " + ip + "\nQuery Type = " + query + "\nName: authoritative_dns_table.txt\nIP: " + myIPAddress;
            			  found = true;
            			  break;
            		  }
	        	  }
	        	  if (found == false) {
	        	      
	        	      data = "IP Address not found.";
	        	      
	        	  }
	          } catch (FileNotFoundException e) {
	        	  data = "Authoritative file not found.";
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
