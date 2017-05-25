import java.io.*;
import java.util.*;
import java.net.*;

class hisCinemaDDNS {
  public static final int portNum = 40501; //hisCinemaDDNS Auth port Num.
  private static int count = 0;
  public static int RECORDS = 2;
  
  public final static int HerCinemaAuth_Port = herCinemaDDNS.portNum;
  public final static String HerCinemaAuth_IP = "localhost";
  
  public static void main(String argv[]) throws IOException {
    System.out.println("Starting hisCinema DDNS...");
    //receive and send buffers
    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];
    
    DNS_Record[] records = new DNS_Record[10];
    records[0] = new DNS_Record("herCDN.com", "www.herCDN.com", "CN");
    records[1] = new DNS_Record("www.herCDN.com", HerCinemaAuth_IP, "A");
    
    System.out.println("hisCinema DDNS IP is: " + InetAddress.getLocalHost().getHostAddress() + ":" + portNum);
    
    DatagramSocket hisCinemaDDNS = new DatagramSocket(portNum);
    System.out.println("Waiting for request...");
    System.out.println();
    
    while(true) {
      
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      hisCinemaDDNS.receive(receivePacket);
      
      DNS_Message fileRequest;
      
      byte[] data = receivePacket.getData();
      ByteArrayInputStream bais = new ByteArrayInputStream(data);
      ObjectInputStream ois = new ObjectInputStream(bais);
      int recordExists = 0;
      try {
        fileRequest = (DNS_Message) ois.readObject();
        if (fileRequest.getQuestion() != null) {
          System.out.println("Received a DNS Request from LocalDDNS");
          System.out.println("Received DNS request with record " + fileRequest.print());
          for (int i = 0; i < RECORDS; i ++){
            if (fileRequest.getQuestion().equals(records[i])) recordExists = 1;
          }
          if (recordExists == 0) 
          {
            System.out.println("Unable to find record in hisCinema DDNS");
            System.out.println();
            
            // Making reply message
            DNS_Message fileReply = new DNS_Message();
            fileReply.createReply(fileRequest);
            DNS_Record answer = new DNS_Record("herCDN.com", "NSherCDN.com", "NS");
            fileReply.setAnswer(answer);
            
            // Sending reply message to local
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(fileReply);
            
            sendData  = baos.toByteArray();
            System.out.println();
            System.out.println("Press enter to send DNS Reponse to Local DDNS");
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            String input = inFromUser.readLine();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            hisCinemaDDNS.send(sendPacket);
            System.out.println("Sending DNS Reply to Local DDNS with record" + fileReply.getAnswer().getName());
          }
          else if (recordExists == 1) System.out.println("Record found");
        }
        else {
          System.out.println("DNS does not have a question");
        }
        
      }
      catch (ClassNotFoundException e) {
        e.printStackTrace(); }
      /*//listen for connection
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
       }*/
    }
  }
}

