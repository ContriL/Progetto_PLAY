package application.core;

import application.exercises.Exercise;
import application.screens.auth.Main;
import application.screens.exercises.ExerciseScreen;
import application.screens.exercises.ScreenCompleteCode;
import application.screens.exercises.ScreenCompareCode;

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
 * 
 */
public class NavigationManager {
    private static NavigationManager instance;
    private Stage primaryStage;
    private Map<String, Scene> cachedScenes = new HashMap<>();

    private double currentWidth = 1200;
    private double currentHeight = 800;
    private boolean isMaximized = false;

    private NavigationManager() {}

    public static NavigationManager getInstance() {
        if (instance == null) {
            instance = new NavigationManager();
        }
        return instance;
    }

    public void initialize(Stage stage) {
        this.primaryStage = stage;

        // Mantieni le dimensioni quando cambiano
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (!stage.isMaximized()) {
                currentWidth = newVal.doubleValue();
            }
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (!stage.isMaximized()) {
                currentHeight = newVal.doubleValue();
            }
        });

        // Salva stato massimizzato
        stage.maximizedProperty().addListener((obs, oldVal, newVal) -> {
            isMaximized = newVal;
        });
    }

    /**
     * Forza il mantenimento delle dimensioni correnti
     */
    private void maintainCurrentSize() {
        if (primaryStage != null) {
            if (isMaximized) {
                primaryStage.setMaximized(true);
            } else {
                primaryStage.setMaximized(false);
                primaryStage.setWidth(currentWidth);
                primaryStage.setHeight(currentHeight);
            }
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    

    public void goToHome() {
        Scene homeScene = Home.getScene(primaryStage, getLoginScene());
        primaryStage.setScene(homeScene);
        maintainCurrentSize();

    }

    public void goToLogin() {
        Scene loginScene = getLoginScene();
        primaryStage.setScene(loginScene);
        maintainCurrentSize();
    }

    public void goToExerciseGrid() {
        Scene gridScene = ExerciseGridScreen.createScene(primaryStage);
        primaryStage.setScene(gridScene);
        maintainCurrentSize();
    }

    public void goToUserProgress() {
        Scene currentScene = primaryStage.getScene();
        Scene progressScene = UserProgressScreen.getScene(primaryStage, currentScene);
        primaryStage.setScene(progressScene);
        maintainCurrentSize();
    }

    public void goToProfile() {
        // Crea lo screen passando stage nel costruttore
        ProfileScreen profileScreen = new ProfileScreen(primaryStage);
        // Chiama createScene() senza parametri
        Scene profileScene = profileScreen.createScene();
        primaryStage.setScene(profileScene);
        maintainCurrentSize();
    }



    public void showExerciseRules(Exercise exercise, String returnTo) {
        Scene rulesScene = application.screens.exercises.ExerciseRulesScreen.createScene(
                primaryStage, exercise, returnTo);
        primaryStage.setScene(rulesScene);
        maintainCurrentSize();
    }

    
    public void startExercise(Exercise exercise, String returnTo) {
        Scene exerciseScene = createExerciseScene(exercise);
        primaryStage.setScene(exerciseScene);
        maintainCurrentSize();
    }

    public void logout() {
        Main.setCurrentUser("");
        goToLogin();
    }

    

    private Scene getLoginScene() {
        String key = "login";
        if (!cachedScenes.containsKey(key)) {
            cachedScenes.put(key, Main.getLoginScene(primaryStage));
        }
        return cachedScenes.get(key);
    }


    private Scene createExerciseScene(Exercise exercise) {
        if (exercise instanceof application.exercises.QuizEP) {
            return ScreenQuizEP.getScene(primaryStage, getGridScene(), exercise);
        } else if (exercise instanceof application.exercises.CompleteCode) {
            return ScreenCompleteCode.getScene(primaryStage, getGridScene(), exercise);
        } else if (exercise instanceof application.exercises.CompareCode) {
            return ScreenCompareCode.getScene(primaryStage, getGridScene(), exercise);
        } else if (exercise instanceof application.exercises.FindErrorExercise) {
            return ExerciseScreen.getScene(primaryStage, getGridScene(), exercise);
        } else if (exercise instanceof application.exercises.OrderStepsExercise) {
            return ExerciseScreen.getScene(primaryStage, getGridScene(), exercise);
        } else if (exercise instanceof application.exercises.WhatPrintsExercise) {
            return ExerciseScreen.getScene(primaryStage, getGridScene(), exercise);
        }

        throw new IllegalArgumentException("Tipo di esercizio non supportato: " + exercise.getClass().getSimpleName());
    }

    
    private Scene getGridScene() {
        return ExerciseGridScreen.createScene(primaryStage);
    }

    
    public void clearCache() {
        cachedScenes.clear();
    }

    public void cacheScene(String key, Scene scene) {
        cachedScenes.put(key, scene);
    }
}