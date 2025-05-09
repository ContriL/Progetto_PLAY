package application;

import application.exercises.Exercise;
import application.exercises.QuizEP;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenQuizEP {

    private static final Map<Integer, Integer> correctCountMap = new HashMap<>();

    public static Scene getScene(Stage stage, Scene anteprima, Exercise exercise) {

        BorderPane root = new BorderPane();
        Scene screenQuiz = new Scene(root, 700, 550);

        // CSS
        File css = new File("/Users/lorenzocontri/Desktop/Progetto_Programmazione/Progetto_PLAY/Play_Progetto/src/application/application.css");
        screenQuiz.getStylesheets().add("file://" + css.getAbsolutePath());

        // Barra superiore
        HBox navBar = new HBox(15);
        navBar.setPadding(new Insets(10));
        navBar.setAlignment(Pos.CENTER_RIGHT);
        navBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        Button homeButton = new Button("Home");
        Button progressButton = new Button("I miei Progressi");
        Button logoutButton = new Button("Logout");
        Button backButton = new Button("Indietro");

        // AGGIUNTA DEI BOTTONI ALLA NAVBAR
        navBar.getChildren().addAll(homeButton, progressButton, logoutButton, backButton);

        VBox topContainer = new VBox();
        topContainer.getChildren().add(navBar);

        Text headerText = new Text(exercise.getTitle() + " - Livello " + exercise.getDifficulty());
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Text descriptionText = new Text(exercise.getDescription());

        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15));
        headerBox.getChildren().addAll(headerText, descriptionText);

        topContainer.getChildren().add(headerBox);
        root.setTop(topContainer);

        Label resultLabel = new Label();
        Label saveMessage = new Label();
        Button submitAll = new Button("Invia tutte le risposte");
        Button passToNextLevel = new Button("Prossimo Livello");
        passToNextLevel.setDisable(true); // inizialmente disabilitato

        final int[] localCorrectCount = {0};

        VBox quizContainer = new VBox(15);
        quizContainer.setPadding(new Insets(15));

        if (exercise instanceof QuizEP) {
            QuizEP quiz = (QuizEP) exercise;
            VBox quizBox = quiz.getExerciseUI();

            submitAll.setOnAction(e -> {
                submitAll.setDisable(true);
                List<ToggleGroup> groups = quiz.getToggleGroups();
                List<String> answers = quiz.getAnswers();

                localCorrectCount[0] = 0;
                for (int i = 0; i < groups.size(); i++) {
                    ToggleGroup group = groups.get(i);
                    RadioButton selected = (RadioButton) group.getSelectedToggle();
                    if (selected != null && selected.getText().equals(answers.get(i))) {
                        localCorrectCount[0]++;
                    }
                }

                int difficulty = exercise.getDifficulty();
                correctCountMap.put(difficulty, localCorrectCount[0]);

                resultLabel.setText("Hai risposto correttamente a " + localCorrectCount[0] + " su " + answers.size() + " domande.");

                String currenUser = Main.getCurrentUser();
                String type = "quizEP";
                int totalQuestion = exercise.getTotalQuestions();
                boolean saved = UserProgress.saveProgress(currenUser, type, difficulty, localCorrectCount[0], totalQuestion);
                if (saved) {
                    saveMessage.setText("I tuoi progressi sono stati salvati correttamente");
                    saveMessage.setTextFill(Color.GREEN);
                } else {
                    saveMessage.setText("Errore durante il salvataggio: progressi non salvati");
                    saveMessage.setTextFill(Color.RED);
                }

                if (localCorrectCount[0] >= 2 && difficulty < 3) {
                    passToNextLevel.setDisable(false);
                }
            });

            passToNextLevel.setOnAction(e -> {
                int nextLevel = exercise.getDifficulty() + 1;
                if (nextLevel <= 3) {
                    Exercise nextExercise = new QuizEP(nextLevel);
                    Scene nextScene = ScreenQuizEP.getScene(stage, anteprima, nextExercise);
                    stage.setScene(nextScene);
                } else {
                    saveMessage.setText("Hai completato tutti i livelli disponibili.");
                    saveMessage.setTextFill(Color.BLUE);
                }
            });

            HBox buttonRow = new HBox(15, submitAll, passToNextLevel);
            buttonRow.setAlignment(Pos.CENTER);

            quizContainer.getChildren().addAll(quizBox, buttonRow, resultLabel, saveMessage);
            root.setCenter(quizContainer);

            // Bottone Indietro
            backButton.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Conferma uscita");
                alert.setHeaderText("Attenzione: stai tornando indietro.");
                alert.setContentText("Le risposte mancanti verranno considerate sbagliate. Vuoi salvare e tornare indietro?");
                ButtonType yesButton = new ButtonType("Sì", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(yesButton, noButton);

                alert.showAndWait().ifPresent(response -> {
                    if (response == yesButton) {
                        List<ToggleGroup> groups = quiz.getToggleGroups();
                        List<String> answers = quiz.getAnswers();

                        localCorrectCount[0] = 0;
                        for (int i = 0; i < groups.size(); i++) {
                            ToggleGroup group = groups.get(i);
                            RadioButton selected = (RadioButton) group.getSelectedToggle();
                            if (selected != null && selected.getText().equals(answers.get(i))) {
                                localCorrectCount[0]++;
                            }
                        }

                        int difficulty = exercise.getDifficulty();
                        correctCountMap.put(difficulty, localCorrectCount[0]);

                        String currentUser = Main.getCurrentUser();
                        String type = "quizEP";
                        int totalQuestions = exercise.getTotalQuestions();
                        UserProgress.saveProgress(currentUser, type, difficulty, localCorrectCount[0], totalQuestions);

                        stage.setScene(DiffcultyQuizSelection.getScene(stage, anteprima));
                    }
                });
            });

            // Bottone Home
            homeButton.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Conferma uscita");
                alert.setHeaderText("Attenzione: stai tornando alla schermata iniziale.");
                alert.setContentText("Le risposte mancanti verranno considerate sbagliate. Vuoi salvare e tornare indietro?");
                ButtonType yesButton = new ButtonType("Sì", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(yesButton, noButton);

                alert.showAndWait().ifPresent(response -> {
                    if (response == yesButton) {
                        List<ToggleGroup> groups = quiz.getToggleGroups();
                        List<String> answers = quiz.getAnswers();

                        localCorrectCount[0] = 0;
                        for (int i = 0; i < groups.size(); i++) {
                            ToggleGroup group = groups.get(i);
                            RadioButton selected = (RadioButton) group.getSelectedToggle();
                            if (selected != null && selected.getText().equals(answers.get(i))) {
                                localCorrectCount[0]++;
                            }
                        }

                        int difficulty = exercise.getDifficulty();
                        correctCountMap.put(difficulty, localCorrectCount[0]);

                        String currentUser = Main.getCurrentUser();
                        String type = "quizEP";
                        int totalQuestions = exercise.getTotalQuestions();
                        UserProgress.saveProgress(currentUser, type, difficulty, localCorrectCount[0], totalQuestions);

                        stage.setScene(DiffcultyQuizSelection.getScene(stage, anteprima));
                    }
                });
            });

            // Bottone Logout
            logoutButton.setOnAction(e -> {
                Main.setCurrentUser("");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Conferma uscita");
                alert.setHeaderText("Attenzione: stai eseguendo un logout.");
                alert.setContentText("Le risposte mancanti verranno considerate sbagliate. Vuoi salvare e tornare indietro?");
                ButtonType yesButton = new ButtonType("Sì", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(yesButton, noButton);

                alert.showAndWait().ifPresent(response -> {
                    if (response == yesButton) {
                        List<ToggleGroup> groups = quiz.getToggleGroups();
                        List<String> answers = quiz.getAnswers();

                        localCorrectCount[0] = 0;
                        for (int i = 0; i < groups.size(); i++) {
                            ToggleGroup group = groups.get(i);
                            RadioButton selected = (RadioButton) group.getSelectedToggle();
                            if (selected != null && selected.getText().equals(answers.get(i))) {
                                localCorrectCount[0]++;
                            }
                        }

                        int difficulty = exercise.getDifficulty();
                        correctCountMap.put(difficulty, localCorrectCount[0]);

                        String currentUser = Main.getCurrentUser();
                        String type = "quizEP";
                        int totalQuestions = exercise.getTotalQuestions();
                        UserProgress.saveProgress(currentUser, type, difficulty, localCorrectCount[0], totalQuestions);

                        stage.setScene(Home.getScene(stage, anteprima));
                    }
                });
            });
        }

        // Bottone "I miei progressi"
        progressButton.setOnAction(e -> stage.setScene(UserProgressScreen.getScene(stage, anteprima)));

        return screenQuiz;
    }

    public int getCorrectCount(int difficulty) {
        return correctCountMap.getOrDefault(difficulty, 0);
    }
}
