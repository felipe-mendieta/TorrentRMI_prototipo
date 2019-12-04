package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends UnicastRemoteObject implements OperacionesServidor {

    private static final long serialVersionUID = 1;
    private int PUERTO = 3232;
    private String nombreDelArchivo;

    public Servidor(int PUERTO) throws RemoteException {
        super();
        this.PUERTO = PUERTO;

    }

    public boolean iniciarServidor() {
        try {
            String dirIP = (InetAddress.getLocalHost()).toString();
            Registry registry = LocateRegistry.createRegistry(PUERTO);//Cuando acceda el cliente va a acceder a los metodos de esa registro en ese puerto
            registry.bind("objetoServidor", (OperacionesServidor) this);
            System.out.println("Escuchando en: " + dirIP + ":" + PUERTO);
        } catch (UnknownHostException | AlreadyBoundException | RemoteException e) {
            System.out.println("No se realizo la conexion.");
            return false;
        }
        return true;
    }

    @Override
    public boolean login(OperacionesCliente c) throws RemoteException {//Envia el archivo

        try {
            File f1 = new File(nombreDelArchivo);
            FileInputStream in = new FileInputStream(f1);
            byte[] mydata = new byte[(int) (f1.length())];
            int mylen = in.read(mydata);
            while (mylen > 0) {
                c.sendData(f1.getName(), mydata, mylen);//Nombre del archivo, bytes de datos, tamArchivo
                mylen = in.read(mydata);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        return true;
    }

    @Override
    public int calcularMayor(int num1, int num2) throws RemoteException {
        return Math.max(num2, num1);
    }

    public void setNombreDelArchivo(String nombreDelArchivo) {
        this.nombreDelArchivo = nombreDelArchivo;
    }

    public Servidor crearServidor(int PUERTO) {
        try {
            Servidor sevidor = new Servidor(PUERTO);
            return sevidor;
        } catch (RemoteException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
