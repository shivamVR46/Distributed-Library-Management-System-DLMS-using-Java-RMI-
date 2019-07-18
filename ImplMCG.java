/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment;

//import static Assignment.Mcgill.b;
//import static Assignment.Mcgill.h1;
import static Assignment.ServerCommute.MID;
import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 *
 * @author shivam
 */
public class ImplMCG  implements IntremoteMCG  {
    int flag=0,rqt=0,i=0;
    
    Queue<String> wait_list = new LinkedList<>();
    HashMap <String,Queue<String>> user = new HashMap<>();
    
    ArrayList b = new ArrayList();
    ArrayList b1 = new ArrayList();
    
    HashMap<String,BookDetails> h1 = new HashMap<>();
    

    HashMap<String,List<String>> borrow = new HashMap<String,List<String>>();
    List<String> l = new ArrayList<String>();
    
    List<String> ext_borrow = new ArrayList<String>();
    
    Logger logf;
    FileHandler fh;

    
    public ImplMCG() throws IOException{
        logf = Logger.getLogger(this.getClass().getSimpleName());
        fh = new FileHandler("D:/SERVER/MCGILL.log", true);
        fh.setFormatter(new SimpleFormatter());
        logf.addHandler(fh);
        dataFill();
   
    }
    
    public void dataFill()
    {
            BookDetails[] bd1 = new BookDetails[100];
        for(int i=0;i<bd1.length;i++)
        {
             bd1[i]=new BookDetails();
        }
          
        bd1[0].name ="Distributed Systems Design"; 
        bd1[0].quantity = 3;
        h1.put("MCG1012",bd1[0]);
        
        bd1[1].name ="Advanced Programming Practices"; 
        bd1[1].quantity = 4;
        h1.put("MCG1023",bd1[1]);
 
        bd1[2].name ="Algorithms"; 
        bd1[2].quantity = 5;
        h1.put("MCG1034",bd1[2]);
        
        bd1[3].name = "Biology";
        bd1[3].quantity = 1;
        h1.put("MCG1045",bd1[3]);
     
        b.add("MCGU1111");
        b.add("MCGU2222");
        b.add("MCGU3333");
        b.add("MCGU4444");
        b1.add("MCGM1111");
        b1.add("MCGM2222");
        b1.add("MCGM3333");
        b1.add("MCGM4444");
        
    
    }
    @Override
    public int addItem(String managerID, String itemID, String itemName, int quantity) throws RemoteException {
        //To change body of generated methods, choose Tools | Templates.
        if (b1.contains(managerID)) {
            System.out.println("inside");
           
            if (h1.containsKey(itemID)) 
            {
                BookDetails bd = h1.get(itemID);
                if (itemName.equals(bd.name)) 
                {
                    synchronized (this) 
                    {
                        bd.quantity = bd.quantity + quantity;
                        h1.put(itemID, bd);
                        String s1 = "";
//                        printW();
                        if (i == 1) {
                            //System.out.println("Before adding waitlist");
                           // printW();
                           Queue<String> wait_list1 = user.get(itemID);
                            if(user.containsKey(itemID)){
                            if(!wait_list1.isEmpty())
                            {
                                i=0;
                                wait_list1 = user.get(itemID);
                                String wt_remove = wait_list1.remove();
                                borrowItemLocal(wt_remove,itemID,30);
                                String s3 = "";
                                s3 = s3 + " book added ";
                                logf.info(s3);
                                return 0;
                            }
                          }  
                        }
                        else
                        {
                        s1 = s1 + "Total number of " + bd.name + " : " + bd.quantity;
                        logf.log(Level.INFO," Total number of " + bd.name + " : " + quantity + " added by :" + managerID + " , Total quantity now :" + bd.quantity);
                        return 0;
                   
                        }
                    }
                } 
            }
                else {
                BookDetails bd1 = new BookDetails();
                    synchronized (this) {
                        bd1.name = itemName;
                        bd1.quantity = quantity;
                        h1.put(itemID, bd1);
                        String s = "";
                        s = s + "Added Item : " + bd1.name + " ; quantity :" + bd1.quantity + " itemID :" + itemID;
                        logf.info(s);

                        return 0;
                    }
                }
            
        }
        else
        {
            return 3;
        }
        return 1000;
    }
       
