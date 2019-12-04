/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package version2.ejecutar;

import java.rmi.RemoteException;
import modelo.Cliente;

public class MainCliente {

    public static void main(String[] args) throws RemoteException {
        Cliente cliente1 = Cliente.crearCliente("localhost", 3232);
        Cliente cliente21 = Cliente.crearCliente("localhost", 3232);
       
    }

}
