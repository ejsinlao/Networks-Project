import java.net.*;
import java.io.*;
public class HTTP_Request implements Serializable 
{
 private String method;
 private String url;
 private double version;

 private String host;
 private String connection;
 private String userAgent;
 private String acceptLanguage;

 //Read Pages 105 and 107 of Textbook for this
 public HTTP_Request(String method, String url, double version)
 {
  this.method = method;
  this.url = url;
  this.version = version;
 }

 public String getMethod()
 {
  return this.method;
 }
 public String getURL()
 {
  return url;
 }
 public double getVersion()
 {
  return this.version;
 }
}