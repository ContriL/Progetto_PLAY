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
import java.util.List;

public class ScreenQuizEP {

    private static int correctCount = 0;

    public static Scene getScene(Stage stage, Scene selectionScene, Exercise exercise) {
         
         BorderPane root = new BorderPane();
         Scene screenQuiz = new Scene(root, 700, 550);

         // Configurazione CSS
        //File css = new File("C:/Users/dadas/IdeaProjects/Progetto_PLAY/Play_Progetto/src/application/application.css");
        File css = new File("/Users/lorenzocontri/Desktop/Progetto_Programmazione/Progetto_PLAY/Play_Progetto/src/application/application.css");
        screenQuiz.getStylesheets().add("file://" + css.getAbsolutePath());
        

        // Barra di navigazione superiore
        HBox navBar = new HBox(15);
        navBar.setPadding(new Insets(10));
        navBar.setAlignment(Pos.CENTER_RIGHT);
        navBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        // Pulsanti di navigazione
        Button homeButton = new Button("Home");
        Button progressButton = new Button("I miei Progressi");
        Button logoutButton = new Button("Logout");
        Button backButton = new Button("indietro");

        // Gestione eventi dei pulsanti di navigazione
        homeButton.setOnAction(e -> {
            Scene homeScene = Home.getScene(stage, selectionScene);
            stage.setScene(homeScene);
        });

        progressButton.setOnAction(e -> {
            Scene progressScene = UserProgressScreen.getScene(stage, selectionScene);
            stage.setScene(progressScene);
        });

        logoutButton.setOnAction(e -> {
            Main.setCurrentUser("");
            stage.setScene(selectionScene);
        });

        backButton.setOnAction(e -> {
            Scene previousScene = DiffcultyQuizSelection.getScene(stage, selectionScene);
            stage.setScene(previousScene);
        });

        navBar.getChildren().addAll(homeButton, progressButton, backButton, logoutButton);

        VBox topContainer = new VBox();
        topContainer.getChildren().add(navBar);

        // Intestazione
        Text headerText = new Text(exercise.getTitle() + " - Livello " + exercise.getDifficulty());
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Text descriptionText = new Text(exercise.getDescription());

        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15));
        headerBox.getChildren().addAll(headerText, descriptionText);

        topContainer.getChildren().add(headerBox);
        root.setTop(topContainer);

        if (exercise instanceof QuizEP) {
            QuizEP quiz = (QuizEP) exercise;
            VBox quizBox = quiz.getExerciseUI();

            Label resultLabel = new Label();
            Button submitAll = new Button("Invia tutte le risposte");
            Label saveMessage = new Label();
            submitAll.setOnAction(e -> {
                //int correctCount = 0;
                List<ToggleGroup> groups = quiz.getToggleGroups();
                List<String> answers = quiz.getAnswers();

                for (int i = 0; i < groups.size(); i++) {
                    ToggleGroup group = groups.get(i);
                    RadioButton selected = (RadioButton) group.getSelectedToggle();
                    if (selected != null && selected.getText().equals(answers.get(i))) {
                        correctCount++;
                    }
                }
                resultLabel.setText("Hai risposto correttamente a " + correctCount + " su " + answers.size() + " domande.");
                String currenUser = Main.getCurrentUser();
                String type = "quizEP";
                int diff = exercise.getDifficulty();
                int totalQuestion = exercise.getTotalQuestions();
                boolean saved = UserProgress.saveProgress(currenUser, type, diff, answers.size(), totalQuestion);
                if(saved){
                    saveMessage.setText("I tuoi progressi sono stati salvati corretamente");
                    saveMessage.setTextFill(Color.GREEN);
                } else {
                    saveMessage.setText("Errore durante il salvataggio: progressi non salvati");
                    saveMessage.setTextFill(Color.RED);
                }
            });

            VBox quizContainer = new VBox(15, quizBox, submitAll, resultLabel,saveMessage);
            quizContainer.setPadding(new Insets(15));
            root.setCenter(quizContainer);
        }


        return screenQuiz;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    
}
