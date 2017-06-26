package com.dyn.parser;


import com.dyn.parser.utils.ParserProgressMonitor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Parser extends Application {

		public static ParserProgressMonitor monitor = new ParserProgressMonitor();
		public static Stage primStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Digital Youth Network Log Parser");
		Pane myPane = FXMLLoader.load(getClass().getResource("/assets/dyn/parser/parser.fxml"));
		Scene myScene = new Scene(myPane);
		primaryStage.setScene(myScene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primStage = primaryStage;
	}

}
