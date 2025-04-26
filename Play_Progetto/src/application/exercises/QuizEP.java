
package application.exercises;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class QuizEP extends AbstractExercise {
    private List<String> questionList;
    private List<String> correctAnswers;

    public QuizEP(int difficulty) {
        this.difficulty = difficulty;
        this.title = "Quiz sull'ereditarietà e sul polimorfismo";
        this.questionList = new ArrayList<>();
        this.correctAnswers = new ArrayList<>();

        switch (difficulty) {
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

    public VBox createBeginnerExerciseUI() {
        HBox box = new HBox(20);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.TOP_LEFT);

        VBox q1Box = createQuestionBox(
            "Cosa significa ereditarietà in Java?",
            new String[] {
                "A. Quando una classe può accedere solo ai metodi statici di un'altra classe",
                "B. Quando una classe può estendere un'interfaccia",
                "C. Quando una classe può ereditare metodi e proprietà da un'altra classe",
                "D. Quando una classe è dichiarata come final"
            },
            "C. Quando una classe può ereditare metodi e proprietà da un'altra classe"
        );

        VBox q2Box = createQuestionBox(
            "Quale parola chiave si usa per ereditare una classe in Java?",
            new String[] {"A. implement", "B. inherit", "C. extends", "D. import"},
            "C. extends"
        );

        VBox q3Box = createQuestionBox(
            "Qual è la superclasse implicita di ogni classe in Java?",
            new String[] {"A. Object", "B. Class", "C. Main", "D. Static"},
            "A. Object"
        );

        box.getChildren().addAll(q1Box, q2Box, q3Box);
        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox container = new VBox(scrollPane);
        return container;
    }

    public VBox createIntermediateExerciseUI() {
        HBox box = new HBox(20);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.TOP_LEFT);

        VBox q1Box = createQuestionBox(
            "Cosa stampa il programma: class Animale { void faiVerso() { System.out.println(\"Verso generico\"); } } class Cane extends Animale { void faiVerso() { System.out.println(\"Bau\"); } } public class Main { public static void main(String[] args) { Animale a = new Cane(); a.faiVerso(); }}",
            new String[] {
                "A. Verso generico",
                "B. Bau",
                "C. Errore di compilazione",
                "D. Nulla perchè faiVerso non è static"
            },
            "B. Bau"
        );

        VBox q2Box = createQuestionBox(
            "In Java, cosa succede se una sottoclasse ridefinisce un metodo della superclasse?",
            new String[] {
                "A. Il metodo originale viene chiamato",
                "B. Si verifica un errore di compilazione",
                "C. Viene chiamata la versione ridefinita della sottoclasse",
                "D. Viene eseguito entrambi i metodi"
            },
            "C. Viene chiamata la versione ridefinita della sottoclasse"
        );

        VBox q3Box = createQuestionBox(
            "Quale tipo di metodo può essere sovrascritto in una sottoclasse?",
            new String[] {
                "A. private",
                "B. static",
                "C. final",
                "D. protected"
            },
            "D. protected"
        );

        box.getChildren().addAll(q1Box, q2Box, q3Box);
        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox container = new VBox(scrollPane);
        return container;
    }

    public VBox createAdvancedExerciseUI() {
        HBox box = new HBox(20);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.TOP_LEFT);

        VBox q1Box = createQuestionBox(
            "Quale affermazione sul polimorfismo in Java è corretta?",
            new String[] {
                "A. Il polimorfismo permette a una variabile di riferirsi solo a oggetti della sua stessa classe",
                "B. Il polimorfismo si applica solo ai metodi static",
                "C. Il polimorfismo in Java è possibile solo con le interfacce",
                "D. Il polimorfismo consente a un riferimento di tipo genitore di invocare metodi sovrascritti da una sottoclasse"
            },
            "D. Il polimorfismo consente a un riferimento di tipo genitore di invocare metodi sovrascritti da una sottoclasse"
        );

        VBox q2Box = createQuestionBox(
            "Quale metodo viene invocato se una sottoclasse ridefinisce un metodo e lo chiami tramite un riferimento alla superclasse?",
            new String[] {
                "A. Quello della superclasse",
                "B. Entrambi",
                "C. Quello della sottoclasse",
                "D. Nessuno"
            },
            "C. Quello della sottoclasse"
        );

        VBox q3Box = createQuestionBox(
            "Cosa accade se si usa instanceof con un oggetto null?",
            new String[] {
                "A. Genera un errore",
                "B. Restituisce true",
                "C. Restituisce false",
                "D. Non è possibile usare instanceof con null"
            },
            "C. Restituisce false"
        );

        box.getChildren().addAll(q1Box, q2Box, q3Box);
        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox container = new VBox(scrollPane);
        return container;
    }

    private VBox createQuestionBox(String questionText, String[] options, String correct) {
        VBox questionBox = new VBox(5);
        questionBox.setPrefWidth(400);
        Label questionLabel = new Label(questionText);
        questionLabel.setWrapText(true);
        questionList.add(questionText);
        ToggleGroup group = new ToggleGroup();
        correctAnswers.add(correct);

        List<RadioButton> buttons = new ArrayList<>();
        for (String option : options) {
            RadioButton rb = new RadioButton(option);
            rb.setWrapText(true);
            rb.setToggleGroup(group);
            buttons.add(rb);
        }

        Label resultLabel = new Label();
        Button submit = new Button("Invia risposta");
        int index = correctAnswers.size() - 1;
        submit.setOnAction(e -> {
            RadioButton selected = (RadioButton) group.getSelectedToggle();
            resultLabel.setText(
                selected != null && selected.getText().equals(correctAnswers.get(index))
                    ? "✅ Risposta corretta!"
                    : "❌ Risposta sbagliata."
            );
        });

        questionBox.getChildren().add(questionLabel);
        questionBox.getChildren().addAll(buttons);
        questionBox.getChildren().addAll(submit, resultLabel);

        return questionBox;
    }

    public VBox getExerciseUI() {
        switch (difficulty) {
            case 1: return createBeginnerExerciseUI();
            case 2: return createIntermediateExerciseUI();
            case 3: return createAdvancedExerciseUI();
            default: throw new IllegalArgumentException("Livello di difficoltà non valido");
        }
    }

    @Override
    public List<String> getQuestions() {
        return questionList;
    }

    @Override
    public boolean checkAnswer(int questionIndex, String userAnswer) {
        if (questionIndex < 0 || questionIndex >= correctAnswers.size()) return false;
        return userAnswer.toLowerCase().contains(correctAnswers.get(questionIndex).toLowerCase());
    }

    @Override
    public int getTotalQuestions() {
        return questionList.size();
    }
}
