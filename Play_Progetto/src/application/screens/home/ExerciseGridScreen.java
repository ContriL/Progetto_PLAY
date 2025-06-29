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
 * Schermata griglia esercizi
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

      
        // Griglia degli esercizi
        GridPane exerciseGrid = createCleanExerciseGrid();

        
        contentBox.getChildren().add(exerciseGrid); 

        setCenter(contentBox);
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

   
    private VBox createCleanExerciseCard(String icon, String title, String exerciseType, String username) {
        VBox card = new VBox(15);
        card.getStyleClass().add("exercise-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(280);
        card.setPrefHeight(180);

        
        String baseColor = "#a385cf";
        String hoverColor = "#d1b3ff";

        card.setStyle("-fx-background-color: " + baseColor + "; -fx-background-radius: 15px; -fx-padding: 20px;");

        // Icona
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(32));
        iconLabel.setStyle("-fx-text-fill: #475569;");

        // Titolo
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");

        // Livello raggiunto
        String levelText = getLevelText(username, exerciseType);
        Label levelLabel = new Label(levelText);
        levelLabel.getStyleClass().add("card-subtitle");

        // Barra di progresso
        HBox progressBar = createVisibleProgressBar(username, exerciseType);

        card.getChildren().addAll(iconLabel, titleLabel, levelLabel, progressBar);

        // Effetto hover corretto
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: " + hoverColor + "; -fx-background-radius: 15px; -fx-padding: 20px;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: " + baseColor + "; -fx-background-radius: 15px; -fx-padding: 20px;"));

        // Click
        card.setOnMouseClicked(e -> goToExerciseRules(exerciseType, username));

        return card;
    }

    /**
     * Crea una barra di progresso 
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
            segment.setPrefHeight(6);

            if (i <= maxLevel) {
                // Livello completato - colore in base al livello
                segment.getStyleClass().add("level-" + i);
                switch (i) {
                    case 1:
                        segment.setStyle("-fx-background-color: #fbbf24;"); 
                        break;
                    case 2:
                        segment.setStyle("-fx-background-color: #3b82f6;");
                        break;
                    case 3:
                        segment.setStyle("-fx-background-color: #10b981;"); 
                        break;
                }
            } else {
                
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
        for (int level = 3; level >= 1; level--) {
            if (UserProgress.hasPassedLevel(username, exerciseType, level)) {
                return level;
            }
        }
        return 0; 
    }

    private void goToExerciseRules(String exerciseType, String username) {
        System.out.println("🎯 Cliccato su esercizio: " + exerciseType);

        
        int nextLevel = getNextLevel(username, exerciseType);

        
        Exercise exercise = createExercise(exerciseType, nextLevel);

        if (exercise != null) {
            NavigationManager navManager = NavigationManager.getInstance();
            navManager.showExerciseRules(exercise, "grid");
        } else {
            System.err.println("❌ Errore: impossibile creare esercizio " + exerciseType + " livello " + nextLevel);
        }
    }

    private int getNextLevel(String username, String exerciseType) {
        
        for (int level = 1; level <= 3; level++) {
            if (!UserProgress.hasPassedLevel(username, exerciseType, level)) {
                return level; 
            }
        }
        return 3;
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