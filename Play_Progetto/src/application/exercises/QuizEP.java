package application.exercises;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class QuizEP extends AbstractExercise{
    private List<String> questionList; // Frammenti di codice con errori
    private List<String> correctAnswers; // Le risposte corrette

    public QuizEP(int difficulty) {
        this.difficulty = difficulty;
        this.title = "Quiz sull'ereditarietà e sul polimorfismo";
        this.questionList = new ArrayList<>();
        this.correctAnswers = new ArrayList<>();

        switch(difficulty) {
            case 1:
                this.description = "Quiz livello facile.";
                createBeginnerExerciseUI();
                break;
            case 2:
                this.description = "Quiz livello intermedio.";
                createIntermediateExerciseUI();
                break;
            case 3:
                this.description = "Quiz livello avanzato.";
                createAdvancedExerciseUI();
                break;
            default:
                throw new IllegalArgumentException("Livello di difficoltà non valido");
        }
    }

    public VBox createBeginnerExerciseUI(){
        Label questionLabel = new Label("Cosa significa ereditarietà in Java?");
        questionList.add(questionLabel.getText());
        RadioButton option1 = new RadioButton("A. Quando una classe può accedere solo ai metodi statici di un'altra classe");
        RadioButton option2 = new RadioButton("B. Quando una classe può estendere un'interfaccia");
        RadioButton option3 = new RadioButton("C. Quando una classe può ereditare metodi e proprietà da un'altra classe");
        RadioButton option4 = new RadioButton("D. Quando una classe è dichiarata come final");
        correctAnswers.add(option3.getText());
        

        ToggleGroup group = new ToggleGroup();
        option1.setToggleGroup(group);
        option2.setToggleGroup(group);
        option3.setToggleGroup(group);
        option4.setToggleGroup(group);
        
        Button submitButton = new Button("Invia risposta");
        Label resultLabel = new Label();

        submitButton.setOnAction(e -> {
            RadioButton selected = (RadioButton) group.getSelectedToggle();

            if (selected != null && selected == option3) {
                resultLabel.setText("✅ Risposta corretta!");
            } else {
                resultLabel.setText("❌ Risposta sbagliata.");
            }
        });

        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(questionLabel, option1, option2, option3, option4, submitButton, resultLabel);
        return box;
    }

    public VBox createIntermediateExerciseUI(){
        Label questionLabel = new Label("Cosa stampa il programma: class Animale {\n" + //
                        "    void faiVerso() {\n" + //
                        "        System.out.println(\"Verso generico\");\n" + //
                        "    }\n" + //
                        "}\n" + //
                        "\n" + //
                        "class Cane extends Animale {\n" + //
                        "    void faiVerso() {\n" + //
                        "        System.out.println(\"Bau\");\n" + //
                        "    }\n" + //
                        "}\n" + //
                        "\n" + //
                        "public class Main {\n" + //
                        "    public static void main(String[] args) {\n" + //
                        "        Animale a = new Cane();\n" + //
                        "        a.faiVerso();\n" + //
                        "    }\n" + //
                        "}");
        questionList.add(questionLabel.getText());
        RadioButton option1 = new RadioButton("A.Verso generico");
        RadioButton option2 = new RadioButton("Bau");
        RadioButton option3 = new RadioButton("Errore di compilazione");
        RadioButton option4 = new RadioButton("Nulla perchè faiverso non è static");
        correctAnswers.add(option2.getText());
        

        ToggleGroup group = new ToggleGroup();
        option1.setToggleGroup(group);
        option2.setToggleGroup(group);
        option3.setToggleGroup(group);
        option4.setToggleGroup(group);
        
        Button submitButton = new Button("Invia risposta");
        Label resultLabel = new Label();

        submitButton.setOnAction(e -> {
            RadioButton selected = (RadioButton) group.getSelectedToggle();

            if (selected != null && selected == option2) {
                resultLabel.setText("✅ Risposta corretta!");
            } else {
                resultLabel.setText("❌ Risposta sbagliata.");
            }
        });
        
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(questionLabel, option1, option2, option3, option4, submitButton, resultLabel);
        return box;

        

    }
    
    
    
    public VBox createAdvancedExerciseUI(){
        Label questionLabel = new Label("Quale affermazione sul polimorfismo in Java è corretta?");
        questionList.add(questionLabel.getText());
        RadioButton option1 = new RadioButton("A. Il polimorfismo permette a una variabile di riferirsi solo a oggetti della sua stessa classe");
        RadioButton option2 = new RadioButton("B. Il polimorfismo si applica solo ai metodi static");
        RadioButton option3 = new RadioButton("C. Il polimorfismo in Java è possibile solo con le interfacce");
        RadioButton option4 = new RadioButton("D. Il polimorfismo consente a un riferimento di tipo genitore di invocare metodi sovrascritti da una sottoclasse");
        correctAnswers.add(option4.getText());

        ToggleGroup group = new ToggleGroup();
        option1.setToggleGroup(group);
        option2.setToggleGroup(group);
        option3.setToggleGroup(group);
        option4.setToggleGroup(group);
        
        Button submitButton = new Button("Invia risposta");
        Label resultLabel = new Label();

        submitButton.setOnAction(e -> {
            RadioButton selected = (RadioButton) group.getSelectedToggle();

            if (selected != null && selected == option4) {
                resultLabel.setText("✅ Risposta corretta!");
            } else {
                resultLabel.setText("❌ Risposta sbagliata.");
            }
        });
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(questionLabel, option1, option2, option3, option4, submitButton, resultLabel);
        return box;
    }

    //da creare i testi dei quiz
     public VBox getExerciseUI() {
    switch (difficulty) {
        case 1:
            return createBeginnerExerciseUI();
        case 2:
            return createIntermediateExerciseUI();
        case 3:
            return createAdvancedExerciseUI();
        default:
            throw new IllegalArgumentException("Livello di difficoltà non valido");
    }
}



    @Override
    public List<String> getQuestions() {
        return questionList;
    }

    @Override
    public boolean checkAnswer(int questionIndex, String userAnswer) {
        if (questionIndex < 0 || questionIndex >= correctAnswers.size()) {
            return false;
        }

        // La verifica della risposta potrebbe essere più sofisticata nella versione finale
        return userAnswer.toLowerCase().contains(correctAnswers.get(questionIndex).toLowerCase());
    }

    @Override
    public int getTotalQuestions() {
        return questionList.size();
    }
    
}
