package DNS;

import java.io.*;
import java.net.*;
import java.util.*;


public class ThreadServer extends Thread {
	
		protected DatagramSocket serverSocket = null;
		@Override
		public void run(){
			try {
				serverSocket = new DatagramSocket(9876);
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
         	
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            String data = null;
	           while(true) {
	            	// Creating a datagram packet that will store the packet we will receive
	                  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	                // Getting packet from client
	                  try {
						serverSocket.receive(receivePacket);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	   
	                  String sentence = new String( receivePacket.getData());
	                  System.out.println("Client Requested: " + sentence.trim());
	                // This gets the IP Address of the client
	                  InetAddress IPAddress = receivePacket.getAddress();
	                // This gets the port number of the client
	                  int port = receivePacket.getPort();
	                  
	                  
	                  boolean found = false;
	                  String hostName, ip = null, canonicalName = null, query = null;
	                  Scanner file;
	                  try {
	                	  file = new Scanner (new File("C:\\Users\\master\\Desktop\\DNS Server\\src\\DNS\\local_dns_table.txt"));
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
	                			  data = "Reply from Local DNS Server:\nURL = " + sentence.trim() + "\nIP Address = " + ip + "\nQuery Type = " + query;
	                			  found = true;
	                			  break;
	                		  }
	                	  }
	                	  if (found == false) {
	                			// Creating Input Stream to read input from user
	                	      DatagramSocket clientSocket = new DatagramSocket();
	                	      
	                	      sendData = sentence.getBytes();
	                	      
	                	   // Creating a datagram packet that will store the packet we will send
	                	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9877);
	                	   // Sending packet to the server
	                	      clientSocket.send(sendPacket);

	                	   // Getting packet from server
	                	      clientSocket.receive(receivePacket);
	                	      
	                	      data = new String(receivePacket.getData());
	                	
	                	      clientSocket.close();
	                	  }
	                  } catch (FileNotFoundException e) {
	                	  data = "Local file not found.";
	                  } catch (SocketException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
	                  
	                  System.out.println(data.trim());
	               // Converts string to bytes
	                  sendData = data.getBytes();
	                // Creating a datagram packet that will store the packet we will send
	                  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	                // Sending packet to the client
	                  try {
						serverSocket.send(sendPacket);
					} catch (IOException e) {
						e.printStackTrace();
					}
	                  // This is to reset the buffer each loop
	                  receiveData = new byte[1024];
	                  sendData = new byte[1024];
	                  data = null;
	               }
		}
}
