package version2.ejecutar;

import java.rmi.RemoteException;
import modelo.Servidor;

public class MainServer {
    public static void main(String[] args) throws RemoteException {
        Servidor servidor =Servidor.crearServidor(3232);
        servidor.iniciarServidor();
        
    }
}
