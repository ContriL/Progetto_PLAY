package application.exercises;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 * Quiz su Ereditarietà e Polimorfismo
 * 
 */
public class QuizEP extends AbstractExercise {

    
    private List<QuestionData> questionsList;
    private List<String> userAnswers; 
    private int currentQuestionIndex = 0;

    public QuizEP(int difficulty) {
        this.difficulty = difficulty;
        this.title = "Quiz sull'ereditarietà e sul polimorfismo";
        this.questionsList = new ArrayList<>();
        this.userAnswers = new ArrayList<>();

        switch (difficulty) {
            case 1:
                this.description = "Quiz livello principiante: concetti base di ereditarietà e polimorfismo.";
                initBeginnerQuestions();
                break;
            case 2:
                this.description = "Quiz livello intermedio: applicazioni pratiche di ereditarietà e polimorfismo.";
                initIntermediateQuestions();
                break;
            case 3:
                this.description = "Quiz livello avanzato: concetti complessi e casi particolari.";
                initAdvancedQuestions();
                break;
            default:
                throw new IllegalArgumentException("Livello di difficoltà non valido");
        }

        
        for (int i = 0; i < questionsList.size(); i++) {
            userAnswers.add(null);
        }
    }

    
    public VBox getCurrentQuestionUI() {
        if (currentQuestionIndex >= questionsList.size()) {
            return createNoMoreQuestionsUI();
        }

        VBox questionContainer = new VBox(20);
        questionContainer.setPadding(new Insets(20));
        questionContainer.setAlignment(Pos.TOP_LEFT);

        QuestionData currentQuestion = questionsList.get(currentQuestionIndex);

        
        Label titleLabel = new Label(String.format("%s (%d/%d)",
                title, currentQuestionIndex + 1, questionsList.size()));
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        
        Label questionLabel = new Label(currentQuestion.questionText);
        questionLabel.setWrapText(true);
        questionLabel.setStyle("-fx-font-size: 14px; -fx-padding: 0 0 15 0;");
        questionLabel.setPrefWidth(600);

        ToggleGroup answerGroup = new ToggleGroup();
        VBox optionsBox = new VBox(10);

        for (String option : currentQuestion.options) {
            RadioButton radioButton = new RadioButton(option);
            radioButton.setToggleGroup(answerGroup);
            radioButton.setWrapText(true);
            radioButton.setPrefWidth(550);

            if (userAnswers.get(currentQuestionIndex) != null &&
                    userAnswers.get(currentQuestionIndex).equals(option)) {
                radioButton.setSelected(true);
            }

            optionsBox.getChildren().add(radioButton);
        }

        
        answerGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton selected = (RadioButton) newToggle;
                userAnswers.set(currentQuestionIndex, selected.getText());
            }
        });

        questionContainer.getChildren().addAll(titleLabel, questionLabel, optionsBox);
        return questionContainer;
    }

    
    private VBox createNoMoreQuestionsUI() {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));

        Label message = new Label("Quiz completato!");
        message.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        container.getChildren().add(message);
        return container;
    }

    
    public boolean goToNextQuestion() {
        if (currentQuestionIndex < questionsList.size() - 1) {
            currentQuestionIndex++;
            return true;
        }
        return false; 
    }

    
    public boolean goToPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            return true;
        }
        return false; 
    }

    
    public boolean hasNextQuestion() {
        return currentQuestionIndex < questionsList.size() - 1;
    }

    
    public boolean hasPreviousQuestion() {
        return currentQuestionIndex > 0;
    }

    
    public int getCurrentQuestionNumber() {
        return currentQuestionIndex + 1;
    }

    
    public boolean allQuestionsAnswered() {
        for (String answer : userAnswers) {
            if (answer == null || answer.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    
    public int calculateScore() {
        int correctCount = 0;
        for (int i = 0; i < questionsList.size(); i++) {
            if (userAnswers.get(i) != null &&
                    userAnswers.get(i).equals(questionsList.get(i).correctAnswer)) {
                correctCount++;
            }
        }
        return correctCount;
    }

   

    private void initBeginnerQuestions() {
        questionsList.add(new QuestionData(
                "Cosa significa ereditarietà in Java?",
                new String[]{
                        "A. Quando una classe può accedere solo ai metodi statici di un'altra classe",
                        "B. Quando una classe può estendere un'interfaccia",
                        "C. Quando una classe può ereditare metodi e proprietà da un'altra classe",
                        "D. Quando una classe è dichiarata come final"
                },
                "C. Quando una classe può ereditare metodi e proprietà da un'altra classe"
        ));

        questionsList.add(new QuestionData(
                "Quale parola chiave si usa per ereditare una classe in Java?",
                new String[]{
                        "A. implement",
                        "B. inherit",
                        "C. extends",
                        "D. import"
                },
                "C. extends"
        ));

        questionsList.add(new QuestionData(
                "Qual è la superclasse implicita di ogni classe in Java?",
                new String[]{
                        "A. Object",
                        "B. Class",
                        "C. Main",
                        "D. Static"
                },
                "A. Object"
        ));
    }

    private void initIntermediateQuestions() {
        questionsList.add(new QuestionData(
                "Qual è il risultato della chiamata di un metodo sovrascritto se il tipo di riferimento è della superclasse ma l'oggetto è della sottoclasse?",
                new String[]{
                        "A. Viene eseguito il metodo della superclasse",
                        "B. Viene eseguito il metodo della sottoclasse",
                        "C. Si verifica un errore di compilazione",
                        "D. Vengono eseguiti entrambi i metodi"
                },
                "B. Viene eseguito il metodo della sottoclasse"
        ));

        questionsList.add(new QuestionData(
                "In Java, cosa succede se una sottoclasse ridefinisce un metodo della superclasse?",
                new String[]{
                        "A. Il metodo originale viene chiamato",
                        "B. Si verifica un errore di compilazione",
                        "C. Viene chiamata la versione ridefinita della sottoclasse",
                        "D. Vengono eseguiti entrambi i metodi"
                },
                "C. Viene chiamata la versione ridefinita della sottoclasse"
        ));

        questionsList.add(new QuestionData(
                "Quale tipo di metodo può essere sovrascritto in una sottoclasse?",
                new String[]{
                        "A. private",
                        "B. static",
                        "C. final",
                        "D. protected"
                },
                "D. protected"
        ));
    }

    private void initAdvancedQuestions() {
        questionsList.add(new QuestionData(
                "Quale affermazione sul polimorfismo in Java è corretta?",
                new String[]{
                        "A. Il polimorfismo permette a una variabile di riferirsi solo a oggetti della sua stessa classe",
                        "B. Il polimorfismo si applica solo ai metodi static",
                        "C. Il polimorfismo in Java è possibile solo con le interfacce",
                        "D. Il polimorfismo consente a un riferimento di tipo genitore di invocare metodi sovrascritti da una sottoclasse"
                },
                "D. Il polimorfismo consente a un riferimento di tipo genitore di invocare metodi sovrascritti da una sottoclasse"
        ));

        questionsList.add(new QuestionData(
                "Quale metodo viene invocato se una sottoclasse ridefinisce un metodo e lo chiami tramite un riferimento alla superclasse?",
                new String[]{
                        "A. Quello della superclasse",
                        "B. Entrambi",
                        "C. Quello della sottoclasse",
                        "D. Nessuno"
                },
                "C. Quello della sottoclasse"
        ));

        questionsList.add(new QuestionData(
                "Cosa accade se si usa instanceof con un oggetto null?",
                new String[]{
                        "A. Genera un errore",
                        "B. Restituisce true",
                        "C. Restituisce false",
                        "D. Non è possibile usare instanceof con null"
                },
                "C. Restituisce false"
        ));
    }

    

    @Override
    public List<String> getQuestions() {
        
        List<String> questions = new ArrayList<>();
        for (QuestionData q : questionsList) {
            questions.add(q.questionText);
        }
        return questions;
    }

    @Override
    public boolean checkAnswer(int questionIndex, String userAnswer) {
        if (questionIndex < 0 || questionIndex >= questionsList.size()) {
            return false;
        }

        QuestionData question = questionsList.get(questionIndex);
        return userAnswer != null && userAnswer.equals(question.correctAnswer);
    }

    @Override
    public int getTotalQuestions() {
        return questionsList.size();
    }

    
    public List<String> getUserAnswers() {
        return new ArrayList<>(userAnswers);
    }

    
    public List<String> getCorrectAnswers() {
        List<String> correct = new ArrayList<>();
        for (QuestionData q : questionsList) {
            correct.add(q.correctAnswer);
        }
        return correct;
    }

    /**
     * @deprecated 
     * 
     */
    @Deprecated
    public VBox getExerciseUI() {
        switch (difficulty) {
            case 1: return createBeginnerExerciseUI();
            case 2: return createIntermediateExerciseUI();
            case 3: return createAdvancedExerciseUI();
            default: throw new IllegalArgumentException("Livello di difficoltà non valido");
        }
    }

    /**
     * @deprecated 
     */
    @Deprecated
    public VBox createBeginnerExerciseUI() {
        return createAllQuestionsUI();
    }

    /**
     * @deprecated 
     */
    @Deprecated
    public VBox createIntermediateExerciseUI() {
        return createAllQuestionsUI();
    }

    /**
     * @deprecated 
     */
    @Deprecated
    public VBox createAdvancedExerciseUI() {
        return createAllQuestionsUI();
    }

    private VBox createAllQuestionsUI() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_LEFT);

        for (int i = 0; i < questionsList.size(); i++) {
            QuestionData question = questionsList.get(i);

            VBox questionBox = createQuestionBox(question, i);
            container.getChildren().add(questionBox);
        }

        return container;
    }

    
    private VBox createQuestionBox(QuestionData question, int questionIndex) {
        VBox questionBox = new VBox(10);
        questionBox.setPrefWidth(400);

        Label questionLabel = new Label(question.questionText);
        questionLabel.setWrapText(true);
        questionLabel.setStyle("-fx-font-weight: bold;");

        ToggleGroup group = new ToggleGroup();
        VBox optionsBox = new VBox(5);

        for (String option : question.options) {
            RadioButton rb = new RadioButton(option);
            rb.setWrapText(true);
            rb.setToggleGroup(group);

            
            if (userAnswers.get(questionIndex) != null &&
                    userAnswers.get(questionIndex).equals(option)) {
                rb.setSelected(true);
            }

            
            rb.setOnAction(e -> {
                if (rb.isSelected()) {
                    userAnswers.set(questionIndex, option);
                }
            });

            optionsBox.getChildren().add(rb);
        }

        questionBox.getChildren().addAll(questionLabel, optionsBox);
        return questionBox;
    }

    
    public List<ToggleGroup> getToggleGroups() {
        
        List<ToggleGroup> groups = new ArrayList<>();

        
        for (int i = 0; i < questionsList.size(); i++) {
            groups.add(new ToggleGroup());
        }

        return groups;
    }

    
    public List<String> getAnswers() {
        return getCorrectAnswers();
    }

    
    public void resetQuiz() {
        currentQuestionIndex = 0;
        for (int i = 0; i < userAnswers.size(); i++) {
            userAnswers.set(i, null);
        }
    }

    

    private static class QuestionData {
        public final String questionText;
        public final String[] options;
        public final String correctAnswer;

        public QuestionData(String questionText, String[] options, String correctAnswer) {
            this.questionText = questionText;
            this.options = options.clone();
            this.correctAnswer = correctAnswer;
        }
    }
}