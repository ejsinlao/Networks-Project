import java.io.*;
import java.net.*;

class Client {
  
  public final static String LocalDDNS_IP = "localhost"; //use "localhost" if on same machine
  public final static int LocalDDNS_Port = LocalDDNS.portNum;
  public final static String HisCinema_IP = "localhost";
  public final static int HisCinema_Port = hisCinema.portNum;
  public final static String file = hisCinema.indexFile;
  
  public static void main(String args[]) throws Exception {
    
    System.out.println("My IP is : " + InetAddress.getLocalHost().getHostAddress());
    byte[] aByte = new byte[1];
    int bytesRead;
    
    Socket clientSocket = null;
    InputStream inStream = null;
    
    try {
      //Connect to hiscinema.com web server for index.txt
      clientSocket = new Socket(HisCinema_IP , HisCinema_Port);
      inStream = clientSocket.getInputStream();
    } catch (IOException ex) {
      System.out.println(ex);
    }
    System.out.println("Connected to hiscinema.com");
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    if (inStream != null) {
      
      FileOutputStream fos = null;
      BufferedOutputStream bos = null;
      
      try {
        fos = new FileOutputStream(file);
        bos = new BufferedOutputStream(fos);
        bytesRead = inStream.read(aByte, 0, aByte.length);
        
        do {
          baos.write(aByte);
          bytesRead = inStream.read(aByte);
        } while (bytesRead != -1);
        
        bos.write(baos.toByteArray());
        bos.flush();
        bos.close();
        clientSocket.close();
      } catch (IOException ex) {
        System.out.println(ex);
      }
    }
    /*
    //---------------------------------------------------------------------
    ///UDP TO THAT GUY NAMED LOCAL DDNS FAM
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    DatagramSocket clientDatagramSocket = new DatagramSocket();
    
    InetAddress IPAddress = InetAddress.getByName(LocalDDNS_IP);
    
    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];
    
    String sentence = inFromUser.readLine();
    sendData = sentence.getBytes();
    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, LocalDDNS_Port);
    clientDatagramSocket.send(sendPacket);
    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
    clientDatagramSocket.receive(receivePacket);
    
    IPAddress = receivePacket.getAddress();
    int port = receivePacket.getPort();
    int length = receivePacket.getLength();
    
    String response = new String(receivePacket.getData());
    System.out.println("Received data from Server : "
                         + IPAddress.toString() + ":" + port
                         + " with length: " + length + " with response:\n" + response);
    clientDatagramSocket.close();*/
  }
}