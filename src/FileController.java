import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileController {

	private static MyFile[] listOfFiles;

	public FileController() {
		// TODO Auto-generated constructor stub
	}
	
	public static void unfillFiles(){
		listOfFiles = null;
	}

	public static void SetFiles(boolean server){
		File folder;
		if(server)
			folder = new File("Server/");
		else 
			folder = new File("Local/");
		File[] files = folder.listFiles();
		listOfFiles = new MyFile[files.length];
		for(int n = 0; n < files.length; n++)
			listOfFiles[n] = new MyFile(files[n].getName(), 
					files[n].length(), 
					HashFile(files[n].getName(), server));
	}

	public static String HashFile(String location, boolean server){

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			FileInputStream fis;
			if(server)
				fis = new FileInputStream("Server/" + location);
			else
				fis = new FileInputStream("Local/" + location);

			byte[] dataBytes = new byte[1024];

			int nread = 0; 
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}

			byte[] mdbytes = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			fis.close();
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};

		return null;
	}

	public static MyFile[] getListOfFiles() {
		return listOfFiles;
	}

	public static void setListOfFiles(MyFile[] listOfFiles) {
		FileController.listOfFiles = listOfFiles;
	}
}
