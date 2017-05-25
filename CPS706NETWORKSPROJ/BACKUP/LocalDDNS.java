import java.io.*;
import java.util.*;
import java.net.*;

class LocalDDNS {
  public static final int portNum = 40500;
  public static final int RECORDS = 4;
  
  public final static int HisCinemaAuth_Port = hisCinemaDDNS.portNum;
  public final static String HisCinemaAuth_IP = "localhost";
  
  private static int count = 0;
  
  public static void main(String argv[]) throws IOException {
    System.out.println("Starting localDDNS...");
    System.out.println("Loading records...");
    DNS_Record[] records = new DNS_Record[10];
    //Assume local DDNS has the following records :
    records[0] = new DNS_Record("herCDN.com", "NSherCDN.com", "NS", "999");
    records[1] = new DNS_Record("NSherCDN.com", "IPher", "A", "999");
    records[2] = new DNS_Record("hiscinema.com", "NShiscinema.com", "NS", "999");
    records[3] = new DNS_Record("NShiscinema.com", "IPhis", "A", "999");
    System.out.println("Records Loaded.");
    
    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];
    System.out.println("My IP is : " + InetAddress.getLocalHost().getHostAddress()+ ":" + portNum);
    DatagramSocket ddnsSocket = new DatagramSocket(portNum);
    System.out.println("Waiting for Client request...");
    
    while(true) {
      
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      ddnsSocket.receive(receivePacket);
      
      //LocalDNS, this, receive request from client and forward to another server : hisCinema.com DDNS
      //DDNS reply/requests are with a DNS message (TextBook Pg.141 - 2.5)
      //How da frik will we implement the DNS messages...hmm. array?
      
      InetAddress IPAddress;
      int port = receivePacket.getPort();
      int length = receivePacket.getLength();
      
      String request = new String(receiveData, 0, length);
      
      System.out.println("Received data from: "
                           + receivePacket.getAddress().toString() + ":" + port
                           + " with length: " + length + " asking for: " + request);
      
      String recordReturn = "RECORD NOT FOUND";
      //send back dns message here
      for(int x = 0; x < RECORDS; x++) {
        if(records[x].getName().equalsIgnoreCase(request)) {
          recordReturn = records[x].getValue();
        }
      }
      
      if (count == 0)
      {
    	  sendData = recordReturn.getBytes();
    	  //set send IP as hisCinema.com to forward
    	  IPAddress = InetAddress.getByName(HisCinemaAuth_IP);
    	  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, HisCinemaAuth_Port);
    	  ddnsSocket.send(sendPacket);
    	  System.out.println("Sent to HisCinemaAuth_DDNS : " + recordReturn);

    	  //set send IP as Client to send
    	  IPAddress = receivePacket.getAddress();
    	  sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
    	  ddnsSocket.send(sendPacket);
    	  System.out.println("Sent to Client : " + recordReturn);
    	  count++;
      }
    }
  }
}