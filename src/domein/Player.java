package domein;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player implements java.io.Serializable{
	//variables
	private String name;
	private List<Card> cards = new ArrayList<>();
	private boolean hasTakenRow;
	
	//constructor
	public Player () {
		
	}
	
	public Player (String name) {
		setName(name);
	}
	
	//getters
	public String getName() {
		return name;
	}
	
	public List<Card> getCards() {
		return cards;
	}
	
	public String[] getCardsTypesSorted() {
		String[] types = new String[cards.size()];
		for (int i = 0; i < cards.size(); i++) {
			types[i] = cards.get(i).getType();
		}
		Arrays.sort(types);
		return types;
	}
	
	public boolean getHasTakenRow() {
		return hasTakenRow;
	}


	//setters
	public final void setName (String name) {
		if (name == "") {
			throw new IllegalArgumentException("De speler moet een naam krijgen");
		}
		this.name = name;
	}
	
	public void setHasTakenRow(boolean hasTakenRow) {
		this.hasTakenRow = hasTakenRow;
	}
}
