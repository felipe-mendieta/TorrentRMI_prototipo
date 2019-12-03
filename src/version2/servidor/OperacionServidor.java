package version2.servidor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class OperacionServidor extends UnicastRemoteObject implements OperacionInterfaz {

    public static final long serialVersionUID = 1;
    public final int PUERTO = 3232;

    public OperacionServidor() throws RemoteException {
        super();
    }

    public boolean iniciarServidor() {
        try {
            String dirIP = (InetAddress.getLocalHost()).toString();
            Registry registry=LocateRegistry.createRegistry(PUERTO);//Cuando acceda el cliente va a acceder a los metodos de esa registro en ese puerto
            registry.bind("objetoServidor", (OperacionInterfaz)this);
            System.out.println("Escuchando en: " + dirIP + ":" + PUERTO);
        } catch (UnknownHostException | AlreadyBoundException | RemoteException e) {
            System.out.println("No se realizo la conexion.");
            return false;
        }
        return true;
    }

    @Override
    public int calcularMayor(int num1, int num2) throws RemoteException {
        return Math.max(num2, num1);
    }

}
