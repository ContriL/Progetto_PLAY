package application.components;

import application.exercises.Exercise;
import application.exercises.FindErrorExercise;
import application.exercises.OrderStepsExercise;
import application.exercises.WhatPrintsExercise;
import application.exercises.QuizEP;
import application.exercises.CompleteCode;
import application.exercises.CompareCode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Componente riusabile per la schermata finale di tutti gli esercizi
 * Creato per evitare duplicazione di codice tra i vari esercizi
 */
public class FinalResultScreen {

    /**
     * Crea la schermata finale completa con statistiche e azioni
     */
    public static ScrollPane createFinalScreen(Exercise exercise, int correctAnswers, int totalQuestions, boolean saved) {
        // Layout principale centrato perfettamente
        VBox finalScreen = new VBox(30);
        finalScreen.setAlignment(Pos.CENTER);
        finalScreen.setPadding(new Insets(50, 50, 50, 50)); // Padding uniforme
        finalScreen.getStyleClass().add("content-container");
        finalScreen.setMaxWidth(Region.USE_PREF_SIZE); // Lascia che si dimensioni automaticamente
        finalScreen.setPrefWidth(Region.USE_COMPUTED_SIZE);

        // Creo tutte le sezioni
        VBox headerSection = createHeader(exercise, correctAnswers, totalQuestions);
        HBox statsSection = createStatsCards(correctAnswers, totalQuestions);
        VBox evaluationSection = createEvaluationMessage(correctAnswers, totalQuestions);
        VBox progressSection = createProgressInfo(saved);
        HBox actionsSection = createActionButtons(exercise);

        // Aggiungo tutto con separatori
        finalScreen.getChildren().addAll(
                headerSection,
                new Separator(),
                statsSection,
                evaluationSection,
                progressSection,
                new Separator(),
                actionsSection
        );

        // Creo ScrollPane per gestire contenuti lunghi e centrare tutto
        ScrollPane scrollPane = new ScrollPane(finalScreen);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent;");

        // Wrapper per centrare orizzontalmente il contenuto
        StackPane centerWrapper = new StackPane(finalScreen);
        centerWrapper.setAlignment(Pos.CENTER);

        // Aggiorno lo ScrollPane con il wrapper centrato
        scrollPane.setContent(centerWrapper);

        return scrollPane;
    }

    /**
     * Configura le azioni dei pulsanti nella schermata finale
     */
    public static void setupActions(ScrollPane scrollPane, Exercise exercise, Stage stage, Scene returnScene) {
        // Prendo il StackPane contenuto nello ScrollPane, poi il VBox al suo interno
        StackPane centerWrapper = (StackPane) scrollPane.getContent();
        VBox finalScreen = (VBox) centerWrapper.getChildren().get(0);

        // Prendo i pulsanti dall'ultima sezione
        HBox actionsSection = (HBox) finalScreen.getChildren().get(finalScreen.getChildren().size() - 1);

        Button retryButton = (Button) actionsSection.getChildren().get(0);
        Button nextLevelButton = (Button) actionsSection.getChildren().get(1);
        Button backToGridButton = (Button) actionsSection.getChildren().get(2);

        // Collego le azioni
        retryButton.setOnAction(e -> {
            // Rifaccio lo stesso esercizio
            Exercise newExercise = createSameTypeExercise(exercise);
            if (newExercise != null) {
                restartExercise(newExercise, stage, returnScene);
            }
        });

        nextLevelButton.setOnAction(e -> {
            if (exercise.getDifficulty() < 3) {
                // Vado al livello successivo
                Exercise nextExercise = createSameTypeExercise(exercise, exercise.getDifficulty() + 1);
                if (nextExercise != null) {
                    restartExercise(nextExercise, stage, returnScene);
                }
            }
        });

        backToGridButton.setOnAction(e -> stage.setScene(returnScene));
    }

    // Header con icona e titolo
    private static VBox createHeader(Exercise exercise, int correctAnswers, int totalQuestions) {
        VBox header = new VBox(20);
        header.setAlignment(Pos.CENTER);

        // Icona basata sul risultato
        double percentage = (double) correctAnswers / totalQuestions * 100;
        String icon = percentage >= 80 ? "🏆" : percentage >= 60 ? "✅" : "💪";

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 52px;");

        Label completionTitle = new Label("Esercizio Completato!");
        completionTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 0, 3);");

