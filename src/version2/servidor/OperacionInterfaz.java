package version2.servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OperacionInterfaz extends Remote{
    public int calcularMayor(int num1,int num2) throws RemoteException;
}
