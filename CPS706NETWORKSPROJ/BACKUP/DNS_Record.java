 //Read Page 139 of Textbook for this
public class DNS_Record {
  private String name;
  private String value;
  private String type;
  private String ttl;
  
  DNS_Record(){
    name = "Name";
    value = "Value";
    type = "Type";
    ttl = "TTL";
  }
  
  DNS_Record(String name, String value, String type, String ttl) {
    this.name = name;
    this.value = value;
    this.type = type;
    this.ttl = ttl;
  }
  
  String getName() { return name; }
  String getValue() { return value; }
  String getType() { return type; }
  String getTTL() { return ttl; }
  
  void setName(String name) { this.name = name; }
  void setValue(String value) { this.value = value; }
  void setType(String type) { this.type = type; }
  void setTTL(String ttl) { this.ttl = ttl; }
}