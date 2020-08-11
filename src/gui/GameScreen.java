package gui;

import domein.Game;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import persistentie.Mapper;

public class GameScreen extends GridPane{
	
	//nodes om op het venster te plaatsen
	Node[][] nodes = {new Label[7], new Button[17], new ImageView[17]};
	//domeincontroller
	Game game;

	public GameScreen(Game game) {
		this.game = game;
		buildGui();
		game.resetRoundBooleans();
		updateGui();
	}	
	
	private void buildGui() {
		//layout gridpane
		for (int i = 0; i <= 10; i++) {
	         ColumnConstraints column = new ColumnConstraints(100);
	         column.setHalignment(HPos.CENTER);
	         this.getColumnConstraints().add(column);
	     }
		for (int i = 0; i <= 13; i++) {
			RowConstraints row = new RowConstraints(30);
			this.getRowConstraints().add(row);
		}
		this.setPadding(new Insets(0,25,25,25));		
		
		//niet herhalende elemente maken en op gridpane zetten
		nodes[2][15] = new ImageView();
		nodes[1][15] = new Button("Draw Card");
		nodes[0][6] = new Label("Remaining Cards: 72");
		nodes[0][5] = new Label("Round 1");
		nodes[1][16] = new Button("Save Game");
		((Labeled) nodes[0][5]).setUnderline(true);
		((Labeled) nodes[0][6]).setUnderline(true);
		nodes[2][15] = new ImageView();
		nodes[2][16] = new ImageView( new Image(this.getClass().getResourceAsStream("/resources/arrow.png"),100,30,false,false));
		
		
		
		this.add(nodes[1][15], 0, 10);
		this.add(nodes[0][5], 0, 0);
		this.add(nodes[0][6], 1, 0,2,1);
		this.add(nodes[2][15], 0, 6);
		this.add(nodes[2][16], 1, 11);
		this.add(nodes[1][16], 0, 12);
		
		
		//herhalende nodes maken en op gridpane zetten
		for (int i = 0; i < 5; i++) {		
			nodes[0][i] = new Label(String.format("Player ", i+1));	
			nodes[1][i] = new Button("Take Row");
			nodes[1][i].setDisable(true);
			nodes[1][i+5] = new Button("Place Card");
			nodes[1][i+5].setDisable(true);
			nodes[1][i+10] = new Button("Show Cards");
			
			
			
			this.add(nodes[0][i], i*2+2, 11);
			this.add(nodes[1][i], i*2+2, 1);
			this.add(nodes[1][i+5], i*2+2, 2);
			this.add(nodes[1][i+10], i*2+2, 12);
		
		}
		
		//imageviews maken en op juist plaats zetten
		int imvCounter = 0;
		for (int kol = 0; kol < 5; kol++) {
			for (int row = 0; row < 3; row++) {
				
			}
		}
		for (int row = 0; row < 3; row++) {
			for (int kol = 0; kol < 5; kol++) {
				nodes[2][imvCounter] = new ImageView();
				nodes[2][imvCounter].resize(100, 150);
				this.add(nodes[2][imvCounter++], kol*2+2, row+5);
			}
		}
		
	
		//spelersnamen instellen
		for (int i = 0; i < game.getPlayer().length; i++) {
			((Label) nodes[0][i]).setText(game.getPlayer()[i].getName());
			
		}
		
		//5de speler uitschakelen
		if (game.getPlayer().length == 4) {
			for (int i = 0; i < 7; i++) {
				getNodesByPlayer()[4][i].setDisable(true);
			}
			
		}
		
		//venster maken
		Stage gameScreenStage = new Stage();
		Scene scene = new Scene(this);
		gameScreenStage.setScene(scene);
		gameScreenStage.setTitle("");
		gameScreenStage.show();
		gameScreenStage.setResizable(false);
		
		assignActionEvents();
	}
	
	private Node[][] getNodesByPlayer() {
		Node[][] returnArray = new Node[5][7];
		for (int i = 0; i < 5; i++) {
			returnArray[i][0] = nodes[1][i];
			returnArray[i][1] = nodes[1][i+5];
			returnArray[i][2] = nodes[0][i];
			returnArray[i][3] = nodes[2][i];
			returnArray[i][4] = nodes[2][i+5];
			returnArray[i][5] = nodes[2][i+10];
			returnArray[i][6] = nodes[1][i+10];
		}
		
		return returnArray;
	}
	
	private void insertImage(ImageView imv, String filename) {
		imv.setImage(new Image(getClass().getResourceAsStream("/resources/" + filename)));
	}
	
