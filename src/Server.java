import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.SwingUtilities;
import javax.swing.text.Utilities;


public class Server
{
	@SuppressWarnings("resource")
	public static void main(String args[])
	{
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				ServerSocket serverSocket=null;

				System.out.println("Server is on!");
				try
				{
					serverSocket = new ServerSocket(52282);
					System.out.println("listening on port: " + serverSocket.getLocalPort());
				}
				catch(IOException e)
				{
					e.printStackTrace();
					System.out.println("Server error");
				}

				FileController.SetFiles(true);

				while(true)
				{
					Socket clientSocket=null;
					try
					{
						clientSocket = serverSocket.accept();
						new Thread(new SocketDetector(clientSocket)).start();
					}
					catch(Exception e)
					{
						e.printStackTrace();
						System.out.println("Connection Error");
					}
				}

			}
		});	
	}
}