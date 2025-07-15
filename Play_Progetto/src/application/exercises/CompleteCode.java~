package application.exercises;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class CompleteCode extends AbstractExercise {
    private List<String> questionList;
    private List<String> Answers;
    private List<TextArea> userInputs;

    private int currentQuestionIndex = 0;
    private List<String> userAnswers; 

    public CompleteCode(int difficulty) {
        this.difficulty = difficulty;
        this.title = "Completamento codice Java";
        this.questionList = new ArrayList<>();
        this.Answers = new ArrayList<>();
        this.userInputs = new ArrayList<>();
        this.userAnswers = new ArrayList<>();

        switch (difficulty) {
            case 1:
                this.description = "Livello principiante: completa frammenti base.";
                break;
            case 2:
                this.description = "Livello intermedio: completa metodi con array/collezioni.";
                break;
            case 3:
                this.description = "Livello avanzato: completa codice con più classi e API.";
                break;
            default:
                throw new IllegalArgumentException("Livello di difficoltà non valido");
        }

        
        initializeQuestions();

       
        for (int i = 0; i < questionList.size(); i++) {
            userAnswers.add("");
        }
    }

    /**
     * Inizializza le domande basandosi sul livello
     */
    private void initializeQuestions() {
        List<CodeSnippet> exercises;
        switch (difficulty) {
            case 1:
                exercises = getBeginnerSnippets();
                break;
            case 2:
                exercises = getIntermediateSnippets();
                break;
            case 3:
                exercises = getAdvancedSnippets();
                break;
            default:
                throw new IllegalArgumentException("Livello di difficoltà non valido");
        }

        for (CodeSnippet snippet : exercises) {
            questionList.add(snippet.code);
            Answers.add(snippet.solution.trim());
        }
    }

    
    public VBox getCurrentQuestionUI() {
        if (currentQuestionIndex >= questionList.size()) {
            return createNoMoreQuestionsUI();
        }

        VBox questionContainer = new VBox(20);
        questionContainer.setPadding(new Insets(20));
        questionContainer.setAlignment(Pos.TOP_LEFT);

        
        Label titleLabel = new Label(String.format("%s (%d/%d)",
                title, currentQuestionIndex + 1, questionList.size()));
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        
        String currentCode = questionList.get(currentQuestionIndex);
        Label codeLabel = new Label(currentCode);
        codeLabel.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 13; -fx-border-color: gray; -fx-border-radius: 5; -fx-padding: 10;");
        codeLabel.setWrapText(true);
        codeLabel.setPrefWidth(600);

        
        VBox inputBox = new VBox(10);
        Label inputLabel = new Label("Inserisci il codice mancante:");

        TextArea inputArea = new TextArea();
        inputArea.setPromptText("Inserisci qui le righe mancanti...");
        inputArea.setPrefRowCount(3);
        inputArea.setPrefWidth(600);

        
        inputArea.setText(userAnswers.get(currentQuestionIndex));

        
        inputArea.textProperty().addListener((obs, oldText, newText) -> {
            userAnswers.set(currentQuestionIndex, newText);
        });

        inputBox.getChildren().addAll(inputLabel, inputArea);
        questionContainer.getChildren().addAll(titleLabel, codeLabel, inputBox);

        return questionContainer;
    }

    
    private VBox createNoMoreQuestionsUI() {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));

        Label message = new Label("Tutte le domande completate!");
        message.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        container.getChildren().add(message);
        return container;
    }

    
    public boolean goToNextQuestion() {
        if (currentQuestionIndex < questionList.size() - 1) {
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
        return currentQuestionIndex < questionList.size() - 1;
    }

    
    public boolean hasPreviousQuestion() {
        return currentQuestionIndex > 0;
    }

    
    public int getCurrentQuestionNumber() {
        return currentQuestionIndex + 1;
    }

    
    public boolean allQuestionsAttempted() {
        for (String answer : userAnswers) {
            if (answer == null || answer.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    
    public int calculateScore() {
        int correctCount = 0;
        for (int i = 0; i < userAnswers.size(); i++) {
            if (checkAnswer(i, userAnswers.get(i))) {
                correctCount++;
            }
        }
        return correctCount;
    }

    
    public List<String> getUserAnswers() {
        return new ArrayList<>(userAnswers);
    }

    
    public void resetQuiz() {
        currentQuestionIndex = 0;
        for (int i = 0; i < userAnswers.size(); i++) {
            userAnswers.set(i, "");
        }
    }

   

    
    public VBox getExerciseUI() {
        VBox container = new VBox(30);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_LEFT);

        List<CodeSnippet> exercises;
        switch (difficulty) {
            case 1:
                exercises = getBeginnerSnippets();
                break;
            case 2:
                exercises = getIntermediateSnippets();
                break;
            case 3:
                exercises = getAdvancedSnippets();
                break;
            default:
                throw new IllegalArgumentException("Livello di difficoltà non valido");
        }

        
        for (int i = 0; i < exercises.size(); i++) {
            CodeSnippet snippet = exercises.get(i);
            VBox block = new VBox(10);
            block.setStyle("-fx-border-color: gray; -fx-border-radius: 5; -fx-padding: 15; -fx-margin: 10;");

            
            Label exerciseTitle = new Label("ESERCIZIO " + (i + 1) + " di " + exercises.size());
            exerciseTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2563eb; -fx-padding: 0 0 10 0;");

            Label codeLabel = new Label(snippet.code);
            codeLabel.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 13; -fx-background-color: #f5f5f5; -fx-padding: 10;");
            codeLabel.setWrapText(true);

            Label instructionLabel = new Label("Inserisci il codice mancante:");
            instructionLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #666; -fx-padding: 5 0;");

            TextArea inputArea = new TextArea();
            inputArea.setPromptText("Scrivi qui il codice per l'esercizio " + (i + 1) + "...");
            inputArea.setPrefRowCount(4);
            inputArea.setPrefWidth(700);
            inputArea.setStyle("-fx-font-family: 'monospace';");

            questionList.add(snippet.code);
            Answers.add(snippet.solution.trim());
            userInputs.add(inputArea);

            block.getChildren().addAll(exerciseTitle, codeLabel, instructionLabel, inputArea);
            container.getChildren().add(block);
        }

        return container;
    }

    public List<TextArea> getUserInputs() {
        
        List<TextArea> inputs = new ArrayList<>();
        for (String answer : userAnswers) {
            TextArea area = new TextArea(answer);
            inputs.add(area);
        }
        return inputs;
    }

    public List<String> getAnswers() {
        return Answers;
    }

    @Override
    public List<String> getQuestions() {
        return questionList;
    }

    @Override
public boolean checkAnswer(int questionIndex, String userAnswer) {
    if (questionIndex < 0 || questionIndex >= Answers.size()) return false;

    String expected = Answers.get(questionIndex);
    String actual = userAnswer;

    String expectedNorm = normalize(expected);
    String actualNorm = normalize(actual);

    
    if (expectedNorm.equals(actualNorm)) {
        return true;
    }

    
    String[] expectedLines = expected.trim().split("\\n+");
    String[] actualLines = actual.trim().split("\\n+");

    if (expectedLines.length != actualLines.length) return false;

    for (int i = 0; i < expectedLines.length; i++) {
        String expectedLine = expectedLines[i].trim().replaceAll("\\s+", " ");
        String actualLine = actualLines[i].trim().replaceAll("\\s+", " ");
        if (!expectedLine.equalsIgnoreCase(actualLine)) {
            return false;
        }
    }

    return true;
}


private String normalize(String s) {
    return s.toLowerCase()
            .replaceAll("\\\\n", "\n")         
            .replaceAll("\\s+", " ")           
            .replaceAll("[;{}]", "")           
            .trim();
}


    @Override
    public int getTotalQuestions() {
        return questionList.size();
    }

    
    public List<ToggleGroup> getToggleGroups() {
        List<ToggleGroup> toggleGroups = new ArrayList<>();
        for (String answer : userAnswers) {
            ToggleGroup group = new ToggleGroup();
            RadioButton fakeButton = new RadioButton(answer);
            fakeButton.setToggleGroup(group);
            group.selectToggle(fakeButton);
            toggleGroups.add(group);
        }
        return toggleGroups;
    }

    
    public List<String> getUserResponses() {
        return getUserAnswers();
    }

    public VBox getCodeUI() {
        return getCurrentQuestionUI();
    }

    public List<Boolean> evaluateUserInput() {
        List<Boolean> results = new ArrayList<>();
        for (int i = 0; i < userAnswers.size(); i++) {
            String userText = userAnswers.get(i);
            boolean correct = checkAnswer(i, userText);
            results.add(correct);
        }
        return results;
    }

    

    private List<CodeSnippet> getBeginnerSnippets() {
        List<CodeSnippet> list = new ArrayList<>();
        list.add(new CodeSnippet("""
            public class Main {
                public static void main(String[] args) {
                    int x = 5;
                    // dichiara una variabile y e assegna il valore 10
                    // stampa la somma tra x e y
                }
            }
            """, "int y = 10;\nSystem.out.println(x + y);"));

        list.add(new CodeSnippet("""
            public class Main {
                public static void main(String[] args) {
                    // crea un ciclo for che stampa i numeri da 1 a 5
                }
            }
            """, "for(int i = 1; i <= 5; i++) {\n    System.out.println(i);\n}"));

        list.add(new CodeSnippet("""
            public class Main {
                public static void main(String[] args) {
                    int a = 7;
                    // verifica se a è maggiore di 5 e stampa "Maggiore"
                }
            }
            """, "if (a > 5) {\n    System.out.println(\"Maggiore\");\n}"));

        return list;
    }

    private List<CodeSnippet> getIntermediateSnippets() {
        List<CodeSnippet> list = new ArrayList<>();
        list.add(new CodeSnippet("""
            public class Main {
                public static int sommaPari(int[] numeri) {
                    int somma = 0;
                    // cicla sull'array e somma solo i numeri pari
                    return somma;
                }
            }
            """, "for (int n : numeri) {\n    if (n % 2 == 0) {\n        somma += n;\n    }\n}"));

        list.add(new CodeSnippet("""
            public class Main {
                public static String unisci(String[] parole) {
                    String risultato = "";
                    // concatena tutte le parole dell'array separate da uno spazio
                    return risultato.trim();
                }
            }
            """, "for (String p : parole) {\n    risultato += p + \" \";\n}"));

        list.add(new CodeSnippet("""
            public class Main {
                public static List<String> filtra(List<String> lista) {
                    List<String> risultato = new ArrayList<>();
                    // aggiungi alla lista solo le stringhe con lunghezza > 3
                    return risultato;
                }
            }
            """, "for (String s : lista) {\n    if (s.length() > 3) {\n        risultato.add(s);\n    }\n}"));

        return list;
    }

    private List<CodeSnippet> getAdvancedSnippets() {
        List<CodeSnippet> list = new ArrayList<>();
        list.add(new CodeSnippet("""
            public class User {
                private String name;

                // completa il costruttore per inizializzare name
                public User(String name) {
                    //
                }
            }
            """, "this.name = name;"));

        list.add(new CodeSnippet("""
            public class UserManager {
                private List<User> users = new ArrayList<>();

                public List<String> getNames() {
                    // restituisci una lista con solo i nomi usando stream
                }
            }
            """, "return users.stream().map(u -> u.getName()).collect(Collectors.toList());"));

        list.add(new CodeSnippet("""
            public class SafeCounter {
                private int count = 0;

                public synchronized void increment() {
                    // incrementa il contatore in modo thread-safe
                }

                public int getValue() {
                    return count;
                }
            }
            """, "count++;"));

        return list;
    }

    
    private static class CodeSnippet {
        String code;
        String solution;

        CodeSnippet(String code, String solution) {
            this.code = code;
            this.solution = solution;
        }
    }
}