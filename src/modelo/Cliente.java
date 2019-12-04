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
    private String serverAddress ;
    private int PUERTO = 3232;
    static String nombreArchivo;
    private int tamArchivo;
    private static final Set<String> ipsNoDisp = new HashSet<>();

    public Cliente(String serverAddress, int PUERTO) throws RemoteException {
        super();
        this.serverAddress = serverAddress;
        this.PUERTO = PUERTO;
    }

    @Override
    public void run() {
        try {
            registry = LocateRegistry.getRegistry(serverAddress, PUERTO);
        } catch (RemoteException e) {
            ipsNoDisp.add(serverAddress);
            System.out.println("Ips no disponibles: \n" + Cliente.ipsNoDisp.toString());
        }
    }

    public OperacionesServidor ejecutarMetodoRemoto(String identificadorObjeto) {
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
            File f = new File("archivosRecibidos/" + filename);
            f.createNewFile();
            try (FileOutputStream out = new FileOutputStream(f, true)) {
                out.write(data, 0, len);
                out.flush();
            }
            System.out.println("Done writing data.");
        } catch (IOException e) {
            System.out.println("Ip:" + this.serverAddress + " desconectada.");
            System.out.println("Ips no disponibles: \n" + Cliente.ipsNoDisp.toString());
            e.printStackTrace();
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
                    nombreArchivo = br.nextLine();
                } else if (i == 1) {
                    this.tamArchivo = Integer.parseInt(br.nextLine());

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
        Runnable cliente;
        try {
            cliente = new Cliente(serverAddress, PUERTO);
            Thread hilo=new Thread(cliente);
            hilo.start();
            return (Cliente)cliente;
        } catch (RemoteException ex) {
            System.err.println("Ip no disponible, no se conecto con: "+PUERTO);
            ipsNoDisp.add(serverAddress);
            System.out.println("Ips no disponibles: \n" + Cliente.ipsNoDisp.toString());
            
        }

        return null;
    }

}
