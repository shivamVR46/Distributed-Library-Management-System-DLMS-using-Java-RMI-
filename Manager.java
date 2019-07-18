/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author shivam
 */
public class Manager {
    static String itemID,managerID,itemName;
    static int quantity;
      
   static Logger logf;
   static FileHandler fh;
         
    public static void Log() throws IOException{
         
    
//        Logger LOGGER = Logger.getLogger(Manager.class.getName());
        logf = Logger.getLogger(Manager.class.getSimpleName());
        fh = new FileHandler("D:/CLIENT/MANAGER/"+managerID+".log", true);
        fh.setFormatter(new SimpleFormatter());
        logf.addHandler(fh);
        
        }
    
    public static void main(String[] args) throws IOException {
        
    
   
        
         System.out.println("------ASSIGNEMENT-------");
         Scanner sc = new Scanner(System.in);
         
         System.out.println("Enter Manager ID : ");
         managerID = sc.next();
         
         int len = managerID.length();
               
         if(len==8&&managerID.substring(0,4).equals("CONM"))
            {
                Log();
                conLibrary();
                fh.close();
            } 
         else if(len==8&&managerID.substring(0,4).equals("MCGM"))
             {
                 Log();
                 mcgLibrary();
                 fh.close();
             } 
         else if(len==8&&managerID.substring(0,4).equals("MONM"))
          {
              Log();
              monLibrary(); 
              fh.close();
          }
     }
        public static void conLibrary() throws IOException
        {
                       
            try{
                int port = 1099;
                Registry registry = LocateRegistry.getRegistry(1099);
                IntremoteCON stub = (IntremoteCON) registry.lookup("//localhost:" + port + "/IntremoteCON");
                Scanner sc1 = new Scanner(System.in);
                
                System.out.println("1.) Add Item");
                System.out.println("2.) Item Availability");
                System.out.println("3.) Remove Item");
                
                System.out.println("Enter your choice : ");
                int choice;
                choice = sc1.nextInt();
           
                if(choice==1)
              {
                  System.out.println("Enter itemID : ");
                  itemID = sc1.next();
                  int len1 = itemID.length();
                  if(len1==7 && itemID.substring(0,3).equals("CON"))
                  {
                        System.out.println("Enter item Name : ");
                        sc1.nextLine();
                        itemName = sc1.nextLine();
                        System.out.println("Enter quantity : ");
                        quantity = sc1.nextInt();
                    
                        int a = stub.addItem(managerID, itemID, itemName, quantity);
                        if(a==0)
                        {
                        logf.log(Level.INFO, "{0} added Book : {1} {2} with quantity : {3}", new Object[]{managerID, itemID, itemName, quantity});
                         System.out.println(" Added Book : " + itemID + " "+ itemName + " with quantity : " + quantity);
                        }
                        else if(a==3)
                        {
                            logf.info("Not authorized");
                            System.out.println("Not authorized");
                        }
                        
                  }
                  else
                  {
                      logf.info("Invalid itemID !");
                      System.out.println("Invalid itemID ! ");
                  }
                        
              }
              else if(choice==2)
              {
                        List x = stub.itemAvailability(managerID);
                        logf.info(" Item Availability" + x);
                        System.out.println(x);
              }         
                    
              else if(choice==3)
              {
                  System.out.println("Enter Item ID : ");
                        itemID=sc1.next();
                        int len1 = itemID.length();
                  if(len1==7 && itemID.substring(0,3).equals("CON"))
                  {
                        System.out.println("Do you want to decrease the quantity of an item ? (y|n)");
                        String ans = sc1.next();
                        
                        if(ans.equals("y"))
                        {
                            System.out.println("Enter quantity : ");
                            quantity = sc1.nextInt();
                            int g = stub.removeItem(managerID,itemID,quantity);
                            if(g==4)
                            {
                                logf.info(managerID + " cleared " + itemID + "  with quantity : " + quantity);
                                System.out.println(managerID + " cleared " + itemID + "  with quantity : " + quantity );
                            }
                            else if(g==5)
                            {
                                logf.info("Book already borrowed and quantity : " + quantity + " thus clearing the item !");
                                System.out.println("Book already borrowed and quantity : " + quantity + " thus clearing the item !");
                            }
                            else if(g==6)
                            {
                                logf.info(itemID + " Cleared by " + managerID);
                                System.out.println( itemID + " Cleared ");
                            }
                            else if(g==8)
                            {
                                logf.info("not authorised");
                                System.out.println("not authorised");
                            }
                            else if(g==100)
                            {
                                logf.info("No operation performed");
                                System.out.println("Add Item cannot be performed");
                            }
                            
                        }
                        else if (ans.equals("n"))
                        {   
                             int c = stub.removeItem(managerID, itemID, 0);
                             if(c==6)
                             {
                                 logf.info( itemID + " Cleared by " + managerID);
                                 System.out.println( itemID + " Cleared ");
                             }
                        }
                        
                  }
                  else
                  {
                      logf.info("Invalid itemID");
                      System.out.println("Invalid itemID");
                  }
              }         
               else
                {
                        System.out.println("Choose any of three choices ! ");
                }
                    
        }catch(Exception e)
        {
             System.out.println("Manager Exception " + e.toString());
        }
    }
        public static void mcgLibrary()
        {
           try{
               int port = 1099;
                Registry registry = LocateRegistry.getRegistry(port);
                IntremoteMCG stub = (IntremoteMCG) registry.lookup("//localhost:"+ port + "/IntremoteMCG");
                Scanner sc1 = new Scanner(System.in);
                
                System.out.println("1.) Add Item");
                System.out.println("2.) Item Availability");
                System.out.println("3.) Remove Item");
                
                System.out.println("Enter your choice : ");
                int choice;
                choice = sc1.nextInt();
                
              if(choice==1)
              {
                  System.out.println("Enter itemID : ");
                  itemID = sc1.next();
                  int len1 = itemID.length();
                  if(len1==7 && itemID.substring(0,3).equals("MCG"))
                  {
                        System.out.println("Enter item Name : ");
                        sc1.nextLine();
                        itemName = sc1.nextLine();
                        System.out.println("Enter quantity : ");
                        quantity = sc1.nextInt();
                    
                        int b = stub.addItem(managerID, itemID, itemName, quantity);
                        if(b==0)
                        {
                            logf.log(Level.INFO, "{0} added Book : {1} {2} with quantity : {3}", new Object[]{managerID, itemID, itemName, quantity});
                            System.out.println(" Added Book : " + itemID + " "+ itemName + " with quantity : " + quantity);
                        }
                        else if(b==3)
                        {
                            logf.info("Not authorized");
                            System.out.println("Not authorized");
                        }
                  }
              }
              else if(choice==2)
              {
                       List g =  stub.itemAvailability(managerID);
                       logf.info(" Item availability : " + g);
                       System.out.println(g);
              }         
                    
              else if(choice==3)
              {
                        System.out.println("Enter Item ID : ");
                            itemID=sc1.next();
                            int len2 = itemID.length();
                  if(len2==7 && itemID.substring(0,3).equals("MCG"))
                  {
                      System.out.println("Do you want to decrease the quantity of an item ? (y|n)");
                        String ans = sc1.next();
                        
                        if(ans.equals("y"))
                        {
                            System.out.println("Enter quantity : ");
                            quantity = sc1.nextInt();
                            int g1 = stub.removeItem(managerID,itemID,quantity);
                            if(g1==4)
                            {
                                logf.info(managerID + " cleared " + itemID + "  with quantity : " + quantity );
                                System.out.println(managerID + " cleared " + itemID + "  with quantity : " + quantity );
                            }
                            else if(g1==5)
                            {
                                logf.info("Book already borrowed and quantity : " + quantity + " thus clearing the item !");
                                System.out.println("Book already borrowed and quantity : " + quantity + " thus clearing the item !");
                            }
                            else if(g1==6)
                            {
                                logf.info(itemID + " Cleared by " + managerID);
                                System.out.println( itemID + " Cleared ");
                            }
                            else if(g1==8)
                            {
                                logf.info("not authorised");
                                System.out.println("not authorised");
                            }
                            else if(g1==100)
                            {
                                logf.info("No operation performed");
                                System.out.println("Add Item cannot be performed");
                            }
                     
                            
                        }
                  
                        else if (ans.equals("n"))
                        {   
                             int c = stub.removeItem(managerID, itemID, 0);
                             if(c==6)
                             {
                                 logf.info( itemID + " Cleared by " + managerID);
                                 System.out.println( itemID + " Cleared ");
                             }
                        }
                  }
              }
               else
                {
                        System.out.println("Choose any of three choices ! ");
                }
              
               
              }
        catch(Exception e)
        {
             System.out.println("Manager Exception " + e.toString());
        }
        
        }
      
        
        public static void monLibrary()
        {
            try{
                int port = 1099;
                Registry registry = LocateRegistry.getRegistry(port);
                IntremoteMON stub = (IntremoteMON) registry.lookup("//localhost:"+ port +"/IntremoteMON");
                Scanner sc1 = new Scanner(System.in);
                
                System.out.println("1.) Add Item");
                System.out.println("2.) Item Availability");
                System.out.println("3.) Remove Item");
                
                System.out.println("Enter your choice : ");
                int choice;
                choice = sc1.nextInt();
                
              if(choice==1)
              {
                        System.out.println("Enter itemID : ");
                            itemID=sc1.next();
                            int len1 = itemID.length();
                  if(len1==7 && itemID.substring(0,3).equals("MON"))
                  {
                       
                        System.out.println("Enter item Name : ");
                        sc1.nextLine();
                        itemName = sc1.nextLine();
                        System.out.println("Enter quantity : ");
                        quantity = sc1.nextInt();
                    
                         int c = stub.addItem(managerID, itemID, itemName, quantity);
                        if(c==0)
                        {
                            logf.log(Level.INFO, "{0} added Book : {1} {2} with quantity : {3}", new Object[]{managerID, itemID, itemName, quantity});
                            System.out.println(" Added Book : " + itemID + " "+ itemName + " with quantity : " + quantity);
                            
                         }
                        else if(c==3)
                        {
                            logf.info("not authorized");
                            System.out.println("Not authorized");
                        }
                   }
              }
              else if(choice==2)
              {
                       List g =  stub.itemAvailability(managerID);
                       logf.info("item availability : " + g);
                       System.out.println(g);
              }         
                    
              else if(choice==3)
              {
                        System.out.println("Enter Item ID : ");         
                            itemID=sc1.next();
                           int len3 = itemID.length();
                  if(len3==7 && itemID.substring(0,3).equals("MON"))
                  {
                         System.out.println("Do you want to decrease the quantity of an item ? (y|n)");
                        String ans = sc1.next();
                        
                        if(ans.equals("y"))
                        {
                            System.out.println("Enter quantity : ");
                            quantity = sc1.nextInt();
                            int g2 = stub.removeItem(managerID,itemID,quantity);
                            if(g2==4)
                            {
                                logf.info(managerID + " cleared " + itemID + "  with quantity : " + quantity);
                                System.out.println(managerID + " cleared " + itemID + "  with quantity : " + quantity );
                            }
                            else if(g2==5)
                            {
                                logf.info("Book already borrowed and quantity : " + quantity + " thus clearing the item !");
                                System.out.println("Book already borrowed and quantity : " + quantity + " thus clearing the item !");
                            }
                            else if(g2==6)
                            {
                                logf.info( itemID + " Cleared by " + managerID);
                                System.out.println( itemID + " Cleared ");
                            }
                            else if(g2==8)
                            {
                                logf.info("not authorised");
                                System.out.println("not authorised");
                            }
                            else if(g2==100)
                            {
                                logf.info("No operation performed");
                                System.out.println("Add Item cannot be performed");
                            }
                     
                            
                        }
                  
                        else if (ans.equals("n"))
                        {   
                             int c1 = stub.removeItem(managerID, itemID, 0);
                             if(c1==6)
                             {
                                 logf.info(itemID + " Cleared by " + managerID);
                                 System.out.println( itemID + " Cleared ");
                             }
                        }
                  }
              }
              
               else
                {
                        System.out.println("Choose any of three choices ! ");
                }
              
                    
        }catch(Exception e)
        {
             System.out.println("Manager Exception " + e.toString());
        }
     }
}
        
     
        

