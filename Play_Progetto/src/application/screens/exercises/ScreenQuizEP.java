package application.screens.exercises;

import application.screens.auth.Main;
import application.UserProgress;
import application.core.BaseScreen;
import application.core.NavigationManager;
import application.components.NavigationBar;
import application.exercises.Exercise;
import application.exercises.QuizEP;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import application.core.DialogUtils;

/**
 * Schermata Quiz sull'ereditarietà e il polimorfismo
 */
public class ScreenQuizEP extends BaseScreen {

    private static QuizEP currentQuiz;
    private static VBox questionContainer;
    private static Button previousButton;
    private static Button nextButton;
    private static Button submitButton;
    private static Button exitButton;
    private static Label questionCounterLabel;
    private static Text resultText;
    private static boolean quizCompleted = false;
    private static Scene returnScene;

    private Exercise exercise;

    public ScreenQuizEP(Stage stage, Exercise exercise, Scene returnScene) {
        super(stage, 1200, 800);
        this.exercise = exercise;

        if (!(exercise instanceof QuizEP)) {
            throw new IllegalArgumentException("Questo screen è solo per QuizEP");
        }

        currentQuiz = (QuizEP) exercise;
        ScreenQuizEP.returnScene = returnScene;
        quizCompleted = false;

        
        loadRealContent();
    }

    private void loadRealContent() {
        if (exercise != null && currentQuiz != null) {
            
            VBox centerContent = createCenterContent();
            setCenter(centerContent);

            
            HBox bottomNav = createBottomNavigation();
            setBottom(bottomNav);

            
            updateCurrentQuestionDisplay();

            
            updateTitle(exercise.getTitle());
            updateDescription(exercise.getDescription());
        }
    }

    @Override
    protected NavigationBar createNavigationBar() {
        NavigationBar navbar = new NavigationBar(false);

        navbar.setButtonAction("home", () ->
                handleNavigation(() -> NavigationManager.getInstance().goToHome()));

        navbar.setButtonAction("progress", () ->
                handleNavigation(() -> NavigationManager.getInstance().goToUserProgress()));

        navbar.setButtonAction("profile", () ->
                handleNavigation(() -> NavigationManager.getInstance().goToProfile()));

        navbar.setButtonAction("logout", () ->
                handleNavigation(() -> NavigationManager.getInstance().logout()));

        return navbar;
    }

    /**
     * Helper per determinare se mostrare conferma uscita
     */
    private void handleNavigation(Runnable navigationAction) {
        if (quizCompleted) {
            // Quiz completato - navigazione libera
            navigationAction.run();
        } else {
            // Quiz in corso - chiedi conferma
            confirmExit(navigationAction);
        }
    }

    @Override
    protected String getScreenTitle() {
        return exercise != null ? exercise.getTitle() : "Quiz";
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

  
    private VBox createCenterContent() {
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));
        centerContent.setAlignment(Pos.TOP_CENTER);


        questionCounterLabel = new Label();
        questionCounterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        questionCounterLabel.setStyle("-fx-text-fill: #2563eb;");


        questionContainer = new VBox(15);
        questionContainer.setAlignment(Pos.TOP_LEFT);
        questionContainer.setPadding(new Insets(20));
        questionContainer.setStyle("-fx-border-color: #e2e8f0; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-color: #ffffff; -fx-background-radius: 8;");


        resultText = new Text();
        resultText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        resultText.setVisible(false);

