package application;

import application.core.NavigationManager;
import application.core.StyleManager;
import application.exercises.CompleteCode;
import application.exercises.Exercise;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenCompleteCode {

    private static final Map<Integer, Integer> correctCountMap = new HashMap<>();

    public static Scene getScene(Stage stage, Scene anteprima, Exercise exercise) {

        BorderPane root = new BorderPane();
        Scene screenCC = new Scene(root, 700, 550);

        // Usa StyleManager invece del CSS manuale
        StyleManager.applyMainStyles(screenCC);

        // Barra superiore
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

        VBox topContainer = new VBox();
        topContainer.getChildren().add(navBar);

        Text headerText = new Text(exercise.getTitle() + " - Livello " + exercise.getDifficulty());
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Text descriptionText = new Text(exercise.getDescription());

        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15));
        headerBox.getChildren().addAll(headerText, descriptionText);

        topContainer.getChildren().add(headerBox);
        root.setTop(topContainer);

        Label resultLabel = new Label();
        Label saveMessage = new Label();
        Button submitAll = new Button("Verifica Codice");
        Button passToNextLevel = new Button("Prossimo Livello");
        passToNextLevel.setDisable(true); // inizialmente disabilitato

        VBox codeContainer = new VBox(15);
        codeContainer.setPadding(new Insets(15));

        if (exercise instanceof CompleteCode) {
            CompleteCode codeExercise = (CompleteCode) exercise;
            VBox codeUI = codeExercise.getCodeUI();

            submitAll.setOnAction(e -> {
                List<Boolean> results = codeExercise.evaluateUserInput();
                int correctCount = (int) results.stream().filter(b -> b).count();
                int totalQuestions = results.size();
                int difficulty = exercise.getDifficulty();

                if (correctCount == totalQuestions) {
                    resultLabel.setText("✔ Tutti i frammenti sono corretti!");
                    resultLabel.setTextFill(Color.GREEN);
                    passToNextLevel.setDisable(false);
                } else {
                    resultLabel.setText("✘ Alcuni frammenti contengono errori.");
                    resultLabel.setTextFill(Color.RED);
                }

                correctCountMap.put(difficulty, correctCount);

                // Salvataggio dei progressi
                String currentUser = Main.getCurrentUser();
                String type = "CompleteCode"; // Corretto per matching con UserProgress
                boolean saved = UserProgress.saveProgress(currentUser, type, difficulty, correctCount, totalQuestions);

                if (saved) {
                    saveMessage.setText("I tuoi progressi sono stati salvati correttamente.");
                    saveMessage.setTextFill(Color.GREEN);
                } else {
                    saveMessage.setText("Errore durante il salvataggio dei progressi.");
                    saveMessage.setTextFill(Color.RED);
                }
            });

            passToNextLevel.setOnAction(e -> {
                int nextLevel = exercise.getDifficulty() + 1;
                if (nextLevel <= 3) {
                    Exercise nextExercise = new CompleteCode(nextLevel);
                    Scene nextScene = ScreenCompleteCode.getScene(stage, anteprima, nextExercise);
                    stage.setScene(nextScene);
                } else {
                    saveMessage.setText("Hai completato tutti i livelli disponibili.");
                    saveMessage.setTextFill(Color.BLUE);
                }
            });

            HBox buttonRow = new HBox(15, submitAll, passToNextLevel);
            buttonRow.setAlignment(Pos.CENTER);

            codeContainer.getChildren().addAll(codeUI, buttonRow, resultLabel, saveMessage);
            root.setCenter(codeContainer);
        }

        // Logica comune di conferma e salvataggio per i pulsanti di uscita
        EventHandler<ActionEvent> confirmAndExitHandler = event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma uscita");
            alert.setHeaderText("Attenzione: stai per uscire.");
            alert.setContentText("Le risposte mancanti verranno considerate sbagliate. Vuoi salvare e continuare?");
            ButtonType yesButton = new ButtonType("Sì", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == yesButton) {
                    if (exercise instanceof CompleteCode) {
                        CompleteCode codeExercise = (CompleteCode) exercise;
                        List<Boolean> results = codeExercise.evaluateUserInput();
                        int correctCount = (int) results.stream().filter(b -> b).count();
                        int totalQuestions = results.size();
                        int difficulty = exercise.getDifficulty();

                        correctCountMap.put(difficulty, correctCount);

                        String currentUser = Main.getCurrentUser();
                        String type = "CompleteCode";
                        UserProgress.saveProgress(currentUser, type, difficulty, correctCount, totalQuestions);
                    }

                    // Usa NavigationManager invece di scene obsolete
                    NavigationManager navManager = NavigationManager.getInstance();
                    Object source = event.getSource();

                    if (source == backButton) {
                        navManager.goToExerciseGrid();
                    } else if (source == homeButton) {
                        navManager.goToHome();
                    } else if (source == logoutButton) {
                        navManager.logout();
                    }
                }
            });
        };

        // Assegna la logica ai pulsanti di uscita
        backButton.setOnAction(confirmAndExitHandler);
        homeButton.setOnAction(confirmAndExitHandler);
        logoutButton.setOnAction(confirmAndExitHandler);

        progressButton.setOnAction(e -> {
            NavigationManager.getInstance().goToUserProgress();
        });

        return screenCC;
    }

    public int getCorrectCount(int difficulty) {
        return correctCountMap.getOrDefault(difficulty, 0);
    }
}