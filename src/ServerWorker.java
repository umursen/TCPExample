import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerWorker implements Runnable {

	protected Socket clientSocket; 
	protected BufferedReader bufferedReader;

	public ServerWorker(Socket clientSocket, BufferedReader bufferedReader) {
		this.clientSocket = clientSocket;
		this.bufferedReader = bufferedReader;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		workSocket(clientSocket);
	}

	public void workSocket(Socket socket)
	{
		String line=null;
		PrintWriter os=null;

		try
		{
			os=new PrintWriter(socket.getOutputStream());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		try
		{

			os.println("Hello Dear Client...");
			os.flush();

			String answer = null;

			line=bufferedReader.readLine();
			while(line.compareTo("QUIT")!=0)
			{
				System.out.println("line: " + line);
				if(line.equals("sync check")){
					line=bufferedReader.readLine();
					answer = ServerReply.SyncCheck(line).toString();
					os.println(answer);
					os.flush();	
					System.out.println("Response to Client  :  "+answer);
				}
				if(line.equals("sync all")){
					line=bufferedReader.readLine();
					answer = ServerReply.SyncAll(line).toString();
					os.println(answer);
					os.flush();	
					System.out.println("Response to Client  :  "+answer);
				}
				if(line.equals("sync a file")){
					line=bufferedReader.readLine();
					answer = ServerReply.Sync(line).toString();
					os.println(answer);
					os.flush();	
					System.out.println("Response to Client  :  "+answer);
				}
				if(line.equals("multiEcho")){
					line=bufferedReader.readLine();
					answer = line;
					os.println(answer);
					os.flush();	
					System.out.println("Response to Client  :  "+answer);
				}
				if(line.equals("blockData")){
					line = bufferedReader.readLine();
					ServerReply.BlockData(line, socket.getOutputStream());
				}

				line=bufferedReader.readLine();
			}
		}
		catch (IOException e) 
		{
			try {
				bufferedReader.close();
				os.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}

		finally
		{    
			try{
				System.out.println("Closing Connection");
				if (bufferedReader!=null)
				{
					bufferedReader.close(); 
				}

				if(os!=null)
				{
					os.close();
				}
				if (socket!=null)
				{
					socket.close();
				}
			}
			catch(IOException ie)
			{
				ie.printStackTrace();
			}
		}
	}
}
