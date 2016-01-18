import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientDataTransferRunnable implements Runnable{

	private static int BUFFER_SIZE = 101683000;

	private String name;
	private String work;
	private String size;
	
	public ClientDataTransferRunnable(String name, String work, String size) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.work = work;
		this.size = size;
	}
	
	private static void transfer(String name, String work, String size) throws IOException {
		InetAddress address=InetAddress.getLocalHost();
		Socket dataTransferSocket = new Socket(address, 4445);
		InputStream clientInputStream = dataTransferSocket.getInputStream();
		FileOutputStream fileOuputStream = 
				new FileOutputStream("Local/" + name);
		BufferedOutputStream bos = new BufferedOutputStream(fileOuputStream);

		byte[] buffer = new byte[BUFFER_SIZE];
		int read;
		int totalRead = 0;

		if(work.equals("add"))
			System.out.println("Downloading " + name + " " + size);
		else
			System.out.println("Updating " + name + " " + size);
		
		PrintWriter os= new PrintWriter(dataTransferSocket.getOutputStream());
		os.println(name);
		os.flush();
		read = clientInputStream.read(buffer,0,buffer.length);
		totalRead = read;
		do {
			read = clientInputStream.read(buffer, totalRead, (buffer.length-totalRead));
			if(read >= 0) totalRead += read;
		} while(read > 0);

		bos.write(buffer,0,totalRead);
		bos.flush();
		if(work.equals("add"))
			System.out.println(name + " has been downloaded completely");
		else
			System.out.println(name + " has been updated successfully");
		bos.close();		
		dataTransferSocket.close();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			transfer(name, work, size);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
