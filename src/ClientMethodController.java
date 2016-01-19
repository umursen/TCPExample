import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientMethodController {

	public static String getListOfFilesAsJSONArray(){
		MyFile[] localFiles = FileController.getListOfFiles();
		try {
			return new JSONArray(localFiles).toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getFileAsJSONArray(String name){
		MyFile[] localFiles = FileController.getListOfFiles();
		MyFile[] fileToSend = new MyFile[1];
		for(MyFile f : localFiles){
			if(name.equals(f.getName())){
				try {
					fileToSend[0] = f;
					return new JSONArray(fileToSend).toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		MyFile jobj = new MyFile(name, 0, " ");
		fileToSend[0] = jobj;
		try {
			return new JSONArray(fileToSend).toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void syncResponse(String response){

		JSONArray serverAnswer = null;

		try {
			serverAnswer = new JSONArray(response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(serverAnswer.equals(null)){
			System.out.println("No update required!");
			return;
		}

		JSONObject jObj;
		String work = "";
		String name = "";
		String size = "";
		ClientDataTransferRunnable clientDataTransferRunnable;
		for(int n = 0; n < serverAnswer.length(); n++){
			try {
				jObj = (JSONObject) serverAnswer.get(n);
				work = jObj.getString("work");
				name = jObj.getString("name");
				size = jObj.getString("size");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(work.equals("add") || work.equals("update"))
				new Thread(new ClientDataTransferRunnable(name, work, size)).run();
			if(work.equals("delete")){
				File file = new File("Local/" + name);
				if(file.delete()){
					System.out.println("Deleting " + name + " " + size);
					System.out.println(name + " has been deleted successfully.");
				} else {
					System.out.println("File " + name + " exists neither in server nor client " );
				}
			}
		}
		System.out.println("Sync all finished.");
	}

	public static long multiEcho(String line, PrintWriter os, BufferedReader is) throws IOException{
		String response = "";
		int echoCount = Character.getNumericValue(line.charAt(10));
		long[] timeArr = new long[echoCount];
		line = line.charAt(10) + ":" +  line.substring(12);
		for(int n = 0; n < echoCount; n++){
			os.println("multiEcho");
			os.flush();
			long tStart = System.nanoTime();
			os.println(line);
			os.flush();
			response = is.readLine();
			long tEnd = System.nanoTime();
			long tDelta = tEnd - tStart;
			timeArr[n] = tDelta;
			System.out.println("#"+ n + ": time: " + tDelta);
		}
		
		return calculateTime(timeArr);
	}

	private static long calculateTime(long[] timeArr) {
		// TODO Auto-generated method stub
		long sum = 0;
		for(long l : timeArr){
			sum += l;
		}
		return sum/(timeArr.length);
	}

	public static void blockData(String line, PrintWriter os, InputStream is) throws IOException{
		int n = 0;
		while(line.charAt(n) != ' '){
			n++;
		}
		n++;
		int i = n;
		while(line.charAt(n) != ' '){
			n++;
		}
		n++;
		String printLine = line.substring(i,n -1) + ":" + line.substring(n);
		os.println("blockData");
		os.flush();
		os.println(printLine);
		os.flush();
		
		int size = Integer.parseInt(line.substring(n));
		byte[] buffer = new byte[size];
		
		int response = is.read(buffer,0,buffer.length);
		String decoded = new String(buffer, "UTF-8");
		System.out.println(decoded);
	}
}
