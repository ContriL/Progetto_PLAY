package application.screens.home;

import application.screens.auth.Main;
import application.UserProgress;
import application.core.BaseScreen;
import application.core.NavigationManager;
import application.components.NavigationBar;
import application.exercises.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Schermata griglia esercizi con barra di avanzamento e stile pulito.
 */
public class ExerciseGridScreen extends BaseScreen {

    public ExerciseGridScreen(Stage stage) {
        super(stage, 1200, 800);
    }

    @Override
    protected NavigationBar createNavigationBar() {
        return NavigationBar.forMainScreens();
    }

    @Override
    protected String getScreenTitle() {
        return "CIAO " + Main.getCurrentUser();
    }

    @Override
    protected String getScreenDescription() {
        return "Scegli un esercizio per iniziare ad imparare!";
    }

    @Override
    protected void initializeContent() {
        VBox contentBox = new VBox(30);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(40));
        contentBox.getStyleClass().add("content-container");

        GridPane exerciseGrid = createExerciseGrid();
        contentBox.getChildren().add(exerciseGrid);
        setCenter(contentBox);
    }

    // Crea la griglia degli esercizi
    private GridPane createExerciseGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER);

        String user = Main.getCurrentUser();

        grid.add(createExerciseCard("🎯", "Trova l'errore", "FindError", user), 0, 0);
        grid.add(createExerciseCard("🔄", "Ordina i passi", "OrderSteps", user), 1, 0);
        grid.add(createExerciseCard("👁️", "Cosa stampa?", "WhatPrints", user), 0, 1);
        grid.add(createExerciseCard("📝", "Quiz EP", "quizEP", user), 1, 1);
        grid.add(createExerciseCard("💻", "Completa il Codice", "CompleteCode", user), 0, 2);
        grid.add(createExerciseCard("🤔", "Confronta il codice", "CompareCode", user), 1, 2);

        return grid;
    }

    // Crea una singola card esercizio
    private VBox createExerciseCard(String icon, String title, String type, String user) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(280, 180);
        card.getStyleClass().add("exercise-card");

        String base = "#a385cf", hover = "#d1b3ff";
        card.setStyle("-fx-background-color: " + base + "; -fx-background-radius: 15px; -fx-padding: 20px;");

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(32));
        iconLabel.setStyle("-fx-text-fill: #475569;");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");

        Label levelLabel = new Label(getLevelLabel(user, type));
        levelLabel.getStyleClass().add("card-subtitle");

        HBox progressBar = createProgressBar(user, type);

        card.getChildren().addAll(iconLabel, titleLabel, levelLabel, progressBar);

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: " + hover + "; -fx-background-radius: 15px; -fx-padding: 20px;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: " + base + "; -fx-background-radius: 15px; -fx-padding: 20px;"));
        card.setOnMouseClicked(e -> openExercise(type, user));

        return card;
    }

    // Crea barra di avanzamento livelli (3 livelli)
    private HBox createProgressBar(String user, String type) {
        HBox bar = new HBox(4);
        bar.setAlignment(Pos.CENTER);
        bar.setPrefWidth(180);

        int level = getMaxLevel(user, type);

        for (int i = 1; i <= 3; i++) {
            Region segment = new Region();
            segment.setPrefSize(50, 6);

            if (i <= level) {
                switch (i) {
                    case 1 -> segment.setStyle("-fx-background-color: #fbbf24;");
                    case 2 -> segment.setStyle("-fx-background-color: #3b82f6;");
                    case 3 -> segment.setStyle("-fx-background-color: #10b981;");
                }
            } else {
                segment.setStyle("-fx-background-color: #e2e8f0;");
            }

            bar.getChildren().add(segment);
        }

        return bar;
    }

    // Testo del livello raggiunto
    private String getLevelLabel(String user, String type) {
        return switch (getMaxLevel(user, type)) {
            case 1 -> "(Livello Principiante)";
            case 2 -> "(Livello Intermedio)";
            case 3 -> "(Livello Avanzato)";
            default -> "(Nessun livello completato)";
        };
    }

    // Calcola il livello massimo superato
    private int getMaxLevel(String user, String type) {
        for (int i = 3; i >= 1; i--) {
            if (UserProgress.hasPassedLevel(user, type, i)) return i;
        }
        return 0;
    }

    // Apre l'esercizio corretto in base al livello
    private void openExercise(String type, String user) {
        int nextLevel = getNextLevel(user, type);
        Exercise ex = createExercise(type, nextLevel);
        if (ex != null)
            NavigationManager.getInstance().showExerciseRules(ex, "grid");
    }

    // Trova il prossimo livello non completato
    private int getNextLevel(String user, String type) {
        for (int i = 1; i <= 3; i++) {
            if (!UserProgress.hasPassedLevel(user, type, i)) return i;
        }
        return 3;
    }

    // Crea istanza dell’esercizio corretto
    private Exercise createExercise(String type, int level) {
        return switch (type) {
            case "FindError", "OrderSteps", "WhatPrints", "CompareCode" ->
                    ExerciseFactory.createExercise(type, level);
            case "quizEP" -> new QuizEP(level);
            case "CompleteCode" -> new CompleteCode(level);
            default -> null;
        };
    }

    // Metodo statico per compatibilità con NavigationManager
    public static Scene createScene(Stage stage) {
        return new ExerciseGridScreen(stage).createScene();
    }

    @Override
    protected void configureScene(Scene scene) {
        super.configureScene(scene);
        if (stage != null) stage.setTitle("PLAY - Scegli un Esercizio");
    }
}