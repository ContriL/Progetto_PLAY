package application.screens.exercises;

import application.core.BaseScreen;
import application.components.NavigationBar;
import application.core.NavigationManager;
import application.exercises.Exercise;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

// Mostra le regole e spiegazioni di un esercizio prima di iniziare.

public class ExerciseRulesScreen extends BaseScreen {

    private Exercise exercise;
    private String returnDestination;

    public ExerciseRulesScreen(Stage stage, Exercise exercise, String returnDestination) {
        super(stage, 1200, 800);
        this.exercise = exercise;
        this.returnDestination = returnDestination;

        
        updateRulesContent();
    }

    @Override
    protected NavigationBar createNavigationBar() {
        NavigationBar navbar = new NavigationBar(true);
        navbar.setBackAction(() -> {
            NavigationManager navManager = NavigationManager.getInstance();
            if ("grid".equals(returnDestination)) {
                navManager.goToExerciseGrid();
            } else {
                navManager.goToHome();
            }
        });
        return navbar;
    }

    @Override
    protected String getScreenTitle() {
        
        if (exercise == null) {
            return "Regole Esercizio";
        }
        return exercise.getTitle();
    }

    @Override
    protected String getScreenDescription() {
        return "COSA DEVI FARE";
    }

    @Override
    protected void initializeContent() {
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));

        
        if (exercise == null) {
            Label waitingLabel = new Label("Caricamento regole...");
            contentBox.getChildren().add(waitingLabel);

            
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
                if (exercise != null) {
                    
                    Platform.runLater(this::recreateContent);
                }
            }));
            timeline.setCycleCount(50); 
            timeline.play();
        } else {
            createRulesContent(contentBox);
        }

        setCenter(contentBox);
    }

    private void recreateContent() {
        VBox newContent = new VBox(20);
        newContent.setAlignment(Pos.CENTER);
        newContent.setPadding(new Insets(30));
        createRulesContent(newContent);
        setCenter(newContent);
    }

    private void createRulesContent(VBox contentBox) {
        
        Label rulesLabel = new Label(getRulesText());
        rulesLabel.setWrapText(true);
        rulesLabel.setFont(Font.font("Arial", 16));
        rulesLabel.setMaxWidth(600);
        rulesLabel.setStyle("-fx-text-alignment: justify;");

        
        VBox buttonBox = new VBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button okButton = new Button("OK - Inizia Esercizio");
        okButton.setPrefWidth(200);
        okButton.setPrefHeight(40);
        okButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        okButton.setOnAction(e -> startExercise());

        Button cancelButton = new Button("Annulla");
        cancelButton.setPrefWidth(200);
        cancelButton.setPrefHeight(40);
        cancelButton.setOnAction(e -> goBack());

        buttonBox.getChildren().addAll(okButton, cancelButton);

        contentBox.getChildren().addAll(rulesLabel, buttonBox);
        setCenter(contentBox);
    }

    private String getRulesText() {
        
        if (exercise == null) {
            return "Caricamento delle regole dell'esercizio in corso...\n\n" +
                    "Le regole verranno visualizzate a breve.";
        }
        String exerciseType = exercise.getClass().getSimpleName();

        switch (exerciseType) {
            case "QuizEP":
                return "In questo esercizio dovrai rispondere a domande sui concetti di " +
                        "ereditarietà e polimorfismo in Java.\n\n" +
                        "• Leggi attentamente ogni domanda\n" +
                        "• Scegli la risposta corretta tra le opzioni disponibili\n" +
                        "• Puoi procedere alla domanda successiva dopo aver risposto\n" +
                        "• Al termine riceverai il risultato con il punteggio ottenuto\n\n" +
                        "Questo esercizio ti aiuterà a consolidare la comprensione dei " +
                        "concetti fondamentali dell'orientazione agli oggetti in Java.";

            case "CompleteCode":
                return "In questo esercizio dovrai completare frammenti di codice Java.\n\n" +
                        "• Ti verranno mostrati dei pezzi di codice incompleti\n" +
                        "• Dovrai scrivere le righe mancanti per completare il codice\n" +
                        "• Fai attenzione alla sintassi e alla logica del programma\n" +
                        "• Al termine il tuo codice verrà valutato per correttezza\n\n" +
                        "Questo esercizio ti aiuterà a migliorare le tue capacità di " +
                        "scrittura e comprensione del codice Java.";

            case "FindErrorExercise":
                return "In questo esercizio dovrai trovare gli errori nel codice Java.\n\n" +
                        "• Ti verranno mostrati dei frammenti di codice con errori\n" +
                        "• Dovrai identificare quale tipo di errore è presente\n" +
                        "• Gli errori possono essere di sintassi, logica o performance\n" +
                        "• Spiega brevemente qual è l'errore trovato\n\n" +
                        "Questo esercizio ti aiuterà a sviluppare le capacità di " +
                        "debugging e analisi del codice.";

            case "OrderStepsExercise":
                return "In questo esercizio dovrai ordinare i passi di un algoritmo.\n\n" +
                        "• Ti verranno mostrati i passi di un algoritmo in ordine casuale\n" +
                        "• Dovrai riordinarli nella sequenza corretta di esecuzione\n" +
                        "• Usa il drag & drop per spostare i passi\n" +
                        "• Considera le dipendenze tra le varie operazioni\n\n" +
                        "Questo esercizio ti aiuterà a comprendere meglio la logica " +
                        "e la sequenza degli algoritmi.";

            case "WhatPrintsExercise":
                return "In questo esercizio dovrai prevedere l'output di un programma Java.\n\n" +
                        "• Ti verrà mostrato del codice Java completo\n" +
                        "• Dovrai scrivere esattamente cosa stamperà il programma\n" +
                        "• Fai attenzione ai dettagli: spazi, nuove righe, punteggiatura\n" +
                        "• Una riga per ogni println, rispetta l'ordine di output\n\n" +
                        "Questo esercizio ti aiuterà a migliorare la capacità di " +
                        "tracciare l'esecuzione del codice mentalmente.";

            default:
                return "In questo esercizio metterai alla prova le tue conoscenze di programmazione Java.\n\n" +
                        "• Leggi attentamente le istruzioni\n" +
                        "• Rispondi con precisione\n" +
                        "• Al termine riceverai un feedback dettagliato\n\n" +
                        "Buona fortuna!";
        }
    }

    private void startExercise() {
        NavigationManager navManager = NavigationManager.getInstance();
        navManager.startExercise(exercise, returnDestination);
    }

    private void goBack() {
        NavigationManager navManager = NavigationManager.getInstance();
        if ("grid".equals(returnDestination)) {
            navManager.goToExerciseGrid();
        } else {
            navManager.goToHome();
        }
    }

    
    public static Scene createScene(Stage stage, Exercise exercise, String returnDestination) {
        ExerciseRulesScreen screen = new ExerciseRulesScreen(stage, exercise, returnDestination);
        return screen.createScene();
    }

    @Override
    protected void configureScene(Scene scene) {
        super.configureScene(scene);
        if (stage != null) {
            stage.setTitle("PLAY - Regole " + exercise.getTitle());
        }
    }

    private void updateRulesContent() {
        if (exercise != null) {
            
            updateTitle(exercise.getTitle());

            
        }
    }

}
