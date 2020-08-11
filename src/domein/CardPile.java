package domein;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CardPile implements java.io.Serializable{
	//variables
	private List<Card> cards = new ArrayList<>();
	private List<String> colors = Arrays.asList(new String[]
			{"oranje", "blauw", "bruin", "geel", "grijs", "groen", "roze"});
	
	//constructors
	public CardPile() {
		Collections.shuffle(colors);
		
		for (int i = 0; i < colors.size(); i++) {
			for (int j = 0; j < 9; j++) {
				Card card = new Card(colors.get(i));
				cards.add(card);
			}
		}
		
		for (int j = 0; j < 10; j++) {
			Card card = new Card("+2");
			cards.add(card);
		}
		
		for (int j = 0; j < 3; j++) {
			Card card = new Card("joker");
			cards.add(card);
		}
	}
	
	//getters
	public List<String> getColors() {
		return colors;
	}
	
	public List<Card> getCards() {
		return cards;
	}
	
	//methods
	public Card drawCard() {
		Random rand = new Random();
		int cardIndex = rand.nextInt(cards.size());
		Card card = cards.get(cardIndex);
		cards.remove(cardIndex);
		return card;
	}
	
	//methods
	public Card drawCard(String color) {
		int i = 0;
		
		while (i < cards.size() && cards.get(i).getType() != color) {		
			i++;
		}
		
		return cards.remove(i);
	}
}