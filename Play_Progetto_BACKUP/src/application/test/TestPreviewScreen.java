package application.test;

import application.core.NavigationManager;
import application.exercises.QuizEP;
import application.exercises.CompleteCode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe di test per verificare che ExercisePreviewScreen funzioni correttamente
 * prima di eliminare AnteprimaCC e AnteprimaQuiz.
 */
public class TestPreviewScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Inizializza il NavigationManager
        NavigationManager.getInstance().initialize(primaryStage);

        // Test con QuizEP
        testQuizPreview(primaryStage);
    }

    private void testQuizPreview(Stage stage) {
        // Crea un esercizio quiz di test
        QuizEP quiz = new QuizEP(1); // Livello principiante

        // Crea la schermata di anteprima
        Scene previewScene = ExercisePreviewScreen.forQuizDifficulty(stage, quiz);

        stage.setTitle("Test - Preview Quiz");
        stage.setScene(previewScene);
        stage.show();

        System.out.println("✅ Test Quiz Preview - OK");

        // Dopo 3 secondi, testa il Code Preview
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(
                        javafx.util.Duration.seconds(3),
                        e -> testCodePreview(stage)
                )
        );
        timeline.play();
    }

    private void testCodePreview(Stage stage) {
        // Crea un esercizio code di test
        CompleteCode code = new CompleteCode(1); // Livello principiante

        // Crea la schermata di anteprima
        Scene previewScene = ExercisePreviewScreen.forCodeDifficulty(stage, code);

        stage.setTitle("Test - Preview Code");
        stage.setScene(previewScene);

        System.out.println("✅ Test Code Preview - OK");
        System.out.println("🎉 Entrambi i test completati con successo!");
    }

    public static void main(String[] args) {
        System.out.println("🚀 Avvio test per ExercisePreviewScreen...");
        launch(args);
    }
}