	private void insertCardImage(Node node, String card) {		
		((ImageView)node).setImage(new Image(getClass().getResourceAsStream("/resources/" + card + ".JPG"),100,150,false,false));
	}
	
	private void updateGui() {
		//remaining cards
		((Label)nodes[0][6]).setText(String.format("Remaining Cards: %d", game.getPile().getCards().size()));
		
		//checkt wanneer take row en place card buttons gedisabled moeten worden
		for (int i = 0; i < game.getPlayer().length; i++) {
			getNodesByPlayer()[i][1].setDisable(game.getDrawnCard() == null || game.getRow()[i].getCards().size() == 3 || game.getRow()[i].getIsTaken());
			getNodesByPlayer()[i][0].setDisable(game.getDrawnCard() != null || game.getRow()[i].getIsTaken() || game.getRow()[i].getCards().size() == 0);
		}
		//laat de getrokken kaart zien
		nodes[1][15].setDisable(game.getDrawnCard() != null);
		if (game.getDrawnCard() == null) {
			((ImageView)nodes[2][15]).setImage(null);
		} else {
			insertCardImage(nodes[2][15], game.getDrawnCard().getType());
		}
		//kaarten in de rijen weergeven
		for (int i = 0; i < game.getPlayer().length; i++) {
			for (int j = 0; j < game.getRow()[i].getCards().size(); j++) {
				insertCardImage(getNodesByPlayer()[i][j+3], game.getRow()[i].getCards().get(j).getType());
				
			}
		}
		
	
		//verplaatst pijltje om te weten aan wie het is
		setColumnIndex(nodes[2][16], (game.getTurnCounter()*2) + 1);
		//verandert het label met de rondeteller naar "laatste ronde"
		if (game.getPile().getCards().size() == 15) {
			((Label) nodes[0][5]).setText("Laatste ronde!!!");
		}
		//drawcard uitschakelen
		int temp = 0;
		for (int i = 0; i < game.getPlayer().length; i++) {
			if (game.getRow()[i].getIsTaken() || game.getRow()[i].getCards().size() == 3) {
				temp++;
			}
		}
		if (temp == game.getPlayer().length) {
			nodes[1][15].setDisable(true);
		}
		for (int i = 0; i < game.getPlayer().length; i++) {
			if (game.getRow()[i].getIsTaken()) {
				for (int j = 0; j < 3; j++) {
					((ImageView)getNodesByPlayer()[i][j+3]).setImage(null);

				}
			}
		}
		if (game.roundFinished()) {
			for (int i = 0; i < nodes[2].length-      2; i++) {
				((ImageView)nodes[2][i]).setImage(null);
			}
		}
		
		
		//rondenummer instellen
		((Label) nodes[0][5]).setText(String.format("Round %d", game.getRoundCounter()));
		if (game.gameFinished()) {
			//beëindigt het spel en laat de scores zien
			String score = "";
			game.saveHighScores();
			for (int i = 0; i < game.getPlayer().length; i++) {
				score += String.format("%s heeft een score van %d behaald.%n", game.getPlayer()[i].getName(), game.calculateScore(game.getPlayer()[i]));
			}			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Final Results");
			alert.setHeaderText(null);
			alert.setContentText(score);
			alert.showAndWait();
			System.exit(0);
		}
		
	
	}
	
	private void putCardInRow(int row) {
		game.getRow()[row - 1].addCard(game.giveDrawnCard());
		game.nextPlayer();
		updateGui();
	}
	
	private void showCardsPlayer(int playerNumber) {
		Stage playerCardStage = new Stage();
        playerCardStage.setTitle("Cards " + game.getPlayer()[playerNumber].getName()); 
        GridPane playerCardPane = new GridPane();
        playerCardPane.setHgap(10);
        playerCardPane.setPadding(new Insets(10,10,10,10));
        int[] cardCount = {0,0,0,0,0,0,0,0,0};
        String[] uniqueCardTypes = {"oranje", "blauw", "bruin", "geel", "grijs", "groen", "roze", "+2", "joker"};
        
        for (int i = 0; i < game.getPlayer()[playerNumber].getCards().size(); i++) {
        	int j = 0;
        	
			while ( !game.getPlayer()[playerNumber].getCards().get(i).getType().equals(uniqueCardTypes[j])) {
				j++;
			}
			cardCount[j]++;
		}
        
        for (int i = 0; i < uniqueCardTypes.length; i++) {
        	Label lblCardCount = new Label(Integer.toString(cardCount[i]));
        	Font font = new Font("System", 33);
        	lblCardCount.setFont(font);
        	playerCardPane.add(lblCardCount, i, 1);
        	ImageView cardImageView = new ImageView(new Image(getClass().getResourceAsStream("/resources/" + uniqueCardTypes[i] + ".JPG")));
        	cardImageView.setFitWidth(100.0);
        	cardImageView.setFitHeight(150.0);
        	
        	playerCardPane.add(cardImageView, i, 0);
        	ColumnConstraints columnConstraints = new ColumnConstraints(100);
        	
        	columnConstraints.setHalignment(HPos.CENTER);
        	playerCardPane.getColumnConstraints().add(columnConstraints);        	
		}   
        
        Scene scene = new Scene(playerCardPane, uniqueCardTypes.length *110 + 20, 200); 
        playerCardStage.setScene(scene); 
        playerCardStage.show();
        
        updateGui();
	}
	
