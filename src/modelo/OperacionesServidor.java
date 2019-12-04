package modelo;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface OperacionesServidor extends Remote{
    public boolean login(OperacionesCliente c) throws RemoteException;
    public int calcularMayor(int num1,int num2) throws RemoteException;
}
