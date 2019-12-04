/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package version2.ejecutar;

import java.rmi.RemoteException;
import modelo.Cliente;
import modelo.OperacionesServidor;



public class MainCliente {
    public static void main(String[] args) throws RemoteException {
        Cliente cliente1=new Cliente();
        cliente1.conectarAlServidor();
        OperacionesServidor objetoRemoto;
        objetoRemoto=cliente1.ejecutarMetodoRemoto("objetoServidor");
        
        objetoRemoto.login(cliente1);//Aqui pedimos el archivo al servidor
        
    }
    
}
