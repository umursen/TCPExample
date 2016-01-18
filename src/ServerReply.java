import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerReply {

	public static JSONArray SyncCheck(String jsonArrayString){

		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(jsonArrayString);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		MyFile[] clientFiles = new MyFile[jsonArray.length()];

		if (jsonArray != null) { 
			for (int i=0;i<jsonArray.length();i++){ 
				try {
					clientFiles[i] = 
							new MyFile(((JSONObject) jsonArray.get(i)).getString("name"),
									((JSONObject) jsonArray.get(i)).getDouble("size"),
									((JSONObject) jsonArray.get(i)).getString("hashCode")
									);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		ArrayList<String> responses = new ArrayList<String>();
		MyFile[] serverFiles = FileController.getListOfFiles();

		ArrayList<MyFile> filesToAdd = FilesToAdd(clientFiles,serverFiles);
		ArrayList<MyFile> filesToUpdate = FilesToUpdate(clientFiles,serverFiles);
		ArrayList<MyFile> filesToDelete = FilesToDelete(clientFiles,serverFiles);

		HandleResponse(1,filesToAdd,responses);
		HandleResponse(2,filesToUpdate,responses);
		HandleResponse(3,filesToDelete,responses);

		double bytes = 0;

		for(MyFile f : filesToAdd)
			bytes+= f.getSize();
		for(MyFile f : filesToUpdate)
			bytes+= f.getSize();
		for(MyFile f : filesToDelete)
			bytes+= f.getSize();

		responses.add("The total size of the updates is " + FileSizeConverter.Convert(bytes));
		responses.add("Sync check finished.");

		return new JSONArray(responses);
	}

	public static JSONArray SyncAll(String jsonArrayString){

		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(jsonArrayString);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		MyFile[] clientFiles = new MyFile[jsonArray.length()];

		if (jsonArray != null) { 
			for (int i=0;i<jsonArray.length();i++){ 
				try {
					clientFiles[i] = 
							new MyFile(((JSONObject) jsonArray.get(i)).getString("name"),
									((JSONObject) jsonArray.get(i)).getDouble("size"),
									((JSONObject) jsonArray.get(i)).getString("hashCode")
									);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		MyFile[] serverFiles = FileController.getListOfFiles();

		ArrayList<MyFile> filesToAdd = FilesToAdd(clientFiles,serverFiles);
		ArrayList<MyFile> filesToUpdate = FilesToUpdate(clientFiles,serverFiles);
		ArrayList<MyFile> filesToDelete = FilesToDelete(clientFiles,serverFiles);

		JSONArray respondToSend = new JSONArray();

		JSONObject obj;

		try{
			for(MyFile f : filesToAdd){
				obj = new JSONObject();
				obj.put("name", f.getName());
				obj.put("size", FileSizeConverter.Convert(f.getSize()));
				obj.put("work", "add");
				respondToSend.put(obj);
			}

			for(MyFile f : filesToDelete){
				obj = new JSONObject();
				obj.put("name", f.getName());
				obj.put("size", FileSizeConverter.Convert(f.getSize()));
				obj.put("work", "delete");

				respondToSend.put(obj);
			}

			for(MyFile f : filesToUpdate){
				obj = new JSONObject();
				obj.put("name", f.getName());
				obj.put("size", FileSizeConverter.Convert(f.getSize()));
				obj.put("work", "update");
				respondToSend.put(obj);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return respondToSend;
	}

	private static void HandleResponse(int n, ArrayList<MyFile> files, ArrayList<String> response){
		switch(n){
		case 1: for(MyFile f : files){
			response.add(f.getName() + " add " + FileSizeConverter.Convert(f.getSize())); 
		};break;
		case 2: for(MyFile f : files){
			response.add(f.getName() + " update " + FileSizeConverter.Convert(f.getSize()));
		};break;
		case 3: for(MyFile f : files){
			response.add(f.getName() + " delete " + FileSizeConverter.Convert(f.getSize()));
		};break;
		}
	}

	private static ArrayList<MyFile> FilesToUpdate(MyFile[] clientFiles, MyFile[] serverFiles) {

		ArrayList<MyFile> files = new ArrayList<MyFile>();

		for(int x = 0; x < serverFiles.length; x++){
			if(FileNameExists(serverFiles[x].getName(),clientFiles) &&
					!FileHashValueExists(serverFiles[x].getHashCode(),clientFiles))
				files.add(serverFiles[x]);
		}
		return files;
	}

	private static ArrayList<MyFile> FilesToAdd(MyFile[] clientFiles, MyFile[] serverFiles) {
		ArrayList<MyFile> files = new ArrayList<MyFile>();
		for(int x = 0; x < serverFiles.length; x++){
			System.out.println("server file : " + serverFiles[x].getName());
			if(!FileNameExists(serverFiles[x].getName(),clientFiles) &&
					!FileHashValueExists(serverFiles[x].getHashCode(),clientFiles)){
				files.add(serverFiles[x]);
			}
		}
		return files;
	}

	private static ArrayList<MyFile> FilesToDelete(MyFile[] clientFiles, MyFile[] serverFiles) {

		ArrayList<MyFile> files = new ArrayList<MyFile>();

		for(int x = 0; x < clientFiles.length; x++){
			if(!FileNameExists(clientFiles[x].getName(),serverFiles) &&
					!FileHashValueExists(clientFiles[x].getHashCode(),serverFiles))
				files.add(clientFiles[x]);
		}
		return files;
	}

	private static boolean FileHashValueExists(String value, MyFile[] files){

		for(int n = 0; n < files.length;n++){
			if(value.equals(files[n].getHashCode()))
				return true;
		}
		return false;
	}

	private static boolean FileNameExists(String value, MyFile[] files){

		for(int n = 0; n < files.length;n++){
			if(value.equals(files[n].getName()))
				return true;
		}
		return false;
	}


	public static JSONArray Sync(String jsonObjectString){
		JSONObject jsonObject = null;
		JSONArray jArr = null;
		try {
			jArr = new JSONArray(jsonObjectString);
			jsonObject = (JSONObject) jArr.get(0);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		MyFile f = null;
		try {
			f = new MyFile(jsonObject.getString("name"),
					jsonObject.getDouble("size"),
					jsonObject.getString("hashCode")
					);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MyFile[] serverFiles = FileController.getListOfFiles();

		JSONArray respondToSend = new JSONArray();

		JSONObject obj;


		try{
			if(!FileNameExists(f.getName(), serverFiles)){
				//error
				obj = new JSONObject();
				obj.put("name", f.getName());
				obj.put("size", FileSizeConverter.Convert(f.getSize()));
				obj.put("work", "delete");
				respondToSend.put(obj);
			} else if(!FileHashValueExists(f.getHashCode(), serverFiles) && !f.getHashCode().equals(" ")){
				obj = new JSONObject();
				obj.put("name", f.getName());
				obj.put("size", FileSizeConverter.Convert(f.getSize()));
				obj.put("work", "update");
				respondToSend.put(obj);
			} else {
				obj = new JSONObject();
				obj.put("name", f.getName());
				obj.put("size", FileSizeConverter.Convert(f.getSize()));
				obj.put("work", "add");
				respondToSend.put(obj);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return respondToSend;
	}

	public static void BlockData(String line, OutputStream socketOutputStream) {
		int n = 0;
		while(line.charAt(n) != ':')			
			n++;
		System.out.println(line + " " + n);
		int startByte = Integer.parseInt(line.substring(0,n));
		int size = Integer.parseInt(line.substring(n+1));
		int finishByte = startByte + size;
		
		byte[] buffer = new byte[size];

		try {
			Path path = Paths.get("Server/alice.txt");
			byte[] data = Files.readAllBytes(path);
			System.arraycopy(data, startByte, buffer, 0, size);
			socketOutputStream.write(buffer,0,buffer.length);
			socketOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
