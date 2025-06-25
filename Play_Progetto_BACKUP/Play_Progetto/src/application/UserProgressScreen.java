package application;

import application.core.StyleManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UserProgressScreen {

    // Classe interna per rappresentare una voce di progresso utente
    public static class ProgressEntry {
        private final String exerciseType;
        private final int difficulty;
        private final int correctAnswers;
        private final int totalQuestions;
        private final double percentage;
        private final String timestamp;

        public ProgressEntry(String exerciseType, int difficulty, int correctAnswers,
                             int totalQuestions, double percentage, String timestamp) {
            this.exerciseType = exerciseType;
            this.difficulty = difficulty;
            this.correctAnswers = correctAnswers;
            this.totalQuestions = totalQuestions;
            this.percentage = percentage;
            this.timestamp = timestamp;
        }

        // Getter (necessari per TableView)
        public String getExerciseType() {
            // Conversione dei codici interni in nomi leggibili
            switch (exerciseType) {
                case "FindError": return "Trova l'errore";
                case "OrderSteps": return "Ordina i Passi";
                case "WhatPrints": return "Cosa Stampa?";
                default: return exerciseType;
            }
        }

        public String getDifficulty() {
            switch (difficulty) {
                case 1: return "Principiante";
                case 2: return "Intermedio";
                case 3: return "Avanzato";
                default: return String.valueOf(difficulty);
            }
        }

        public int getCorrectAnswers() { return correctAnswers; }
        public int getTotalQuestions() { return totalQuestions; }

        public String getPercentage() {
            return String.format("%.1f%%", percentage);
        }

        public String getTimestamp() { return timestamp; }

        public String getStatus() {
            return percentage >= 70.0 ? "Superato" : "Da migliorare";
        }
    }

    @SuppressWarnings("unchecked")
    public static Scene getScene(Stage stage, Scene homeScene) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        StyleManager.applyMainStyles(scene);


        // Intestazione
        Text headerText = new Text("I tuoi progressi");
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Text userText = new Text("Utente: " + Main.getCurrentUser());
        userText.setFont(Font.font("Arial", FontWeight.NORMAL, 16));

        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20));
        headerBox.getChildren().addAll(headerText, userText);
        root.setTop(headerBox);

        // Carica i dati di progresso dell'utente
        List<String> progressData = UserProgress.getUserProgress(Main.getCurrentUser());
        ObservableList<ProgressEntry> progressEntries = FXCollections.observableArrayList();

        // Sommario dei livelli massimi raggiunti per tipo di esercizio
        Map<String, Integer> maxLevelByExercise = new HashMap<>();

        for (String progressLine : progressData) {
            String[] parts = progressLine.split(",");
            if (parts.length >= 7) {
                String exerciseType = parts[1];
                int difficulty = Integer.parseInt(parts[2]);
                int correctAnswers = Integer.parseInt(parts[3]);
                int totalQuestions = Integer.parseInt(parts[4]);
                double percentage = Double.parseDouble(parts[5]);
                String timestamp = parts[7];

                progressEntries.add(new ProgressEntry(
                        exerciseType, difficulty, correctAnswers, totalQuestions, percentage, timestamp
                ));

                // Aggiorna il livello massimo per questo tipo di esercizio
                maxLevelByExercise.put(exerciseType,
                        Math.max(difficulty, maxLevelByExercise.getOrDefault(exerciseType, 0)));
            }
        }

        // Crea tabella per visualizzare i progressi
        TableView<ProgressEntry> tableView = new TableView<>();
        tableView.setEditable(false);

        TableColumn<ProgressEntry, String> exerciseColumn = new TableColumn<>("Tipo Esercizio");
        exerciseColumn.setCellValueFactory(new PropertyValueFactory<>("exerciseType"));

        TableColumn<ProgressEntry, String> difficultyColumn = new TableColumn<>("Livello");
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));

        TableColumn<ProgressEntry, Integer> correctColumn = new TableColumn<>("Risposte Corrette");
        correctColumn.setCellValueFactory(new PropertyValueFactory<>("correctAnswers"));

        TableColumn<ProgressEntry, Integer> totalColumn = new TableColumn<>("Totale Domande");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalQuestions"));

        TableColumn<ProgressEntry, String> percentageColumn = new TableColumn<>("Percentuale");
        percentageColumn.setCellValueFactory(new PropertyValueFactory<>("percentage"));

        TableColumn<ProgressEntry, String> statusColumn = new TableColumn<>("Stato");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<ProgressEntry, String> timestampColumn = new TableColumn<>("Data e Ora");
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        tableView.getColumns().addAll(
                exerciseColumn, difficultyColumn, correctColumn, totalColumn,
                percentageColumn, statusColumn, timestampColumn
        );

        tableView.setItems(progressEntries);

        // Crea un riepilogo dei livelli raggiunti
        VBox summaryBox = new VBox(10);
        summaryBox.setPadding(new Insets(20));

        Text summaryTitle = new Text("Riepilogo livelli raggiunti:");
        summaryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        summaryBox.getChildren().add(summaryTitle);

        // Se non ci sono dati, mostra un messaggio
        if (maxLevelByExercise.isEmpty()) {
            Text noDataText = new Text("Non hai ancora completato nessun esercizio.");
            noDataText.setFill(Color.GRAY);
            summaryBox.getChildren().add(noDataText);
        } else {
            // Mostra i livelli massimi raggiunti per ogni tipo di esercizio
            for (Map.Entry<String, Integer> entry : maxLevelByExercise.entrySet()) {
                String exerciseType = entry.getKey();
                int maxLevel = entry.getValue();

                String exerciseName;
                switch (exerciseType) {
                    case "FindError": exerciseName = "Trova l'errore"; break;
                    case "OrderSteps": exerciseName = "Ordina i Passi"; break;
                    case "WhatPrints": exerciseName = "Cosa Stampa?"; break;
                    default: exerciseName = exerciseType;
                }

                String levelName;
                switch (maxLevel) {
                    case 1: levelName = "Principiante"; break;
                    case 2: levelName = "Intermedio"; break;
                    case 3: levelName = "Avanzato"; break;
                    default: levelName = "Livello " + maxLevel;
                }

                Text exerciseText = new Text(exerciseName + ": " + levelName);
                boolean passed = UserProgress.hasPassedLevel(Main.getCurrentUser(), exerciseType, maxLevel);
                exerciseText.setFill(passed ? Color.GREEN : Color.ORANGE);

                summaryBox.getChildren().add(exerciseText);
            }
        }

        // Contenitore centrale con tabella e riepilogo
        VBox centerBox = new VBox(20);
        centerBox.setPadding(new Insets(10));
        centerBox.getChildren().addAll(tableView, summaryBox);
        root.setCenter(centerBox);

        // Pulsante per tornare alla home
        Button backButton = new Button("Torna alla Home");
        backButton.setPrefWidth(150);
        backButton.setOnAction(e -> stage.setScene(Home.getScene(stage, scene)));

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20));
        buttonBox.getChildren().add(backButton);
        root.setBottom(buttonBox);

        stage.setTitle("PLAY - I tuoi progressi");

        return scene;
    }
}