        Label exerciseTitle = new Label(exercise.getTitle() + " - Livello " + exercise.getDifficulty());
        exerciseTitle.setStyle("-fx-font-size: 20px; -fx-text-fill: rgba(255,255,255,0.9); -fx-font-weight: bold;");

        header.getChildren().addAll(iconLabel, completionTitle, exerciseTitle);
        return header;
    }

    // Le quattro card delle statistiche - centrate perfettamente
    private static HBox createStatsCards(int correctAnswers, int totalQuestions) {
        HBox statsBox = new HBox(25);
        statsBox.setAlignment(Pos.CENTER);
        // Rimuovo larghezza fissa per permettere centraggio automatico

        double percentage = (double) correctAnswers / totalQuestions * 100;

        // Card con dimensioni migliori
        VBox correctCard = createStatCard("✅", "Corrette", String.valueOf(correctAnswers), "#2ecc71");
        VBox wrongCard = createStatCard("❌", "Sbagliate", String.valueOf(totalQuestions - correctAnswers), "#e74c3c");
        VBox percentageCard = createStatCard("📊", "Percentuale", String.format("%.1f%%", percentage), "#3498db");
        VBox totalCard = createStatCard("📝", "Totale", String.valueOf(totalQuestions), "#9b59b6");

        statsBox.getChildren().addAll(correctCard, wrongCard, percentageCard, totalCard);
        return statsBox;
    }

    // Singola card delle statistiche
    private static VBox createStatCard(String icon, String label, String value, String color) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(160); // Leggermente più larghe
        card.setPrefHeight(120);
        card.setStyle("-fx-background-color: rgba(255,255,255,0.15); " +
                "-fx-background-radius: 15; -fx-padding: 18; " +
                "-fx-border-color: " + color + "; -fx-border-width: 0 0 4 0; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 4);");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 28px;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: white; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 1);");

        Label labelLabel = new Label(label);
        labelLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(255,255,255,0.85); -fx-font-weight: bold;");

        card.getChildren().addAll(iconLabel, valueLabel, labelLabel);
        return card;
    }

    // Messaggio di valutazione personalizzato
    private static VBox createEvaluationMessage(int correctAnswers, int totalQuestions) {
        VBox evaluation = new VBox(12);
        evaluation.setAlignment(Pos.CENTER);
        evaluation.setPadding(new Insets(25));
        evaluation.setMaxWidth(600);
        evaluation.setStyle("-fx-background-color: rgba(255,255,255,0.12); " +
                "-fx-background-radius: 18; -fx-padding: 25;");

        double percentage = (double) correctAnswers / totalQuestions * 100;

        Label evaluationTitle;
        Label evaluationDescription;

        if (percentage >= 80) {
            evaluationTitle = new Label("🌟 Eccellente!");
            evaluationTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2ecc71;");
            evaluationDescription = new Label("Hai dimostrato una padronanza eccellente dell'argomento. Complimenti!");
        } else if (percentage >= 60) {
            evaluationTitle = new Label("👍 Molto Bene!");
            evaluationTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #3498db;");
            evaluationDescription = new Label("Hai superato l'esercizio con successo. Continua così!");
        } else {
            evaluationTitle = new Label("💪 Continua a Praticare!");
            evaluationTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f39c12;");
            evaluationDescription = new Label("Ogni errore è un'opportunità per imparare. Riprova!");
        }

        evaluationDescription.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.9); " +
                "-fx-text-alignment: center;");
        evaluationDescription.setWrapText(true);
        evaluationDescription.setMaxWidth(550);

        evaluation.getChildren().addAll(evaluationTitle, evaluationDescription);
        return evaluation;
    }

    // Info sul salvataggio progressi
    private static VBox createProgressInfo(boolean saved) {
        VBox progressInfo = new VBox(8);
        progressInfo.setAlignment(Pos.CENTER);

        String statusIcon = saved ? "💾" : "⚠️";
        String statusText = saved ? "I tuoi progressi sono stati salvati!" : "Errore nel salvataggio dei progressi";
        String statusColor = saved ? "#2ecc71" : "#e74c3c";

        Label progressLabel = new Label(statusIcon + " " + statusText);
        progressLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + statusColor + ";");

        progressInfo.getChildren().add(progressLabel);

        if (saved) {
            Label encouragementLabel = new Label("Puoi vedere i tuoi progressi nella sezione dedicata!");
            encouragementLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(255,255,255,0.8);");
            progressInfo.getChildren().add(encouragementLabel);
        }

        return progressInfo;
    }

    // I tre pulsanti di azione
    private static HBox createActionButtons(Exercise exercise) {
        HBox actions = new HBox(25);
        actions.setAlignment(Pos.CENTER);

        // Rifare stesso esercizio
        Button retryButton = new Button("🔄 Riprova Esercizio");
        retryButton.getStyleClass().add("secondary");
        retryButton.setPrefWidth(190);
        retryButton.setPrefHeight(50);

        // Livello successivo o messaggio di completamento
        Button nextLevelButton = new Button("⬆️ Livello Successivo");
        nextLevelButton.setPrefWidth(190);
        nextLevelButton.setPrefHeight(50);

        if (exercise.getDifficulty() >= 3) {
            nextLevelButton.setDisable(true);
            nextLevelButton.setText("🎊 Tutti i Livelli Completati!");
            nextLevelButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; " +
                    "-fx-font-weight: bold; -fx-opacity: 0.8;");
        }

        // Tornare alla griglia
        Button backToGridButton = new Button("🎯 Scegli Esercizio");
        backToGridButton.getStyleClass().add("secondary");
        backToGridButton.setPrefWidth(190);
        backToGridButton.setPrefHeight(50);

        actions.getChildren().addAll(retryButton, nextLevelButton, backToGridButton);
        return actions;
    }

    // Helper per creare esercizio dello stesso tipo
    private static Exercise createSameTypeExercise(Exercise exercise) {
        return createSameTypeExercise(exercise, exercise.getDifficulty());
    }

    private static Exercise createSameTypeExercise(Exercise exercise, int difficulty) {
        try {
            if (exercise instanceof FindErrorExercise) {
                return new FindErrorExercise(difficulty);
            } else if (exercise instanceof OrderStepsExercise) {
                return new OrderStepsExercise(difficulty);
            } else if (exercise instanceof WhatPrintsExercise) {
                return new WhatPrintsExercise(difficulty);
            } else if (exercise instanceof QuizEP) {
                return new QuizEP(difficulty);
            } else if (exercise instanceof CompleteCode) {
                return new CompleteCode(difficulty);
            } else if (exercise instanceof CompareCode) {
                return new CompareCode(difficulty);
            }
        } catch (Exception e) {
            System.err.println("Errore nella creazione dell'esercizio: " + e.getMessage());
        }
        return null;
    }

    // Helper per riavviare un esercizio
    private static void restartExercise(Exercise exercise, Stage stage, Scene returnScene) {
        try {
            // Uso la classe appropriata per ogni tipo di esercizio
            Scene newScene = null;

            if (exercise instanceof FindErrorExercise || exercise instanceof OrderStepsExercise || exercise instanceof WhatPrintsExercise) {
                // Esercizi base - uso ExerciseScreen
                newScene = application.screens.exercises.ExerciseScreen.getScene(stage, returnScene, exercise);
            } else if (exercise instanceof QuizEP) {
                // Quiz EP - uso ScreenQuizEP
                newScene = application.screens.exercises.ScreenQuizEP.getScene(stage, returnScene, exercise);
            } else if (exercise instanceof CompleteCode) {
                // Complete Code - uso ScreenCompleteCode
                newScene = application.screens.exercises.ScreenCompleteCode.getScene(stage, returnScene, exercise);
            } else if (exercise instanceof CompareCode) {
                // Compare Code - uso ScreenCompareCode
                newScene = application.screens.exercises.ScreenCompareCode.getScene(stage, returnScene, exercise);
            }

            if (newScene != null) {
                stage.setScene(newScene);
            }
        } catch (Exception e) {
            System.err.println("Errore nel riavvio dell'esercizio: " + e.getMessage());
            // Fallback - torna alla griglia
            stage.setScene(returnScene);
        }
    }
}