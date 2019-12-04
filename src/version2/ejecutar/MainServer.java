package version2.ejecutar;

import java.rmi.RemoteException;
import modelo.Servidor;

public class MainServer {
    public static void main(String[] args) throws RemoteException {
        Servidor servidor=new Servidor(3232);
        servidor.setNombreDelArchivo("repositorioArchivos/parte.pdf");
        servidor.iniciarServidor();
    }
}