    @Override
    public int removeItem(String managerID, String itemID, int quantity) throws RemoteException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
             if (b1.contains(managerID)) {
             BookDetails bd = h1.get(itemID);
              //printH();
            if (h1.containsKey(itemID)) {
              //  System.out.println("inside");
                synchronized (this) {
                    
                    if (quantity >= 1) {
                       
                        if (bd.quantity >= quantity) {
                            bd.quantity = bd.quantity - quantity;
                            h1.put(itemID,bd);
                            String s = "";
                            s = s + "Cleared " + bd.name + "; quantity left : " + bd.quantity;
                            logf.info(itemID + " Sucessfully Done " + s);
                            return 4; // 0 is item cleared with some quantity
                        } 
                        else if (borrow.containsValue(itemID)) 
                        {
                            String s3 = "";
                            s3 = s3 + "Book already borrowed and quantity : " + bd.quantity + " , thus clearing the item ! ";
                           logf.info(s3);
                            h1.remove(itemID);
                        logf.info(itemID + " Sucessfully Done " + s3);
                            return 5; // item removed totally as was borrowed
                        }
                         else if (bd.quantity < quantity){
                            String s4 = "";
                            s4 = s4 + " Cannot perform Add operation ";
                            logf.info(s4);
                            return 100;  // no operation performed 
                        }
                    }
                    else if (bd.quantity == 0 || quantity==0) 
                    {
                        String s = "";
                        s = s + "Cleared " + bd.name;
                        h1.remove(itemID);
                        logf.info(itemID + " Sucessfully Done " + s);
                        return 6; // 6 item removed as was 0 quantity
                    }
                    
                }
            }
            return 7; // 7 is invalid itemID
        }
        return 8; // 8 not authorized
   }
   
   // @Override
    public List itemAvailability(String managerID) throws RemoteException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        List x1 = new ArrayList<String>(); 
        List x= new ArrayList<Integer>();
        
        if(b1.contains(managerID))
        {
            //BookDetails bd = new BookDetails();
            for(String itemID : h1.keySet())
           {
               String e;
               e = itemID + " " + h1.get(itemID).name + " " + h1.get(itemID).quantity;
               x1.add(e);
            }
            logf.log(Level.INFO, " Items Listed Sucessfully ", new Object[]{managerID, null});
            return x1;
        } 
        logf.log(Level.INFO, " Not Sucessfull ", new Object[]{managerID, null});
      return x;
    
    }

    @Override
    public String borrowItem(String userID, String itemID, int numberOfDays,int y) throws RemoteException {
        BookDetails bd = h1.get(itemID);
        
        //if(b.contains(userID))
         if (y == 0) 
            {
                if (itemID.substring(0, 3).equals("MCG")) 
                {
                    String result = borrowItemLocal(userID, itemID, numberOfDays);
                     return result;
                }
            }
            else 
            {
              String s1 =  Waitlist(userID, itemID);
              return s1;
            }
                
               if(itemID.substring(0,3).equals("CON"))
               {
                   
                   String borrow_msg = "";
                   int msg_type = 10; // 10 is request
                   int req_type = 2; // 2 is borrow
                   borrow_msg = borrow_msg + msg_type + "," + req_type + "," + userID + "," + itemID + "," + numberOfDays;
                  String MsgID;
                   synchronized(this)
                   {
                        MsgID = "" + (++ServerCommute.MID);
                   }
                   String msg = "" + MsgID + "," + borrow_msg;
                   try{
                    Mcgill.sendMessage(msg,8888);    
                    logf.info("Request sent from McGill to Concordia : " + msg );
                   }catch(Exception e)
                   {
                       System.out.println(e.getMessage());
                   }
                   boolean l = true;
                   String result = "";
                   String newmsgID = "" + MID;
                   while(l){
                       if(ServerCommute.responseM.get(newmsgID) != null)
                       {
                           result = result + ServerCommute.responseM.get(newmsgID);
                           ServerCommute.responseM.remove(newmsgID);
                           l = false;
                           logf.info(" From Concordia : " + result);
                           return result;
                       }
                   }
                   
               }
            else if(itemID.substring(0,3).equals("MON"))
               {
                   String borrow_msg = "";
                   int msg_type = 10; // 10 is request
                   int req_type = 2; // 2 is borrow
                   borrow_msg = borrow_msg + msg_type + "," + req_type + "," + userID + "," + itemID + "," + numberOfDays;
                  String MsgID;
                   synchronized(this)
                   {
                        MsgID = "" + (++ServerCommute.MID);
                   }
                   String msg = "" + MsgID + "," + borrow_msg;
                   try{
                    Mcgill.sendMessage(msg,6666);    
                    logf.info("Request sent from McGill to Montreal : " + msg );
                   }catch(Exception e)
                   {
                       System.out.println(e.getMessage());
                   }
                   boolean l = true;
                   String result = "";
                   String newmsgID = "" + MID;
                   while(l){
                       if(ServerCommute.responseM.get(newmsgID) != null)
                       {
                           result = result + ServerCommute.responseM.get(newmsgID);
                           ServerCommute.responseM.remove(newmsgID);
                           l = false;
                           logf.info(" From Montreal : " + result);
                           return result;
                     
                       }
                   }
                   
               }
            
        return "Invalid userID";
    }
    
    public String borrowItemLocal(String userID,String itemID,int numberOfDays)
           
    {    if (h1.containsKey(itemID)) {
            BookDetails b = h1.get(itemID);
            if (ext_borrow.contains(userID)) {
                String y = "";
                y = y + userID + " cannot access more than once";
                logf.info(" Information Logged " + y);
                return y;
            } 

                if (b == null) {

                } else {
                    synchronized (this) {
                        if (b.quantity >= 1) {

                            String a = "";
                            b.quantity = b.quantity - 1;
                            h1.put(itemID, b);
                            if (borrow.containsKey(userID)) {
                                List a1 = borrow.get(userID);
                                a1.add(itemID);
                                borrow.put(userID, a1);
                            } else {
                                List<String> a2 = new ArrayList();
                                a2.add(itemID);
                                borrow.put(userID, a2);
                            }

                            a = a + "Borrowed Book : " + b.name + " with remaning quantity : " + b.quantity + " for :" + numberOfDays + " days";
                            logf.info(" Successfully Done " + a);

                            if (userID.substring(0, 4).equals("CONU") || userID.substring(0, 4).equals("MONU")) {
                                ext_borrow.add(userID);
                            }
                        return a;
                        }
                        
                        else if (b.quantity == 0) {

                            String s = "not_available";

                            logf.info(" Book info :" + s);
                            return s;
                        }
                    }
                }
            
        }
        return "";
    }
    
    public String Waitlist(String userID,String itemID)
    {
        if (itemID.substring(0, 3).equals("MCG")) 
        {
               
                synchronized (this) {
                    i = 1;
                    String s1 = "";
                    //flag = 0;
                    if (user.containsKey(itemID)) {
                        wait_list = user.get(itemID);
                        wait_list.add(userID);
                        user.put(itemID, wait_list);

                    } else {
                        Queue<String> wait_list1 = new LinkedList<>();
                        wait_list1.add(userID);
                        user.put(itemID, wait_list1);
                    }

                    s1 = s1 + userID + " added to waitlist for " + itemID;
                    logf.info(" Sucessfully Done" + s1);
 
                    return s1;
                }
            }
            
        else if(itemID.substring(0,3).equals("CON"))
        {
            String wait_msg = "";
            int msg_type = 10;
            int req_type = 4;
            wait_msg = wait_msg + msg_type + "," + req_type + "," + userID + "," + itemID;
            String MID;
            synchronized(this){
                MID = "" + (++ServerCommute.MID);
            }
            String msg = "" + MID + "," + wait_msg;
            try{
                Mcgill.sendMessage(msg,8888);
                logf.info("Request from McGill to Concordia : " + msg );
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            boolean l = true;
            String result = "";
            String newmsgID = "" + MID;
            while(l){
                if(ServerCommute.responseM.get(newmsgID) != null)
                {
                    result = result + ServerCommute.responseM.get(newmsgID);
                    ServerCommute.responseM.remove(newmsgID);
                    logf.info(" From Concordia :" + result);
                    return result;
                }
            }
        }
        
        else if(itemID.substring(0,3).equals("MON"))
        {
            String wait_msg = "";
            int msg_type = 10;
            int req_type = 4;
            wait_msg = wait_msg + msg_type + "," + req_type + "," + userID + "," + itemID;
            String MID;
            synchronized(this){
                MID = "" + (++ServerCommute.MID);
            }
            String msg = "" + MID + "," + wait_msg;
            try{
                Mcgill.sendMessage(msg,6666);
                logf.info("Request from McGill to Montreal: " + msg );
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            boolean l = true;
            String result = "";
            String newmsgID = "" + MID;  
            while(l){
                if(ServerCommute.responseM.get(newmsgID) != null)
                {
                    result = result + ServerCommute.responseM.get(newmsgID);
                    ServerCommute.responseM.remove(newmsgID);
                    logf.info("From Montreal : " + result);
                    return result;
                }
            }
        }
        return "itemId Invalid !";
    }
              
    @Override
    public String findItem(String userID, String itemName) throws RemoteException {
        String result = ""; 
        if(b.contains(userID))
        {
            result = FindItemLocal(userID,itemName) + "\n";
            String find_msg = "";
            String msg_type = "" + 10; // 0 is type : request
            String req_type = "" + 1; // 1 is type : findItem
            find_msg = find_msg + msg_type + "," + req_type + "," + userID + "," + itemName;
            String MID1 = "";
            String MID2 = "";
            
            try{
                synchronized(this)
                {
                    MID1 = "" + (++MID);
                }
                String msg = "" + MID1 + "," + find_msg;
                Mcgill.sendMessage(msg,8888);
                logf.info(" From McGill to Concordia :" + msg);
            }catch(Exception e)
            {
                
            }
            try{
                synchronized(this)
                {
                    MID2 = "" + (++MID);
                }
                String msg = "" + MID2 + "," + find_msg;
                Mcgill.sendMessage(msg,6666);
                logf.info(" From McGill to Montreal :" + msg);
            }catch(Exception e)
            {
                
            }
            
            boolean i=true,j=true;
            String newmsgID1 = "" + MID1;
            String newmsgID2 = "" + MID2;
            while(i|j){
                if(ServerCommute.responseM.get(newmsgID1)!=null)
                {
                    result = result + ServerCommute.responseM.get(newmsgID1) + "\n";
                    i = false;
                    logf.info("From Concordia: " + result);
                    ServerCommute.responseM.remove(newmsgID1);
                }
                if(ServerCommute.responseM.get(newmsgID2)!=null)
                {
                    result = result + ServerCommute.responseM.get(newmsgID2)+ "\n";
                    j = false;
                    logf.info("From Montreal: " + result);
                    ServerCommute.responseM.remove(newmsgID2);
                }
            }
            return result;
            
        }
        return "Invalid userID";
      } 
        
        public String FindItemLocal(String userID,String itemName)
        {
            //BookDetails bd = new BookDetails();
            Set set = h1.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) 
            {
                Map.Entry mentry = (Map.Entry)iterator.next();
                BookDetails bd1=h1.get(mentry.getKey());
                String temp = bd1.name;
                 if(itemName.equals(temp))
                    {
                        String s = "";
                         s = s + bd1.name + " book available in McGill Library";
                         logf.info("Sucessfully Done " + bd1.name + " available ");
                       return s;
                    }
            }
      logf.info("Not available :" + itemName);
      return "Not Available in McGill Library";
    }
    
    //@Override
    public String returnItem(String userID, String itemID) throws RemoteException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        BookDetails bd = h1.get(itemID); 
        if(itemID.substring(0,3).equals("MCG")) // b is arrayList of Mcgill userIDs & managerIDs
        {
           String s = returnItemLocal(userID,itemID);
           return s;
        }
        else if(itemID.substring(0,3).equals("CON"))
        {
           String MsgID;
           synchronized(this)
           {
               MsgID = "" + (++ServerCommute.MID);
           }
           String return_msg = "";
           int msg_type = 10; // 10 is request
           int req_type = 3; // 3 is returnItem 
           return_msg = return_msg + msg_type + "," + req_type + "," + userID + "," + itemID;
           
           String msg = "" + MsgID + "," + return_msg;
           try{
                Mcgill.sendMessage(msg,8888);
                logf.info(" From McGill to Concordia : " + msg);
           }catch(Exception e)
           {
               System.out.println(e.getMessage());
           }
        
        String newmsgID = "" + MID;
       boolean loop = true;
        while(loop){
            if(ServerCommute.responseM.get(newmsgID) != null)
            {
                String res = ServerCommute.responseM.get(newmsgID);
                logf.info(" From Concordia :" + res);
                return res;
            }
        }
     }
        else if(itemID.substring(0,3).equals("MON"))
        {
            String MsgID;
            synchronized(this)
            {
                MsgID = "" + (++ServerCommute.MID);
            }
            String return_type = "";
            int msg_type = 10;
            int req_type = 3;
            return_type = return_type + msg_type + "," + req_type + "," + userID + "," + itemID;
            String msg = "" + MsgID + "," + return_type;
            try{
                Mcgill.sendMessage(msg,6666);
                 logf.info(" From McGill to Montreal: " + msg);
            }catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
            String newmsgID = "" + MID;
       boolean loop = true;
        while(loop){
            if(ServerCommute.responseM.get(newmsgID) != null)
            {
                String res = ServerCommute.responseM.get(newmsgID);
                logf.info(" From  Montreal:" + res);
                return res;
            }
        }
            
        }
        logf.info(userID + " not allowed ");
         return "User not allowed";
    }
    
    public String returnItemLocal(String userID,String itemID){
        
     synchronized (this) {
            BookDetails bd = h1.get(itemID);
//        System.out.println("Before return ");
//            print();
//            printH();
//            printW();
            if (borrow.containsKey(userID)) {
                List x1 = borrow.get(userID);
                if (x1.contains(itemID)) 
                {
                    bd.quantity = bd.quantity + 1;
                    h1.put(itemID, bd);
                    String s = "book_receieved";
                    logf.info(itemID + s);
                    x1.remove(itemID);
                    if (x1.size() == 0) {
                        borrow.remove(userID);
                    }

                    if (i == 1) {
                        if (user.containsKey(itemID)) {
                            Queue<String> wait_list = user.get(itemID);
                            String obj = wait_list.remove();
                            
                            if (wait_list.size() > 0) {
                                borrowItemLocal(obj, itemID, 30);
                            } else {
                                borrowItemLocal(obj, itemID, 30);
                                user.remove(itemID);
                            }

                        }
                    }
                    logf.info(s);
                    return s;
                } 
           }else {
                    String s1 = "Book not borrowed here and thus not receieved";
                    logf.info(itemID + s1);
                    return s1;
                }
        }
     
    return " not borrowed";
    }
}