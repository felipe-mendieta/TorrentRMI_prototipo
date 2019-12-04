package modelo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente extends UnicastRemoteObject implements OperacionesCliente {

    private Registry registry;
    private String serverAddress = "localhost";
    private int PUERTO = 3232;
    static String nombreArchivo;
    private static final Set<String> ipsNoDisp = new HashSet<>();
    public Cliente(String serverAddress,int PUERTO) throws RemoteException {
        super();
        this.serverAddress=serverAddress;
        this.PUERTO=PUERTO;
    }    
    

    public boolean conectarAlServidor() {
        try {
            registry = LocateRegistry.getRegistry(serverAddress, PUERTO);
        } catch (RemoteException e) {
            ipsNoDisp.add(serverAddress);
            System.out.println("Ips no disponibles: \n" + Cliente.ipsNoDisp.toString());
            return false;
        }
        return true;
    }

    public OperacionesServidor ejecutarMetodoRemoto(String identificadorObjeto) {
        try {
            return (OperacionesServidor) (registry.lookup(identificadorObjeto));
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean sendData(String filename, byte[] data, int len) throws RemoteException {
        try {
            File f = new File("archivosRecibidos/" + filename);
            f.createNewFile();
            try (FileOutputStream out = new FileOutputStream(f, true)) {
                out.write(data, 0, len);
                out.flush();
            }
            System.out.println("Done writing data.");
        } catch (IOException e) {
            System.out.println("Ip:" + this.serverAddress + " desconectada.");
            System.out.println("Ips no disponibles: \n" + Cliente.ipsNoDisp.toString());
            e.printStackTrace();
        }
        return true;
    }
    
    public Cliente crearCliente(String serverAddress,int PUERTO){
        Cliente cliente;
        try {
            cliente = new Cliente(serverAddress, PUERTO);
            cliente.conectarAlServidor();
            return cliente;
        } catch (RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
