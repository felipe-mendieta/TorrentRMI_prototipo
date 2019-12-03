package version1.servidor;

import java.rmi.Naming;

public class StartFileServer {
    public static void main(String[] args) {
        try {
            java.rmi.registry.LocateRegistry.createRegistry(3232);

            FileServer fs = new FileServer();
            
            fs.setFile("parte.pdf");
            Naming.rebind("rmi://127.0.0.1/ABC", fs);
            System.out.println("File Server is Ready");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
