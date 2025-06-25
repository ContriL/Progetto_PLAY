package application;

import application.exercises.Exercise;
import application.exercises.QuizEP;
import application.core.NavigationManager;
import application.core.StyleManager;
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

/**
 * Schermata Quiz CONFORME alle specifiche del PDF del professore.
 * Mostra UNA domanda alla volta con navigazione Precedente/Successiva.
 */
public class ScreenQuizEP {

    private static QuizEP currentQuiz;
    private static VBox questionContainer;
    private static Button previousButton;
    private static Button nextButton;
    private static Button submitButton;
    private static Label progressLabel;
    private static Text resultText;
    private static boolean quizCompleted = false;

    public static Scene getScene(Stage stage, Scene returnScene, Exercise exercise) {
        if (!(exercise instanceof QuizEP)) {
            throw new IllegalArgumentException("Questo screen è solo per QuizEP");
        }

        currentQuiz = (QuizEP) exercise;
        quizCompleted = false;

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 700, 550);

        StyleManager.applyMainStyles(scene);

        // === NAVBAR SUPERIORE ===
        HBox navBar = createNavigationBar(stage, returnScene);
        root.setTop(navBar);

        // === AREA CENTRALE CON DOMANDA ===
        VBox centerContent = createCenterContent();
        root.setCenter(centerContent);

        // === AREA INFERIORE CON PULSANTI NAVIGAZIONE ===
        HBox bottomContent = createBottomContent(stage, returnScene);
        root.setBottom(bottomContent);

        // Carica la prima domanda
        updateQuestionDisplay();

