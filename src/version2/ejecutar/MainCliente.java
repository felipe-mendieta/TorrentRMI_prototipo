/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package version2.ejecutar;

import java.rmi.RemoteException;
import version2.cliente.OperacionCliente;
import version2.servidor.OperacionInterfaz;



public class MainCliente {
    public static void main(String[] args) throws RemoteException {
        OperacionCliente cliente1=new OperacionCliente();
        cliente1.conectarAlServidor();
        OperacionInterfaz objetoRemoto;
        objetoRemoto=cliente1.ejecutarMetodoRemoto("objetoServidor");
        System.out.println(objetoRemoto.calcularMayor(5, 6));
        
    }
    
}
