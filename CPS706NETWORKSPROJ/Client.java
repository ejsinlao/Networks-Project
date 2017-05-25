import java.io.*;
import java.net.*;

class Client {
  
  public final static int Client_Port = 40509;
  public final static String LocalDDNS_IP = "localhost"; //use "localhost" if on same machine
  public final static int LocalDDNS_Port = LocalDDNS.portNum;
  public final static String HisCinema_IP = "localhost";
  public final static int HisCinema_Port = hisCinema.portNum;
  public final static int HerCinema_Port = herCinema.portNum;
  public final static String file = "index.txt";
  public final static String videoDownload = "Dove.mp4";
  public static int selectedVideo = 0;
  public final static int videoSize = 8000000; // actual video file is 6 097 193 bytes 
  private static int httpIn = 0;
  private static int httpOut = 0;
  public static void main(String args[]) throws Exception {
    
    System.out.println("Starting Client...");
    System.out.println("My IP is : " + InetAddress.getLocalHost().getHostAddress());
    System.out.println();
    byte[] aByte = new byte[1];
    int bytesRead;
    
    Socket clientSocket = null;
    InputStream inStream = null;
    OutputStream outStream = null;
    
    try {
      //Connect to hiscinema.com web server for index.txt using TCP
      clientSocket = new Socket(HisCinema_IP , HisCinema_Port);
      inStream = clientSocket.getInputStream(); 
    } catch (IOException ex) {
      System.out.println(ex);
    }
    System.out.println("Press enter to connect to hisCinema.com");
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    String input = inFromUser.readLine();

    System.out.println("Connecting to hisCinema.com...");
    System.out.println();
    outStream = clientSocket.getOutputStream();
    
    //create object stream to send http get object
    ObjectOutputStream oos = new ObjectOutputStream(outStream);
    HTTP_Request request = new HTTP_Request("GET", file, 1.1);
    oos.writeObject(request);
    
    //create object stream to get http response object
    ObjectInputStream ois = new ObjectInputStream(inStream);
    HTTP_Response response = (HTTP_Response) ois.readObject();
    if (response != null) {
      if (response.getStatusCode() == 200)
      {
        System.out.println("HTTP Response received with a status code of " + response.getPhrase() + " (" + response.getStatusCode() + ")");
        System.out.println();
      }
      else {
        System.out.println("HTTP Error " + response.getStatusCode());
        System.out.println();
      }
    }
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    //Receieve file from webServer
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
    
    //PARSING FILE AND SELECTING VIDEO
    //USE THE selectedVideo variable to pick which video to read
    
    //store selectedVideo from index.txt in url String
    BufferedReader r =  new BufferedReader(new FileReader("index.txt"));  
    System.out.print("Please enter a video to download (0 to 5): ");
    inFromUser = new BufferedReader(new InputStreamReader(System.in));
    selectedVideo = Integer.parseInt(inFromUser.readLine());
    String url= "";
    for(int i =0 ; i<= selectedVideo ; i++){
      url = r.readLine();
    }
    
    //Create UDP socket for client with port Client_Port
    DatagramSocket clientDatagramSocket = new DatagramSocket(Client_Port);
    //set IP for Local DDNS
    InetAddress IPAddress = InetAddress.getByName(LocalDDNS_IP);
    
    //set up buffers
    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];
    
    //create DNS_Message object
    DNS_Message fileRequestMsg = new DNS_Message();
    fileRequestMsg.createQuery(1);
    DNS_Record fileRequestRR = new DNS_Record("index.txt", url, "V");
    fileRequestMsg.setQuestion(fileRequestRR);
    System.out.println();
    System.out.println("Creating DNS Request...");
    
    //set ObjectOutputStream with ByteArrayOutputStream & write object to it
    baos = new ByteArrayOutputStream();
    oos = new ObjectOutputStream(baos);
    oos.writeObject(fileRequestMsg);
    
    sendData = baos.toByteArray();
    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, LocalDDNS_Port);
    clientDatagramSocket.send(sendPacket);
    System.out.println("Sending DNS Request to Local DDNS");
    
    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
    clientDatagramSocket.receive(receivePacket);
    
    byte[] data = receivePacket.getData();
    
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    ois = new ObjectInputStream(bais);
    DNS_Message fileResolveMsg = new DNS_Message();
    String HerCinema_IP = "";
    
    try {
      fileResolveMsg = (DNS_Message) ois.readObject();
      if (fileResolveMsg != null){
        System.out.println("Successfully received a DNS response with record " + fileResolveMsg.print());
        HerCinema_IP = fileResolveMsg.getAnswer().getValue();
      }
      System.out.println();
    }catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    
    /*Connects to herCinema and requests for file. 
     * Idk how to do it 100 % cause idk how to ask for herCinemaIP and herCinemaPort
     * Will ask Zulfi or Kenny in morning
     * 
     */
    byte [] byteVideo = new byte[videoSize];
    try {
      //Connect to herCinema.com web server for Dove.mp4 using TCP
      clientSocket = new Socket(HerCinema_IP , HerCinema_Port);
      
      inStream = clientSocket.getInputStream(); 
    } catch (IOException ex) {
      System.out.println(ex);
    }
    System.out.println("Connected to hercinema.com");
    outStream = clientSocket.getOutputStream();
    
    //create object stream to send http get object
    oos = new ObjectOutputStream(outStream);
    request = new HTTP_Request("GET", videoDownload, 1.1);
    oos.writeObject(request);
    
    //create object stream to get http response object
    ois = new ObjectInputStream(inStream);
    response = (HTTP_Response) ois.readObject();
    if (response != null) {
      if (response.getStatusCode() == 200)
      {
        System.out.println("HTTP Response received with a status code of " + response.getPhrase() + " (" + response.getStatusCode() + ")");
      }
      else {
        System.out.println("HTTP Error " + response.getStatusCode());
      }
    }
    
    baos = new ByteArrayOutputStream();
    
    //Receieve file from webServer
    if (inStream != null) {
      
      FileOutputStream fos = null;
      BufferedOutputStream bos = null;
      
      try {
        fos = new FileOutputStream(file);
        bos = new BufferedOutputStream(fos);
        bytesRead = inStream.read(byteVideo, 0, byteVideo.length);
        
        do {
          baos.write(byteVideo);
          bytesRead = inStream.read(byteVideo);
        } while (bytesRead != -1);
        
        bos.write(baos.toByteArray());
        bos.flush();
        bos.close();
        clientSocket.close();
      } catch (IOException ex) {
        System.out.println(ex);
      }
    }
  }
}
