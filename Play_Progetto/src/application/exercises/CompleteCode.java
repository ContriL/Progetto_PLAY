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

    public CompleteCode(int difficulty) {
        this.difficulty = difficulty;
        this.title = "Completamento codice Java";
        this.questionList = new ArrayList<>();
        this.Answers = new ArrayList<>();
        this.userInputs = new ArrayList<>();

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

        for (CodeSnippet snippet : exercises) {
            VBox block = new VBox(10);
            block.setStyle("-fx-border-color: gray; -fx-border-radius: 5; -fx-padding: 10;");
            Label codeLabel = new Label(snippet.code);
            codeLabel.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 13;");
            codeLabel.setWrapText(true);

            TextArea inputArea = new TextArea();
            inputArea.setPromptText("Inserisci qui le righe mancanti...");
            inputArea.setPrefRowCount(3);
            inputArea.setPrefWidth(700);

            questionList.add(snippet.code);
            Answers.add(snippet.solution.trim());
            userInputs.add(inputArea);

            block.getChildren().addAll(codeLabel, inputArea);
            container.getChildren().add(block);
        }

        return container;
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

    public List<TextArea> getUserInputs() {
        return userInputs;
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
        String expected = Answers.get(questionIndex).replaceAll("\\s+", "");
        String actual = userAnswer.replaceAll("\\s+", "");
        return actual.contains(expected);
    }

    @Override
    public int getTotalQuestions() {
        return 3;
    }

    /**
     * Metodo per compatibilità con ScreenComleteCode.
     * Ogni TextArea viene associata ad un ToggleGroup per rispettare la stessa struttura.
     */
    public List<ToggleGroup> getToggleGroups() {
        List<ToggleGroup> toggleGroups = new ArrayList<>();
        for (TextArea input : userInputs) {
            ToggleGroup group = new ToggleGroup();
            RadioButton fakeButton = new RadioButton(input.getText());
            fakeButton.setToggleGroup(group);
            group.selectToggle(fakeButton); // Seleziona sempre la risposta scritta
            toggleGroups.add(group);
        }
        return toggleGroups;
    }

    /**
     * Restituisce le risposte utente come lista di stringhe
     */
    public List<String> getUserResponses() {
        return userInputs.stream().map(TextArea::getText).collect(Collectors.toList());
    }

    public VBox getCodeUI() {
        return getExerciseUI();
    }

    public List<Boolean> evaluateUserInput() {
        List<Boolean> results = new ArrayList<>();
        for (int i = 0; i < userInputs.size(); i++) {
            String userText = userInputs.get(i).getText();
            boolean correct = checkAnswer(i, userText);
            results.add(correct);
        }
        return results;
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
