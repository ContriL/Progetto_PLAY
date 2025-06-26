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

/**
 * Schermata Quiz PERFETTAMENTE CONFORME alle specifiche del PDF.
 * - Mostra UNA domanda alla volta (4/18 nelle specifiche)
 * - Navigazione Precedente/Successiva come richiesto
 * - Salvataggio automatico delle risposte tra navigazioni
 * - REFACTORATO per usare BaseScreen ed eliminare duplicazioni
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

        // Ora che tutto è inizializzato, carica il contenuto vero
        loadRealContent();
    }

    /**
     * Carica il contenuto vero dopo che exercise è stato assegnato
     */
    private void loadRealContent() {
        if (exercise != null && currentQuiz != null) {
            // Crea il contenuto completo
            VBox centerContent = createCenterContent();
            setCenter(centerContent);

            // Area inferiore con pulsanti
            HBox bottomNav = createBottomNavigation();
            setBottom(bottomNav);

            // Carica la prima domanda
            updateCurrentQuestionDisplay();

            // Aggiorna l'header
            updateTitle(exercise.getTitle());
            updateDescription(exercise.getDescription());
        }
    }

    @Override
    protected NavigationBar createNavigationBar() {
        NavigationBar navbar = new NavigationBar(false); // Senza pulsante indietro standard

        // Personalizza le azioni navbar con conferma di uscita
        // I metodi di navbar (Home, Progressi, Logout) avranno conferma automatica
        return navbar;
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
        // Durante l'inizializzazione BaseScreen, exercise è null
        // Crea placeholder che verrà sostituito da loadRealContent()
        VBox placeholderBox = new VBox();
        placeholderBox.setPadding(new Insets(20));
        setCenter(placeholderBox);
    }

    /**
     * Area centrale con contenitore domanda
     */
    private VBox createCenterContent() {
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));
        centerContent.setAlignment(Pos.TOP_CENTER);

        // CONFORME AL PDF: "Quiz (1/3)" - Counter domanda corrente
        questionCounterLabel = new Label();
        questionCounterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        questionCounterLabel.setStyle("-fx-text-fill: #2563eb;");

        // Container per la domanda corrente
        questionContainer = new VBox(15);
        questionContainer.setAlignment(Pos.TOP_LEFT);
        questionContainer.setPadding(new Insets(20));
        questionContainer.setStyle("-fx-border-color: #e2e8f0; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-color: #ffffff; -fx-background-radius: 8;");

        // Area risultato finale (nascosta inizialmente)
        resultText = new Text();
        resultText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        resultText.setVisible(false);

        centerContent.getChildren().addAll(questionCounterLabel, questionContainer, resultText);
        return centerContent;
    }

    /**
     * Area inferiore con pulsanti - ESATTAMENTE come nel PDF (slide 18)
     */
    private HBox createBottomNavigation() {
        HBox bottomNav = new HBox(15);
        bottomNav.setAlignment(Pos.CENTER);
        bottomNav.setPadding(new Insets(20));

        // PULSANTI ESATTAMENTE COME NEL PDF
        previousButton = new Button("Domanda Precedente");
        nextButton = new Button("Prossima domanda");
        submitButton = new Button("Termina Quiz");
        exitButton = new Button("Esci dall'esercizio");

        // Styling per uniformità
        previousButton.setPrefWidth(160);
        nextButton.setPrefWidth(160);
        submitButton.setPrefWidth(120);
        exitButton.setPrefWidth(160);

        // Event handlers
        previousButton.setOnAction(e -> goToPreviousQuestion());
        nextButton.setOnAction(e -> goToNextQuestion());
        submitButton.setOnAction(e -> finishQuiz());
        exitButton.setOnAction(e -> confirmExit(() -> NavigationManager.getInstance().goToExerciseGrid()));

        bottomNav.getChildren().addAll(previousButton, nextButton, submitButton, exitButton);
        return bottomNav;
    }

    /**
     * Aggiorna la visualizzazione della domanda corrente
     */
    private void updateCurrentQuestionDisplay() {
        if (quizCompleted) return;

        // Aggiorna il counter - CONFORME AL PDF: "Quiz (1/3)"
        questionCounterLabel.setText(String.format("Quiz (%d/%d)",
                currentQuiz.getCurrentQuestionNumber(), currentQuiz.getTotalQuestions()));

        // Carica l'UI della domanda corrente
        VBox currentQuestionUI = currentQuiz.getCurrentQuestionUI();
        questionContainer.getChildren().clear();
        questionContainer.getChildren().add(currentQuestionUI);

        // Aggiorna stato pulsanti
        updateButtonStates();
    }

    /**
     * Aggiorna lo stato dei pulsanti in base alla posizione corrente
     */
    private void updateButtonStates() {
        // Pulsante "Precedente" - disabilitato se siamo alla prima domanda
        previousButton.setDisable(!currentQuiz.hasPreviousQuestion());

        // Pulsante "Successiva" - disabilitato se siamo all'ultima domanda
        nextButton.setDisable(!currentQuiz.hasNextQuestion());

        // Pulsante "Termina Quiz" - visibile solo quando si è almeno all'ultima domanda
        // e si è risposto ad almeno una domanda
        boolean canSubmit = currentQuiz.getCurrentQuestionNumber() >= currentQuiz.getTotalQuestions() &&
                hasAnsweredAtLeastOne();
        submitButton.setVisible(canSubmit);
    }

    /**
     * Verifica se l'utente ha risposto almeno a una domanda
     */
    private boolean hasAnsweredAtLeastOne() {
        for (String answer : currentQuiz.getUserAnswers()) {
            if (answer != null && !answer.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Naviga alla domanda precedente
     */
    private void goToPreviousQuestion() {
        if (currentQuiz.goToPreviousQuestion()) {
            updateCurrentQuestionDisplay();
        }
    }

    /**
     * Naviga alla domanda successiva
     */
    private void goToNextQuestion() {
        if (currentQuiz.goToNextQuestion()) {
            updateCurrentQuestionDisplay();
        }
    }

    /**
     * Termina il quiz e mostra i risultati
     */
    private void finishQuiz() {
        quizCompleted = true;

        // Calcola risultati
        int correctAnswers = currentQuiz.calculateScore();
        int totalQuestions = currentQuiz.getTotalQuestions();
        double percentage = (double) correctAnswers / totalQuestions * 100;

        // Nascondi l'area della domanda
        questionContainer.setVisible(false);

        // Mostra risultati CONFORMI AL PDF (slide 20)
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

        // Salva progressi
        saveProgress(correctAnswers, totalQuestions);

        // Aggiorna counter con messaggio di salvataggio
        questionCounterLabel.setText("Quiz completato - Progressi salvati!");
        questionCounterLabel.setStyle("-fx-text-fill: green;");

        // Sostituisci i pulsanti con quelli finali
        replaceWithFinalButtons();
    }

    /**
     * Sostituisce i pulsanti di navigazione con quelli finali
     */
    private void replaceWithFinalButtons() {
        HBox finalButtons = new HBox(15);
        finalButtons.setAlignment(Pos.CENTER);
        finalButtons.setPadding(new Insets(20));

        Button newQuizButton = new Button("Nuovo Esercizio");
        Button nextLevelButton = new Button("Livello Successivo");
        exitButton.setText("Esci dall'esercizio"); // Mantieni quello esistente

        newQuizButton.setPrefWidth(150);
        nextLevelButton.setPrefWidth(150);

        // Nuovo quiz stesso livello
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

        // Abilita "Livello Successivo" solo se ha superato il quiz (>=60%)
        double percentage = (double) currentQuiz.calculateScore() / currentQuiz.getTotalQuestions() * 100;
        nextLevelButton.setDisable(percentage < 60 || currentQuiz.getDifficulty() >= 3);

        finalButtons.getChildren().addAll(newQuizButton, nextLevelButton, exitButton);
        setBottom(finalButtons);
    }

    /**
     * Salva i progressi del quiz
     */
    private void saveProgress(int correctAnswers, int totalQuestions) {
        String currentUser = Main.getCurrentUser();
        boolean saved = UserProgress.saveProgress(
                currentUser, "quizEP", currentQuiz.getDifficulty(),
                correctAnswers, totalQuestions
        );

        if (!saved) {
            System.err.println("Errore durante il salvataggio dei progressi del quiz");
        }
    }

    /**
     * Conferma di uscita con salvataggio parziale - CONFORME ALLE SPECIFICHE DEL PROF
     */
    private static void confirmExit(Runnable exitAction) {
        // Se il quiz non è completato ma ha risposto ad almeno una domanda
        if (!quizCompleted && currentQuiz.getUserAnswers().stream().anyMatch(answer -> answer != null && !answer.trim().isEmpty())) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma Uscita");
            alert.setHeaderText("⚠️ Attenzione: stai per uscire dal quiz");
            alert.setContentText("I tuoi progressi verranno salvati automaticamente.\n\n" +
                    "Vuoi continuare e uscire dal quiz?");

            // Pulsanti personalizzati
            ButtonType confirmButton = new ButtonType("✅ Sì, salva ed esci", ButtonBar.ButtonData.YES);
            ButtonType cancelButton = new ButtonType("❌ No, continua quiz", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(confirmButton, cancelButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == confirmButton) {
                    // Salva progresso parziale
                    int answeredQuestions = 0;
                    for (String answer : currentQuiz.getUserAnswers()) {
                        if (answer != null && !answer.trim().isEmpty()) {
                            answeredQuestions++;
                        }
                    }

                    if (answeredQuestions > 0) {
                        // Salva progresso direttamente usando UserProgress
                        String currentUser = Main.getCurrentUser();
                        boolean saved = UserProgress.saveProgress(
                                currentUser,
                                "quizEP",
                                currentQuiz.getDifficulty(),
                                currentQuiz.calculateScore(),
                                currentQuiz.getTotalQuestions()
                        );

                        if (saved) {
                            System.out.println("✅ Progresso parziale salvato prima dell'uscita");
                        }
                           }

                    exitAction.run();
                }
                // Se clicca "No" o chiude, non fa nulla (continua il quiz)
            });
        } else {
            // Se non ha risposto a nulla o il quiz è completato, esce direttamente
            exitAction.run();
        }
    }

    /**
     * Mostra un alert informativo
     */
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

    // Metodo statico per compatibilità con il codice esistente
    public static Scene getScene(Stage stage, Scene returnScene, Exercise exercise) {
        ScreenQuizEP screen = new ScreenQuizEP(stage, exercise, returnScene);
        return screen.createScene();
    }
}