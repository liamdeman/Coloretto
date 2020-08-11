package main;

import gui.WelcomeScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class StartUp extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//Scene scene = new Scene(new WelcomeScreenController(primaryStage));
		//primaryStage.setScene(scene);
		//primaryStage.setTitle("Coloretto");
		//primaryStage.show();
	
		new WelcomeScreen(primaryStage);
		
		   
	}
	
	  
	    

	public static void main(String args[]) {
		launch(args);
	}


}
