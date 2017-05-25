 //Read Page 141 of Textbook for this
import java.net.*;
import java.io.*;
public class DNS_Message implements Serializable
{
 // Header
 int id; // 16-bit number that identifies the query. Copied into reply message to a query

 // Flags
 int qr;
 int opCode;
 int aa;
 int tc; 
 int rd;
 int ra;
 int reservered;
 int rcode;
 
 // Number of records per section
 int numberOfQuestionsRR = 0;
 int numberOfAnswersRR = 0;
 int numberOfAuthorityRR = 0;
 int numberOfAdditionalRR = 0;

 // Records
 DNS_Record[] questions;
 DNS_Record[] answers;
 DNS_Record[] authority;
 DNS_Record[] additional;

 public DNS_Message(){};

 public void createQuery(int id)
 {
  this.id = id;
  this.qr = 0;
  this.numberOfAdditionalRR = 0;
  this.numberOfAuthorityRR = 0;
  this.numberOfAdditionalRR = 0;
  questions = new DNS_Record[10];
 }
 public void createReply(DNS_Message query)
 {
  this.id = query.getID();
  this.qr = 1;
  questions = new DNS_Record[10];
  answers = new DNS_Record[10];
  authority = new DNS_Record[10];
  additional = new DNS_Record[10];
  this.questions[0] = query.getQuestion();
 }
 
 public void setID(int id)
 {
  this.id = id;
 }
 public void setQR(int bit)
 {
  if (bit != 0 || bit != 1) System.out.println("Invalid input");
  else this.qr = bit;
 }
 
 public void setQuestion(DNS_Record question)
 {
  questions[0] = question;
  this.numberOfQuestionsRR++;
 }
 
 public void setAnswer(DNS_Record answer)
 {
  answers[0] = answer;
  this.numberOfAnswersRR++;
 }
 
 public DNS_Record getQuestion()
 {
  return questions[0];
 }
 
 public DNS_Record getAnswer()
 {
  return answers[0];
 }
 
 public int getID()
 {
  return this.id;
 }
 
 public int getQR()
 {
  return this.qr;
 }

 public String print()
 {
  String result = null;
 	if (this.qr == 00)
 	{
    result = "DNS Query (" + this.questions[0].getName() + ", " + this.questions[0].getValue() + ", " + this.questions[0].getType() + ")";
 	}
 	else if (this.qr == 1)
 	{
 		result = "DNS Reply (" + this.answers[0].getName() + ", " + this.answers[0].getValue() + ", " + this.answers[0].getType() + ") for: DNS Query (" + this.questions[0].getName() + ", " + this.questions[0].getValue() + ", " + this.questions[0].getType() + ")";
 	}
  return result;
 }
 
 // Prob useless
 public void setOpCode(int bit)
 {
  if (bit != 0 || bit != 1) System.out.println("Invalid input");
  else this.opCode = bit;
 }
 public void setAA(int bit)
 {
  if (bit != 0 || bit != 1) System.out.println("Invalid input");
  else this.aa = bit;
 }
 public void setTC(int bit)
 {
  if (bit != 0 || bit != 1) System.out.println("Invalid input");
  else this.tc = bit;
 }
 public void setRD(int bit)
 {
  if (bit != 0 || bit != 1) System.out.println("Invalid input");
  else this.rd = bit;
 }
 public void setRA(int bit)
 {
  if (bit != 0 || bit != 1) System.out.println("Invalid input");
  else this.ra = bit;
 }
 public void setReserved(int bit)
 {
  if (bit != 0 || bit != 1) System.out.println("Invalid input");
  else this.reservered = bit;
 }
 public void setRCode(int bit)
 {
  if (bit != 0 || bit != 1) System.out.println("Invalid input");
  else this.qr = bit;
 }
}