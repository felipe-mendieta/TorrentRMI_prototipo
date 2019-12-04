package modelo;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface OperacionesServidor extends Remote{
    public boolean login(OperacionesCliente c,String nombreDelArchivo) throws RemoteException;
    public long getSizeFile(String nombre) throws RemoteException;
    public int calcularMayor(int num1,int num2) throws RemoteException;
}
