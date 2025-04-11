package application;

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

import application.exercises.Exercise;
import application.exercises.ExerciseFactory;
import application.exercises.QuizEP;

public class DiffcultyQuizSelection {

    public static Scene getScene(Stage stage, Scene  selectionScene){
        
        BorderPane root = new BorderPane();
        Scene DiffquizEP = new Scene(root, 700, 550);

        // Configurazione CSS
        File style = new File("Users/lorenzocontri/Desktop/Progetto_Programmazione/Progetto_PLAY/Play_Progetto/src/application/style.css");
        DiffquizEP.getStylesheets().add(style.getAbsolutePath());

        // Barra di navigazione superiore
        HBox navBar = new HBox(15);
        navBar.setPadding(new Insets(10));
        navBar.setAlignment(Pos.CENTER_RIGHT);
        navBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        // Pulsanti di navigazione
        Button homeButton = new Button("Home");
        Button progressButton = new Button("I miei Progressi");
        Button logoutButton = new Button("Logout");
        Button backButton = new Button("indietro");
        navBar.getChildren().addAll(homeButton, progressButton, logoutButton, backButton);

    
        // Gestione eventi dei pulsanti di navigazione
        homeButton.setOnAction(e -> {
            Scene homeScene = Home.getScene(stage, selectionScene);
            stage.setScene(homeScene);
        });

        progressButton.setOnAction(e -> {
            Scene progressScene = UserProgressScreen.getScene(stage, selectionScene);
            stage.setScene(progressScene);
        });

        logoutButton.setOnAction(e -> {
            Main.setCurrentUser("");
            stage.setScene(selectionScene);
        });

        backButton.setOnAction(e -> {
            Scene previousScene = ExerciseSelectionScreen.getScene(stage, selectionScene);
            stage.setScene(previousScene);
        });

        VBox topContainer = new VBox();
        topContainer.getChildren().add(navBar);
        root.setTop(topContainer);

        // Intestazione
        Text headerText = new Text("Seleziona un livello di difficoltà");
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        HBox header = new HBox(headerText);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20, 0, 20, 0));


        // Contenitore principale con griglia di selezione
        GridPane exerciseGrid = new GridPane();
        exerciseGrid.setHgap(15);
        exerciseGrid.setVgap(15);
        exerciseGrid.setPadding(new Insets(20));
        exerciseGrid.setAlignment(Pos.CENTER);

        ToggleGroup difficultyGroup = new ToggleGroup();

        // Colonna per livelli di difficoltà
        VBox difficultyBox = new VBox(10);
        Text difficultyLabel = new Text("Opzioni:");
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
        exerciseGrid.add(difficultyBox, 0, 0);

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
             if (difficultyGroup.getSelectedToggle() == null) {
                 errorLabel.setText("Seleziona un tipo di esercizio e un livello di difficoltà!");
                 return;
             }
             int difficulty = (int) difficultyGroup.getSelectedToggle().getUserData();
             Exercise selectedExercise = new QuizEP(difficulty);
             Scene exerciseScene = ScreenQuizEP.getScene(stage, DiffquizEP, selectedExercise);
             stage.setScene(exerciseScene);
         });
 
         buttonBar.getChildren().addAll(startButton);

        

         // Area inferiore con pulsanti e messaggio di errore
         VBox bottomBox = new VBox(10);
         bottomBox.setAlignment(Pos.CENTER);
         bottomBox.getChildren().addAll(errorLabel, buttonBar);
         root.setBottom(bottomBox);
 


        
        
        
        return DiffquizEP;
        
    }
    
}