        stage.setTitle("PLAY - " + exercise.getTitle());
        return scene;
    }

    /**
     * Crea la barra di navigazione superiore
     */
    private static HBox createNavigationBar(Stage stage, Scene returnScene) {
        HBox navBar = new HBox(15);
        navBar.setId("navBar");
        navBar.setPadding(new Insets(10));
        navBar.setAlignment(Pos.CENTER_RIGHT);
        navBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        Button homeButton = new Button("Home");
        Button progressButton = new Button("I miei Progressi");
        Button logoutButton = new Button("Logout");
        Button backButton = new Button("Indietro");

        // Event handlers con conferma di uscita
        homeButton.setOnAction(e -> handleExit(stage, "home"));
        progressButton.setOnAction(e -> NavigationManager.getInstance().goToUserProgress());
        logoutButton.setOnAction(e -> handleExit(stage, "logout"));
        backButton.setOnAction(e -> handleExit(stage, "back"));

        navBar.getChildren().addAll(homeButton, progressButton, logoutButton, backButton);
        return navBar;
    }

    /**
     * Crea l'area centrale con header e contenitore domanda
     */
    private static VBox createCenterContent() {
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));

        // Header con titolo e descrizione
        Text headerText = new Text(currentQuiz.getTitle() + " - Livello " + currentQuiz.getDifficulty());
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Text descriptionText = new Text(currentQuiz.getDescription());
        descriptionText.setFont(Font.font("Arial", 14));

        // Progress label - CONFORME AL PDF: "Quiz (1/3)"
        progressLabel = new Label();
        progressLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        progressLabel.setStyle("-fx-text-fill: #333333;");

        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getChildren().addAll(headerText, descriptionText, progressLabel);

        // Contenitore per la domanda corrente
        questionContainer = new VBox();
        questionContainer.setAlignment(Pos.TOP_LEFT);

        // Area risultato (nascosta inizialmente)
        resultText = new Text();
        resultText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        resultText.setVisible(false);

        centerContent.getChildren().addAll(headerBox, questionContainer, resultText);
        return centerContent;
    }

    /**
     * Crea l'area inferiore con pulsanti di navigazione - CONFORME AL PDF
     */
    private static HBox createBottomContent(Stage stage, Scene returnScene) {
        HBox bottomContent = new HBox(15);
        bottomContent.setAlignment(Pos.CENTER);
        bottomContent.setPadding(new Insets(20));

        // Pulsanti come nel PDF
        previousButton = new Button("Domanda Precedente");
        nextButton = new Button("Prossima domanda");
        submitButton = new Button("Termina Quiz");
        Button exitButton = new Button("Esci dall'esercizio");

        // Styling
        previousButton.setPrefWidth(150);
        nextButton.setPrefWidth(150);
        submitButton.setPrefWidth(120);
        exitButton.setPrefWidth(150);

        // Event handlers
        previousButton.setOnAction(e -> goToPreviousQuestion());
        nextButton.setOnAction(e -> goToNextQuestion());
        submitButton.setOnAction(e -> submitQuiz(stage, returnScene));
        exitButton.setOnAction(e -> handleExit(stage, "back"));

        bottomContent.getChildren().addAll(previousButton, nextButton, submitButton, exitButton);
        return bottomContent;
    }

    /**
     * Aggiorna la visualizzazione della domanda corrente
     */
    private static void updateQuestionDisplay() {
        if (quizCompleted) {
            return;
        }

        // Aggiorna progress label
        progressLabel.setText(String.format("Domanda %d di %d",
                currentQuiz.getCurrentQuestionNumber(), currentQuiz.getTotalQuestions()));

        // Carica UI della domanda corrente
        VBox currentQuestionUI = currentQuiz.getCurrentQuestionUI();
        questionContainer.getChildren().clear();
        questionContainer.getChildren().add(currentQuestionUI);

        // Aggiorna stato pulsanti
        previousButton.setDisable(!currentQuiz.hasPreviousQuestion());
        nextButton.setDisable(!currentQuiz.hasNextQuestion());

        // Mostra pulsante "Termina Quiz" solo se tutte le domande sono state visitate
        submitButton.setVisible(currentQuiz.getCurrentQuestionNumber() == currentQuiz.getTotalQuestions());
    }

    /**
     * Va alla domanda precedente
     */
    private static void goToPreviousQuestion() {
        if (currentQuiz.goToPreviousQuestion()) {
            updateQuestionDisplay();
        }
    }

    /**
     * Va alla domanda successiva
     */
    private static void goToNextQuestion() {
        if (currentQuiz.goToNextQuestion()) {
            updateQuestionDisplay();
        }
    }

    /**
     * Termina il quiz e mostra i risultati
     */
    private static void submitQuiz(Stage stage, Scene returnScene) {
        quizCompleted = true;

        // Calcola risultati
        int correctAnswers = currentQuiz.calculateScore();
        int totalQuestions = currentQuiz.getTotalQuestions();
        double percentage = (double) correctAnswers / totalQuestions * 100;

        // Nascondi area domanda e pulsanti navigazione
        questionContainer.setVisible(false);
        previousButton.setVisible(false);
        nextButton.setVisible(false);
        submitButton.setVisible(false);

        // Mostra risultati
        resultText.setText(String.format(
                "Quiz completato!\n\n" +
                        "Hai risposto correttamente a %d domande su %d.\n" +
                        "Percentuale di successo: %.1f%%\n\n" +
                        "%s",
                correctAnswers, totalQuestions, percentage,
                percentage >= 60 ? "Congratulazioni! Hai superato il quiz." :
                        "Continua a studiare per migliorare il tuo punteggio."
        ));
        resultText.setFill(percentage >= 60 ? Color.GREEN : Color.ORANGE);
        resultText.setVisible(true);

        // Salva progressi
        String currentUser = Main.getCurrentUser();
        boolean saved = UserProgress.saveProgress(
                currentUser, "quizEP", currentQuiz.getDifficulty(),
                correctAnswers, totalQuestions
        );

        // Aggiorna progress label
        progressLabel.setText(saved ?
                "Progressi salvati con successo!" :
                "Errore durante il salvataggio dei progressi.");
        progressLabel.setStyle(saved ?
                "-fx-text-fill: green;" :
                "-fx-text-fill: red;");

        // Mostra pulsanti finali
        createFinalButtons(stage, returnScene);
    }

    /**
     * Crea i pulsanti finali dopo il completamento del quiz
     */
    private static void createFinalButtons(Stage stage, Scene returnScene) {
        HBox finalButtons = new HBox(15);
        finalButtons.setAlignment(Pos.CENTER);
        finalButtons.setPadding(new Insets(20));

        Button newQuizButton = new Button("Nuovo Quiz");
        Button nextLevelButton = new Button("Livello Successivo");
        Button homeButton = new Button("Torna alla Home");

        newQuizButton.setOnAction(e -> {
            // Riavvia lo stesso livello
            currentQuiz.resetQuiz();
            quizCompleted = false;
            resultText.setVisible(false);
            questionContainer.setVisible(true);
            updateQuestionDisplay();

            // Ripristina pulsanti originali
            BorderPane parent = (BorderPane) finalButtons.getParent();
            parent.setBottom(createBottomContent(stage, returnScene));
        });

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

        homeButton.setOnAction(e -> NavigationManager.getInstance().goToHome());

        // Abilita "Livello Successivo" solo se ha superato il quiz
        double percentage = (double) currentQuiz.calculateScore() / currentQuiz.getTotalQuestions() * 100;
        nextLevelButton.setDisable(percentage < 60 || currentQuiz.getDifficulty() >= 3);

        finalButtons.getChildren().addAll(newQuizButton, nextLevelButton, homeButton);

        BorderPane parent = (BorderPane) questionContainer.getParent().getParent();
        parent.setBottom(finalButtons);
    }

    /**
     * Gestisce l'uscita dal quiz con conferma
     */
    private static void handleExit(Stage stage, String destination) {
        // Se il quiz non è ancora completato, chiedi conferma
        if (!quizCompleted && currentQuiz.getCurrentQuestionNumber() > 1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma uscita");
            alert.setHeaderText("Attenzione: stai per uscire dal quiz.");
            alert.setContentText("I tuoi progressi verranno salvati parzialmente. Vuoi continuare?");

            ButtonType yesButton = new ButtonType("Sì", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == yesButton) {
                    savePartialProgress();
                    navigateToDestination(destination);
                }
            });
        } else {
            // Se completato o appena iniziato, esci direttamente
            navigateToDestination(destination);
        }
    }

    /**
     * Salva i progressi parziali
     */
    private static void savePartialProgress() {
        int answeredQuestions = 0;
        for (String answer : currentQuiz.getUserAnswers()) {
            if (answer != null && !answer.trim().isEmpty()) {
                answeredQuestions++;
            }
        }

        if (answeredQuestions > 0) {
            int correctAnswers = currentQuiz.calculateScore();
            UserProgress.saveProgress(
                    Main.getCurrentUser(), "quizEP", currentQuiz.getDifficulty(),
                    correctAnswers, answeredQuestions
            );
        }
    }

    /**
     * Naviga alla destinazione specificata
     */
    private static void navigateToDestination(String destination) {
        NavigationManager navManager = NavigationManager.getInstance();
        switch (destination) {
            case "home":
                navManager.goToHome();
                break;
            case "logout":
                navManager.logout();
                break;
            case "back":
            default:
                navManager.goToExerciseGrid();
                break;
        }
    }

    /**
     * Mostra un alert informativo
     */
    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informazione");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}