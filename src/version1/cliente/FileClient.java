package version1.cliente;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class FileClient  extends UnicastRemoteObject implements FileClientInt {

	private static final long serialVersionUID = 1L;
	public String name;
	public  FileClient(String n) throws RemoteException {
		super();
		name=n;
	}
 
        @Override
	public String getName() throws RemoteException{
		return name;
	}
    
        @Override
	public boolean sendData(String filename, byte[] data, int len) throws RemoteException{
        try{
        	File f=new File(filename);
        	f.createNewFile();
        	FileOutputStream out=new FileOutputStream(f,true);
        	out.write(data,0,len);
        	out.flush();
        	out.close();
        	System.out.println("Done writing data...");
        }catch(IOException e){
        	e.printStackTrace();
        }
		return true;
	}
}