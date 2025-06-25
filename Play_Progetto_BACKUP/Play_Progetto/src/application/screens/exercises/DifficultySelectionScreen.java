package application.screens.exercises;

import application.Main;
import application.UserProgress;
import application.core.BaseScreen;
import application.core.NavigationManager;
import application.components.NavigationBar;
import application.exercises.CompleteCode;
import application.exercises.Exercise;
import application.exercises.QuizEP;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Schermata unificata per la selezione della difficoltà degli esercizi.
 * Sostituisce DiffCompleteCode.java e DiffcultyQuizSelection.java.
 */
public class DifficultySelectionScreen extends BaseScreen {

    private String exerciseType; // "quiz" o "code"
    private ToggleGroup difficultyGroup;
    private Label errorLabel;

    public DifficultySelectionScreen(Stage stage, String exerciseType) {
        super(stage, 700, 550);
        this.exerciseType = exerciseType != null ? exerciseType : "quiz"; // Default sicuro
    }

    @Override
    protected NavigationBar createNavigationBar() {
        NavigationBar navbar = new NavigationBar(true);
        navbar.setBackAction("selection");
        return navbar;
    }

    @Override
    protected String getScreenTitle() {
        return "Seleziona un livello di difficoltà";
    }

    @Override
    protected String getScreenDescription() {
        if (exerciseType == null) {
            return "Scegli il livello di difficoltà";
        }
        String typeDescription = exerciseType.equals("quiz") ? "Quiz" : "Completamento Codice";
        return "Scegli il livello di difficoltà per " + typeDescription;
    }

    @Override
    protected void initializeContent() {
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);

        // Griglia per bottoni e barre di progresso
        GridPane selectionGrid = createSelectionGrid();
        contentBox.getChildren().add(selectionGrid);

        setCenter(contentBox);

        // Area inferiore con pulsanti
        VBox bottomBox = createBottomArea();
        setBottom(bottomBox);
    }

    private GridPane createSelectionGrid() {
        GridPane selectionGrid = new GridPane();
        selectionGrid.setHgap(20);
        selectionGrid.setVgap(10);
        selectionGrid.setAlignment(Pos.CENTER);

        difficultyGroup = new ToggleGroup();

        // Bottoni per i livelli
        ToggleButton beginnerBtn = createDifficultyButton("Principiante", 1);
        ToggleButton intermediateBtn = createDifficultyButton("Intermedio", 2);
        ToggleButton advancedBtn = createDifficultyButton("Avanzato", 3);

        // Gestione abilitazione livelli basata sui progressi
        String username = Main.getCurrentUser();
        String progressType = exerciseType.equals("quiz") ? "quizEP" : "CompleteCode";

        intermediateBtn.setDisable(!UserProgress.hasPassedLevel(username, progressType, 1));
        advancedBtn.setDisable(!UserProgress.hasPassedLevel(username, progressType, 2));

        // Barre di progresso
        Region beginnerRegion = createProgressBar(username, progressType, 1);
        Region intermediateRegion = createProgressBar(username, progressType, 2);
        Region advancedRegion = createProgressBar(username, progressType, 3);

        // Aggiunta alla griglia
        selectionGrid.add(beginnerBtn, 0, 0);
        selectionGrid.add(beginnerRegion, 1, 0);

        selectionGrid.add(intermediateBtn, 0, 1);
        selectionGrid.add(intermediateRegion, 1, 1);

        selectionGrid.add(advancedBtn, 0, 2);
        selectionGrid.add(advancedRegion, 1, 2);

        return selectionGrid;
    }

    private ToggleButton createDifficultyButton(String text, int difficulty) {
        ToggleButton button = new ToggleButton(text);
        button.setToggleGroup(difficultyGroup);
        button.setUserData(difficulty);
        button.setPrefSize(200, 40);
        return button;
    }

    private Region createProgressBar(String username, String type, int difficulty) {
        int correctAnswers = UserProgress.getCorrectAnswers(username, type, difficulty);

        Region region = new Region();
        region.setPrefSize(200, 20);
        region.setStyle(getProgressBarStyle(correctAnswers));

        return region;
    }

    private String getProgressBarStyle(int correctAnswers) {
        String baseStyle = "-fx-border-color: black; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10;";

        if (correctAnswers == 0) {
            return "-fx-background-color: red;" + baseStyle;
        } else if (correctAnswers == 1) {
            return "-fx-background-color: linear-gradient(to right, green 33%, red 66%);" + baseStyle;
        } else if (correctAnswers == 2) {
            return "-fx-background-color: linear-gradient(to right, green 66%, red 33%);" + baseStyle;
        } else if (correctAnswers >= 3) {
            return "-fx-background-color: green;" + baseStyle;
        } else {
            return "-fx-background-color: gray;" + baseStyle;
        }
    }

    private VBox createBottomArea() {
        VBox bottomBox = new VBox(10);
        bottomBox.setAlignment(Pos.CENTER);

        errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");

        HBox buttonBar = new HBox(15);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(20));

        Button startButton = new Button("Vai all'anteprima");
        startButton.setOnAction(e -> goToPreview());

        buttonBar.getChildren().add(startButton);
        bottomBox.getChildren().addAll(errorLabel, buttonBar);

        return bottomBox;
    }

    private void goToPreview() {
        if (difficultyGroup.getSelectedToggle() == null) {
            errorLabel.setText("Seleziona un livello di difficoltà!");
            return;
        }

        int difficulty = (int) difficultyGroup.getSelectedToggle().getUserData();
        Exercise selectedExercise;

        if (exerciseType.equals("quiz")) {
            selectedExercise = new QuizEP(difficulty);
        } else {
            selectedExercise = new CompleteCode(difficulty);
        }

        NavigationManager navManager = NavigationManager.getInstance();
        // Salta le regole per ora e vai direttamente all'anteprima
        navManager.showExercisePreview(selectedExercise, exerciseType);
    }


    // Metodi statici per compatibilità con il codice esistente
    public static Scene getSceneForQuiz(Stage stage, Scene returnScene) {
        DifficultySelectionScreen screen = new DifficultySelectionScreen(stage, "quiz");
        return screen.createScene();
    }

    public static Scene getSceneForCode(Stage stage, Scene returnScene) {
        DifficultySelectionScreen screen = new DifficultySelectionScreen(stage, "code");
        return screen.createScene();
    }

    // Per compatibilità con i nomi originali
    public static Scene getScene(Stage stage, Scene returnScene, String type) {
        if (type.equals("quiz")) {
            return getSceneForQuiz(stage, returnScene);
        } else {
            return getSceneForCode(stage, returnScene);
        }
    }
}