import java.io.*;
import java.util.*;
import java.net.*;

class hisCinema {
  public final static String indexFile = "index.txt";
  public final static int portNum = 40502;

  private static int httpIn = 0;
  private static int httpOut = 0;
  
  public static void main(String args[]) throws IOException, ClassNotFoundException {
    
    while (true) {
      System.out.println("Starting hisCinema.com...");
      System.out.println("My IP is : " + InetAddress.getLocalHost().getHostAddress() + ":" + portNum);
      ServerSocket hisCinema = null;
      Socket connectionSocket = null;
      BufferedOutputStream outToClient = null;
      InputStream inStream = null;
      OutputStream outStream = null;

      System.out.println("Waiting for Client request...");
      try {
        hisCinema = new ServerSocket(portNum);
        connectionSocket = hisCinema.accept();
        inStream = connectionSocket.getInputStream(); 
        outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
      } catch (IOException ex) {
        System.out.println(ex);
      }
      
      if (httpIn == 0)
      {
        try{
        ObjectInputStream ois = new ObjectInputStream(inStream);
        HTTP_Request request = (HTTP_Request) ois.readObject();
        if (request.getVersion() != 1.1)
        {
          System.out.println("HTTP Error 505");
        }
        if (request != null) {
          if (request.getMethod().equals("GET"))
          {
            System.out.println("Obtained GET request from client");
          } 

          httpIn = 1;}
        } catch (IOException e) {
          System.out.println(e);
        }
        
      }
     
      if (httpOut == 0)
      {
    	  outStream = connectionSocket.getOutputStream();
    	  ObjectOutputStream oos = new ObjectOutputStream(outStream);
    	  HTTP_Response response = new HTTP_Response(1.1, 200);
    	  oos.writeObject(response);
        System.out.println("Sending HTTP response to GET request...");
    	  httpOut = 1;
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