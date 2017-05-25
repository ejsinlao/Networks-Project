import java.net.*;
import java.io.*;
public class HTTP_Response implements Serializable
{
 private double version;
 private int statusCode;
 private String phrase;

 private String connection;
 private String date;
 private String server;
 private String lastModified;
 private String contentLength;
 private String contentType;

 private String data;

  //Read Pages 105 and 107 of Textbook for this
 public HTTP_Response(double version, int statusCode)
 {
  this.version = version;
  this.statusCode = statusCode;
  if (statusCode == 200) phrase = "OK";
  else if (statusCode == 400) phrase = "Bad Request";
  else if (statusCode == 404) phrase = "Not Found";
  else if (statusCode == 505) phrase = "HTTP Version Not Supported";
  }

 public void setData(String data)
 {
  this.data = data;
 }
 public String getData()
 {
  return this.data;
 }
 public double getVersion()
 {
  return this.version;
 }
 public int getStatusCode()
 {
  return this.statusCode;
 }
 public String getPhrase()
 {
  return this.phrase;
 }
}
