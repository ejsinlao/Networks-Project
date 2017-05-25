import java.io.*;
import java.util.*;
import java.net.*;

class herCinemaDDNS {
  public static final int portNum = 40503; //herCinemaDDNS Auth port Num.
  public static final String herCinema_IP = herCinema.IP;
  
  public static void main(String argv[]) throws IOException {
    System.out.println("Starting herCinema DDNS...");
    DNS_Record[] records = new DNS_Record[10];
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    //Assume local DDNS has the following records :
    records[0] = new DNS_Record("video.netcinema.com", "herCDN.com", "R");
    
    //receive and send buffers
    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];
    System.out.println("herCinema DDNS IP is : " + InetAddress.getLocalHost().getHostAddress() + ":" + portNum);
    
    DatagramSocket herCinemaDDNS = new DatagramSocket(portNum);
    System.out.println("Waiting for request...");
    System.out.println();
    
    while(true) {
      //listen for connection
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      herCinemaDDNS.receive(receivePacket);
      byte[] data = receivePacket.getData();
      
      //get connector's IP & Port & msg length (msg will be replaced with DNS_Message)
      InetAddress IPAddress = receivePacket.getAddress();
      int port = receivePacket.getPort();
      int length = receivePacket.getLength();
      
      ByteArrayInputStream bais = new ByteArrayInputStream(data);
      ObjectInputStream ois = new ObjectInputStream(bais);
      DNS_Message fileRequestMsg = new DNS_Message();
      
      try {
        fileRequestMsg = (DNS_Message) ois.readObject();
        System.out.println("Received a DNS Request from: "
                           + IPAddress.toString() + ":" + port
                           + " with length: " + length);
        System.out.println("Received DNS request with record " + fileRequestMsg.print());
        System.out.println();
      }catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
      
      //create DNS_Message object
      DNS_Message fileReplyMsg = new DNS_Message();
      fileReplyMsg.createReply(fileRequestMsg);
      DNS_Record dnsRecord = new DNS_Record("video.netcinema.com", herCinema_IP, "A");
      fileReplyMsg.setAnswer(dnsRecord); 
      
      //create streams to write object to
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(fileReplyMsg);
      
      sendData = baos.toByteArray();
      System.out.println("Press enter to send DNS Response to Local DDNS");
      String input = inFromUser.readLine();
      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
      herCinemaDDNS.send(sendPacket);
      System.out.println("Sent DNS reply to " + IPAddress.toString() + ":" + port);
    }
  }
}