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
 * REFACTORATO per usare BaseScreen ed eliminare duplicazioni navbar.
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

    public ScreenCompleteCode(Stage stage, Exercise exercise, Scene returnScene) {
        super(stage, 1200, 800);
        this.exercise = exercise;
        this.returnScene = returnScene;

        // Ora che exercise è assegnato, carica il contenuto vero
        loadRealContent();
    }

    /**
     * Carica il contenuto vero dopo che exercise è stato assegnato
     */
    private void loadRealContent() {
        if (exercise != null && exercise instanceof CompleteCode) {
            VBox contentBox = createCompleteCodeContent((CompleteCode) exercise);
            setCenter(contentBox);

            // Aggiorna anche l'header
            updateTitle(exercise.getTitle() + " - Livello " + exercise.getDifficulty());
            updateDescription(exercise.getDescription());
        }
    }

    @Override
    protected NavigationBar createNavigationBar() {
        NavigationBar navbar = new NavigationBar(true); // Con pulsante indietro

        // Azione personalizzata per il pulsante indietro con conferma
        navbar.setBackAction(() -> {
            showExitConfirmation(() -> {
                // Salva progressi prima di uscire
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
        // Durante l'inizializzazione BaseScreen, exercise è null
        // Crea placeholder che verrà sostituito da loadRealContent()
        VBox placeholderBox = new VBox();
        placeholderBox.setPadding(new Insets(20));
        setCenter(placeholderBox);
    }

    @Override
    public void onShow() {
        super.onShow();
        // Non fare nulla qui - il contenuto è già caricato da loadRealContent()
    }

    /**
     * Crea il contenuto per l'esercizio Complete Code
     */
    private VBox createCompleteCodeContent(CompleteCode codeExercise) {
        VBox codeContainer = new VBox(15);
        codeContainer.setPadding(new Insets(15));

        // Inizializza componenti UI
        resultLabel = new Label();
        saveMessage = new Label();
        submitAll = new Button("Verifica Codice");
        passToNextLevel = new Button("Prossimo Livello");
        passToNextLevel.setDisable(true); // inizialmente disabilitato

        // Ottieni l'UI del codice dall'esercizio
        VBox codeUI = codeExercise.getCodeUI();

        // Configura i pulsanti
        setupSubmitButton(codeExercise);
        setupNextLevelButton();

        // Layout dei pulsanti
        HBox buttonRow = new HBox(15, submitAll, passToNextLevel);
        buttonRow.setAlignment(Pos.CENTER);

        codeContainer.getChildren().addAll(codeUI, buttonRow, resultLabel, saveMessage);
        return codeContainer;
    }

    /**
     * Configura il pulsante di submit
     */
    private void setupSubmitButton(CompleteCode codeExercise) {
        submitAll.setOnAction(e -> {
            List<Boolean> results = codeExercise.evaluateUserInput();
            int correctCount = (int) results.stream().filter(b -> b).count();
            int totalQuestions = results.size();
            int difficulty = exercise.getDifficulty();

            if (correctCount == totalQuestions) {
                resultLabel.setText("✔ Tutti i frammenti sono corretti!");
                resultLabel.setTextFill(Color.GREEN);
                passToNextLevel.setDisable(false);
            } else {
                resultLabel.setText("✘ Alcuni frammenti contengono errori.");
                resultLabel.setTextFill(Color.RED);
            }

            correctCountMap.put(difficulty, correctCount);

            // Salvataggio dei progressi
            String currentUser = Main.getCurrentUser();
            String type = "CompleteCode";
            boolean saved = UserProgress.saveProgress(currentUser, type, difficulty, correctCount, totalQuestions);

            if (saved) {
                saveMessage.setText("I tuoi progressi sono stati salvati correttamente.");
                saveMessage.setTextFill(Color.GREEN);
            } else {
                saveMessage.setText("Errore durante il salvataggio dei progressi.");
                saveMessage.setTextFill(Color.RED);
            }
        });
    }

    /**
     * Configura il pulsante per il livello successivo
     */
    private void setupNextLevelButton() {
        passToNextLevel.setOnAction(e -> {
            int nextLevel = exercise.getDifficulty() + 1;
            if (nextLevel <= 3) {
                Exercise nextExercise = new CompleteCode(nextLevel);
                Scene nextScene = ScreenCompleteCode.getScene(stage, returnScene, nextExercise);
                stage.setScene(nextScene);
            } else {
                saveMessage.setText("Hai completato tutti i livelli disponibili.");
                saveMessage.setTextFill(Color.BLUE);
            }
        });
    }

    /**
     * Mostra popup di conferma uscita - CONFORME ALLE SPECIFICHE DEL PROF
     */
    private void showExitConfirmation(Runnable onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Uscita");
        alert.setHeaderText("⚠️ Attenzione: stai per uscire dall'esercizio");
        alert.setContentText("I tuoi progressi verranno salvati automaticamente.\n\n" +
                "Vuoi continuare e uscire dall'esercizio?");

        // Pulsanti personalizzati
        ButtonType confirmButton = new ButtonType("✅ Sì, salva ed esci", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("❌ No, continua esercizio", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                onConfirm.run();
            }
            // Se clicca "No" o chiude, non fa nulla (continua l'esercizio)
        });
    }

    /**
     * Salva i progressi prima di uscire
     */
    private void saveProgressBeforeExit() {
        if (exercise instanceof CompleteCode) {
            CompleteCode codeExercise = (CompleteCode) exercise;
            List<Boolean> results = codeExercise.evaluateUserInput();
            int correctCount = (int) results.stream().filter(b -> b).count();
            int totalQuestions = results.size();
            int difficulty = exercise.getDifficulty();

            correctCountMap.put(difficulty, correctCount);

            String currentUser = Main.getCurrentUser();
            String type = "CompleteCode";
            UserProgress.saveProgress(currentUser, type, difficulty, correctCount, totalQuestions);

            System.out.println("✅ Progresso salvato prima dell'uscita: " + correctCount + "/" + totalQuestions);
        }
    }

    /**
     * Mostra contenuto di errore se l'esercizio non è del tipo corretto
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
            stage.setTitle("PLAY - " + (exercise != null ? exercise.getTitle() : "Completa il Codice"));
        }
    }

    // Metodo statico per compatibilità con il codice esistente
    public static Scene getScene(Stage stage, Scene anteprima, Exercise exercise) {
        ScreenCompleteCode screen = new ScreenCompleteCode(stage, exercise, anteprima);
        return screen.createScene();
    }

    // Metodo per compatibilità con eventuali chiamate esterne
    public int getCorrectCount(int difficulty) {
        return correctCountMap.getOrDefault(difficulty, 0);
    }
}