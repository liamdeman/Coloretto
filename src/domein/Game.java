package domein;
import java.util.Arrays;

import persistentie.Mapper;


public class Game implements java.io.Serializable{
	//variables
	private Player[] player;	
	private CardPile pile;	
	private Row[] row;
	private Card drawnCard;
	private int turnCounter;
	private int roundCounter;
	
	//constructor
	public Game(String[] names) {
		pile = new CardPile();
		player = new Player[names.length];
		row = new Row[names.length];
		turnCounter = 0;
		roundCounter = 0;
		for (int i = 0; i < names.length; i++) {
			player[i] = new Player(names[i]);
			player[i].getCards().add(pile.drawCard(pile.getColors().get(i)));
			row[i] = new Row();
		}
		
	}
	
	//getters		
	public Row[] getRow() {
		return row;
	}
	
	public CardPile getPile() {
		return pile;
	}
	
	public Player[] getPlayer() {
		return player;
	}

	public Card getDrawnCard() {
		return this.drawnCard;
	}
	
	public int getTurnCounter() {
		return this.turnCounter;
	}
	
	public int getRoundCounter() {
		return this.roundCounter;
	}
	
	
	
	
	
	public void nextPlayer() {
		if (this.roundFinished()) {
			resetRound();
		}else {
			turnCounter++;
			if (turnCounter  == player.length) {
				turnCounter=0;
			}
			while (player[turnCounter].getHasTakenRow()) {
				turnCounter++;

				if (turnCounter  == player.length) {
					turnCounter=0;

				}
			}
		}
		
		
	}
	
	public void drawCard() {
		drawnCard = pile.drawCard();
		
	}

	public Card giveDrawnCard() {
		Card card = this.drawnCard;
		this.drawnCard = null;
		return card;
	}
	
	public boolean roundFinished() {
		boolean result = true;
		
		for (Player player : getPlayer()) {
			if (!player.getHasTakenRow()) {
				result = false;
				break;
			}
		}
		
		
		return result;
	}
	
	public void assignRowToPlayer(int selectedRow, int selectedPlayer) {
		System.out.println(selectedRow);
		if (row[selectedRow].getCards().size() == 0) {
			throw new IllegalArgumentException("Je kan geen lege rij nemen.");
		}
		
		row[selectedRow].setIsTaken(true);
		player[selectedPlayer].getCards().addAll(row[selectedRow].getCards());
		player[selectedPlayer].setHasTakenRow(true);
		row[selectedRow].getCards().clear();
	}
	
	public void resetRound() {
		resetRoundBooleans();
		roundCounter++;
		turnCounter = 0;
		

	}
	
	public void resetRoundBooleans() {
		for (int i = 0; i < getPlayer().length; i++) {
			getPlayer()[i].setHasTakenRow(false);
			getRow()[i].setIsTaken(false);
		}
	}
	public boolean gameFinished() {
		return (this.getPile().getCards().size() <= 15 && this.roundFinished());
	}
	public void saveHighScores() {
		
		for (Player player : player) {
			Mapper.setHighScoreToDatabase(player.getName(), calculateScore(player));
		}
	}
	public int calculateScore(Player player) {
		int plusTwee = 0;
		int joker = 0;
		int score = 0;
		int[] colorCount = new int[7];
				
		for (Card card : player.getCards()) {		
			switch (card.getType()) {
				case "+2":
					plusTwee++;
					break;
						
				case "joker":
					joker++;
					break;	
						
				default:
					colorCount[pile.getColors().indexOf(card.getType())]++;
					break;
			}
		}
		
		for (int i = 0; i < plusTwee; i++) {
			score += 2;
		}
		
		Arrays.sort(colorCount);
		
		for (int i = 0; i < joker; i++) {
			int j = 6;

			while (j > 4) {
				if (colorCount[j] < 6) {
					colorCount[j]++;
					break;
				}

				j--;
			}

			if (j == 4) {
				colorCount[6]++;
			}
		}
		
		for (int i = 0; i < colorCount.length; i++) {
			if (i > 3) {
				score += (colorCount[i] * (colorCount[i] + 1)) / 2;
			} else {
				score -= (colorCount[i] * (colorCount[i] + 1)) / 2;
			}
		}
		
		return score;
	}
}