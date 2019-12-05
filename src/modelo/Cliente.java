package modelo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

public class Cliente extends UnicastRemoteObject implements OperacionesCliente, Runnable {

    private Registry registry;
    private String serverAddress;
    private int PUERTO = 3232;
    private File archivoLlegada;
    private String nombreArchivoAPedir;
    private long tamArchivoOriginal;
    private static final Set<String> ipsNoDisp = new HashSet<>();
    public OperacionesServidor objetoRemoto;

    public Cliente(String serverAddress, int PUERTO) throws RemoteException {
        super();
        this.serverAddress = serverAddress;
        this.PUERTO = PUERTO;
    }

    public static synchronized boolean addSet(String ipNoDisp) {
        return ipsNoDisp.add(ipNoDisp);
    }

    @Override
    public void run() {

        try {
            objetoRemoto = (OperacionesServidor) ejecutarMetodoRemoto("objetoServidor");
            System.out.println("nombre del archivo pedido:" + nombreArchivoAPedir + " a " + serverAddress);

            objetoRemoto.login(this, nombreArchivoAPedir);//AquiOperacionesServidor pedimos el archivo al servidor

        } catch (Exception ex) {

            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ip:" + this.serverAddress + " desconectada.");
            System.out.println("Ips no disponibles: \n" + Cliente.ipsNoDisp.toString());
            Cliente.addSet(serverAddress);
            System.out.println("Redistribuyendo a 87");
            Cliente cliente = Cliente.crearCliente("172.16.147.87", 3232);//pidiendo a linux
            Cliente.pedirParteAlServer(nombreArchivoAPedir, cliente);//aqui iria cliente.llenarArchivoplano

        }

    }

    public void conectarAlServidor() {
        try {
            registry = LocateRegistry.getRegistry(serverAddress, PUERTO);

        } catch (RemoteException e) {
            Cliente.addSet(serverAddress);
            System.out.println("No se conecto al servidor.Ips no disponibles: \n" + Cliente.ipsNoDisp.toString());
        }
    }

    public OperacionesServidor ejecutarMetodoRemoto(String identificadorObjeto) {//candidato 
        try {
            return (OperacionesServidor) (registry.lookup(identificadorObjeto));
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean sendData(String filename, byte[] data, int len) throws RemoteException {
        try {
            archivoLlegada = new File("archivosRecibidos/" + filename);
            archivoLlegada.createNewFile();
            try (FileOutputStream out = new FileOutputStream(archivoLlegada, true)) {
                out.write(data, 0, len);
                out.flush();
            }
            System.out.println("Archivo trasmitido correctamente.");
        } catch (Exception e) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, e);

        }
        return true;
    }

    public Set<String> leerArchivoPlano() {

        Set<String> servidores = new HashSet<>();
        File file = leerArchivo();
        System.out.println(file);
        try (Scanner br = new Scanner(new FileReader(file))) {
            int i = 0;

            while (br.hasNextLine()) {

                if (i == 0) {

                    nombreArchivoAPedir = br.nextLine();
                } else if (i == 1) {
                    this.tamArchivoOriginal = Long.parseLong(br.nextLine());

                } else if (i > 1) {
                    servidores.add(br.nextLine());
                }
                i++;
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo");
        }
        System.out.println(servidores.toString());
        return servidores;
    }

    public static File leerArchivo() {
        JFileChooser fileChooser = new JFileChooser(); // Interfaz para seleccionar archivo
        fileChooser.showOpenDialog(fileChooser);
        String ruta = fileChooser.getSelectedFile().getAbsolutePath();//ruta del archivo elegido
        return new File(ruta);
    }

    public static Cliente crearCliente(String serverAddress, int PUERTO) {
        Cliente cliente;
        try {
            cliente = new Cliente(serverAddress, PUERTO);
            cliente.conectarAlServidor();

            return cliente;
        } catch (RemoteException ex) {
        }

        return null;
    }

    public static void pedirParteAlServer(String nomParte, Cliente cliente) {
        cliente.setNombreArchivoAPedir(nomParte);//aqui iria cliente.llenarArchivoplano
        Thread hilo = new Thread(cliente);
        hilo.start();
    }

    public File getArchivoLlegada() {
        return archivoLlegada;
    }

    public void setNombreArchivoAPedir(String nombreArchivoAPedir) {
        this.nombreArchivoAPedir = nombreArchivoAPedir;
    }

    public String getNombreArchivoAPedir() {
        return nombreArchivoAPedir;
    }

}
