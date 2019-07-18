/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author shivam
 */
public class startRmiRegistry {

    public static void main(String args[]) throws RemoteException {
        Registry r = LocateRegistry.createRegistry(1099);
        while(true){
            
        }
    }
}