        centerContent.getChildren().addAll(questionCounterLabel, questionContainer, resultText);
        return centerContent;
    }

   
    private HBox createBottomNavigation() {
        HBox bottomNav = new HBox(15);
        bottomNav.setAlignment(Pos.CENTER);
        bottomNav.setPadding(new Insets(20));

        
        previousButton = new Button("Domanda Precedente");
        nextButton = new Button("Prossima domanda");
        submitButton = new Button("Termina Quiz");
        exitButton = new Button("Esci dall'esercizio");

        
        previousButton.setPrefWidth(160);
        nextButton.setPrefWidth(160);
        submitButton.setPrefWidth(120);
        exitButton.setPrefWidth(160);

        previousButton.setOnAction(e -> goToPreviousQuestion());
        nextButton.setOnAction(e -> goToNextQuestion());
        submitButton.setOnAction(e -> finishQuiz());
        exitButton.setOnAction(e -> confirmExit(() -> NavigationManager.getInstance().goToExerciseGrid()));

        bottomNav.getChildren().addAll(previousButton, nextButton, submitButton, exitButton);
        return bottomNav;
    }

    
    private void updateCurrentQuestionDisplay() {
        if (quizCompleted) return;

        
        questionCounterLabel.setText(String.format("Quiz (%d/%d)",
                currentQuiz.getCurrentQuestionNumber(), currentQuiz.getTotalQuestions()));

        
        VBox currentQuestionUI = currentQuiz.getCurrentQuestionUI();
        questionContainer.getChildren().clear();
        questionContainer.getChildren().add(currentQuestionUI);

        
        updateButtonStates();
    }

 
    private void updateButtonStates() {
        
        previousButton.setDisable(!currentQuiz.hasPreviousQuestion());

        
        nextButton.setDisable(!currentQuiz.hasNextQuestion());

 
        boolean canSubmit = currentQuiz.getCurrentQuestionNumber() >= currentQuiz.getTotalQuestions() &&
                hasAnsweredAtLeastOne();
        submitButton.setVisible(canSubmit);
    }

 
    private boolean hasAnsweredAtLeastOne() {
        for (String answer : currentQuiz.getUserAnswers()) {
            if (answer != null && !answer.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

   
    private void goToPreviousQuestion() {
        if (currentQuiz.goToPreviousQuestion()) {
            updateCurrentQuestionDisplay();
        }
    }

 
    private void goToNextQuestion() {
        if (currentQuiz.goToNextQuestion()) {
            updateCurrentQuestionDisplay();
        }
    }


    private void finishQuiz() {
        quizCompleted = true;

        
        int correctAnswers = currentQuiz.calculateScore();
        int totalQuestions = currentQuiz.getTotalQuestions();
        double percentage = (double) correctAnswers / totalQuestions * 100;

        
        questionContainer.setVisible(false);

        
        String resultMessage = String.format(
                "Bene hai superato l'esercizio e hai risposto correttamente a %d domande su %d.\n\n" +
                        "Percentuale di successo: %.1f%%\n\n" +
                        "%s\n\n" +
                        "Prova con un nuovo esercizio.",
                correctAnswers, totalQuestions, percentage,
                percentage >= 60 ? "Congratulazioni! Hai superato il quiz." :
                        "Continua a studiare per migliorare."
        );

        resultText.setText(resultMessage);
        resultText.setFill(percentage >= 60 ? Color.GREEN : Color.ORANGE);
        resultText.setVisible(true);


        String currentUser = Main.getCurrentUser();
        boolean saved = UserProgress.saveProgress(
                currentUser,
                "quizEP",
                currentQuiz.getDifficulty(),
                correctAnswers,
                totalQuestions
        );

        if (!saved) {
            System.err.println("Errore durante il salvataggio dei progressi del quiz");
        }

        questionCounterLabel.setText("Quiz completato - Progressi salvati!");
        questionCounterLabel.setStyle("-fx-text-fill: green;");

        replaceWithFinalButtons();
    }

    
    private void replaceWithFinalButtons() {
        HBox finalButtons = new HBox(15);
        finalButtons.setAlignment(Pos.CENTER);
        finalButtons.setPadding(new Insets(20));

        Button newQuizButton = new Button("Nuovo Esercizio");
        Button nextLevelButton = new Button("Livello Successivo");
        exitButton.setText("Esci dall'esercizio");
        exitButton.setOnAction(e -> confirmExit(() -> NavigationManager.getInstance().goToExerciseGrid()));

        newQuizButton.setPrefWidth(150);
        nextLevelButton.setPrefWidth(150);

        
        newQuizButton.setOnAction(e -> {
            QuizEP newQuiz = new QuizEP(currentQuiz.getDifficulty());
            Scene newScene = ScreenQuizEP.getScene(stage, returnScene, newQuiz);
            stage.setScene(newScene);
        });

        // Livello successivo (se esiste e se ha superato)
        nextLevelButton.setOnAction(e -> {
            int nextLevel = currentQuiz.getDifficulty() + 1;
            if (nextLevel <= 3) {
                QuizEP nextQuiz = new QuizEP(nextLevel);
                Scene nextScene = ScreenQuizEP.getScene(stage, returnScene, nextQuiz);
                stage.setScene(nextScene);
            } else {
                showAlert("Hai già completato tutti i livelli disponibili!");
            }
        });

        
        double percentage = (double) currentQuiz.calculateScore() / currentQuiz.getTotalQuestions() * 100;
        nextLevelButton.setDisable(percentage < 60 || currentQuiz.getDifficulty() >= 3);

        finalButtons.getChildren().addAll(newQuizButton, nextLevelButton, exitButton);
        setBottom(finalButtons);
    }



    /**
     * Gestisce conferma uscita secondo specifiche Prof
     */
    private static void confirmExit(Runnable exitAction) {
        // Determina se il quiz è completato
        boolean isCompleted = quizCompleted;

        // Calcola risposte corrette attuali
        int correctAnswers = currentQuiz.calculateScore();

        // Usa DialogUtils con specifiche Prof
        DialogUtils.showExerciseExitConfirmation(
                currentQuiz,           // Exercise
                isCompleted,           // È completato?
                correctAnswers,        // Risposte corrette attuali
                exitAction            // Azione se conferma
        );
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informazione");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    protected void configureScene(Scene scene) {
        super.configureScene(scene);
        if (stage != null) {
            stage.setTitle("PLAY - " + (exercise != null ? exercise.getTitle() : "Quiz EP"));
        }
    }

    
    public static Scene getScene(Stage stage, Scene returnScene, Exercise exercise) {
        ScreenQuizEP screen = new ScreenQuizEP(stage, exercise, returnScene);
        return screen.createScene();
    }
}