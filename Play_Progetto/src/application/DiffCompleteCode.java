package application;

import application.exercises.CompleteCode;
import application.exercises.Exercise;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DiffCompleteCode {

    public static Scene getScene(Stage stage, Scene selectionScene) {

        BorderPane root = new BorderPane();
        Scene DiffCompCode = new Scene(root, 700, 550);

        // Configurazione CSS
        try {
            String cssPath = ClassLoader.getSystemResource("application/application.css").toExternalForm();
            // Aggiungi il foglio di stile SOLO UNA VOLTA
            DiffCompCode.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Errore nel caricamento del file CSS: " + e.getMessage());
        }

        // Barra di navigazione superiore
        HBox navBar = new HBox(15);
        navBar.setId("navBar");
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
	        Scene loginScene = Main.getLoginScene(stage);
	        stage.setScene(loginScene);
        });
        backButton.setOnAction(e -> stage.setScene(ExerciseSelectionScreen.getScene(stage, selectionScene)));

        VBox topContainer = new VBox(navBar);
        root.setTop(topContainer);

        // Intestazione
        Text headerText = new Text("Seleziona un livello di difficoltà");
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        HBox header = new HBox(headerText);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20, 0, 20, 0));

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);

        // Griglia per bottoni e barre
        GridPane selectionGrid = new GridPane();
        selectionGrid.setHgap(20);
        selectionGrid.setVgap(10);
        selectionGrid.setAlignment(Pos.CENTER);

        ToggleGroup difficultyGroup = new ToggleGroup();

        ToggleButton beginnerBtn = new ToggleButton("Principiante");
        beginnerBtn.setToggleGroup(difficultyGroup);
        beginnerBtn.setUserData(1);
        beginnerBtn.setPrefSize(200, 40);

        ToggleButton intermediateBtn = new ToggleButton("Intermedio");
        intermediateBtn.setToggleGroup(difficultyGroup);
        intermediateBtn.setUserData(2);
        intermediateBtn.setPrefSize(200, 40);
        

        ToggleButton advancedBtn = new ToggleButton("Avanzato");
        advancedBtn.setToggleGroup(difficultyGroup);
        advancedBtn.setUserData(3);
        advancedBtn.setPrefSize(200, 40);
        advancedBtn.setDisable(true);

        // Recupero dei progressi per ciascun livello
        String username = Main.getCurrentUser();
        String type = "CompleteCode";
        intermediateBtn.setDisable(!UserProgress.hasPassedLevel(username, type, 1));
        advancedBtn.setDisable(!UserProgress.hasPassedLevel(username, type, 2));

        

        int beginnerCorrect = UserProgress.getCorrectAnswers(username, type, 1);
        int intermediateCorrect = UserProgress.getCorrectAnswers(username, type, 2);
        int advancedCorrect = UserProgress.getCorrectAnswers(username, type, 3);

        Region beginnerRegion = new Region();
        beginnerRegion.setPrefSize(200, 20);
        beginnerRegion.setStyle(getStyleBar(beginnerCorrect));

        Region intermediateRegion = new Region();
        intermediateRegion.setPrefSize(200, 20);
        intermediateRegion.setStyle(getStyleBar(intermediateCorrect));

        Region advancedRegion = new Region();
        advancedRegion.setPrefSize(200, 20);
        advancedRegion.setStyle(getStyleBar(advancedCorrect));

        // Aggiunta alla griglia
        selectionGrid.add(beginnerBtn, 0, 0);
        selectionGrid.add(beginnerRegion, 1, 0);

        selectionGrid.add(intermediateBtn, 0, 1);
        selectionGrid.add(intermediateRegion, 1, 1);

        selectionGrid.add(advancedBtn, 0, 2);
        selectionGrid.add(advancedRegion, 1, 2);

        contentBox.getChildren().addAll(header, selectionGrid);
        root.setCenter(contentBox);

        

        // Area inferiore: error label e start button
        HBox buttonBar = new HBox(15);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(20));

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");

        Button startButton = new Button("Vai all'anteprima");

        startButton.setOnAction(e -> {
            if (difficultyGroup.getSelectedToggle() == null) {
                errorLabel.setText("Seleziona un tipo di esercizio e un livello di difficoltà!");
                return;
            }
            int difficulty = (int) difficultyGroup.getSelectedToggle().getUserData();
            Exercise selectedExercise = new CompleteCode(difficulty);
            Scene anteprima = AnteprimaCC.getScene(stage, DiffCompCode, selectedExercise);
            stage.setScene(anteprima);
        });

        buttonBar.getChildren().addAll(startButton);

        VBox bottomBox = new VBox(10);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.getChildren().addAll(errorLabel, buttonBar);
        root.setBottom(bottomBox);

        return DiffCompCode;
    }

    // Funzione per creare lo stile della barra in base alle risposte corrette
    private static String getStyleBar(int correctAnswers) {
        if (correctAnswers == 0) {
            return "-fx-background-color: red;" +
                   "-fx-border-color: black;" +
                   "-fx-border-width: 1;" +
                   "-fx-border-radius: 10;" +
                   "-fx-background-radius: 10;";
        } else if (correctAnswers == 1) {
            return "-fx-background-color: linear-gradient(to right, green 33%, red 66%);" +
                   "-fx-border-color: black;" +
                   "-fx-border-width: 1;" +
                   "-fx-border-radius: 10;" +
                   "-fx-background-radius: 10;";
        } else if (correctAnswers == 2) {
            return "-fx-background-color: linear-gradient(to right, green 66%, red 33%);" +
                   "-fx-border-color: black;" +
                   "-fx-border-width: 1;" +
                   "-fx-border-radius: 10;" +
                   "-fx-background-radius: 10;";
        } else if (correctAnswers >= 3) {
            return "-fx-background-color: green;" +
                   "-fx-border-color: black;" +
                   "-fx-border-width: 1;" +
                   "-fx-border-radius: 10;" +
                   "-fx-background-radius: 10;";
        } else {
            return "-fx-background-color: gray;" +
                   "-fx-border-color: black;" +
                   "-fx-border-width: 1;" +
                   "-fx-border-radius: 10;" +
                   "-fx-background-radius: 10;";
        }
    }
    
}
