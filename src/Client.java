import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

public class Client 
{
	public static void main(String args[]) throws IOException
	{
		InetAddress address = InetAddress.getLocalHost();
	System.out.println(address);
		Socket communicationSocket = new Socket("192.168.1.21", 52282);

		String line=null;
		BufferedReader br=null;
		BufferedReader is=null;
		PrintWriter os=null;

		FileController.SetFiles(false);

		br= new BufferedReader(new InputStreamReader(System.in));
		is=new BufferedReader(new InputStreamReader(communicationSocket.getInputStream()));
		os= new PrintWriter(communicationSocket.getOutputStream());

		System.out.println("Client Address : "+address);

		String response=null;
		try
		{
			os.println("client");
			os.flush();
			response=is.readLine();
			System.out.println("Server Response : "+response);

			String printLine= "";

			line=br.readLine(); 
			while(line.compareTo("EXIT SERVER")!=0)
			{	
				FileController.SetFiles(false);
				if(line.equals("-l")){
					ListMethods();
				} else
					if(line.equals("sync check")){
						printLine = ClientMethodController.getListOfFilesAsJSONArray();
						if(printLine!=null){
							os.println("sync check");
							os.flush();
							os.println(printLine);
							os.flush();
							response=is.readLine();
							ClientMethodPrinter.PrintSyncCheck(response);
						}
					} else if(line.equals("sync all")){
						printLine = ClientMethodController.getListOfFilesAsJSONArray();
						if(printLine!=null){
							os.println("sync all");
							os.flush();
							os.println(printLine);
							os.flush();
							response=is.readLine();
							FileController.unfillFiles();
							ClientMethodController.syncResponse(response);
							FileController.SetFiles(false);
						}
					} else if(line.charAt(0) == 's'){
						final String syncName = line.substring(5);
						printLine = ClientMethodController.getFileAsJSONArray(syncName);
						os.println("sync a file");
						os.flush();
						os.println(printLine);
						os.flush();
						response=is.readLine();
						FileController.unfillFiles();
						ClientMethodController.syncResponse(response);
						FileController.SetFiles(false);
					} else if(line.length() > 4 && line.substring(0, 5).equals("multi")){
						long elapsedTime = ClientMethodController.multiEcho(line, os, is);
						System.out.println("The average time elapsed is " + elapsedTime + "ms");
					} else if(line.length() > 4 && line.substring(0, 5).equals("block")){
						ClientMethodController.blockData(line, os, communicationSocket.getInputStream());
					} else {
						System.out.println("Unknown command...");
					}
				line=br.readLine();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("Socket read Error");
		}
		finally
		{
			is.close();
			os.close();
			br.close();
			communicationSocket.close();
			System.out.println("Connection Closed");
		}
	}

	private static void ListMethods() {
		// TODO Auto-generated method stub
		System.out.println(Constants.SYNC_CHECK);
		System.out.println(Constants.SYNC_ALL);
		System.out.println(Constants.SYNC);
		System.out.println(Constants.MULTI_ECHO);
		System.out.println(Constants.BLOCK_DATA);
	}
}