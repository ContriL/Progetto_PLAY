package application.screens.exercises;

import application.screens.auth.Main;
import application.UserProgress;
import application.core.BaseScreen;
import application.core.NavigationManager;
import application.components.NavigationBar;
import application.exercises.CompareCode;
import application.exercises.Exercise;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * Schermata per l'esercizio Compare Code.
 */
public class ScreenCompareCode extends BaseScreen {

    private static final Map<Integer, Integer> correctCountMap = new HashMap<>();

    private Exercise exercise;
    private Scene returnScene;

    // Componenti UI
    private Label resultLabel;
    private Label saveMessage;
    private Button submitCurrent;
    private Button passToNextLevel;
    private Button nextQuestion; 
    private Button previousQuestion; 
    private VBox codeContainer; 

    public ScreenCompareCode(Stage stage, Exercise exercise, Scene returnScene) {
        super(stage, 1400, 900); // Più largo per i due codici affiancati
        this.exercise = exercise;
        this.returnScene = returnScene;

        loadRealContent();
    }

    private void loadRealContent() {
        if (exercise != null && exercise instanceof CompareCode) {
            codeContainer = createCompareCodeContent((CompareCode) exercise);
            setCenter(createScrollableContent(codeContainer));

            // Aggiorna anche l'header
            updateTitle(exercise.getTitle() + " - Livello " + exercise.getDifficulty());
            updateDescription(exercise.getDescription());
        }
    }

    /**
     * Crea un contenuto scrollabile per gestire contenuti lunghi
     */
    private ScrollPane createScrollableContent(VBox content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        return scrollPane;
    }

    @Override
    protected NavigationBar createNavigationBar() {
        NavigationBar navbar = new NavigationBar(true); 

        navbar.setBackAction(() -> {
            showExitConfirmation(() -> {
                saveProgressBeforeExit();
                NavigationManager.getInstance().goToExerciseGrid();
            });
        });

        return navbar;
    }

    @Override
    protected String getScreenTitle() {
        return exercise != null ? exercise.getTitle() + " - Livello " + exercise.getDifficulty() : "Confronta il Codice";
    }

    @Override
    protected String getScreenDescription() {
        return exercise != null ? exercise.getDescription() : "Caricamento...";
    }

    @Override
    protected void initializeContent() {
        VBox placeholderBox = new VBox();
        placeholderBox.setPadding(new Insets(20));
        setCenter(placeholderBox);
    }

    @Override
    public void onShow() {
        super.onShow();
    }

    /**
     * Crea il contenuto per l'esercizio Compare Code
     */
    private VBox createCompareCodeContent(CompareCode codeExercise) {
        codeContainer = new VBox(20);
        codeContainer.setPadding(new Insets(20));

        // Inizializza componenti UI
        resultLabel = new Label();
        saveMessage = new Label();
        submitCurrent = new Button("Valuta Confronto");
        passToNextLevel = new Button("Prossimo Livello");
        passToNextLevel.setDisable(true);

        // Bottoni di navigazione tra domande
        nextQuestion = new Button("Prossima Domanda →");
        previousQuestion = new Button("← Domanda Precedente");

        // Ottieni l'UI della domanda corrente
        VBox questionUI = codeExercise.getCurrentQuestionUI();

        setupSubmitButton(codeExercise);
        setupNextLevelButton();
        setupNavigationButtons(codeExercise); 

        // Riga di navigazione tra domande
        HBox navigationRow = new HBox(15, previousQuestion, nextQuestion);
        navigationRow.setAlignment(Pos.CENTER);

        // Riga delle azioni principali
        HBox actionRow = new HBox(15, submitCurrent, passToNextLevel);
        actionRow.setAlignment(Pos.CENTER);

        updateNavigationButtons(codeExercise);

        // Separatori visivi
        Separator sep1 = new Separator();
        Separator sep2 = new Separator();

        codeContainer.getChildren().addAll(
            questionUI, 
            sep1,
            navigationRow, 
            actionRow, 
            sep2,
            resultLabel, 
            saveMessage
        );
        
        return codeContainer;
    }

    /**
     * Configura i bottoni di navigazione tra domande
     */
    private void setupNavigationButtons(CompareCode codeExercise) {
        nextQuestion.setOnAction(e -> {
            if (codeExercise.goToNextQuestion()) {
                refreshQuestionUI(codeExercise);
                updateNavigationButtons(codeExercise);
                clearMessages(); 
            }
        });

        previousQuestion.setOnAction(e -> {
            if (codeExercise.goToPreviousQuestion()) {
                refreshQuestionUI(codeExercise);
                updateNavigationButtons(codeExercise);
                clearMessages();
            }
        });
    }

