package application.core;

import application.exercises.Exercise;
import application.screens.auth.Main;
import application.UserProgress;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 * Utilità per la gestione dei dialog di conferma nell'applicazione PLAY.
 * Implementa le specifiche del progetto per la gestione dell'uscita dagli esercizi.
 */
public class DialogUtils {

    /**
     * Mostra dialog di conferma per l'uscita dall'esercizio.
     * Gestisce il salvataggio dei progressi secondo le specifiche del progetto.
     */
    public static void showExerciseExitConfirmation(Exercise exercise,
                                                    boolean isExerciseCompleted,
                                                    int currentCorrectAnswers,
                                                    Runnable onConfirmedExit) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Uscita");
        alert.setHeaderText("⚠️ Attenzione: stai per uscire dall'esercizio");

        // Messaggio diverso in base allo stato dell'esercizio
        String message;
        if (isExerciseCompleted) {
            message = "L'esercizio è completato. Il tuo risultato verrà salvato.\n\n" +
                    "Vuoi uscire dall'esercizio?";
        } else {
            message = "L'esercizio non è ancora completato.\n" +
                    "Secondo le regole, l'uscita anticipata sarà considerata come un fallimento.\n\n" +
                    "Vuoi continuare e uscire dall'esercizio?";
        }
        alert.setContentText(message);

        // Bottoni personalizzati
        ButtonType confirmButton = new ButtonType("✅ Sì, esci", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("❌ No, continua esercizio", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        // Applica stili
        try {
            alert.getDialogPane().getStylesheets().add(
                    DialogUtils.class.getResource("/application/application.css").toExternalForm()
            );
        } catch (Exception e) {
        }

        alert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                // Salva secondo le specifiche del Prof
                saveExerciseResultAccordingToSpecs(exercise, isExerciseCompleted, currentCorrectAnswers);
                // Esegui azione di uscita
                onConfirmedExit.run();
            }
            // Se cancella, non fa nulla - continua l'esercizio
        });
    }

    /**
     * Salva il risultato dell'esercizio
     * - Se completato: salva risultato reale
     * - Se non completato: salva come fallimento
     */
    private static void saveExerciseResultAccordingToSpecs(Exercise exercise,
                                                           boolean isCompleted,
                                                           int correctAnswers) {
        String currentUser = Main.getCurrentUser();
        String exerciseType = getExerciseTypeName(exercise);
        int difficulty = exercise.getDifficulty();
        int totalQuestions = exercise.getTotalQuestions();

        if (isCompleted) {
            UserProgress.saveProgress(currentUser, exerciseType, difficulty, correctAnswers, totalQuestions);
        } else {
            // Uscita anticipata: salva come fallimento (0 punti)
            UserProgress.saveProgress(currentUser, exerciseType, difficulty, 0, totalQuestions);
        }
    }


    private static String getExerciseTypeName(Exercise exercise) {
        String className = exercise.getClass().getSimpleName();

        // Mappa i nomi delle classi ai nomi usati nel sistema di progresso
        switch (className) {
            case "QuizEP":
                return "quizEP";
            case "CompleteCode":
                return "CompleteCode";
            case "CompareCode":
                return "CompareCode";
            case "FindErrorExercise":
                return "FindError";
            case "OrderStepsExercise":
                return "OrderSteps";
            case "WhatPrintsExercise":
                return "WhatPrints";
            default:
                return className;
        }
    }

    // Dialog semplice per messaggi generici

    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Dialog per errori

    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}