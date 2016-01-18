
public class MyFile {

	private String name;
	private double size;
	private String hashCode;
	
	public MyFile(String name, double size, String hashCode) {
		this.hashCode = hashCode;
		this.name=name;
		this.size = size;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public String getHashCode() {
		return hashCode;
	}
	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}
	
}