    /**
     * Aggiorna lo stato dei bottoni di navigazione
     */
    private void updateNavigationButtons(CompareCode codeExercise) {
        nextQuestion.setDisable(!codeExercise.hasNextQuestion());
        previousQuestion.setDisable(!codeExercise.hasPreviousQuestion());

        int current = codeExercise.getCurrentQuestionNumber();
        int total = codeExercise.getTotalQuestions();
        
        nextQuestion.setText(String.format("Prossima Domanda → (%d/%d)", 
            Math.min(current + 1, total), total));
        previousQuestion.setText(String.format("← Domanda Precedente (%d/%d)", 
            Math.max(current - 1, 1), total));
    }

    /**
     * Aggiorna l'UI con la nuova domanda
     */
    private void refreshQuestionUI(CompareCode codeExercise) {
        if (codeContainer.getChildren().size() > 0) {
            codeContainer.getChildren().remove(0);
        }

        // Inserisci la nuova UI della domanda
        VBox newQuestionUI = codeExercise.getCurrentQuestionUI();
        codeContainer.getChildren().add(0, newQuestionUI);

        // Aggiorna il titolo della finestra
        int current = codeExercise.getCurrentQuestionNumber();
        int total = codeExercise.getTotalQuestions();
        updateTitle(String.format("%s - Livello %d - Confronto %d/%d", 
            exercise.getTitle(), exercise.getDifficulty(), current, total));
    }

    /**
     * Pulisce i messaggi di stato
     */
    private void clearMessages() {
        resultLabel.setText("");
        saveMessage.setText("");
        // Non disabilitare passToNextLevel qui, potrebbe essere già abilitato
    }

    /**
     * Configura il bottone di submit per la valutazione
     */
    private void setupSubmitButton(CompareCode codeExercise) {
        submitCurrent.setOnAction(e -> {
            int currentIndex = codeExercise.getCurrentQuestionNumber() - 1;
            
            // Per CompareCode, passiamo una stringa vuota come placeholder
            // La logica di valutazione è gestita internamente dalla classe
            boolean isCorrect = codeExercise.checkAnswer(currentIndex, "");
            
            if (isCorrect) {
                resultLabel.setText("✔ Valutazione corretta! Buona analisi del codice.");
                resultLabel.setTextFill(Color.GREEN);
            } else {
                resultLabel.setText("✘ Valutazione non completamente corretta. Ricontrolla le differenze tra i codici.");
                resultLabel.setTextFill(Color.ORANGE);
            }

            // Controlla se tutti i confronti sono stati completati
            if (codeExercise.allQuestionsAttempted()) {
                int totalScore = codeExercise.calculateScore();
                int totalQuestions = codeExercise.getTotalQuestions();
                
                // Mostra il punteggio finale
                resultLabel.setText(resultLabel.getText() + 
                    String.format("\n\nPunteggio finale: %d/%d confronti corretti", totalScore, totalQuestions));
                
                // Abilita il prossimo livello se il punteggio è sufficiente (almeno 70%)
                if ((totalScore * 100.0 / totalQuestions) >= 66.0)  {
                    passToNextLevel.setDisable(false);
                    resultLabel.setText(resultLabel.getText() + "\n🎉 Livello completato con successo!");
                }
            }

            // Salva sempre il progresso dopo ogni valutazione
            saveCurrentProgress(codeExercise);
        });
    }

    /**
     * Salva il progresso corrente
     */
    private void saveCurrentProgress(CompareCode codeExercise) {
        int correctCount = codeExercise.calculateScore();
        int totalQuestions = codeExercise.getTotalQuestions();
        int difficulty = exercise.getDifficulty();

        correctCountMap.put(difficulty, correctCount);

        String currentUser = Main.getCurrentUser();
        String type = "CompareCode";
        boolean saved = UserProgress.saveProgress(currentUser, type, difficulty, correctCount, totalQuestions);

        if (saved) {
            saveMessage.setText(String.format("Progresso salvato: %d/%d confronti corretti.", 
                correctCount, totalQuestions));
            saveMessage.setTextFill(Color.GREEN);
        } else {
            saveMessage.setText("Errore durante il salvataggio dei progressi.");
            saveMessage.setTextFill(Color.RED);
        }
    }

    /**
     * Configura il bottone per il prossimo livello
     */
    /**
     * Configura il bottone per il prossimo livello
     */
    private void setupNextLevelButton() {
        passToNextLevel.setOnAction(e -> {
            int nextLevel = exercise.getDifficulty() + 1;
            if (nextLevel <= 3) {
                Exercise nextExercise = new CompareCode(nextLevel);
                Scene nextScene = ScreenCompareCode.getScene(stage, returnScene, nextExercise);
                stage.setScene(nextScene);
            } else {
                // Ha completato tutti i 3 livelli - mostra riepilogo finale
                showFinalSummary();
            }
        });
    }

