/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.rmi.RemoteException;
import java.util.logging.*;
import sun.rmi.runtime.Log;


/**
 *
 * @author shivam
 */
public class User {
    static String userID,itemID,itemNameU;
    
    static Logger logf;
    static FileHandler fh;
    
    public static void Log() throws IOException{
         Logger LOGGER = Logger.getLogger(Manager.class.getName());
        logf = Logger.getLogger(Manager.class.getSimpleName());
        fh = new FileHandler("D:/CLIENT/USER/"+userID+".log", true);
        fh.setFormatter(new SimpleFormatter());
        logf.addHandler(fh);
    }
    public static void main(String args[]) throws IOException
    {
        //String itemID,itemNameU;
        
        //String lib;

            System.out.println("--------- ASSIGNMENT 1 ---------");
        
            Scanner sc = new Scanner(System.in);
            
            System.out.println("Enter User ID : ");
            userID = sc.next(); 
             
            int len = userID.length();
               
            if(len==8&&userID.substring(0,4).equals("CONU"))
                //{
                 // if(userID.substring(0,4).equals("CONU"))
                    {
                        Log();
                        conLibrary();
                    } 
                //}   
            else if(len==8&&userID.substring(0,4).equals("MCGU"))
               // {
                    //if(userID.substring(0,4).equals("MCGU"))
                        {
                            Log();
                            mcgLibrary();
                        } 
               // }
            else if(len==8&&userID.substring(0,4).equals("MONU"))
              //  {
                    if(userID.substring(0,4).equals("MONU"))
                        {
                            Log();
                            monLibrary(); 
                        }
               // }
    }
        public static void conLibrary()
       {
            int choice;
            Scanner sc1 = new Scanner(System.in);
            try{
                int port=1099;
            Registry registry = LocateRegistry.getRegistry(port);
            IntremoteCON stub = (IntremoteCON) registry.lookup("//localhost:"+port +"/IntremoteCON");
          
            
            System.out.println("1.) Find ");
            System.out.println("2.) Borrow ");
            System.out.println("3.) Return ");
            
            System.out.println("Enter your choice : ");
            choice = sc1.nextInt(); 
            int wt=0;
            
            if(choice==1)
            {
                
                   System.out.println("Enter name of the book : ");
                   sc1.nextLine();
                   itemNameU = sc1.nextLine();
                       
                   String b = stub.findItem(userID, itemNameU);  
                   logf.info( userID + " : "+  b);
                   System.out.println(b); 
             }     
                   
            else if(choice==2)
            {
                        System.out.println("Enter Item ID : ");
                        itemID = sc1.next();
                        
                       
                         int len1 = itemID.length();
                  if(len1==7)
                  {
                            System.out.println("Enter number of days you want : ");
                            int numberOfDays = sc1.nextInt();
                          
                            String a =  stub.borrowItem(userID, itemID,numberOfDays,0);
                            logf.info(userID + ":" +  a);
                            System.out.println(a);
                            
                            if(a.equals("not_available")) 
                            {
                                System.out.println("Do you want to get added to the waitlist ? (y|n)");
                                String y = sc1.next();
                                
                                if(y.equals("y"))
                                {
                                    String x = stub.borrowItem(userID,itemID,numberOfDays,1); 
                                    logf.info( userID + " : " + x);
                                    System.out.println(x);
                                }
                                else
                                {
                                    logf.info(userID + " Method ends !");
                                    System.out.println(" Method ends !");
                                }        
                            }
                        }  
                        
            }
            else if(choice==3)
            {
                  System.out.println("Enter Item ID : ");
                  itemID = sc1.next();
                   int len1 = itemID.length();
                  if(len1==7)// && itemID.substring(0,3).equals("CON"))
                  {
                  String c =  stub.returnItem(userID, itemID);
                  logf.info(userID + ":" + c);
                  System.out.println(c);
                  }
                  else
                  {
                      logf.info(userID + ":" + "Invalid itemID !");
                      System.out.println("Invalid itemID !");
                  }
            }
            else
            {
                System.out.println("Please choose of of the above options !");
            }  
        } catch(Exception e)
        {
            System.out.println("User Exception " + e.toString());
        }
    } 
        public static void mcgLibrary()
        {
            int choice;
            Scanner sc1 = new Scanner(System.in);
            try{
                int port = 1099;
            Registry registry = LocateRegistry.getRegistry(port);
            IntremoteMCG stub = (IntremoteMCG) registry.lookup("//localhost:" + port + "/IntremoteMCG");
          
            
            System.out.println("1.) Find ");
            System.out.println("2.) Borrow ");
            System.out.println("3.) Return ");
            
            System.out.println("Enter your choice : ");
            choice = sc1.nextInt(); 
                
            if(choice==1)
            {
                
                   System.out.println("Enter name of the book : ");
                   sc1.nextLine();
                   itemNameU = sc1.nextLine();
                       
                   String b = stub.findItem(userID, itemNameU);  
                   logf.info( userID + " : "+  b);
                   System.out.println(b); 
             }     
                   
            else if(choice==2)
            {   
                             System.out.println("Enter Item ID : ");
                        itemID = sc1.next();
                        
                         int len1 = itemID.length();
                  if(len1==7)
                  {
                            System.out.println("Enter number of days you want : ");
                            int numberOfDays = sc1.nextInt();
                          
                            String a =  stub.borrowItem(userID, itemID,numberOfDays,0);
                            logf.info(userID +":" + a);
                            System.out.println(a);
                            
                            if(a.equals("not_available"))
                            {
                                System.out.println("Do you want to get added to the waitlist ? (y|n)");
                                String y = sc1.next();
                                
                                if(y.equals("y"))
                                {
                                    String x = stub.borrowItem(userID,itemID,numberOfDays,1); 
                                    logf.info(userID + ":" + x);
                                    System.out.println(x);
                                }
                                else
                                {
                                    logf.info(userID +  ": Method ends !");
                                    System.out.println(" Method ends !");
                                }        
                            }
                        }  
                        else
                            {
                                logf.info(userID + ": Invalid itemID !" );
                                System.out.println("Invalid itemID !");
                            }
            }
            else if(choice==3)
            {
                     System.out.println("Enter Item ID : ");
                  itemID = sc1.next();
                   int len1 = itemID.length();
                  if(len1==7)
                  {
                  String c =  stub.returnItem(userID, itemID);
                  logf.info(userID + ":" + c);
                  System.out.println(c);
                  }
                  else
                  {
                      logf.info(userID + ":" + "Invalid itemID !");
                      System.out.println("Invalid itemID !");
                  }

            }
            else
            {
                System.out.println("Please choose of of the above options !");
            }  
        } catch(Exception e)
        {
            System.out.println("User Exception " + e.toString());
        } 
        }
        public static void monLibrary()
        {
            int choice;
            Scanner sc1 = new Scanner(System.in);
            try{
                int port = 1099;
            Registry registry = LocateRegistry.getRegistry(port);
            IntremoteMON stub = (IntremoteMON) registry.lookup("//localhost:" + port + "/IntremoteMON");
          
            
            System.out.println("1.) Find ");
            System.out.println("2.) Borrow ");
            System.out.println("3.) Return ");
            
            System.out.println("Enter your choice : ");
            choice = sc1.nextInt(); 
                
            if(choice==1)
                {
                
                    System.out.println("Enter name of the book : ");
                    sc1.nextLine();
                    itemNameU = sc1.nextLine();
                       
                   String b = stub.findItem(userID, itemNameU);  
                   logf.info(userID + ":" + b);
                   System.out.println(b); 
                }     
                   
            else if(choice==2)
                {
                         System.out.println("Enter Item ID : ");
                        itemID = sc1.next();
                        
                         int len1 = itemID.length();
                  if(len1==7)
                  {
                            System.out.println("Enter number of days you want : ");
                            int numberOfDays = sc1.nextInt();
                          
                            String a =  stub.borrowItem(userID, itemID,numberOfDays,0); 
                            logf.info(userID + ":" + a);
                            System.out.println(a);
                            
                            if(a.equals("not_available"))
                            {
                                System.out.println("Do you want to get added to the waitlist ? (y|n)");
                                String y = sc1.next();
                                
                                if(y.equals("y"))
                                {
                                    String x = stub.borrowItem(userID,itemID,numberOfDays,1); 
                                    logf.info(userID + ":" + x);
                                    System.out.println(x);
                                }
                                else
                                {
                                    logf.info(userID + ": Method ends !");
                                    System.out.println(" Method ends !");
                                }        
                            }
                        }  
                        else
                            {
                                logf.info(userID + ": Invalid itemID !");
                                System.out.println("Invalid itemID !");
                            }
                }
            else if(choice==3)
                {
                     System.out.println("Enter Item ID : ");
                  itemID = sc1.next();
                   int len1 = itemID.length();
                   
                  if(len1 == 7)
                  {
                  String c =  stub.returnItem(userID, itemID);
                  logf.info(userID + ":" + c);
                  System.out.println(c);
                  }
                  else
                  {
                      logf.info(userID + ": Invalid itemID !");
                      System.out.println("Invalid itemID !");
                  }

                }
            else
                {
                    System.out.println("Please choose of of the above options !");
                }  
        } catch(Exception e)
        {
            System.out.println("User Exception " + e.toString());
        }
        }
        
  }

    

