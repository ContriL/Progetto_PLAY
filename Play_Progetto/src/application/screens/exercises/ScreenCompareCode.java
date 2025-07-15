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
import application.core.DialogUtils;

import java.util.HashMap;
import java.util.Map;

 //Schermata per l'esercizio Compare Code.//
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
        super(stage, 1400, 900); 
        this.exercise = exercise;
        this.returnScene = returnScene;

        loadRealContent();
    }

    private void loadRealContent() {
        if (exercise != null && exercise instanceof CompareCode) {
            codeContainer = createCompareCodeContent((CompareCode) exercise);
            setCenter(createScrollableContent(codeContainer));

            
            updateTitle(exercise.getTitle() + " - Livello " + exercise.getDifficulty());
            updateDescription(exercise.getDescription());
        }
    }

     //Crea un contenuto scrollabile per gestire contenuti lunghi//
     
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

        // Pulsante indietro//
        navbar.setBackAction(() -> {
            CompareCode codeExercise = (CompareCode) exercise;
            boolean isCompleted = codeExercise.allQuestionsAttempted();
            int correctAnswers = codeExercise.calculateScore();

            DialogUtils.showExerciseExitConfirmation(
                    exercise, isCompleted, correctAnswers,
                    () -> NavigationManager.getInstance().goToExerciseGrid()
            );
        });

        // Altri pulsanti navbar//
        navbar.setButtonAction("home", () -> handleNavigation(() ->
                NavigationManager.getInstance().goToHome()));

        navbar.setButtonAction("progress", () -> handleNavigation(() ->
                NavigationManager.getInstance().goToUserProgress()));

        navbar.setButtonAction("profile", () -> handleNavigation(() ->
                NavigationManager.getInstance().goToProfile()));

        navbar.setButtonAction("logout", () -> handleNavigation(() ->
                NavigationManager.getInstance().logout()));

        return navbar;
    }

    private void handleNavigation(Runnable navigationAction) {
        CompareCode codeExercise = (CompareCode) exercise;
        boolean isCompleted = codeExercise.allQuestionsAttempted();
        int correctAnswers = codeExercise.calculateScore();

        DialogUtils.showExerciseExitConfirmation(
                exercise, isCompleted, correctAnswers, navigationAction
        );
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

    
    // Crea il contenuto per l'esercizio Compare Code//
     
    private VBox createCompareCodeContent(CompareCode codeExercise) {
        codeContainer = new VBox(20);
        codeContainer.setPadding(new Insets(20));

        // Inizializza componenti UI//
        resultLabel = new Label();
        saveMessage = new Label();
        submitCurrent = new Button("Valuta Confronto");
        passToNextLevel = new Button("Prossimo Livello");
        passToNextLevel.setDisable(true);

        // Bottoni di navigazione tra domande//
        nextQuestion = new Button("Prossima Domanda →");
        previousQuestion = new Button("← Domanda Precedente");

        // Ottieni l'UI della domanda corrente//
        VBox questionUI = codeExercise.getCurrentQuestionUI();

        setupSubmitButton(codeExercise);
        setupNextLevelButton();
        setupNavigationButtons(codeExercise); 

        // Riga di navigazione tra domande//
        HBox navigationRow = new HBox(15, previousQuestion, nextQuestion);
        navigationRow.setAlignment(Pos.CENTER);

        // Riga delle azioni principali//
        HBox actionRow = new HBox(15, submitCurrent, passToNextLevel);
        actionRow.setAlignment(Pos.CENTER);

        updateNavigationButtons(codeExercise);

        // Separatori visivi//
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

   //configurazione bottono di navigazione tra domande//
   
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

  // Aggiorna lo stato dei bottoni di navigazione//
    
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

   //Aggiorna l'UI con la nuova domanda//
     
    private void refreshQuestionUI(CompareCode codeExercise) {
        if (codeContainer.getChildren().size() > 0) {
            codeContainer.getChildren().remove(0);
        }

        
        VBox newQuestionUI = codeExercise.getCurrentQuestionUI();
        codeContainer.getChildren().add(0, newQuestionUI);

        
        int current = codeExercise.getCurrentQuestionNumber();
        int total = codeExercise.getTotalQuestions();
        updateTitle(String.format("%s - Livello %d - Confronto %d/%d", 
            exercise.getTitle(), exercise.getDifficulty(), current, total));
    }

   //pulizia messaggi di stato//
   
    private void clearMessages() {
        resultLabel.setText("");
        saveMessage.setText("");
        
    }

    //Configura il bottone di submit per la valutazione//

     private void setupSubmitButton(CompareCode codeExercise) {
         submitCurrent.setOnAction(e -> {
             int currentIndex = codeExercise.getCurrentQuestionNumber() - 1;

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

                 // Salva i progressi
                 saveCurrentProgress(codeExercise);

                 // Mostra FinalResultScreen invece dei messaggi
                 showLevelCompleted(totalScore, totalQuestions);
             } else {
                 saveCurrentProgress(codeExercise);
             }
         });
     }

     /**
      * Mostra FinalResultScreen quando un livello è completato
      */
     private void showLevelCompleted(int correctAnswers, int totalQuestions) {
         ScrollPane finalScreen = application.components.FinalResultScreen.createFinalScreen(
                 exercise, correctAnswers, totalQuestions, true
         );

         application.components.FinalResultScreen.setupActions(finalScreen, exercise, stage, returnScene);

         setCenter(finalScreen);
     }

    //salva progresso corrente//
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

    
     //Configura il bottone per il prossimo livello//
    
    private void setupNextLevelButton() {
        passToNextLevel.setOnAction(e -> {
            int nextLevel = exercise.getDifficulty() + 1;
            if (nextLevel <= 3) {
                Exercise nextExercise = new CompareCode(nextLevel);
                Scene nextScene = ScreenCompareCode.getScene(stage, returnScene, nextExercise);
                stage.setScene(nextScene);
            } else {
                
                showFinalSummary();    //per mostrare dopoa aver completato tutti e 3 i livelli il resoconto finale//
            }
        });
    }
    

   //salva il progresso prima di uscire//
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

    //metodo per creare la scena//
    public static Scene getScene(Stage stage, Scene returnScene, Exercise exercise) {
        ScreenCompareCode screen = new ScreenCompareCode(stage, exercise, returnScene);
        return screen.createScene();
    }

   
    public int getCorrectCount(int difficulty) {
        return correctCountMap.getOrDefault(difficulty, 0);
    }
   
     //funzione per mostrare il resoconto finale post completamento livelli//
    private void showFinalSummary() {
        // Calcola statistiche totali//
        String username = Main.getCurrentUser();
        int level1Score = UserProgress.getCorrectAnswers(username, "CompareCode", 1);
        int level2Score = UserProgress.getCorrectAnswers(username, "CompareCode", 2);
        int level3Score = UserProgress.getCorrectAnswers(username, "CompareCode", 3);
        int totalScore = level1Score + level2Score + level3Score;

        // Crea la schermata finale usando FinalResultScreen//
        ScrollPane finalScreen = application.components.FinalResultScreen.createFinalScreen(
                exercise, totalScore, 9, true // 9 = 3 livelli x 3 confronti//
        );

       
        application.components.FinalResultScreen.setupActions(finalScreen, exercise, stage, returnScene);

        
        setCenter(finalScreen);
    }
}