    /**
     * Mostra dialogo di conferma per l'uscita
     */
    private void showExitConfirmation(Runnable onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Uscita");
        alert.setHeaderText("⚠️ Attenzione: stai per uscire dall'esercizio");
        alert.setContentText("I tuoi progressi verranno salvati automaticamente.\n\n" +
                "Vuoi continuare e uscire dall'esercizio?");

        ButtonType confirmButton = new ButtonType("✅ Sì, salva ed esci", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("❌ No, continua esercizio", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                onConfirm.run();
            }
        });
    }

    /**
     * Salva il progresso prima di uscire
     */
    private void saveProgressBeforeExit() {
        if (exercise instanceof CompareCode) {
            CompareCode codeExercise = (CompareCode) exercise;
            int correctCount = codeExercise.calculateScore();
            int totalQuestions = codeExercise.getTotalQuestions();
            int difficulty = exercise.getDifficulty();

            correctCountMap.put(difficulty, correctCount);

            String currentUser = Main.getCurrentUser();
            String type = "CompareCode";
            UserProgress.saveProgress(currentUser, type, difficulty, correctCount, totalQuestions);

            System.out.println("✅ Progresso CompareCode salvato prima dell'uscita: " + correctCount + "/" + totalQuestions);
        }
    }

    /**
     * Mostra contenuto di errore se necessario
     */
    private void showErrorContent() {
        VBox errorBox = new VBox(20);
        errorBox.setAlignment(Pos.CENTER);
        errorBox.setPadding(new Insets(50));

        Label errorLabel = new Label("Errore: Tipo di esercizio non supportato");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button backButton = new Button("Torna alla Selezione");
        backButton.setOnAction(e -> NavigationManager.getInstance().goToExerciseGrid());

        errorBox.getChildren().addAll(errorLabel, backButton);
        setCenter(errorBox);
    }

    @Override
    protected void configureScene(Scene scene) {
        super.configureScene(scene);
        if (stage != null) {
            stage.setTitle("PLAY - " + (exercise != null ? exercise.getTitle() : "Confronta il Codice"));
        }
    }

    /**
     * Metodo factory per creare la scena
     */
    public static Scene getScene(Stage stage, Scene returnScene, Exercise exercise) {
        ScreenCompareCode screen = new ScreenCompareCode(stage, exercise, returnScene);
        return screen.createScene();
    }

    /**
     * Ottieni il conteggio dei confronti corretti per difficoltà
     */
    public int getCorrectCount(int difficulty) {
        return correctCountMap.getOrDefault(difficulty, 0);
    }
    /**
     * Mostra il riepilogo finale quando tutti i livelli sono completati
     */
    /**
     * Mostra il riepilogo finale quando tutti i livelli sono completati
     */
    private void showFinalSummary() {
        // Calcola statistiche totali da UserProgress
        String username = Main.getCurrentUser();
        int level1Score = UserProgress.getCorrectAnswers(username, "CompareCode", 1);
        int level2Score = UserProgress.getCorrectAnswers(username, "CompareCode", 2);
        int level3Score = UserProgress.getCorrectAnswers(username, "CompareCode", 3);
        int totalScore = level1Score + level2Score + level3Score;
        int maxPossible = 9; // 3 livelli x 3 confronti = 9

        double percentage = (double) totalScore / maxPossible * 100;

        String summaryText = String.format(
                "🎊 COMPLIMENTI! Hai completato tutti i livelli di Confronta il Codice!\n\n" +
                        "📊 RIEPILOGO FINALE:\n" +
                        "• Livello 1 (Principiante): %d/3 confronti corretti\n" +
                        "• Livello 2 (Intermedio): %d/3 confronti corretti\n" +
                        "• Livello 3 (Avanzato): %d/3 confronti corretti\n\n" +
                        "🎯 PUNTEGGIO TOTALE: %d/%d (%.1f%%)\n\n" +
                        "%s",
                level1Score, level2Score, level3Score, totalScore, maxPossible, percentage,
                getPerformanceMessage(percentage)
        );

        // Mostra alert con riepilogo e poi esce automaticamente
        Alert summaryAlert = new Alert(Alert.AlertType.INFORMATION);
        summaryAlert.setTitle("Esercizio Completato!");
        summaryAlert.setHeaderText("🎊 Tutti i livelli completati!");
        summaryAlert.setContentText(summaryText);

        ButtonType okButton = new ButtonType("🏠 Torna alla Griglia", ButtonBar.ButtonData.OK_DONE);
        summaryAlert.getButtonTypes().setAll(okButton);

        summaryAlert.showAndWait().ifPresent(response -> {
            // Esce automaticamente quando clicca OK
            NavigationManager.getInstance().goToExerciseGrid();
        });
    }
    /**
     * Messaggio di valutazione in base alla performance
     */
    private String getPerformanceMessage(double percentage) {
        if (percentage >= 90) {
            return "🌟 ECCELLENTE! Hai dimostrato ottime capacità di analisi del codice!";
        } else if (percentage >= 80) {
            return "👍 MOLTO BENE! Hai una buona comprensione delle best practice!";
        } else if (percentage >= 70) {
            return "✅ BUONO! Continua a praticare per migliorare!";
        } else {
            return "📚 Rivedi i concetti e riprova! La pratica rende perfetti!";
        }
    }
}