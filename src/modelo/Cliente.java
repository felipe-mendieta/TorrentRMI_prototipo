package modelo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente extends UnicastRemoteObject implements OperacionesCliente {

    Registry registry;
    String serverAddress = "localhost";
    static final int PUERTO = 3232;
    static String nombreArchivo;

    public Cliente() throws RemoteException {
        super();

    }

    public boolean conectarAlServidor() {
        try {
            registry = LocateRegistry.getRegistry(serverAddress, PUERTO);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }

    public OperacionesServidor ejecutarMetodoRemoto(String identificador) {
        try {
            return (OperacionesServidor) (registry.lookup(identificador));
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean sendData(String filename, byte[] data, int len) throws RemoteException {
        try {
            File f=new File("archivosRecibidos/"+filename);
            f.createNewFile();
            try (FileOutputStream out = new FileOutputStream(f, true)) {
                out.write(data, 0, len);
                out.flush();
            }
            System.out.println("Done writing data.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
