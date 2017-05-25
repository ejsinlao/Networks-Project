import java.io.*;
import java.util.*;
import java.net.*;

class hisCinema {
  public final static String indexFile = "index.txt";
  public final static int portNum = 40502;
  
  public static void main(String args[]) throws IOException, ClassNotFoundException {
    
    while (true) {
      System.out.println("Starting hisCinema.com...");
      System.out.println("hisCinema.com IP is: " + InetAddress.getLocalHost().getHostAddress() + ":" + portNum);
      ServerSocket hisCinema = null;
      Socket connectionSocket = null;
      BufferedOutputStream outToClient = null;
      InputStream inStream = null;
      OutputStream outStream = null;
      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      
      int statusCode = 0;
      
      System.out.println("Waiting for request...");
      System.out.println();
      hisCinema = new ServerSocket(portNum);
      
      while(true) {
        try {
          connectionSocket = hisCinema.accept();
          inStream = connectionSocket.getInputStream(); 
          outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
        } catch (IOException ex) {
          System.out.println(ex);
        }
        try{
          ObjectInputStream ois = new ObjectInputStream(inStream);
          HTTP_Request request = (HTTP_Request) ois.readObject();
          if (request.getVersion() != 1.1) {
            statusCode = 505;
          }
          if (request != null) {
            if (request.getMethod().equals("GET")) {
              System.out.println("Received GET request from Client for: " + request.getURL());
              System.out.println();
              if(!request.getURL().equalsIgnoreCase(indexFile))
                statusCode = 404;
              else
                statusCode = 200;
            }
            else
              statusCode = 400;
          }
        } catch (IOException e) {
          System.out.println(e);
        }
        
        outStream = connectionSocket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outStream);
        HTTP_Response response = new HTTP_Response(1.1, statusCode);
        System.out.println("Press enter to send index.txt to Client");
        String input = inFromUser.readLine();

        oos.writeObject(response);
        System.out.println("Creating HTTP response for GET request...");
        
        //if get is good n stuff, send file
        if(statusCode == 200) {
          InetAddress IPAddress = connectionSocket.getInetAddress();
          int port = connectionSocket.getPort();
          
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
              
              System.out.println("Sending index.txt to Client...");
            } catch (IOException ex) {
              System.out.println(ex);
            }
          }
        }
      }
    }
  }
}