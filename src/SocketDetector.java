import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketDetector implements Runnable{

	private Socket clientSocket = null;
	private BufferedReader bufferedReader = null;


	public SocketDetector(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public String clientAnswer()
	{
		String line=null;

		InputStream clientInputStream = null;

		try {
			clientInputStream = clientSocket.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		bufferedReader= new BufferedReader(new InputStreamReader(clientInputStream));

		try
		{
			System.out.println("1");
			line=bufferedReader.readLine();
			System.out.println(line);
			return line;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return line;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String answer = clientAnswer();
		if(answer.equals("client")){
			new ServerWorker(clientSocket,bufferedReader).run();
		}
		else{
			new DataTransferWorker(clientSocket,answer).run();			
		}
	}
}
