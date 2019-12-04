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

public class Servidor extends UnicastRemoteObject implements OperacionesServidor, Runnable {

    private static final long serialVersionUID = 1;
    private int PUERTO = 3232;
    private String nombreDelArchivo ;
    Registry registry;

    @Override
    public void run() {

        iniciarServidor();

    }

    public void iniciarServidor() {
        try {
            String dirIP = (InetAddress.getLocalHost()).toString();
            registry = LocateRegistry.createRegistry(PUERTO);//Cuando acceda el cliente va a acceder a los metodos de esa registro en ese puerto
            registry.bind("objetoServidor", (OperacionesServidor) this);
            System.out.println("Escuchando en: " + dirIP + ":" + PUERTO);
        } catch (UnknownHostException | AlreadyBoundException | RemoteException e) {
            System.err.println("Fallo al iniciar el servidor.");
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public Servidor(int PUERTO) throws RemoteException {
        super();
        this.PUERTO = PUERTO;

    }

    @Override
    public boolean login(OperacionesCliente c,String nombreDelArchivo) throws RemoteException {//Envia el archivo

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
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, e);

        }

        return true;
    }

    @Override
    public long getSizeFile(String nombre) {
        File archivo = new File("repositorioArchivos/" + nombre);
        return archivo.length();

    }

    @Override
    public int calcularMayor(int num1, int num2) throws RemoteException {
        return Math.max(num2, num1);
    }

    public void setNombreDelArchivo(String nombreDelArchivo) {
        this.nombreDelArchivo = nombreDelArchivo;
    }

    public static Servidor crearServidor(int PUERTO) {
        try {
            Servidor server = new Servidor(PUERTO);
            //Thread thread = new Thread(server);
            //thread.start();
            return server;
        } catch (RemoteException ex) {

            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
