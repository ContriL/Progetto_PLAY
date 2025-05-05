package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

public class Home extends Main {

	public static Scene getScene(Stage stage, Scene loginScene) {
		BorderPane root = new BorderPane();
		Scene home = new Scene(root, 600, 400);

		File css = new File("/Users/lorenzocontri/Desktop/Progetto_Programmazione/Progetto_PLAY/Play_Progetto/src/application/application.css");
		home.getStylesheets().add("file://" + css.getAbsolutePath());


		// Configurazione CSS
		//File css = new File("C:/Users/dadas/IdeaProjects/Progetto_PLAY/Play_Progetto/src/application/application.css");
		//home.getStylesheets().add("file://" + css.getAbsolutePath());

		// Barra di navigazione superiore
		HBox navBar = new HBox(15);
		navBar.setPadding(new Insets(10));
		navBar.setAlignment(Pos.CENTER_RIGHT);
		navBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

		// Pulsanti di navigazione
		Button homeButton = new Button("Home");
		Button progressButton = new Button("I miei Progressi");
		Button logoutButton = new Button("Logout");

		// Gestione eventi dei pulsanti di navigazione
		homeButton.setOnAction(e -> {
			// Già sulla home, non fa nulla
		});

		progressButton.setOnAction(e -> {
			Scene progressScene = UserProgressScreen.getScene(stage, home);
			stage.setScene(progressScene);
		});

		logoutButton.setOnAction(e -> {
			Main.setCurrentUser("");
			stage.setScene(loginScene);
		});

		navBar.getChildren().addAll(homeButton, progressButton, logoutButton);

		// Intestazione con barra di navigazione
		VBox topContainer = new VBox();
		topContainer.getChildren().add(navBar);
		root.setTop(topContainer);

		// Contenuto centrale
		VBox centerBox = new VBox(20);
		centerBox.setAlignment(Pos.CENTER);

		Text welcomeText = new Text("Benvenuto a PLAY!");
		welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 24));

		Text descriptionText = new Text("Play, Learn, Assess Yourself - Impara a programmare divertendoti");
		descriptionText.setFont(Font.font("Arial", 16));

		Button startButton = new Button("Inizia gli Esercizi");
		startButton.setPrefWidth(200);
		startButton.setPrefHeight(40);
		startButton.setFont(Font.font("Arial", 14));

		startButton.setOnAction(e -> {
			// Passa alla schermata di selezione esercizi
			Scene selectionScene = ExerciseSelectionScreen.getScene(stage, loginScene);
			stage.setScene(selectionScene);
		});

		centerBox.getChildren().addAll(welcomeText, descriptionText, startButton);
		root.setCenter(centerBox);

		stage.setTitle("PLAY - Home");

		return home;
	}
}