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
    private List<ToggleGroup> toggleGroups;

    public QuizEP(int difficulty) {
        this.difficulty = difficulty;
        this.title = "Quiz sull'ereditarietà e sul polimorfismo";
        this.questionList = new ArrayList<>();
        this.correctAnswers = new ArrayList<>();
        this.toggleGroups = new ArrayList<>();

        switch (difficulty) {
            case 1:
                this.description = "Quiz livello facile.";
                break;
            case 2:
                this.description = "Quiz livello intermedio.";
                break;
            case 3:
                this.description = "Quiz livello avanzato.";
                break;
            default:
                throw new IllegalArgumentException("Livello di difficoltà non valido");
        }
    }

    public VBox createBeginnerExerciseUI() {
        VBox box = new VBox(20);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.TOP_LEFT);

        box.getChildren().addAll(
            createQuestionBox(
                "Cosa significa ereditarietà in Java?",
                new String[] {
                    "A. Quando una classe può accedere solo ai metodi statici di un'altra classe",
                    "B. Quando una classe può estendere un'interfaccia",
                    "C. Quando una classe può ereditare metodi e proprietà da un'altra classe",
                    "D. Quando una classe è dichiarata come final"
                },
                "C. Quando una classe può ereditare metodi e proprietà da un'altra classe"
            ),
            createQuestionBox(
                "Quale parola chiave si usa per ereditare una classe in Java?",
                new String[] {"A. implement", "B. inherit", "C. extends", "D. import"},
                "C. extends"
            ),
            createQuestionBox(
                "Qual è la superclasse implicita di ogni classe in Java?",
                new String[] {"A. Object", "B. Class", "C. Main", "D. Static"},
                "A. Object"
            )
        );

        return createFinalLayout(box);
    }

    public VBox createIntermediateExerciseUI() {
        VBox box = new VBox(20);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.TOP_LEFT);

        box.getChildren().addAll(
            createQuestionBox(
                "Qual è il risultato della chiamata di un metodo sovrascritto se il tipo di riferimento è della superclasse ma l'oggetto è della sottoclasse?",
                new String[] {
                    "A. Viene eseguito il metodo della superclasse",
                    "B. Viene eseguito il metodo della sottoclasse",
                    "C. Si verifica un errore di compilazione",
                    "D. Viene eseguito entrambi i metodi"
                },
                "B. Viene eseguito il metodo della sottoclasse"
            ),
            createQuestionBox(
                "In Java, cosa succede se una sottoclasse ridefinisce un metodo della superclasse?",
                new String[] {
                    "A. Il metodo originale viene chiamato",
                    "B. Si verifica un errore di compilazione",
                    "C. Viene chiamata la versione ridefinita della sottoclasse",
                    "D. Viene eseguito entrambi i metodi"
                },
                "C. Viene chiamata la versione ridefinita della sottoclasse"
            ),
            createQuestionBox(
                "Quale tipo di metodo può essere sovrascritto in una sottoclasse?",
                new String[] {
                    "A. private",
                    "B. static",
                    "C. final",
                    "D. protected"
                },
                "D. protected"
            )
        );

        return createFinalLayout(box);
    }

    public VBox createAdvancedExerciseUI() {
        VBox box = new VBox(20);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.TOP_LEFT);

        box.getChildren().addAll(
            createQuestionBox(
                "Quale affermazione sul polimorfismo in Java è corretta?",
                new String[] {
                    "A. Il polimorfismo permette a una variabile di riferirsi solo a oggetti della sua stessa classe",
                    "B. Il polimorfismo si applica solo ai metodi static",
                    "C. Il polimorfismo in Java è possibile solo con le interfacce",
                    "D. Il polimorfismo consente a un riferimento di tipo genitore di invocare metodi sovrascritti da una sottoclasse"
                },
                "D. Il polimorfismo consente a un riferimento di tipo genitore di invocare metodi sovrascritti da una sottoclasse"
            ),
            createQuestionBox(
                "Quale metodo viene invocato se una sottoclasse ridefinisce un metodo e lo chiami tramite un riferimento alla superclasse?",
                new String[] {
                    "A. Quello della superclasse",
                    "B. Entrambi",
                    "C. Quello della sottoclasse",
                    "D. Nessuno"
                },
                "C. Quello della sottoclasse"
            ),
            createQuestionBox(
                "Cosa accade se si usa instanceof con un oggetto null?",
                new String[] {
                    "A. Genera un errore",
                    "B. Restituisce true",
                    "C. Restituisce false",
                    "D. Non è possibile usare instanceof con null"
                },
                "C. Restituisce false"
            )
        );

        return createFinalLayout(box);
    }

    private VBox createQuestionBox(String questionText, String[] options, String correct) {
        VBox questionBox = new VBox(5);
        questionBox.setPrefWidth(400);

        Label questionLabel = new Label(questionText);
        questionLabel.setWrapText(true);

        questionList.add(questionText);
        correctAnswers.add(correct);

        ToggleGroup group = new ToggleGroup();
        toggleGroups.add(group);

        questionBox.getChildren().add(questionLabel); // <<--- ADESSO PRIMA la DOMANDA

        for (String option : options) {
            RadioButton rb = new RadioButton(option);
            rb.setWrapText(true);
            rb.setToggleGroup(group);
            questionBox.getChildren().add(rb);
        }

        return questionBox;
    }

    private VBox createFinalLayout(VBox questionsBox) {
        ScrollPane scrollPane = new ScrollPane(questionsBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        Label resultLabel = new Label();
        Button submitAll = new Button("Invia tutte le risposte");
        submitAll.setOnAction(e -> {
            int correctCount = 0;
            for (int i = 0; i < toggleGroups.size(); i++) {
                ToggleGroup group = toggleGroups.get(i);
                RadioButton selected = (RadioButton) group.getSelectedToggle();
                if (selected != null && selected.getText().equals(correctAnswers.get(i))) {
                    correctCount++;
                }
            }
            resultLabel.setText("Hai risposto correttamente a " + correctCount + " su " + correctAnswers.size() + " domande.");
        });

        VBox container = new VBox(10, scrollPane, submitAll, resultLabel);
        container.setPadding(new Insets(10));
        return container;
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