	private void takeRow(int row) {
		game.assignRowToPlayer(row, game.getTurnCounter());
		updateGui();
		game.nextPlayer();
		updateGui();
	}
	//events
		private void assignActionEvents() {
			((Button)nodes[1][10]).setOnAction(this::btnShowCardsPlayerOneOnAction);
			((Button)nodes[1][11]).setOnAction(this::btnShowCardsPlayerTwoOnAction);
			((Button)nodes[1][12]).setOnAction(this::btnShowCardsPlayerThreeOnAction);
			((Button)nodes[1][13]).setOnAction(this::btnShowCardsPlayerFourOnAction);
			((Button)nodes[1][14]).setOnAction(this::btnShowCardsPlayerFiveOnAction);
			((Button)nodes[1][15]).setOnAction(this::btnDrawCardOnAction);
			((Button)nodes[1][0]).setOnAction(this::btnRowOneOnAction);
			((Button)nodes[1][1]).setOnAction(this::btnRowTwoOnAction);
			((Button)nodes[1][2]).setOnAction(this::btnRowThreeOnAction);
			((Button)nodes[1][3]).setOnAction(this::btnRowFourOnAction);
			((Button)nodes[1][4]).setOnAction(this::btnRowFiveOnAction);
			((Button)nodes[1][5]).setOnAction(this::btnPlaceCardRowOneOnAction);
			((Button)nodes[1][6]).setOnAction(this::btnPlaceCardRowTwoOnAction);
			((Button)nodes[1][7]).setOnAction(this::btnPlaceCardRowThreeOnAction);
			((Button)nodes[1][8]).setOnAction(this::btnPlaceCardRowFourOnAction);
			((Button)nodes[1][9]).setOnAction(this::btnPlaceCardRowFiveOnAction);
			((Button)nodes[1][16]).setOnAction(this::btnSaveGameOnAction);
		}
		
		public void btnSaveGameOnAction(ActionEvent event) {
			Mapper.saveGame((Object) game);
		}
		 
		public void btnShowCardsPlayerOneOnAction(ActionEvent event) {			
				showCardsPlayer(0);
		}
		 
		public void btnShowCardsPlayerTwoOnAction(ActionEvent event) {
				showCardsPlayer(1);
		}
		 
		public void btnShowCardsPlayerFiveOnAction(ActionEvent event) {
				showCardsPlayer(4);
		}
		 
		public void btnShowCardsPlayerFourOnAction(ActionEvent event) {
				showCardsPlayer(3);
		}
		 
		public void btnShowCardsPlayerThreeOnAction(ActionEvent event) {
				showCardsPlayer(2);
		}
		 
		public void btnDrawCardOnAction(ActionEvent event) {
			game.drawCard();
			updateGui();
		}
		 
		public void btnRowOneOnAction(ActionEvent event) {
			takeRow(0);
		}
		 
		public void btnRowFiveOnAction(ActionEvent event) {
			takeRow(4);
		}
		 
		public void btnRowFourOnAction(ActionEvent event) {
			takeRow(3);
		}
		 
		public void btnRowThreeOnAction(ActionEvent event) {
			takeRow(2);
		}
		 
		public void btnRowTwoOnAction(ActionEvent event) {
			takeRow(1);
		}
		 
		public void btnPlaceCardRowOneOnAction(ActionEvent event) {
			putCardInRow(1);
		}
		 
		public void btnPlaceCardRowFiveOnAction(ActionEvent event) {
			putCardInRow(5);
		}
		 
		public void btnPlaceCardRowFourOnAction(ActionEvent event) {
			putCardInRow(4);
		}
		 
		public void btnPlaceCardRowThreeOnAction(ActionEvent event) {
			putCardInRow(3);
		}
		 
		public void btnPlaceCardRowTwoOnAction(ActionEvent event) {
			putCardInRow(2);
		}
		
}
