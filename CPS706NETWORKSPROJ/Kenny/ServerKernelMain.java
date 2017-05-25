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

public class ServerKernelMain {

 public static void main(String[] args) {
  ServerKernelProgram server = new ServerKernelProgram();
  String IP_Server = new String(args[0]);
  String port_S = "53";
  draw_ASCII();
  server.receiveANDanswer(port_S, IP_Server, args[1]);
 }
 
 public static void draw_ASCII (){
  System.out.println("          )))");  
  System.out.println("         (o o)");      
  System.out.println("-----ooO--(_)--Ooo--------------------------------------------------------------------"); 
  System.out.println("   ffffffffffffffff  DDDDDDDDDDDDD        NNNNNNNN        NNNNNNNN   SSSSSSSSSSSSSSS");
  System.out.println("   f::::::::::::::::f D::::::::::::DDD     N:::::::N       N::::::N SS:::::::::::::::S");
  System.out.println("  f::::::::::::::::::fD:::::::::::::::DD   N::::::::N      N::::::NS:::::SSSSSS::::::S");
  System.out.println("  f::::::fffffff:::::fDDD:::::DDDDD:::::D  N:::::::::N     N::::::NS:::::S     SSSSSSS");
  System.out.println("  f:::::f       ffffff  D:::::D    D:::::D N::::::::::N    N::::::NS:::::S");
  System.out.println("  f:::::f               D:::::D     D:::::DN:::::::::::N   N::::::NS:::::S");
  System.out.println(" f:::::::ffffff         D:::::D     D:::::DN:::::::N::::N  N::::::N S::::SSSS");
  System.out.println(" f::::::::::::f         D:::::D     D:::::DN::::::N N::::N N::::::N  SS::::::SSSSS");
  System.out.println(" f::::::::::::f         D:::::D     D:::::DN::::::N  N::::N:::::::N    SSS::::::::SS");
  System.out.println(" f:::::::ffffff         D:::::D     D:::::DN::::::N   N:::::::::::N       SSSSSS::::S");
  System.out.println("  f:::::f               D:::::D     D:::::DN::::::N    N::::::::::N            S:::::S");
  System.out.println("  f:::::f               D:::::D    D:::::D N::::::N     N:::::::::N            S:::::S");
  System.out.println(" f:::::::f            DDD:::::DDDDD:::::D  N::::::N      N::::::::NSSSSSSS     S:::::S");
  System.out.println(" f:::::::f            D:::::::::::::::DD   N::::::N       N:::::::NS::::::SSSSSS:::::S");
  System.out.println(" f:::::::f            D::::::::::::DDD     N::::::N        N::::::NS:::::::::::::::SS");
  System.out.println(" fffffffff            DDDDDDDDDDDDD        NNNNNNNN         NNNNNNN SSSSSSSSSSSSSSS");
  System.out.println("--------------------------------------------------------------------------------------");
  System.out.println("|  This program is part of DNSpenTest suite, a project hosted by sourcefoge.net and  |");
  System.out.println("|  developed by PerseoIIN and Fahrenheit84.                                          |");
  System.out.println("--------------------------------------------------------------------------------------");
  System.out.println("|  Web Site: http://dnspentest.sourceforge.net                                       |");
  System.out.println("|  Web CVS: http://dnspentest.cvs.sourceforge.net                                    |");
  System.out.println("|  Project's Home: http://sourceforge.net/projects/dnspentest/                       |");
  System.out.println("|  Email: perseoiin@users.sourceforge.net or fahrenheit84@users.sourceforge.net      |");
  System.out.println("--------------------------------------------------------------------------------------");
 }

}
