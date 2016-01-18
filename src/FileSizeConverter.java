import java.io.File;

public class FileSizeConverter {

	public static String Convert(double bytes){
		double kilobytes = (bytes / 1024);
		double megabytes = (kilobytes / 1024);
		double gigabytes = (megabytes / 1024);

		if(bytes<1024)
			return 1 + "KB";
		else if(kilobytes<1024)
			return (int) kilobytes + "KB";
		else if(megabytes<1024)
			return (int) megabytes  + "MB";
		else 
			return (int) gigabytes  + "GB";
	}
	
}
