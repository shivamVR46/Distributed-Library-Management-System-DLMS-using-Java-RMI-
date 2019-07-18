/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
/**
 *
 * @author shivam
 */
public class Montreal extends ImplMON{
    
    public Montreal() throws RemoteException,IOException{
        
    }
    
    public static void main(String args[])
    {
        
        try{
            int port =1099;
          ImplMON obj = new ImplMON();
          IntremoteMON stub = (IntremoteMON)UnicastRemoteObject.exportObject(obj, 0);
          Registry registry = LocateRegistry.getRegistry(port);
          registry.rebind("//localhost:" + port +"/IntremoteMON",stub);
          System.out.println(" Montreal Library is Ready : " + port);
          receive(obj);
         }  catch(Exception e) {
          System.out.println("Montreal Library Server Exception " + e.toString());
           }
        
        
        /* Runnable task = () -> {
			receive();
		};
		Runnable task2 = () -> {
		//	sendMessage(7777);
		};
		Runnable task3 = () -> {
		//	sendMessage(8888);
		};
		
//		receive();
//		sendMessage(7777);
//		sendMessage(6666);
		
		Thread thread = new Thread(task);
		Thread thread2 = new Thread(task2);
		Thread thread3 = new Thread(task3);	
		thread.start();
		thread2.start();
		thread3.start();*/
        
	}

	static void sendMessage(String msg,int serverPort) {
		int port = 6666;
                String my_IP = "localhost";
		try {
                        String add = "";
                        add = my_IP+"," + port;
			DatagramSocket aSocket = new DatagramSocket();
                        msg = add + "," + msg;
                        byte buffer[] = new byte[2000];
			byte[] message = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");
			DatagramPacket send = new DatagramPacket(message, message.length, aHost, serverPort);
			aSocket.send(send);
			System.out.println("message sent  " + serverPort + " is: "
					+ new String(send.getData()) + msg);
			
			/*DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

			aSocket.receive(reply);
			System.out.println("Reply received from the server with port number " + serverPort + " is: "
					+ new String(reply.getData()));*/
		} catch (SocketException e) {
			System.out.println("Socket : " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO: " + e.getMessage());
		} //finally {
		//	if (aSocket != null)
		//		aSocket.close();
		//}
	}
        
	 static void receive(ImplMON mon) {
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(6666);
			byte[] buffer; 
                        System.out.println();
			System.out.println("Server 6666 Started............");
			while (true) {
                                buffer = new byte[2000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
                                String str = new String(request.getData()).trim();
                                Runnable t = () -> {
                                    try{
                                        System.out.println("Receiving at : " + str);
                                        UdpData(str,mon);
                                    }catch(Exception e)
                                    {
                                        System.out.println(e.getMessage());
                                    }
                                }; new Thread(t).start();
				//DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(),
				//		request.getPort());
				//aSocket.send(reply);
			}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}
         
         static void UdpData(String data,ImplMON mon) throws RemoteException{
             String received_data = data.trim();
             String s1[] = received_data.split(",");
             String ip = s1[0];
             int port = Integer.parseInt(s1[1]);
             String MID = s1[2];
             int type = Integer.parseInt(s1[3]);
             
            if(type == 10)  // 10 is request message
            {
                String result = "";
                int req_type = Integer.parseInt(s1[4]);
                if(req_type == 1) // 1 is for Find Item
                {
                    String userID = s1[5];
                    String itemName = s1[6];
                    String result1 = mon.FindItemLocal(userID, itemName);
                    result = result + result1;
                }
                else if (req_type == 2)  // 2 is for Borrow Item
                        {
                            String userID = s1[5];
                            String itemID = s1[6];
                            int numberOfDays = Integer.parseInt(s1[7]);
                            String result2 = mon.borrowItemLocal(userID,itemID,numberOfDays);
                            result = result + result2;
                        }
                else if(req_type == 3) // 3 is for return Item
                {
                    String userID = s1[5];
                    String itemID = s1[6];
                    String result3 = mon.returnItemLocal(userID,itemID);
                    result = result + result3;
                }
                else if(req_type == 4) //4 is for waitlist
                {
                    String userID = s1[5];
                    String itemID = s1[6];
                    String result4 = mon.Waitlist(userID, itemID);
                    result = result + result4;
                }
                
                String responseM = "";
                responseM = responseM + MID + "," + 20 + "," + result;
                sendMessage(responseM,port);
         }
            else if(type == 20) // 20 is for response message
            {
                String t = s1[4];
                ServerCommute.responseM.put(MID,t);
            }
             System.out.println("Completed");

}
    
}
