import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class DataTransferWorker implements Runnable{

	private Socket clientSocket;

	private static String fileName;


	public DataTransferWorker(Socket clientSocket, String fileName) {
		// TODO Auto-generated constructor stub
		this.clientSocket = clientSocket;
		this.fileName = fileName;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			transfer(clientSocket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void transfer(Socket clientSocket) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Socket is Accepted!");
		File file = new File("Server/" + fileName);
		byte[] buffer = new byte[(int)file.length()];
		FileInputStream fileInputStream = new FileInputStream(file);
		BufferedInputStream bin = new BufferedInputStream(fileInputStream);
		bin.read(buffer,0,buffer.length);
		OutputStream socketOutputStream = clientSocket.getOutputStream();
		socketOutputStream.write(buffer,0,buffer.length);
		socketOutputStream.flush();
		System.out.println("Data Transfer is successful...");
		clientSocket.close();
	}
}
