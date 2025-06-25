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

    // NUOVI CAMPI per navigazione una domanda alla volta
    private int currentQuestionIndex = 0;
    private List<String> userAnswers; // Salva le risposte utente per ogni domanda

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

        // Inizializza le domande
        initializeQuestions();

        // Inizializza array risposte utente
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

    /**
     * NUOVO: Ottiene l'UI per la domanda corrente (una alla volta)
     */
    public VBox getCurrentQuestionUI() {
        if (currentQuestionIndex >= questionList.size()) {
            return createNoMoreQuestionsUI();
        }

        VBox questionContainer = new VBox(20);
        questionContainer.setPadding(new Insets(20));
        questionContainer.setAlignment(Pos.TOP_LEFT);

        // Titolo con contatore
        Label titleLabel = new Label(String.format("%s (%d/%d)",
                title, currentQuestionIndex + 1, questionList.size()));
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Codice da completare
        String currentCode = questionList.get(currentQuestionIndex);
        Label codeLabel = new Label(currentCode);
        codeLabel.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 13; -fx-border-color: gray; -fx-border-radius: 5; -fx-padding: 10;");
        codeLabel.setWrapText(true);
        codeLabel.setPrefWidth(600);

        // Area input per la risposta
        VBox inputBox = new VBox(10);
        Label inputLabel = new Label("Inserisci il codice mancante:");

        TextArea inputArea = new TextArea();
        inputArea.setPromptText("Inserisci qui le righe mancanti...");
        inputArea.setPrefRowCount(3);
        inputArea.setPrefWidth(600);

        // Ripristina la risposta precedente se esiste
        inputArea.setText(userAnswers.get(currentQuestionIndex));

        // Salva la risposta quando l'utente scrive
        inputArea.textProperty().addListener((obs, oldText, newText) -> {
            userAnswers.set(currentQuestionIndex, newText);
        });

        inputBox.getChildren().addAll(inputLabel, inputArea);
        questionContainer.getChildren().addAll(titleLabel, codeLabel, inputBox);

        return questionContainer;
    }

    /**
     * UI quando non ci sono più domande
     */
    private VBox createNoMoreQuestionsUI() {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));

        Label message = new Label("Tutte le domande completate!");
        message.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        container.getChildren().add(message);
        return container;
    }

    /**
     * NUOVO: Naviga alla domanda successiva
     */
    public boolean goToNextQuestion() {
        if (currentQuestionIndex < questionList.size() - 1) {
            currentQuestionIndex++;
            return true;
        }
        return false;
    }

    /**
     * NUOVO: Naviga alla domanda precedente
     */
    public boolean goToPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            return true;
        }
        return false;
    }

    /**
     * NUOVO: Verifica se ci sono altre domande
     */
    public boolean hasNextQuestion() {
        return currentQuestionIndex < questionList.size() - 1;
    }

    /**
     * NUOVO: Verifica se ci sono domande precedenti
     */
    public boolean hasPreviousQuestion() {
        return currentQuestionIndex > 0;
    }

    /**
     * NUOVO: Ottiene il numero della domanda corrente (1-based)
     */
    public int getCurrentQuestionNumber() {
        return currentQuestionIndex + 1;
    }

    /**
     * NUOVO: Verifica se tutte le domande sono state tentate
     */
    public boolean allQuestionsAttempted() {
        for (String answer : userAnswers) {
            if (answer == null || answer.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * NUOVO: Calcola il punteggio finale
     */
    public int calculateScore() {
        int correctCount = 0;
        for (int i = 0; i < userAnswers.size(); i++) {
            if (checkAnswer(i, userAnswers.get(i))) {
                correctCount++;
            }
        }
        return correctCount;
    }

    /**
     * NUOVO: Ottiene le risposte dell'utente
     */
    public List<String> getUserAnswers() {
        return new ArrayList<>(userAnswers);
    }

    /**
     * NUOVO: Reset del quiz per ricominciare
     */
    public void resetQuiz() {
        currentQuestionIndex = 0;
        for (int i = 0; i < userAnswers.size(); i++) {
            userAnswers.set(i, "");
        }
    }

    // === METODI ORIGINALI MANTENUTI PER COMPATIBILITÀ ===

    // Modifica il metodo getExerciseUI() per mostrare TUTTI gli esercizi:

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

        // MOSTRA TUTTI GLI ESERCIZI, NON SOLO IL PRIMO
        for (int i = 0; i < exercises.size(); i++) {
            CodeSnippet snippet = exercises.get(i);
            VBox block = new VBox(10);
            block.setStyle("-fx-border-color: gray; -fx-border-radius: 5; -fx-padding: 15; -fx-margin: 10;");

            // TITOLO CHIARO
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
        // Convertire le risposte salvate in TextArea per compatibilità
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

        // DEBUG: Stampa i valori per vedere cosa sta succedendo
        System.out.println("=== DEBUG COMPLETE CODE ===");
        System.out.println("Question Index: " + questionIndex);
        System.out.println("Expected RAW: '" + expected + "'");
        System.out.println("Actual RAW: '" + actual + "'");

        // Normalizza entrambe le stringhe
        String expectedNorm = expected.toLowerCase()
                .trim()
                .replaceAll("\\\\n", "\n")  // Converte \n letterali
                .replaceAll("\\s+", " ");   // Normalizza spazi

        String actualNorm = actual.toLowerCase()
                .trim()
                .replaceAll("\\s+", " ");      // Normalizza spazi

        System.out.println("Expected NORM: '" + expectedNorm + "'");
        System.out.println("Actual NORM: '" + actualNorm + "'");

        // Prova diversi tipi di controlli
        boolean containsCheck = actualNorm.contains(expectedNorm);
        boolean equalsCheck = actualNorm.equals(expectedNorm);

        System.out.println("Contains check: " + containsCheck);
        System.out.println("Equals check: " + equalsCheck);

        // Verifica parola per parola
        String[] expectedWords = expectedNorm.split("\\s+");
        boolean allWordsPresent = true;

        System.out.println("Expected words: " + java.util.Arrays.toString(expectedWords));

        for (String word : expectedWords) {
            if (!word.isEmpty() && !actualNorm.contains(word)) {
                System.out.println("Missing word: '" + word + "'");
                allWordsPresent = false;
            }
        }

        System.out.println("All words present: " + allWordsPresent);
        System.out.println("=== END DEBUG ===");

        // Usa il controllo più permissivo
        return containsCheck || equalsCheck || allWordsPresent;
    }

    @Override
    public int getTotalQuestions() {
        return questionList.size();
    }

    /**
     * Metodo per compatibilità con ScreenCompleteCode.
     */
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

    /**
     * Restituisce le risposte utente come lista di stringhe
     */
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

    // === METODI PRIVATI PER CREARE LE DOMANDE ===

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

    // Classe interna per rappresentare uno snippet
    private static class CodeSnippet {
        String code;
        String solution;

        CodeSnippet(String code, String solution) {
            this.code = code;
            this.solution = solution;
        }
    }
}