package modelo;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface OperacionesCliente extends Remote{
    public boolean sendData(String filename, byte[] data, int len) throws RemoteException;
}
