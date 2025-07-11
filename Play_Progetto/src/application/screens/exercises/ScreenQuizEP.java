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

public class ScreenQuizEP extends BaseScreen {

    private static QuizEP currentQuiz;
    private static VBox questionContainer;
    private static Button previousButton, nextButton, submitButton, exitButton;
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
            setCenter(createCenterContent());
            setBottom(createBottomNavigation());
            updateCurrentQuestionDisplay();
            updateTitle(exercise.getTitle());
            updateDescription(exercise.getDescription());
        }
    }

    @Override
    protected NavigationBar createNavigationBar() {
        NavigationBar navbar = new NavigationBar(false);
        navbar.setButtonAction("home", () -> handleNavigation(() -> NavigationManager.getInstance().goToHome()));
        navbar.setButtonAction("progress", () -> handleNavigation(() -> NavigationManager.getInstance().goToUserProgress()));
        navbar.setButtonAction("profile", () -> handleNavigation(() -> NavigationManager.getInstance().goToProfile()));
        navbar.setButtonAction("logout", () -> handleNavigation(() -> NavigationManager.getInstance().logout()));
        return navbar;
    }

    private void handleNavigation(Runnable navigationAction) {
        if (quizCompleted) navigationAction.run(); // Naviga subito se quiz già finito
        else confirmExit(navigationAction);        // Altrimenti chiedi conferma uscita
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
        VBox box = new VBox(20);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.TOP_CENTER);

        questionCounterLabel = new Label();
        questionCounterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        questionCounterLabel.setStyle("-fx-text-fill: #2563eb;");

        questionContainer = new VBox(15);
        questionContainer.setAlignment(Pos.TOP_LEFT);
        questionContainer.setPadding(new Insets(20));
        questionContainer.setStyle(
                "-fx-border-color: #e2e8f0; -fx-border-width: 1; -fx-border-radius: 8; " +
                        "-fx-background-color: #ffffff; -fx-background-radius: 8;"
        );

        resultText = new Text();
        resultText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        resultText.setVisible(false);

        box.getChildren().addAll(questionCounterLabel, questionContainer, resultText);
        return box;
    }

    private HBox createBottomNavigation() {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));

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

        box.getChildren().addAll(previousButton, nextButton, submitButton, exitButton);
        return box;
    }

    private void updateCurrentQuestionDisplay() {
        if (quizCompleted) return;

        questionCounterLabel.setText(String.format("Quiz (%d/%d)",
                currentQuiz.getCurrentQuestionNumber(), currentQuiz.getTotalQuestions()));

        VBox questionUI = currentQuiz.getCurrentQuestionUI();
        questionContainer.getChildren().setAll(questionUI);

        updateButtonStates();
    }

    private void updateButtonStates() {
        previousButton.setDisable(!currentQuiz.hasPreviousQuestion());
        nextButton.setDisable(!currentQuiz.hasNextQuestion());

        boolean canSubmit = currentQuiz.getCurrentQuestionNumber() >= currentQuiz.getTotalQuestions()
                && hasAnsweredAtLeastOne();
        submitButton.setVisible(canSubmit);
    }

    private boolean hasAnsweredAtLeastOne() {
        return currentQuiz.getUserAnswers().stream()
                .anyMatch(ans -> ans != null && !ans.trim().isEmpty());
    }

    private void goToPreviousQuestion() {
        if (currentQuiz.goToPreviousQuestion()) updateCurrentQuestionDisplay();
    }

    private void goToNextQuestion() {
        if (currentQuiz.goToNextQuestion()) updateCurrentQuestionDisplay();
    }

    private void finishQuiz() {
        quizCompleted = true;

        int correct = currentQuiz.calculateScore();
        int total = currentQuiz.getTotalQuestions();
        double percentage = (double) correct / total * 100;

        questionContainer.setVisible(false);

        String message = String.format(
                "Bene hai superato l'esercizio e hai risposto correttamente a %d domande su %d.\n\n" +
                        "Percentuale di successo: %.1f%%\n\n%s\n\nProva con un nuovo esercizio.",
                correct, total, percentage,
                percentage >= 60 ? "Congratulazioni! Hai superato il quiz." :
                        "Continua a studiare per migliorare."
        );

        resultText.setText(message);
        resultText.setFill(percentage >= 60 ? Color.GREEN : Color.ORANGE);
        resultText.setVisible(true);

        UserProgress.saveProgress(Main.getCurrentUser(), "quizEP", currentQuiz.getDifficulty(), correct, total);

        questionCounterLabel.setText("Quiz completato - Progressi salvati!");
        questionCounterLabel.setStyle("-fx-text-fill: green;");

        replaceWithFinalButtons();
    }

    private void replaceWithFinalButtons() {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));

        Button newQuiz = new Button("Nuovo Esercizio");
        Button nextLevel = new Button("Livello Successivo");

        // Ricomincia stesso livello
        newQuiz.setOnAction(e -> {
            QuizEP q = new QuizEP(currentQuiz.getDifficulty());
            stage.setScene(ScreenQuizEP.getScene(stage, returnScene, q));
        });

        // Prossimo livello se superato
        nextLevel.setOnAction(e -> {
            int level = currentQuiz.getDifficulty() + 1;
            if (level <= 3) {
                QuizEP q = new QuizEP(level);
                stage.setScene(ScreenQuizEP.getScene(stage, returnScene, q));
            } else {
                showAlert("Hai già completato tutti i livelli disponibili!");
            }
        });

        // Blocca il bottone se non ha superato il livello
        double percentage = (double) currentQuiz.calculateScore() / currentQuiz.getTotalQuestions() * 100;
        nextLevel.setDisable(percentage < 60 || currentQuiz.getDifficulty() >= 3);

        exitButton.setText("Esci dall'esercizio");
        exitButton.setOnAction(e -> confirmExit(() -> NavigationManager.getInstance().goToExerciseGrid()));

        box.getChildren().addAll(newQuiz, nextLevel, exitButton);
        setBottom(box);
    }

    private static void confirmExit(Runnable exitAction) {
        DialogUtils.showExerciseExitConfirmation(
                currentQuiz,
                quizCompleted,
                currentQuiz.calculateScore(),
                exitAction
        );
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informazione");
        alert.setHeaderText(null);
        alert.setContentText(msg);
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
        return new ScreenQuizEP(stage, exercise, returnScene).createScene();
    }
}