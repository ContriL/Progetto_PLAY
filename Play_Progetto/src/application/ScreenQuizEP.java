package application;

import application.exercises.Exercise;
import application.exercises.OrderStepsExercise;
import application.exercises.QuizEP;
import javafx.beans.value.ChangeListener;
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
import java.util.ArrayList;
import java.util.List;


public class ScreenQuizEP {


    public static Scene getScene(Stage stage, Scene selectionScene, Exercise exercise) {
         
         BorderPane root = new BorderPane();
         Scene screenQuiz = new Scene(root, 700, 550);

         // Configurazione CSS
        File css = new File("C:/Users/dadas/IdeaProjects/Progetto_PLAY/Play_Progetto/src/application/application.css");
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

        navBar.getChildren().addAll(homeButton, progressButton,backButton,logoutButton);

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

          if(exercise instanceof QuizEP){
            VBox quizBox = ((QuizEP) exercise).getExerciseUI();
            root.setCenter(quizBox);
          }
        return screenQuiz;
    }
    
}
