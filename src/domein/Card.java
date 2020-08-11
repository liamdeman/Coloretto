package domein;

public class Card implements java.io.Serializable{
	//variables
	private String type;
	
	//constructor
	public Card(String type) {
		setType(type);
	}
	
	//setters
	public final void setType(String type) {
		this.type = type;
	}
	
	//getters
	public String getType() {
		return type;
	}
}