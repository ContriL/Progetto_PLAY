package application.screens.exercises;

import application.Main;
import application.core.BaseScreen;
import application.core.NavigationManager;
import application.core.StyleManager;
import application.components.NavigationBar;
import application.exercises.CompleteCode;
import application.exercises.Exercise;
import application.exercises.QuizEP;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Schermata unificata per l'anteprima degli esercizi.
 * Sostituisce AnteprimaCC.java e AnteprimaQuiz.java eliminando la duplicazione.
 */
public class ExercisePreviewScreen extends BaseScreen {

    private Exercise exercise;
    private String returnDestination;
    private VBox questionsBox;
    private ScrollPane scrollPane;
    private Button startButton;
    private Button changeDifficultyButton;

    public ExercisePreviewScreen(Stage stage, Exercise exercise, String returnDestination) {
        super(stage, 700, 550);
        this.exercise = exercise;
        this.returnDestination = returnDestination;
    }

    @Override
    protected NavigationBar createNavigationBar() {
        NavigationBar navbar = new NavigationBar(true);
        navbar.setBackAction(() -> {
            NavigationManager navManager = NavigationManager.getInstance();
            switch (returnDestination.toLowerCase()) {
                case "quiz":
                    navManager.goToQuizDifficultySelection();
                    break;
                case "code":
                    navManager.goToCodeDifficultySelection();
                    break;
                case "selection":
                    navManager.goToExerciseSelection();
                    break;
                default:
                    navManager.goToExerciseSelection();
                    break;
            }
        });
        return navbar;
    }

    @Override
    protected String getScreenTitle() {
        return "Anteprima " + exercise.getTitle();
    }

    @Override
    protected String getScreenDescription() {
        return exercise.getDescription();
    }

    @Override
    protected void initializeContent() {
        // Forza la generazione dell'interfaccia per popolare le domande
        prepareExerciseQuestions();

        // Crea il contenuto principale
        VBox centerBox = createMainContent();
        setCenter(centerBox);

        // Crea l'area dei pulsanti
        HBox buttonBox = createButtonArea();
        setBottom(buttonBox);
    }

    private void prepareExerciseQuestions() {
        if (exercise instanceof QuizEP) {
            ((QuizEP) exercise).getExerciseUI();
        } else if (exercise instanceof CompleteCode) {
            ((CompleteCode) exercise).getExerciseUI();
        }
    }

    private VBox createMainContent() {
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.TOP_CENTER);

        // Area per le domande
        questionsBox = new VBox(20);
        questionsBox.setPadding(new Insets(10));

        populateQuestions();

        // ScrollPane per le domande
        scrollPane = new ScrollPane(questionsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(350);

        centerBox.getChildren().add(scrollPane);

        return centerBox;
    }

    private void populateQuestions() {
        questionsBox.getChildren().clear();

        if (exercise instanceof CompleteCode) {
            populateCodeQuestions();
        } else {
            populateStandardQuestions();
        }
    }

    private void populateCodeQuestions() {
        CompleteCode codeExercise = (CompleteCode) exercise;

        for (String codeSnippet : codeExercise.getQuestions()) {
            VBox block = new VBox();

            Label codeLabel = new Label(codeSnippet);
            codeLabel.setWrapText(true);
            codeLabel.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 13px;");

            block.getChildren().add(codeLabel);
            block.setStyle("-fx-border-color: gray; -fx-border-radius: 5px; -fx-padding: 10px;");

            questionsBox.getChildren().add(block);
        }
    }

    private void populateStandardQuestions() {
        for (String question : exercise.getQuestions()) {
            Label questionLabel = new Label("• " + question);
            questionLabel.setWrapText(true);
            questionLabel.setStyle("-fx-font-size: 14px;");
            questionsBox.getChildren().add(questionLabel);
        }
    }

    private HBox createButtonArea() {
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20));

        // Bottone "Inizia Esercizio"
        startButton = new Button("Inizia Esercizio");
        startButton.setPrefWidth(200);
        startButton.setOnAction(e -> startExercise());

        // Bottone "Cambia Difficoltà"
        changeDifficultyButton = new Button("Cambia Difficoltà");
        changeDifficultyButton.setPrefWidth(200);
        changeDifficultyButton.setOnAction(e -> changeDifficulty());

        buttonBox.getChildren().addAll(startButton, changeDifficultyButton);

        return buttonBox;
    }

    private void startExercise() {
        NavigationManager navManager = NavigationManager.getInstance();
        navManager.startExercise(exercise, returnDestination);
    }

    private void changeDifficulty() {
        NavigationManager navManager = NavigationManager.getInstance();

        if (exercise instanceof QuizEP) {
            navManager.goToQuizDifficultySelection();
        } else if (exercise instanceof CompleteCode) {
            navManager.goToCodeDifficultySelection();
        } else {
            navManager.goToExerciseSelection();
        }
    }

    @Override
    protected void configureScene(Scene scene) {
        // Applica gli stili specifici per questa schermata
        StyleManager.applyMainStyles(scene);

        // Configura il titolo della finestra
        if (stage != null) {
            stage.setTitle("PLAY - Anteprima " + exercise.getTitle());
        }
    }

    // Metodi statici per compatibilità con il codice esistente
    public static Scene createScene(Stage stage, Exercise exercise, String returnDestination) {
        ExercisePreviewScreen screen = new ExercisePreviewScreen(stage, exercise, returnDestination);
        return screen.createScene();
    }

    // Metodi factory per diversi tipi di ritorno
    public static Scene forQuizDifficulty(Stage stage, Exercise exercise) {
        return createScene(stage, exercise, "quiz");
    }

    public static Scene forCodeDifficulty(Stage stage, Exercise exercise) {
        return createScene(stage, exercise, "code");
    }

    public static Scene forExerciseSelection(Stage stage, Exercise exercise) {
        return createScene(stage, exercise, "selection");
    }

    // Metodo per aggiornare l'esercizio (utile per cambi di difficoltà)
    public void updateExercise(Exercise newExercise) {
        this.exercise = newExercise;
        updateTitle("Anteprima " + exercise.getTitle());
        updateDescription(exercise.getDescription());
        prepareExerciseQuestions();
        populateQuestions();
    }

    @Override
    public void onShow() {
        super.onShow();
        // Rinfresca le domande quando la schermata viene mostrata
        prepareExerciseQuestions();
        populateQuestions();
    }
}