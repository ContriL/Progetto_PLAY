package application.screens.home;

import application.Main;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Schermata griglia esercizi pulita e funzionale.
 * Mantiene le barre di progresso visibili e un design sobrio.
 */
public class ExerciseGridScreen extends BaseScreen {

    public ExerciseGridScreen(Stage stage) {
        super(stage, 800, 600);
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

        // Header semplice
        VBox headerSection = createSimpleHeader();

        // Griglia degli esercizi
        GridPane exerciseGrid = createCleanExerciseGrid();

        contentBox.getChildren().addAll(headerSection, exerciseGrid);
        setCenter(contentBox);
    }

    /**
     * Crea un header semplice e pulito
     */
    private VBox createSimpleHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);

        // Titolo
        Label titleLabel = new Label("CIAO " + Main.getCurrentUser());
        titleLabel.getStyleClass().add("main-title");

        // Sottotitolo
        Label subtitleLabel = new Label("Scegli un esercizio per iniziare ad imparare!");
        subtitleLabel.getStyleClass().add("section-title");
        subtitleLabel.setStyle("-fx-text-fill: #64748b;");

        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }

    /**
     * Crea la griglia pulita degli esercizi
     */
    private GridPane createCleanExerciseGrid() {
        GridPane exerciseGrid = new GridPane();
        exerciseGrid.setHgap(25);
        exerciseGrid.setVgap(25);
        exerciseGrid.setAlignment(Pos.CENTER);

        String username = Main.getCurrentUser();

        // Riga 1
        exerciseGrid.add(createCleanExerciseCard("🎯", "Trova l'errore", "FindError", username), 0, 0);
        exerciseGrid.add(createCleanExerciseCard("🔄", "Ordina i passi", "OrderSteps", username), 1, 0);

        // Riga 2
        exerciseGrid.add(createCleanExerciseCard("👁️", "Cosa stampa?", "WhatPrints", username), 0, 1);
        exerciseGrid.add(createCleanExerciseCard("📝", "Quiz EP", "quizEP", username), 1, 1);

        // Riga 3
        exerciseGrid.add(createCleanExerciseCard("💻", "Completa il Codice", "CompleteCode", username), 0, 2);

        return exerciseGrid;
    }

    /**
     * Crea una card pulita per un esercizio
     */
    private VBox createCleanExerciseCard(String icon, String title, String exerciseType, String username) {
        VBox card = new VBox(15);
        card.getStyleClass().add("exercise-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(280);
        card.setPrefHeight(180);

        // Icona (size normale)
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(32)); // Più piccolo di prima
        iconLabel.setStyle("-fx-text-fill: #475569;");

        // Titolo
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");

        // Livello raggiunto (più chiaro)
        String levelText = getLevelText(username, exerciseType);
        Label levelLabel = new Label(levelText);
        levelLabel.getStyleClass().add("card-subtitle");

        // Barra di progresso VISIBILE
        HBox progressBar = createVisibleProgressBar(username, exerciseType);

        card.getChildren().addAll(iconLabel, titleLabel, levelLabel, progressBar);

        // Hover effect semplice
        card.setOnMouseEntered(e -> {
            card.setStyle(card.getStyle() + "-fx-background-color: #f8fafc;");
        });

        card.setOnMouseExited(e -> {
            card.setStyle(card.getStyle().replace("-fx-background-color: #f8fafc;", ""));
        });

        // Click handler
        card.setOnMouseClicked(e -> goToExerciseRules(exerciseType, username));

        return card;
    }

    /**
     * Crea una barra di progresso CHIARAMENTE VISIBILE
     */
    private HBox createVisibleProgressBar(String username, String exerciseType) {
        HBox progressBar = new HBox(4);
        progressBar.getStyleClass().add("progress-bar-container");
        progressBar.setAlignment(Pos.CENTER);
        progressBar.setPrefWidth(180);

        int maxLevel = getMaxLevelReached(username, exerciseType);

        // 3 segmenti per i 3 livelli
        for (int i = 1; i <= 3; i++) {
            Region segment = new Region();
            segment.getStyleClass().add("progress-segment");
            segment.setPrefWidth(50);
            segment.setPrefHeight(6); // Più spesso per essere visibile

            if (i <= maxLevel) {
                // Livello completato - colore in base al livello
                segment.getStyleClass().add("level-" + i);
                switch (i) {
                    case 1:
                        segment.setStyle("-fx-background-color: #fbbf24;"); // Giallo
                        break;
                    case 2:
                        segment.setStyle("-fx-background-color: #3b82f6;"); // Blu
                        break;
                    case 3:
                        segment.setStyle("-fx-background-color: #10b981;"); // Verde
                        break;
                }
            } else {
                // Livello non completato - grigio chiaro
                segment.getStyleClass().add("level-0");
                segment.setStyle("-fx-background-color: #e2e8f0;");
            }

            progressBar.getChildren().add(segment);
        }

        return progressBar;
    }

    private String getLevelText(String username, String exerciseType) {
        int maxLevel = getMaxLevelReached(username, exerciseType);

        switch (maxLevel) {
            case 0: return "(Nessun livello completato)";
            case 1: return "(Livello Principiante)";
            case 2: return "(Livello Intermedio)";
            case 3: return "(Livello Avanzato)";
            default: return "(Principiante)";
        }
    }

    private int getMaxLevelReached(String username, String exerciseType) {
        // Controlla quale è il livello massimo superato
        for (int level = 3; level >= 1; level--) {
            if (UserProgress.hasPassedLevel(username, exerciseType, level)) {
                return level;
            }
        }
        return 0; // Nessun livello completato
    }

    private void goToExerciseRules(String exerciseType, String username) {
        System.out.println("🎯 Cliccato su esercizio: " + exerciseType);

        // Determina quale livello proporre
        int nextLevel = getNextLevel(username, exerciseType);

        // Crea l'esercizio del livello appropriato
        Exercise exercise = createExercise(exerciseType, nextLevel);

        if (exercise != null) {
            NavigationManager navManager = NavigationManager.getInstance();
            navManager.showExerciseRules(exercise, "grid");
        } else {
            System.err.println("❌ Errore: impossibile creare esercizio " + exerciseType + " livello " + nextLevel);
        }
    }

    private int getNextLevel(String username, String exerciseType) {
        // Trova il prossimo livello da fare
        for (int level = 1; level <= 3; level++) {
            if (!UserProgress.hasPassedLevel(username, exerciseType, level)) {
                return level; // Primo livello non completato
            }
        }
        return 3; // Se ha completato tutto, rigioca l'ultimo livello
    }

    private Exercise createExercise(String exerciseType, int level) {
        try {
            switch (exerciseType) {
                case "FindError":
                    return ExerciseFactory.createExercise("FindError", level);
                case "OrderSteps":
                    return ExerciseFactory.createExercise("OrderSteps", level);
                case "WhatPrints":
                    return ExerciseFactory.createExercise("WhatPrints", level);
                case "quizEP":
                    return new QuizEP(level);
                case "CompleteCode":
                    return new CompleteCode(level);
                default:
                    return null;
            }
        } catch (Exception e) {
            System.err.println("❌ Errore creazione esercizio: " + e.getMessage());
            return null;
        }
    }

    // Metodo statico per compatibilità
    public static Scene createScene(Stage stage) {
        ExerciseGridScreen screen = new ExerciseGridScreen(stage);
        return screen.createScene();
    }

    @Override
    protected void configureScene(Scene scene) {
        super.configureScene(scene);
        if (stage != null) {
            stage.setTitle("PLAY - Scegli un Esercizio");
        }
    }
}