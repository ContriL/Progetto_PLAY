package application;

import java.io.File;

import application.exercises.Exercise;
import application.exercises.QuizEP;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AnteprimaQuiz {

    public static Scene getScene(Stage stage, Scene selectionScene, Exercise exercise) {
        BorderPane root = new BorderPane();
        Scene anteprima = new Scene(root, 700, 550);

        // Forza la generazione dell'interfaccia per popolare le domande
        if (exercise instanceof QuizEP) {
            ((QuizEP) exercise).getExerciseUI();
        }

        // CSS
        File css = new File("/Users/lorenzocontri/Desktop/Progetto_Programmazione/Progetto_PLAY/Play_Progetto/src/application/application.css");
        anteprima.getStylesheets().add("file://" + css.getAbsolutePath());

        // Navbar
        HBox navBar = new HBox(15);
        navBar.setPadding(new Insets(10));
        navBar.setAlignment(Pos.CENTER_RIGHT);
        navBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        Button homeButton = new Button("Home");
        Button progressButton = new Button("I miei Progressi");
        Button logoutButton = new Button("Logout");
        Button backButton = new Button("Indietro");

        navBar.getChildren().addAll(homeButton, progressButton, logoutButton, backButton);

        homeButton.setOnAction(e -> stage.setScene(Home.getScene(stage, selectionScene)));
        progressButton.setOnAction(e -> stage.setScene(UserProgressScreen.getScene(stage, selectionScene)));
        logoutButton.setOnAction(e -> {
            Main.setCurrentUser("");
            stage.setScene(Home.getScene(stage, selectionScene));
        });
        backButton.setOnAction(e -> stage.setScene(ExerciseSelectionScreen.getScene(stage, selectionScene)));

        VBox topContainer = new VBox(navBar);
        root.setTop(topContainer);

        // Header
        Text headerText = new Text("Anteprima Quiz");
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        HBox header = new HBox(headerText);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20, 0, 20, 0));

        // Domande mostrate
        VBox questionsBox = new VBox(10);
        questionsBox.setPadding(new Insets(10));

        for (String question : exercise.getQuestions()) {
            Label qLabel = new Label("• " + question);
            qLabel.setWrapText(true);
            questionsBox.getChildren().add(qLabel);
        }

        ScrollPane scrollPane = new ScrollPane(questionsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(350);

        // Bottone "Inizia Esercizi"
        Button startButton = new Button("Inizia Esercizio");
        startButton.setOnAction(e -> stage.setScene(ScreenQuizEP.getScene(stage, selectionScene, exercise)));
        startButton.setPrefWidth(200);

        // Bottone per cambiare difficoltà
        Button changeDifficultyButton = new Button("Cambia Difficoltà");
        changeDifficultyButton.setOnAction(e -> stage.setScene(DiffcultyQuizSelection.getScene(stage, selectionScene)));
        changeDifficultyButton.setPrefWidth(200);

        HBox buttonBox = new HBox(20, startButton, changeDifficultyButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20));

        VBox centerBox = new VBox(header, scrollPane, buttonBox);
        centerBox.setAlignment(Pos.TOP_CENTER);
        root.setCenter(centerBox);

        return anteprima;
    }
}
