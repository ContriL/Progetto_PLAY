package application.screens.exercises;

import application.screens.auth.Main;
import application.UserProgress;
import application.core.BaseScreen;
import application.core.NavigationManager;
import application.components.NavigationBar;
import application.exercises.CompleteCode;
import application.exercises.Exercise;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Schermata per l'esercizio Complete Code.
 * 
 */
public class ScreenCompleteCode extends BaseScreen {

    private static final Map<Integer, Integer> correctCountMap = new HashMap<>();

    private Exercise exercise;
    private Scene returnScene;

    // Componenti UI
    private Label resultLabel;
    private Label saveMessage;
    private Button submitAll;
    private Button passToNextLevel;
    private Button nextExercise; 
    private Button previousExercise; 
    private VBox codeContainer; 

    public ScreenCompleteCode(Stage stage, Exercise exercise, Scene returnScene) {
        super(stage, 1200, 800);
        this.exercise = exercise;
        this.returnScene = returnScene;

        
        loadRealContent();
    }

    
    private void loadRealContent() {
        if (exercise != null && exercise instanceof CompleteCode) {
            codeContainer = createCompleteCodeContent((CompleteCode) exercise);
            setCenter(codeContainer);

            // Aggiorna anche l'header
            updateTitle(exercise.getTitle() + " - Livello " + exercise.getDifficulty());
            updateDescription(exercise.getDescription());
        }
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
        return exercise != null ? exercise.getTitle() + " - Livello " + exercise.getDifficulty() : "Completa il Codice";
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
     * Crea il contenuto per l'esercizio Complete Code
     */
    private VBox createCompleteCodeContent(CompleteCode codeExercise) {
        codeContainer = new VBox(15);
        codeContainer.setPadding(new Insets(15));

        
        resultLabel = new Label();
        saveMessage = new Label();
        submitAll = new Button("Verifica Codice");
        passToNextLevel = new Button("Prossimo Livello");
        passToNextLevel.setDisable(true);

        
        nextExercise = new Button("Prossimo Esercizio →");
        previousExercise = new Button("← Esercizio Precedente");

        
        VBox codeUI = codeExercise.getCurrentQuestionUI();

        setupSubmitButton(codeExercise);
        setupNextLevelButton();
        setupNavigationButtons(codeExercise); 

        HBox navigationRow = new HBox(15, previousExercise, nextExercise);
        navigationRow.setAlignment(Pos.CENTER);

        HBox actionRow = new HBox(15, submitAll, passToNextLevel);
        actionRow.setAlignment(Pos.CENTER);

        updateNavigationButtons(codeExercise);

        codeContainer.getChildren().addAll(codeUI, navigationRow, actionRow, resultLabel, saveMessage);
        return codeContainer;
    }

    
    private void setupNavigationButtons(CompleteCode codeExercise) {
        nextExercise.setOnAction(e -> {
            if (codeExercise.goToNextQuestion()) {
                refreshCodeUI(codeExercise);
                updateNavigationButtons(codeExercise);
                clearMessages(); 
            }
        });

        previousExercise.setOnAction(e -> {
            if (codeExercise.goToPreviousQuestion()) {
                refreshCodeUI(codeExercise);
                updateNavigationButtons(codeExercise);
                clearMessages();
            }
        });
    }


    private void updateNavigationButtons(CompleteCode codeExercise) {
        nextExercise.setDisable(!codeExercise.hasNextQuestion());
        previousExercise.setDisable(!codeExercise.hasPreviousQuestion());

        int current = codeExercise.getCurrentQuestionNumber();
        int total = codeExercise.getTotalQuestions();
        
        nextExercise.setText(String.format("Prossimo Esercizio → (%d/%d)", 
            Math.min(current + 1, total), total));
        previousExercise.setText(String.format("← Esercizio Precedente (%d/%d)", 
            Math.max(current - 1, 1), total));
    }

 
    private void refreshCodeUI(CompleteCode codeExercise) {
        if (codeContainer.getChildren().size() > 0) {
            codeContainer.getChildren().remove(0);
        }

        
        VBox newCodeUI = codeExercise.getCurrentQuestionUI();
        codeContainer.getChildren().add(0, newCodeUI);

        
        int current = codeExercise.getCurrentQuestionNumber();
        int total = codeExercise.getTotalQuestions();
        updateTitle(String.format("%s - Livello %d - Esercizio %d/%d", 
            exercise.getTitle(), exercise.getDifficulty(), current, total));
    }

 
    private void clearMessages() {
        resultLabel.setText("");
        saveMessage.setText("");
        passToNextLevel.setDisable(true);
    }

 
    private void setupSubmitButton(CompleteCode codeExercise) {
        submitAll.setOnAction(e -> {
            int currentIndex = ((CompleteCode) exercise).getCurrentQuestionNumber() - 1;
            List<String> userAnswers = codeExercise.getUserAnswers();
            
            if (currentIndex < userAnswers.size()) {
                String userAnswer = userAnswers.get(currentIndex); 
                boolean isCorrect = codeExercise.checkAnswer(currentIndex, userAnswer);
                
                if (isCorrect) {
                    resultLabel.setText("✔ Esercizio corretto!");
                    resultLabel.setTextFill(Color.GREEN);
                    
                    
                    if (!codeExercise.hasNextQuestion() && codeExercise.allQuestionsAttempted()) {
                        int totalScore = codeExercise.calculateScore();
                        if (totalScore == codeExercise.getTotalQuestions()) {
                            passToNextLevel.setDisable(false);
                        }
                    }
                } else {
                    resultLabel.setText("✘ Esercizio non corretto. Riprova!");
                    resultLabel.setTextFill(Color.RED);
                }

                
                saveCurrentProgress(codeExercise);
            }
        });
    }

   
    private void saveCurrentProgress(CompleteCode codeExercise) {
        int correctCount = codeExercise.calculateScore();
        int totalQuestions = codeExercise.getTotalQuestions();
        int difficulty = exercise.getDifficulty();

        correctCountMap.put(difficulty, correctCount);

        String currentUser = Main.getCurrentUser();
        String type = "CompleteCode";
        boolean saved = UserProgress.saveProgress(currentUser, type, difficulty, correctCount, totalQuestions);

        if (saved) {
            saveMessage.setText(String.format("Progresso salvato: %d/%d esercizi corretti.", 
                correctCount, totalQuestions));
            saveMessage.setTextFill(Color.GREEN);
        } else {
            saveMessage.setText("Errore durante il salvataggio dei progressi.");
            saveMessage.setTextFill(Color.RED);
        }
    }


    private void setupNextLevelButton() {
        passToNextLevel.setOnAction(e -> {
            int nextLevel = exercise.getDifficulty() + 1;
            if (nextLevel <= 3) {
                Exercise nextExercise = new CompleteCode(nextLevel);
                Scene nextScene = ScreenCompleteCode.getScene(stage, returnScene, nextExercise);
                stage.setScene(nextScene);
            } else {
                // Ha completato tutti i livelli - mostra schermata finale
                showFinalSummary();
            }
        });
    }

  
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

  
    private void saveProgressBeforeExit() {
        if (exercise instanceof CompleteCode) {
            CompleteCode codeExercise = (CompleteCode) exercise;
            int correctCount = codeExercise.calculateScore();
            int totalQuestions = codeExercise.getTotalQuestions();
            int difficulty = exercise.getDifficulty();

            correctCountMap.put(difficulty, correctCount);

            String currentUser = Main.getCurrentUser();
            String type = "CompleteCode";
            UserProgress.saveProgress(currentUser, type, difficulty, correctCount, totalQuestions);

            System.out.println("✅ Progresso salvato prima dell'uscita: " + correctCount + "/" + totalQuestions);
        }
    }

   
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
            stage.setTitle("PLAY - " + (exercise != null ? exercise.getTitle() : "Completa il Codice"));
        }
    }

    
    public static Scene getScene(Stage stage, Scene anteprima, Exercise exercise) {
        ScreenCompleteCode screen = new ScreenCompleteCode(stage, exercise, anteprima);
        return screen.createScene();
    }

    
    public int getCorrectCount(int difficulty) {
        return correctCountMap.getOrDefault(difficulty, 0);
    }

    /**
     * Mostra la schermata finale quando tutti i livelli sono completati
     */
    private void showFinalSummary() {
        CompleteCode codeExercise = (CompleteCode) exercise;
        int correctCount = codeExercise.calculateScore();
        int totalQuestions = codeExercise.getTotalQuestions();

        // Crea la schermata finale
        ScrollPane finalScreen = application.components.FinalResultScreen.createFinalScreen(
                exercise, correctCount, totalQuestions, true
        );

        // Configura le azioni dei pulsanti
        application.components.FinalResultScreen.setupActions(finalScreen, exercise, stage, returnScene);

        // Sostituisci tutto il contenuto
        setCenter(finalScreen);
    }
}