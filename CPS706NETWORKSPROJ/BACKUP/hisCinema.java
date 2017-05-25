import java.io.*;
import java.util.*;
import java.net.*;

class hisCinema {
  public final static String indexFile = "index.txt";
  public final static int portNum = 40502;
  
  public static void main(String args[]) throws IOException {
    
    while (true) {
      System.out.println("Starting hisCinema.com...");
      System.out.println("My IP is : " + InetAddress.getLocalHost().getHostAddress() + ":" + portNum);
      ServerSocket hisCinema = null;
      Socket connectionSocket = null;
      BufferedOutputStream outToClient = null;
      
      System.out.println("Waiting for Client request...");
      try {
        hisCinema = new ServerSocket(portNum);
        connectionSocket = hisCinema.accept();
        outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
      } catch (IOException ex) {
        System.out.println(ex);
      }
      InetAddress IPAddress = connectionSocket.getInetAddress();
      int port = connectionSocket.getPort();
      System.out.println("Received data from: "
                           + IPAddress.toString() + ":" + port
                           + " asking for: " + "MSG HERE");
      
      if (outToClient != null) {
        File myFile = new File(indexFile);
        byte[] mybytearray = new byte[(int) myFile.length()];
        
        FileInputStream fis = null;
        
        try {
          fis = new FileInputStream(myFile);
        } catch (FileNotFoundException ex) {
          System.out.println(ex);
        }
        BufferedInputStream bis = new BufferedInputStream(fis);
        
        try {
          bis.read(mybytearray, 0, mybytearray.length);
          outToClient.write(mybytearray, 0, mybytearray.length);
          outToClient.flush();
          outToClient.close();
          connectionSocket.close();
          
          System.out.println("Index.txt sent");
          // File sent, exit the main method
          return;
        } catch (IOException ex) {
          System.out.println(ex);
        }
      }
    }
  }
}