package gui;


import java.util.ArrayList;
import java.util.List;

import domein.Game;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import persistentie.Mapper;

public class WelcomeScreen extends GridPane{
	Stage primaryStage;
	List<String> playerNames = new ArrayList<>();
	int playerAmount = 4;

	
	public WelcomeScreen(Stage primaryStage) {
		this.primaryStage = primaryStage;
		buildGui();
	}
	
	private void startGame(Game game) {
		//Stage gameScreenStage = new Stage();
		//Scene scene = new Scene(new GameScreenController(new Game(playerNames.stream().toArray(String[]::new))));
		//gameScreenStage.setScene(scene);
		//gameScreenStage.setTitle("Coloretto");
		//gameScreenStage.show();		
		//primaryStage.close();
		
		GameScreen gs = new GameScreen(game);
		
		
		primaryStage.close();
	}	
	


	RadioButton rdbPlayerAmountFour = new RadioButton("4");
	RadioButton rdbPlayerAmountFive = new RadioButton("5");
	final ToggleGroup radiobuttons = new ToggleGroup();
	Label lblPlayerName = new Label("Name Player 1:");
	TextField txfPlayerName = new TextField();
	Button btnResumeGame = new Button("Resume saved game");
	Button btnHighscores = new Button("Highscores");
	Button btnNext = new Button("Next");		
	ColumnConstraints col0 = new ColumnConstraints(100);
	ColumnConstraints col1= new ColumnConstraints(34);
	ColumnConstraints col2 = new ColumnConstraints(166);


	private void buildGui() {
		
		rdbPlayerAmountFour.setSelected(true);
		setHalignment(btnNext, HPos.RIGHT);
		setHalignment(btnHighscores, HPos.RIGHT);
		btnResumeGame.setOnAction(this::btnResumeGameOnAction);
		btnNext.setDisable(true);
		btnNext.setOnAction(this::btnNextOnAction);
		txfPlayerName.setOnKeyReleased(this::txfPlayerNameOnKeyPressed);
		rdbPlayerAmountFour.setOnAction(this::rdbPlayerAmountOnAction);
		rdbPlayerAmountFive.setOnAction(this::rdbPlayerAmountOnAction);
		btnHighscores.setOnAction(this::btnHighscoresOnAction);
		rdbPlayerAmountFour.setToggleGroup(radiobuttons);
		rdbPlayerAmountFive.setToggleGroup(radiobuttons);
		btnResumeGame.setDisable(!Mapper.savedGameExists());
		
		this.getColumnConstraints().addAll(col0, col1, col2);
		this.setPadding(new Insets(25,25,25,25));
		Scene scene = new Scene(this, 375, 170);
		this.setHgap(10);
		this.setVgap(10);
		this.add(new Label("Welcome to Coloretto!"), 0, 0, 2,1);
		this.add(new Label("Player amount"), 0, 1);
		this.add(rdbPlayerAmountFour, 1, 1);
		this.add(rdbPlayerAmountFive, 2, 1);
		this.add(lblPlayerName, 0, 2);
		this.add(txfPlayerName, 1, 2, 2, 1);
		this.add(btnResumeGame, 0, 3,2 ,1);
		this.add(btnHighscores, 2, 0);
		this.add(btnNext, 2, 3);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Coloretto");
		primaryStage.show();
		
		primaryStage.setResizable(false);
	}
	
	//eventHandlers
	private void btnResumeGameOnAction(ActionEvent event) {
		startGame(Mapper.getSavedGame());
	}
	
	private void btnHighscoresOnAction(ActionEvent event) {
		System.out.println("ok");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Highscores");
		alert.setHeaderText(null);
		alert.setContentText(Mapper.getHighScoresFromDatabase());
		alert.showAndWait();
		System.exit(0);
		}
	
	private void btnNextOnAction(ActionEvent event) {
		playerNames.add(txfPlayerName.getText());
		lblPlayerName.setText(String.format("Name Player %d:", playerNames.size() + 1));
		txfPlayerName.clear();
		btnHighscores.setDisable(true);
		//btnNext.setDisable(true);
		
		if (playerNames.size() == playerAmount) {
			startGame(new Game(playerNames.stream().toArray(String[]::new)));
		}
			
		rdbPlayerAmountFour.setDisable(true);
		rdbPlayerAmountFive.setDisable(true);
	}
	
	private void txfPlayerNameOnKeyPressed(KeyEvent event) {
		btnNext.setDisable(txfPlayerName.getText().length() == 0);
		
	}
	private void rdbPlayerAmountOnAction(ActionEvent event) {
		if (rdbPlayerAmountFive.isSelected()) {
			playerAmount =5;
		} else {
			playerAmount = 4;
		}
		
	}
	
}
