package version2.ejecutar;

import java.rmi.RemoteException;
import version2.servidor.OperacionServidor;

public class MainServer {
    public static void main(String[] args) throws RemoteException {
        OperacionServidor servidor=new OperacionServidor();
        servidor.iniciarServidor();
    }
}
