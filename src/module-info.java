module ColorettoIteratie3GUI {
	exports gui;
	exports main;
	requires javafx.base;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.controls;
	requires java.sql;
	
	opens gui to javafx.graphics,javafx.fxml;
	opens main to javafx.graphics,javafx.fxml;
}