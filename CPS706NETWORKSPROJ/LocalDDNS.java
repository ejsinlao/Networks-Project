import java.io.*;
import java.util.*;
import java.net.*;

class LocalDDNS {
  public static final int portNum = 40500;
  public static int RECORDS = 4;
  
  public final static int HisCinemaAuth_Port = hisCinemaDDNS.portNum;
  public final static String HisCinemaAuth_IP = "localhost";
  public final static int HerCinemaAuth_Port = herCinemaDDNS.portNum;
  public final static String HerCinemaAuth_IP = "localhost";
  
  private static int count = 0;
  
  public static void main(String argv[]) throws IOException, ClassNotFoundException {
    
    System.out.println("Starting Local DDNS...");
    DNS_Record[] records = new DNS_Record[10];
    //Assume local DDNS has the following records :
    records[0] = new DNS_Record("hiscinema.com", "NShiscinema.com", "NS");
    records[1] = new DNS_Record("NShiscinema.com", HisCinemaAuth_IP, "A");
    records[2] = new DNS_Record("herCDN.com", "NSherCDN.com", "NS");
    records[3] = new DNS_Record("NSherCDN.com", HerCinemaAuth_IP, "A");
    
    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];
    
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Local DDNS IP is: " + InetAddress.getLocalHost().getHostAddress()+ ":" + portNum);
    DatagramSocket ddnsSocket = new DatagramSocket(portNum);
    System.out.println("Waiting for client request...");
    System.out.println();
    
    while(true) {
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      ddnsSocket.receive(receivePacket);
      
      InetAddress client_Address = receivePacket.getAddress();
      int client_Port = receivePacket.getPort();
      DNS_Message client_query = new DNS_Message();
      
      DNS_Message fileRequest;
      byte[] data = receivePacket.getData();
      ByteArrayInputStream bais = new ByteArrayInputStream(data);
      ObjectInputStream ois = new ObjectInputStream(bais);
      int recordExists = 0;
      try {
        fileRequest = (DNS_Message) ois.readObject();
        if (fileRequest.getQuestion() != null) {
          System.out.println("Received DNS request with record " + fileRequest.print());
          client_query = fileRequest;
          
          for (int i = 0; i < RECORDS; i ++){
            if (fileRequest.getQuestion().equals(records[i])) recordExists = 1;
          }
          if (recordExists == 0) System.out.println("Unable to find record in LocalDDNS");
          else if (recordExists == 1) System.out.println("Record found!");
          
          if (recordExists == 0 && fileRequest != null) {
            InetAddress IPAddress = InetAddress.getByName(HisCinemaAuth_IP);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(fileRequest);
            
            sendData = baos.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, HisCinemaAuth_Port);
            System.out.println();
            System.out.println("Press enter to send the DNS request to hisCinema DDNS");
            String input = inFromUser.readLine();
            System.out.println("Sending DNS Request to hisCinemaDDNS for " + fileRequest.getQuestion().getValue() + "...");
            System.out.println();
            ddnsSocket.send(sendPacket);  
            
            //wait for response from hisCinema
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            ddnsSocket.receive(receivePacket);
            
            //set up streams for new receieve packets
            data = receivePacket.getData();
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            
            //get object DNS_Message
            fileRequest = (DNS_Message) ois.readObject();
            
            if(fileRequest.getAnswer() != null) {
              //we got answer, add it to our records
              records[RECORDS] = fileRequest.getAnswer();
              RECORDS++;
              System.out.println("Received a DNS Response from: " + receivePacket.getAddress().toString() + ":" + receivePacket.getPort());
              System.out.println("Received DNS Response with record " + fileRequest.print());
              if(fileRequest.getAnswer().getType().equalsIgnoreCase("NS")) {
                System.out.println();
                //we got an NS Record meaning contact this guy cuz he knows
                //check value against our own records to get IP
                for(int x = 0; x < RECORDS; x++) {
                  if(fileRequest.getAnswer().getValue().equalsIgnoreCase(records[x].getName())){
                    //we got the record. Let's send a DNS Message to it
                    //use original query again
                    DNS_Message fileReplyMsg = client_query;
                    //get IP and set up streams
                    for(int y = 0 ; y < RECORDS; y++) {
                      //if it matches our NS record, we should have an A record in next slot
                      if(records[y].getName().equalsIgnoreCase(fileRequest.getAnswer().getValue()) && records[y].getType().equalsIgnoreCase("A")) {
                        IPAddress = InetAddress.getByName(records[y].getValue());
                        break;
                      }
                    }
                    baos = new ByteArrayOutputStream();
                    oos = new ObjectOutputStream(baos);
                    oos.writeObject(fileReplyMsg);
                    
                    System.out.println("Press enter to send the DNS request to herCinema");
                    input = inFromUser.readLine();
                    System.out.println("Sending DNS request to: " + IPAddress.toString() + ":" + HerCinemaAuth_Port);
                    //send to herCinema
                    sendData = baos.toByteArray();
                    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, HerCinemaAuth_Port);
                    ddnsSocket.send(sendPacket);
                    
                    //wait for reply again
                    receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    ddnsSocket.receive(receivePacket);
                    System.out.println();
                    System.out.println("Received record from : " + receivePacket.getAddress().toString() + ":" + receivePacket.getPort());
                    
                    //set up streams for new receieve packets
                    data = receivePacket.getData();
                    bais = new ByteArrayInputStream(data);
                    ois = new ObjectInputStream(bais);
                    
                    //get object DNS_Message
                    fileRequest = (DNS_Message) ois.readObject();
                    
                    if(fileRequest.getAnswer() != null && fileRequest.getAnswer().getType().equalsIgnoreCase("A")) {
                      System.out.println("Received DNS response with record " + fileRequest.print());
                      //we got the IP yay
                      //create new query with original question and new answer
                      fileReplyMsg = new DNS_Message();
                      fileReplyMsg.createReply(fileRequest);
                      fileReplyMsg.setAnswer(fileRequest.getAnswer());
                      //get IP and set up streams
                      baos = new ByteArrayOutputStream();
                      oos = new ObjectOutputStream(baos);
                      oos.writeObject(fileReplyMsg);
                      
                      //send to client finally
                      System.out.println("Press enter to send DNS Response to Client");
                      input = inFromUser.readLine();
                      System.out.println("Sending DNS response to:" + client_Address.toString() + ":" + client_Port);
                      sendData = baos.toByteArray();
                      sendPacket = new DatagramPacket(sendData, sendData.length, client_Address, client_Port);
                      ddnsSocket.send(sendPacket);
                    }
                  }
                }
              }
            }
          }
          else if (recordExists == 1 && fileRequest != null) {
            //send existing record to client
          }
          else {
            //Error cry here
          }
        }
      }
      catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}