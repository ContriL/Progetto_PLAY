package application.core;

import application.*;
import application.exercises.Exercise;
import application.screens.home.ExerciseGridScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestisce la navigazione tra le diverse schermate dell'applicazione.
 * Implementa il pattern Singleton per garantire un'unica istanza.
 */
public class NavigationManager {
    private static NavigationManager instance;
    private Stage primaryStage;
    private Map<String, Scene> cachedScenes = new HashMap<>();

    private NavigationManager() {}

    public static NavigationManager getInstance() {
        if (instance == null) {
            instance = new NavigationManager();
        }
        return instance;
    }

    public void initialize(Stage stage) {
        this.primaryStage = stage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    // Metodi di navigazione principali
    public void goToHome() {
        Scene homeScene = Home.getScene(primaryStage, getLoginScene());
        primaryStage.setScene(homeScene);
    }

    public void goToLogin() {
        Scene loginScene = getLoginScene();
        primaryStage.setScene(loginScene);
    }

    public void goToExerciseGrid() {
        Scene gridScene = application.screens.home.ExerciseGridScreen.createScene(primaryStage);
        primaryStage.setScene(gridScene);
    }

    public void goToExerciseSelection() {
        // Ora punta alla nuova griglia invece che alla vecchia selezione
        goToExerciseGrid();
    }

    public void goToUserProgress() {
        Scene currentScene = primaryStage.getScene();
        Scene progressScene = UserProgressScreen.getScene(primaryStage, currentScene);
        primaryStage.setScene(progressScene);
    }

    public void goToQuizDifficultySelection() {
        Scene difficultyScene = application.screens.exercises.DifficultySelectionScreen.getSceneForQuiz(
                primaryStage, getCurrentOrDefault("selection")
        );
        primaryStage.setScene(difficultyScene);
    }

    public void goToCodeDifficultySelection() {
        Scene difficultyScene = application.screens.exercises.DifficultySelectionScreen.getSceneForCode(
                primaryStage, getCurrentOrDefault("selection")
        );
        primaryStage.setScene(difficultyScene);
    }

    public void showExercisePreview(Exercise exercise, String returnTo) {
        Scene returnScene = getCurrentOrDefault(returnTo);
        Scene previewScene = createPreviewScene(exercise, returnScene);
        primaryStage.setScene(previewScene);
    }

    public void showExerciseRules(Exercise exercise, String returnTo) {
        System.out.println("🔍 NavigationManager.showExerciseRules chiamato");
        System.out.println("📦 Exercise ricevuto: " + (exercise != null ? exercise.getClass().getSimpleName() : "NULL"));
        System.out.println("🔙 Return destination: " + returnTo);

        Scene rulesScene = application.screens.exercises.ExerciseRulesScreen.createScene(
                primaryStage, exercise, returnTo);

        System.out.println("🎬 Scene creata: " + (rulesScene != null ? "OK" : "NULL"));
        primaryStage.setScene(rulesScene);
    }

    public void startExercise(Exercise exercise, String returnTo) {
        Scene returnScene = getCurrentOrDefault(returnTo);
        Scene exerciseScene = createExerciseScene(exercise, returnScene);
        primaryStage.setScene(exerciseScene);
    }

    public void logout() {
        Main.setCurrentUser("");
        goToLogin();
    }

    // Metodi di supporto
    private Scene getLoginScene() {
        String key = "login";
        if (!cachedScenes.containsKey(key)) {
            cachedScenes.put(key, Main.getLoginScene(primaryStage));
        }
        return cachedScenes.get(key);
    }

    // NUOVO (con ExerciseGridScreen):
    private Scene getCurrentOrDefault(String defaultScene) {
        Scene current = primaryStage.getScene();
        if (current != null) {
            return current;
        }

        // Fallback per scene di default
        switch (defaultScene) {
            case "home": return Home.getScene(primaryStage, getLoginScene());
            case "selection": return ExerciseGridScreen.createScene(primaryStage); // ✅ NUOVO
            case "login": return getLoginScene();
            default: return getLoginScene();
        }
    }

    private Scene createPreviewScene(Exercise exercise, Scene returnScene) {
        // Determina il tipo di ritorno basandosi sul contesto
        String returnDestination = determineReturnDestination(returnScene);

        // Usa il nuovo componente unificato
        return application.screens.exercises.ExercisePreviewScreen.createScene(
                primaryStage, exercise, returnDestination);
    }

    private String determineReturnDestination(Scene returnScene) {
        // Logica per determinare dove tornare basandosi sulla scene corrente
        // Per ora usiamo un default, ma si può migliorare
        if (returnScene != null) {
            // Potresti analizzare la scene per capire da dove arrivi
            // Per ora usiamo "selection" come default sicuro
            return "selection";
        }
        return "selection";
    }

    private Scene createExerciseScene(Exercise exercise, Scene returnScene) {
        System.out.println("🏗️ Creando scene per: " + exercise.getClass().getSimpleName()); // DEBUG

        if (exercise instanceof application.exercises.QuizEP) {
            return ScreenQuizEP.getScene(primaryStage, returnScene, exercise);
        } else if (exercise instanceof application.exercises.CompleteCode) {
            return ScreenCompleteCode.getScene(primaryStage, returnScene, exercise);
        } else if (exercise instanceof application.exercises.FindErrorExercise) {
            return ExerciseScreen.getScene(primaryStage, returnScene, exercise);
        } else if (exercise instanceof application.exercises.OrderStepsExercise) {
            return ExerciseScreen.getScene(primaryStage, returnScene, exercise);
        } else if (exercise instanceof application.exercises.WhatPrintsExercise) {
            return ExerciseScreen.getScene(primaryStage, returnScene, exercise);
        }

        throw new IllegalArgumentException("Tipo di esercizio non supportato: " + exercise.getClass().getSimpleName());
    }

    // Metodi di utilità per la cache
    public void clearCache() {
        cachedScenes.clear();
    }

    public void cacheScene(String key, Scene scene) {
        cachedScenes.put(key, scene);
    }
}