package version2.ejecutar;

import java.rmi.RemoteException;
import modelo.Cliente;

public class MainCliente {

    public static void main(String[] args) throws RemoteException {

        Cliente cliente2 = Cliente.crearCliente("172.16.147.26", 3232);
        Cliente.pedirParteAlServer("repositorioArchivos/parte2.iso", cliente2);//aqui iria cliente.llenarArchivoplano

        Cliente cliente1 = Cliente.crearCliente("172.16.147.74", 3232);
        Cliente.pedirParteAlServer("repositorioArchivos/parte1.iso", cliente1);//aqui iria cliente.llenarArchivoplano

        Cliente cliente = Cliente.crearCliente("172.16.147.100", 3232);//Ficticio
        Cliente.pedirParteAlServer("repositorioArchivos/parte1.iso", cliente);//aqui iria cliente.llenarArchivoplano

    }

}
