package application;

import application.exercises.Exercise;
import application.exercises.ExerciseFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

public class ExerciseSelectionScreen extends Main {

    public static Scene getScene(Stage stage, Scene loginScene) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 700, 500);

        // Configurazione CSS
        //File css = new File("C:/Users/dadas/IdeaProjects/Progetto_PLAY/Play_Progetto/src/application/application.css");
        File css = new File("/Users/lorenzocontri/Desktop/Progetto_Programmazione/Progetto_PLAY/Play_Progetto/src/application/application.css");
        scene.getStylesheets().add("file://" + css.getAbsolutePath());

        // Barra di navigazione superiore
        HBox navBar = new HBox(15);
        navBar.setId("navBar");
        navBar.setPadding(new Insets(10));
        navBar.setAlignment(Pos.CENTER_RIGHT);
        navBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        // Pulsanti di navigazione
        Button homeButton = new Button("Home");
        Button progressButton = new Button("I miei Progressi");
        Button logoutButton = new Button("Logout");

        // Gestione eventi dei pulsanti di navigazione
        homeButton.setOnAction(e -> {
            Scene homeScene = Home.getScene(stage, loginScene);
            stage.setScene(homeScene);
        });

        progressButton.setOnAction(e -> {
            Scene progressScene = UserProgressScreen.getScene(stage, scene);
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

        // Intestazione
        Text headerText = new Text("Seleziona un Esercizio");
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        HBox header = new HBox(headerText);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20, 0, 20, 0));

        topContainer.getChildren().add(header);
        root.setTop(topContainer);

        // Contenitore principale con griglia di selezione
        GridPane exerciseGrid = new GridPane();
        exerciseGrid.setHgap(15);
        exerciseGrid.setVgap(15);
        exerciseGrid.setPadding(new Insets(20));
        exerciseGrid.setAlignment(Pos.CENTER);

        // Gruppi di toggle per tipo di esercizio e livello
        ToggleGroup exerciseTypeGroup = new ToggleGroup();
        ToggleGroup difficultyGroup = new ToggleGroup();

        // Colonna per tipi di esercizi
        VBox exerciseTypeBox = new VBox(10);
        Text exerciseTypeLabel = new Text("Tipo di Esercizio");
        exerciseTypeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        exerciseTypeBox.getChildren().add(exerciseTypeLabel);

        // Pulsanti per i tipi di esercizio
        ToggleButton findErrorBtn = new ToggleButton("Trova l'errore");
        findErrorBtn.setToggleGroup(exerciseTypeGroup);
        findErrorBtn.setUserData("FindError");
        findErrorBtn.setPrefWidth(200);

        ToggleButton orderStepsBtn = new ToggleButton("Ordina i Passi");
        orderStepsBtn.setToggleGroup(exerciseTypeGroup);
        orderStepsBtn.setUserData("OrderSteps");
        orderStepsBtn.setPrefWidth(200);

        ToggleButton whatPrintsBtn = new ToggleButton("Cosa Stampa?");
        whatPrintsBtn.setToggleGroup(exerciseTypeGroup);
        whatPrintsBtn.setUserData("WhatPrints");
        whatPrintsBtn.setPrefWidth(200);

       ToggleButton EPexerciseBtn = new ToggleButton("Quiz eredità e polimorfismo");
       EPexerciseBtn.setToggleGroup(exerciseTypeGroup);
       EPexerciseBtn.setUserData("quizEP");
       EPexerciseBtn.setPrefWidth(200);

        exerciseTypeBox.getChildren().addAll(findErrorBtn, orderStepsBtn, whatPrintsBtn,EPexerciseBtn);
        exerciseGrid.add(exerciseTypeBox, 0, 0);

        // Colonna per livelli di difficoltà
        VBox difficultyBox = new VBox(10);
        Text difficultyLabel = new Text("Livello di Difficoltà");
        difficultyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        difficultyBox.getChildren().add(difficultyLabel);

        // Pulsanti per i livelli di difficoltà
        ToggleButton beginnerBtn = new ToggleButton("Principiante");
        beginnerBtn.setToggleGroup(difficultyGroup);
        beginnerBtn.setUserData(1);
        beginnerBtn.setPrefWidth(200);

        ToggleButton intermediateBtn = new ToggleButton("Intermedio");
        intermediateBtn.setToggleGroup(difficultyGroup);
        intermediateBtn.setUserData(2);
        intermediateBtn.setPrefWidth(200);

        ToggleButton advancedBtn = new ToggleButton("Avanzato");
        advancedBtn.setToggleGroup(difficultyGroup);
        advancedBtn.setUserData(3);
        advancedBtn.setPrefWidth(200);

        difficultyBox.getChildren().addAll(beginnerBtn, intermediateBtn, advancedBtn);
        exerciseGrid.add(difficultyBox, 1, 0);

        root.setCenter(exerciseGrid);

        // Area per pulsanti di azione
        HBox buttonBar = new HBox(15);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(20));

        // Label per messaggio di errore
        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");

        // Pulsanti
        Button startButton = new Button("Inizia Esercizio");

        // Azione per il pulsante Inizia
        startButton.setOnAction(e -> {
            String exerciseType = (String) exerciseTypeGroup.getSelectedToggle().getUserData();
            if (exerciseType.equals("quizEP")) {
                 Scene quiz = DiffcultyQuizSelection.getScene(stage,loginScene);
                 stage.setScene(quiz);
            } else if(exerciseTypeGroup.getSelectedToggle() == null || difficultyGroup.getSelectedToggle() == null){
                errorLabel.setText("Seleziona un tipo di esercizio e un livello di difficoltà!");
                return;
            }
            int difficulty = (int) difficultyGroup.getSelectedToggle().getUserData();

            Exercise selectedExercise = ExerciseFactory.createExercise(exerciseType, difficulty);
            Scene exerciseScene = ExerciseScreen.getScene(stage, scene, selectedExercise);
            stage.setScene(exerciseScene);
        });

        buttonBar.getChildren().addAll(startButton);

        // Area inferiore con pulsanti e messaggio di errore
        VBox bottomBox = new VBox(10);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.getChildren().addAll(errorLabel, buttonBar);
        root.setBottom(bottomBox);

        // Selezione predefinita
        findErrorBtn.setSelected(true);
        beginnerBtn.setSelected(true);

        return scene;
    }
}