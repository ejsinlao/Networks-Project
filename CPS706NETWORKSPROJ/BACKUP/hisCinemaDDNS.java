import java.io.*;
import java.util.*;
import java.net.*;

class hisCinemaDDNS {
  public static final int portNum = 40501; //hisCinemaDDNS Auth port Num.
  private static int count = 0;
  
  public static void main(String argv[]) throws IOException {
    System.out.println("Starting hisCinema. DDNS...");
    //receive and send buffers
    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];
    System.out.println("My IP is : " + InetAddress.getLocalHost().getHostAddress() + ":" + portNum);
    
    DatagramSocket hisCinemaDDNS = new DatagramSocket(portNum);
    System.out.println("Waiting for Client request...");
    
    while(true) {
      //listen for connection
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      hisCinemaDDNS.receive(receivePacket);
      //get connector's IP & Port & msg length (msg will be replaced with DNS_Message)
      InetAddress IPAddress = receivePacket.getAddress();
      int port = receivePacket.getPort();
      int length = receivePacket.getLength();
      
      String request = new String(receiveData, 0, length);
      
      System.out.println("Received data from: "
                           + IPAddress.toString() + ":" + port
                           + " with length: " + length + " asking for: " + request);
      
      String recordReturn = "NOT FOUND";
      
        if (count == 0)
        {
          //turn reply into bytes
          sendData = recordReturn.getBytes();
          //create reply packet and send it.
          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
          hisCinemaDDNS.send(sendPacket);
          System.out.println("Sent : " + recordReturn);
          count++;
        }
    }
  }
}