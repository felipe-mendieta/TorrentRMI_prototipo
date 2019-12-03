package version2.cliente;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import version2.servidor.OperacionInterfaz;//importamos la interfaz o podemos expotar su jar

public class OperacionCliente {
    Registry registry;
    String serverAddress="localhost";
    static final int PUERTO=3232;
    static String nombreArchivo;
    public boolean conectarAlServidor(){
        try {
            registry=LocateRegistry.getRegistry(serverAddress,PUERTO);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
    public OperacionInterfaz ejecutarMetodoRemoto(String identificador){
        try {
            return (OperacionInterfaz)(registry.lookup(identificador));
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(OperacionCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
