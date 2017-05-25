 //Read Page 139 of Textbook for this
import java.net.*;
import java.io.*;
public class DNS_Record implements Serializable{
  private String name;
  private String value;
  private String type;
  private int ttl;
  
  DNS_Record(){
    name = "Name";
    value = "Value";
    type = "Type";
    this.ttl = 999;
  }
  
  DNS_Record(String name, String value, String type) {
    this.name = name;
    this.value = value;
    this.type = type;
  }
  
  String getName() { return name; }
  String getValue() { return value; }
  String getType() { return type; }
  
  void setName(String name) { this.name = name; }
  void setValue(String value) { this.value = value; }
  void setType(String type) { this.type = type; }

  boolean equals(DNS_Record record)
  {
    if (this.name == record.getName() && this.value == record.getValue() && this.type == record.getType())
    {
      return true;
    }
    else return false;
  }
}