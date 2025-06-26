package application.core;

import application.exercises.Exercise;
import application.screens.auth.Main;
import application.screens.exercises.ExerciseScreen;
import application.screens.exercises.ScreenCompleteCode;
import application.screens.exercises.ScreenQuizEP;
import application.screens.home.ExerciseGridScreen;
import application.screens.home.Home;
import application.screens.user.ProfileScreen;
import application.screens.user.UserProgressScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestisce la navigazione tra le diverse schermate dell'applicazione.
 * Implementa il pattern Singleton per garantire un'unica istanza.
 * PULITO - Conforme alle specifiche: nessuna selezione manuale difficoltà.
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

    // === METODI DI NAVIGAZIONE PRINCIPALI ===

    public void goToHome() {
        Scene homeScene = Home.getScene(primaryStage, getLoginScene());
        primaryStage.setScene(homeScene);
    }

    public void goToLogin() {
        Scene loginScene = getLoginScene();
        primaryStage.setScene(loginScene);
    }

    public void goToExerciseGrid() {
        Scene gridScene = ExerciseGridScreen.createScene(primaryStage);
        primaryStage.setScene(gridScene);
    }

    public void goToUserProgress() {
        Scene currentScene = primaryStage.getScene();
        Scene progressScene = UserProgressScreen.getScene(primaryStage, currentScene);
        primaryStage.setScene(progressScene);
    }

    public void goToProfile() {
        Scene profileScene = ProfileScreen.createScene(primaryStage);
        primaryStage.setScene(profileScene);
    }

    /**
     * Mostra le regole di un esercizio con livello AUTOMATICO
     * (conforme alle specifiche - nessuna scelta manuale difficoltà)
     */
    public void showExerciseRules(Exercise exercise, String returnTo) {
        Scene rulesScene = application.screens.exercises.ExerciseRulesScreen.createScene(
                primaryStage, exercise, returnTo);
        primaryStage.setScene(rulesScene);
    }

    /**
     * Avvia un esercizio del livello AUTOMATICO determinato dal sistema
     */
    public void startExercise(Exercise exercise, String returnTo) {
        Scene exerciseScene = createExerciseScene(exercise);
        primaryStage.setScene(exerciseScene);
    }

    public void logout() {
        Main.setCurrentUser("");
        goToLogin();
    }

    // === METODI DI SUPPORTO ===

    private Scene getLoginScene() {
        String key = "login";
        if (!cachedScenes.containsKey(key)) {
            cachedScenes.put(key, Main.getLoginScene(primaryStage));
        }
        return cachedScenes.get(key);
    }

    /**
     * Crea la scene per eseguire un esercizio del livello appropriato
     */
    private Scene createExerciseScene(Exercise exercise) {
        if (exercise instanceof application.exercises.QuizEP) {
            return ScreenQuizEP.getScene(primaryStage, getGridScene(), exercise);
        } else if (exercise instanceof application.exercises.CompleteCode) {
            return ScreenCompleteCode.getScene(primaryStage, getGridScene(), exercise);
        } else if (exercise instanceof application.exercises.FindErrorExercise) {
            return ExerciseScreen.getScene(primaryStage, getGridScene(), exercise);
        } else if (exercise instanceof application.exercises.OrderStepsExercise) {
            return ExerciseScreen.getScene(primaryStage, getGridScene(), exercise);
        } else if (exercise instanceof application.exercises.WhatPrintsExercise) {
            return ExerciseScreen.getScene(primaryStage, getGridScene(), exercise);
        }

        throw new IllegalArgumentException("Tipo di esercizio non supportato: " + exercise.getClass().getSimpleName());
    }

    /**
     * Helper per ottenere la scene della griglia (per return navigation)
     */
    private Scene getGridScene() {
        return ExerciseGridScreen.createScene(primaryStage);
    }

    // === METODI DI UTILITÀ PER LA CACHE ===

    public void clearCache() {
        cachedScenes.clear();
    }

    public void cacheScene(String key, Scene scene) {
        cachedScenes.put(key, scene);
    }
}