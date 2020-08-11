package domein;
import java.util.ArrayList;
import java.util.List;

public class Row implements java.io.Serializable{
	//variables
	private boolean isTaken = false;
	private List<Card> cards;
	
	//constructor
	public Row() {
		cards = new ArrayList<>();
	}
	
	//getters
	public List<Card> getCards() {
		
		return cards;
	}
	
	public boolean getIsTaken() {
		return isTaken;
	}
	
	//setters
	public void setIsTaken(boolean isTaken) {
		this.isTaken = isTaken;
	}
	
	//methods
	public void addCard(Card card) {
		if (cards.size() == 3) {
			throw new IllegalArgumentException("Rij is vol.");
		}
		
		cards.add(card);
	}
}