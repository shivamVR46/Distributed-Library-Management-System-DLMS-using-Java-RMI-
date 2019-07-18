/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author shivam
 */
public interface IntremoteMON extends Remote{
    public int addItem(String managerID,String itemID,String itemName,int quantity) throws RemoteException;
    public int removeItem(String managerID,String itemID,int quantity) throws RemoteException;
    public List itemAvailability(String managerID) throws RemoteException;
    public String borrowItem(String userID,String itemID,int numberOfDays,int z) throws RemoteException;
    public String findItem(String userID,String itemName) throws RemoteException;
    public String returnItem(String userID,String itemID) throws RemoteException;
    //public String decreaseItem(String managerID,String itemID,int quantity) throws RemoteException;
   
}
