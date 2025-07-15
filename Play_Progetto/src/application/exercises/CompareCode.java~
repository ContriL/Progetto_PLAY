package application.exercises;

import javafx.scene.control.TextArea;
import javafx.geometry.Insets;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;



public class CompareCode extends AbstractExercise {
    private List<CodeComparison> comparisons;
    private final List<UserResponse> userResponses;
    private int currentQuestionIndex = 0;

    public CompareCode(int difficulty) {
        this.difficulty = difficulty;
        this.title = "Confronta il codice";
        this.comparisons = new ArrayList<>();
        this.userResponses = new ArrayList<>();

        switch (difficulty) {
            case 1 ->
                    this.description = "Livello principiante: confronta frammenti base con piccole differenze di stile e bug semplici.";
            case 2 ->
                    this.description = "Livello intermedio: confronta implementazioni con classi e metodi, valutando best practice.";
            case 3 ->
                    this.description = "Livello avanzato: confronta codice complesso con Stream API, lambda e gestione eccezioni.";
            default -> throw new IllegalArgumentException("Livello di difficoltà non valido");
        }

        initializeComparisons();
        
        // Inizializza le risposte utente
        for (int i = 0; i < comparisons.size(); i++) {
            userResponses.add(new UserResponse());
        }
    }

    private void initializeComparisons() {
        switch (difficulty) {
            case 1 -> comparisons = getBeginnerComparisons();
            case 2 -> comparisons = getIntermediateComparisons();
            case 3 -> comparisons = getAdvancedComparisons();
        }
    }

    public VBox getCurrentQuestionUI() {
        if (currentQuestionIndex >= comparisons.size()) {
            return createNoMoreQuestionsUI();
        }

        VBox questionContainer = new VBox(20);
        questionContainer.setPadding(new Insets(20));
        questionContainer.setAlignment(Pos.TOP_LEFT);

        // Titolo
        Label titleLabel = new Label(String.format("%s (%d/%d)",
                title, currentQuestionIndex + 1, comparisons.size()));
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        CodeComparison current = comparisons.get(currentQuestionIndex);
        
        // Descrizione del problema
        Label problemLabel = new Label("PROBLEMA: " + current.problemDescription);
        problemLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2563eb; -fx-padding: 10 0;");
        problemLabel.setWrapText(true);

        // Container per i due codici affiancati
        HBox codesContainer = new HBox(20);
        codesContainer.setAlignment(Pos.TOP_CENTER);
        codesContainer.setPrefWidth(1300); // Usa quasi tutta la larghezza



        // --- Codice A ---
      /*  VBox codeABox = new VBox(10);
        Label codeATitle = new Label("VERSIONE A:");
        codeATitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #dc2626;");

        TextArea codeAText = new TextArea(current.codeA);
        codeAText.setEditable(false);
        codeAText.setWrapText(true);
        codeAText.setStyle(
                "-fx-font-family: 'monospace'; -fx-font-size: 12px;"
                        + " -fx-background-color: #f8f9fa; -fx-border-color: #dc2626;"
                        + " -fx-border-width: 2; -fx-border-radius: 5;"
        );
        codeAText.setPrefWidth(600);
        codeAText.setPrefHeight(400);
        codeAText.setPadding(new Insets(10));

        codeABox.getChildren().addAll(codeATitle, codeAText);

        // --- Codice B ---
        VBox codeBBox = new VBox(10);
        Label codeBTitle = new Label("VERSIONE B:");
        codeBTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #16a34a;");

        TextArea codeBText = new TextArea(current.codeB);
        codeBText.setEditable(false);
        codeBText.setWrapText(true);
        codeBText.setStyle(
                "-fx-font-family: 'monospace'; -fx-font-size: 12px;"
                        + " -fx-background-color: #f8f9fa; -fx-border-color: #16a34a;"
                        + " -fx-border-width: 2; -fx-border-radius: 5;"
        );
        codeBText.setPrefWidth(600);
        codeBText.setPrefHeight(400);
        codeBText.setPadding(new Insets(10));

        codeBBox.getChildren().addAll(codeBTitle, codeBText);*/

        // --- Codice A ---
        VBox codeABox = new VBox(10);
        Label codeATitle = new Label("VERSIONE A:");
        codeATitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #dc2626;");

        TextArea codeAText = new TextArea(current.codeA);
        codeAText.setEditable(false);
        codeAText.setWrapText(false);
        codeAText.setStyle(
                "-fx-font-family: 'Consolas', monospace; -fx-font-size: 14px;"
                        + " -fx-background-color: #f8f9fa; -fx-border-color: #dc2626;"
                        + " -fx-border-width: 2; -fx-border-radius: 5;"
        );
        codeAText.setPrefWidth(600);
        codeAText.setPrefHeight(400);
        codeAText.setMinWidth(600);
        codeAText.setMinHeight(400);
        codeAText.setPrefRowCount(20);
        codeAText.setPrefColumnCount(80);
        codeAText.setPadding(new Insets(15));

        codeABox.getChildren().addAll(codeATitle, codeAText);


        // --- Codice B ---
        VBox codeBBox = new VBox(10);
        Label codeBTitle = new Label("VERSIONE B:");
        codeBTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #16a34a;");

        TextArea codeBText = new TextArea(current.codeB);
        codeBText.setEditable(false);
        codeBText.setWrapText(false);
        codeBText.setStyle(
                "-fx-font-family: 'Consolas', monospace; -fx-font-size: 14px;"
                        + " -fx-background-color: #f8f9fa; -fx-border-color: #16a34a;"
                        + " -fx-border-width: 2; -fx-border-radius: 5;"
        );
        codeBText.setPrefWidth(600);
        codeBText.setPrefHeight(400);
        codeBText.setMinWidth(600);
        codeBText.setMinHeight(400);
        codeBText.setPrefRowCount(20);
        codeBText.setPrefColumnCount(80);
        codeBText.setPadding(new Insets(15));

        codeBBox.getChildren().addAll(codeBTitle, codeBText);

        // Aggiungi i due box al contenitore
        codesContainer.getChildren().addAll(codeABox, codeBBox);


        // Domande di valutazione
        VBox questionsBox = createEvaluationQuestions(current);

        questionContainer.getChildren().addAll(titleLabel, problemLabel, codesContainer, questionsBox);

        return questionContainer;
    }

    private VBox createEvaluationQuestions(CodeComparison comparison) {
        VBox questionsContainer = new VBox(15);
        questionsContainer.setStyle("-fx-background-color: #f1f5f9; -fx-padding: 20; -fx-border-radius: 10;");

        Label instructionLabel = new Label("ANALIZZA E CONFRONTA I DUE CODICI:");
        instructionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e40af;");

        UserResponse currentResponse = userResponses.get(currentQuestionIndex);

        // 1. Domanda sulla correttezza
        VBox correctnessBox = new VBox(8);
        Label correctnessLabel = new Label("1. Valutazione della correttezza funzionale:");
        correctnessLabel.setStyle("-fx-font-weight: bold;");

        ToggleGroup correctnessGroup = new ToggleGroup();
        currentResponse.correctnessGroup = correctnessGroup;

        RadioButton bothCorrect = new RadioButton("Entrambe le versioni sono corrette");
        RadioButton aCorrect = new RadioButton("Solo la versione A è corretta");
        RadioButton bCorrect = new RadioButton("Solo la versione B è corretta");
        RadioButton neitherCorrect = new RadioButton("Nessuna delle due versioni è corretta");

        bothCorrect.setToggleGroup(correctnessGroup);
        aCorrect.setToggleGroup(correctnessGroup);
        bCorrect.setToggleGroup(correctnessGroup);
        neitherCorrect.setToggleGroup(correctnessGroup);

        correctnessBox.getChildren().addAll(correctnessLabel, bothCorrect, aCorrect, bCorrect, neitherCorrect);

        // 2. Domanda sull'efficienza
        VBox efficiencyBox = new VBox(8);
        Label efficiencyLabel = new Label("2. Quale versione è più efficiente?");
        efficiencyLabel.setStyle("-fx-font-weight: bold;");

        ToggleGroup efficiencyGroup = new ToggleGroup();
        currentResponse.efficiencyGroup = efficiencyGroup;

        RadioButton aMoreEfficient = new RadioButton("La versione A è più efficiente");
        RadioButton bMoreEfficient = new RadioButton("La versione B è più efficiente");
        RadioButton sameEfficiency = new RadioButton("Hanno la stessa efficienza");

        aMoreEfficient.setToggleGroup(efficiencyGroup);
        bMoreEfficient.setToggleGroup(efficiencyGroup);
        sameEfficiency.setToggleGroup(efficiencyGroup);

        efficiencyBox.getChildren().addAll(efficiencyLabel, aMoreEfficient, bMoreEfficient, sameEfficiency);

        // 3. Domanda sulla leggibilità
        VBox readabilityBox = new VBox(8);
        Label readabilityLabel = new Label("3. Quale versione è più leggibile e segue meglio le best practice?");
        readabilityLabel.setStyle("-fx-font-weight: bold;");

        ToggleGroup readabilityGroup = new ToggleGroup();
        currentResponse.readabilityGroup = readabilityGroup;

        RadioButton aMoreReadable = new RadioButton("La versione A è più leggibile");
        RadioButton bMoreReadable = new RadioButton("La versione B è più leggibile");
        RadioButton sameReadability = new RadioButton("Sono equivalenti per leggibilità");

        aMoreReadable.setToggleGroup(readabilityGroup);
        bMoreReadable.setToggleGroup(readabilityGroup);
        sameReadability.setToggleGroup(readabilityGroup);

        readabilityBox.getChildren().addAll(readabilityLabel, aMoreReadable, bMoreReadable, sameReadability);

        // 4. Campo per la motivazione
        VBox motivationBox = new VBox(8);
        Label motivationLabel = new Label("4. Motiva le tue scelte (spiega le differenze che hai notato):");
        motivationLabel.setStyle("-fx-font-weight: bold;");

        TextArea motivationArea = new TextArea();
        motivationArea.setPromptText("Spiega le tue valutazioni: quali errori/differenze hai notato? Perché una versione è migliore dell'altra?");
        motivationArea.setPrefRowCount(4);
        motivationArea.setPrefWidth(700);
        currentResponse.motivationArea = motivationArea;

        motivationBox.getChildren().addAll(motivationLabel, motivationArea);

        questionsContainer.getChildren().addAll(correctnessBox, efficiencyBox, readabilityBox, motivationBox);

        return questionsContainer;
    }

    private VBox createNoMoreQuestionsUI() {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));

        Label message = new Label("Tutti i confronti completati!");
        message.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        container.getChildren().add(message);
        return container;
    }

    // Metodi di navigazione
    public boolean goToNextQuestion() {
        if (currentQuestionIndex < comparisons.size() - 1) {
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
        return currentQuestionIndex < comparisons.size() - 1;
    }

    public boolean hasPreviousQuestion() {
        return currentQuestionIndex > 0;
    }

    public int getCurrentQuestionNumber() {
        return currentQuestionIndex + 1;
    }

    // Metodi per l'interfaccia Exercise
    @Override
    public List<String> getQuestions() {
        List<String> questions = new ArrayList<>();
        for (int i = 0; i < comparisons.size(); i++) {
            CodeComparison comp = comparisons.get(i);
            questions.add(String.format("Confronta questi due codici:\nA: %s\nB: %s", 
                comp.codeA, comp.codeB));
        }
        return questions;
    }

    @Override
    public boolean checkAnswer(int questionIndex, String userAnswer) {
        if (questionIndex < 0 || questionIndex >= comparisons.size()) return false;
        
        UserResponse response = userResponses.get(questionIndex);
        CodeComparison expected = comparisons.get(questionIndex);
        
        return evaluateResponse(response, expected);
    }

    private boolean evaluateResponse(UserResponse response, CodeComparison expected) {
        int correctAnswers = 0;
        int totalQuestions = 3; // correttezza, efficienza, leggibilità

        // Valuta correttezza
        if (response.correctnessGroup != null && response.correctnessGroup.getSelectedToggle() != null) {
            String selected = ((RadioButton) response.correctnessGroup.getSelectedToggle()).getText();
            if (selected.equals(expected.correctnessAnswer)) {
                correctAnswers++;
            }
        }

        // Valuta efficienza
        if (response.efficiencyGroup != null && response.efficiencyGroup.getSelectedToggle() != null) {
            String selected = ((RadioButton) response.efficiencyGroup.getSelectedToggle()).getText();
            if (selected.equals(expected.efficiencyAnswer)) {
                correctAnswers++;
            }
        }

        // Valuta leggibilità
        if (response.readabilityGroup != null && response.readabilityGroup.getSelectedToggle() != null) {
            String selected = ((RadioButton) response.readabilityGroup.getSelectedToggle()).getText();
            if (selected.equals(expected.readabilityAnswer)) {
                correctAnswers++;
            }
        }

        // Considera corretta se almeno 2 su 3 risposte sono giuste
        return correctAnswers >= 2;
    }

    @Override
    public int getTotalQuestions() {
        return comparisons.size();
    }

    public boolean allQuestionsAttempted() {
        for (UserResponse response : userResponses) {
            if (!response.isComplete()) {
                return false;
            }
        }
        return true;
    }

    public int calculateScore() {
        int correctCount = 0;
        for (int i = 0; i < userResponses.size(); i++) {
            if (evaluateResponse(userResponses.get(i), comparisons.get(i))) {
                correctCount++;
            }
        }
        return correctCount;
    }

    public List<String> getUserResponses() {
        List<String> responses = new ArrayList<>();
        for (int i = 0; i < userResponses.size(); i++) {
            UserResponse response = userResponses.get(i);
            StringBuilder sb = new StringBuilder();
            
            if (response.correctnessGroup != null && response.correctnessGroup.getSelectedToggle() != null) {
                sb.append("Correttezza: ").append(((RadioButton) response.correctnessGroup.getSelectedToggle()).getText()).append("\n");
            }
            if (response.efficiencyGroup != null && response.efficiencyGroup.getSelectedToggle() != null) {
                sb.append("Efficienza: ").append(((RadioButton) response.efficiencyGroup.getSelectedToggle()).getText()).append("\n");
            }
            if (response.readabilityGroup != null && response.readabilityGroup.getSelectedToggle() != null) {
                sb.append("Leggibilità: ").append(((RadioButton) response.readabilityGroup.getSelectedToggle()).getText()).append("\n");
            }
            if (response.motivationArea != null) {
                sb.append("Motivazione: ").append(response.motivationArea.getText());
            }
            
            responses.add(sb.toString());
        }
        return responses;
    }

    public void resetQuiz() {
        currentQuestionIndex = 0;
        userResponses.clear();
        for (int i = 0; i < comparisons.size(); i++) {
            userResponses.add(new UserResponse());
        }
    }

    // Implementazioni per compatibilità con l'interfaccia esistente
    public VBox getExerciseUI() {
        return getCurrentQuestionUI();
    }

    public List<TextArea> getUserInputs() {
        List<TextArea> inputs = new ArrayList<>();
        for (UserResponse response : userResponses) {
            if (response.motivationArea != null) {
                inputs.add(response.motivationArea);
            } else {
                inputs.add(new TextArea());
            }
        }
        return inputs;
    }

    public List<String> getAnswers() {
        List<String> answers = new ArrayList<>();
        for (CodeComparison comp : comparisons) {
            answers.add(String.format("Correttezza: %s, Efficienza: %s, Leggibilità: %s", 
                comp.correctnessAnswer, comp.efficiencyAnswer, comp.readabilityAnswer));
        }
        return answers;
    }

    public List<ToggleGroup> getToggleGroups() {
        List<ToggleGroup> groups = new ArrayList<>();
        for (UserResponse response : userResponses) {
            if (response.correctnessGroup != null) groups.add(response.correctnessGroup);
            if (response.efficiencyGroup != null) groups.add(response.efficiencyGroup);
            if (response.readabilityGroup != null) groups.add(response.readabilityGroup);
        }
        return groups;
    }

    // Metodi per ottenere i confronti per livello
    private List<CodeComparison> getBeginnerComparisons() {
        List<CodeComparison> comparisons = new ArrayList<>();

        // Confronto 1: Somma di array - bug semplice
        comparisons.add(new CodeComparison(
            "Calcolare la somma degli elementi di un array",
            // Codice A - corretto
            """
            public class Main {
                public static void main(String[] args) {
                    int[] numeri = {1, 2, 3, 4, 5};
                    int somma = 0;
                    for (int i = 0; i < numeri.length; i++) {
                        somma += numeri[i];
                    }
                    System.out.println(somma);
                }
            }""",
            // Codice B - con bug (<=)
            """
            public class Main {
                public static void main(String[] args) {
                    int[] numeri = {1, 2, 3, 4, 5};
                    int somma = 0;
                    for (int i = 0; i <= numeri.length; i++) {
                        somma += numeri[i];
                    }
                    System.out.println(somma);
                }
            }""",
            "Solo la versione A è corretta",
            "La versione A è più efficiente", 
            "La versione A è più leggibile"
        ));

        // Confronto 2: Concatenazione stringhe
        comparisons.add(new CodeComparison(
            "Concatenare tre stringhe",
            // Codice A - con +
            """
            public class Main {
                public static void main(String[] args) {
                    String nome = "Mario";
                    String cognome = "Rossi";
                    String titolo = "Dott.";
                    String completo = titolo + " " + nome + " " + cognome;
                    System.out.println(completo);
                }
            }""",
            // Codice B - con StringBuilder  
            """
            public class Main {
                public static void main(String[] args) {
                    String nome = "Mario";
                    String cognome = "Rossi";
                    String titolo = "Dott.";
                    StringBuilder sb = new StringBuilder();
                    sb.append(titolo).append(" ").append(nome).append(" ").append(cognome);
                    System.out.println(sb.toString());
                }
            }""",
            "Entrambe le versioni sono corrette",
            "Hanno la stessa efficienza",
            "La versione A è più leggibile"
        ));

        // Confronto 3: Controllo numero pari
        comparisons.add(new CodeComparison(
            "Verificare se un numero è pari",
            // Codice A - con modulo
            """
            public class Main {
                public static void main(String[] args) {
                    int numero = 10;
                    if (numero % 2 == 0) {
                        System.out.println("Pari");
                    } else {
                        System.out.println("Dispari");
                    }
                }
            }""",
            // Codice B - con divisione
            """
            public class Main {
                public static void main(String[] args) {
                    int numero = 10;
                    if (numero / 2 * 2 == numero) {
                        System.out.println("Pari");
                    } else {
                        System.out.println("Dispari");
                    }
                }
            }""",
            "Entrambe le versioni sono corrette",
            "La versione A è più efficiente",
            "La versione A è più leggibile"
        ));

        return comparisons;
    }

    private List<CodeComparison> getIntermediateComparisons() {
        List<CodeComparison> comparisons = new ArrayList<>();

        // Confronto 1: Uguaglianza di stringhe
        comparisons.add(new CodeComparison(
            "Confrontare due stringhe per uguaglianza",
            // Codice A - con ==
            """
            public class StringComparator {
                public boolean confronta(String s1, String s2) {
                    return s1 == s2;
                }
                
                public static void main(String[] args) {
                    StringComparator sc = new StringComparator();
                    String a = new String("test");
                    String b = new String("test");
                    System.out.println(sc.confronta(a, b));
                }
            }""",
            // Codice B - con equals
            """
            public class StringComparator {
                public boolean confronta(String s1, String s2) {
                    if (s1 == null || s2 == null) {
                        return s1 == s2;
                    }
                    return s1.equals(s2);
                }
                
                public static void main(String[] args) {
                    StringComparator sc = new StringComparator();
                    String a = new String("test");
                    String b = new String("test");
                    System.out.println(sc.confronta(a, b));
                }
            }""",
            "Solo la versione B è corretta",
            "Hanno la stessa efficienza",
            "La versione B è più leggibile"
        ));

        // Confronto 2: Ricerca in lista
        comparisons.add(new CodeComparison(
            "Trovare un elemento in una lista",
            // Codice A - ciclo tradizionale
            """
            public class ListSearcher {
                public boolean contiene(List<String> lista, String elemento) {
                    for (int i = 0; i < lista.size(); i++) {
                        if (lista.get(i).equals(elemento)) {
                            return true;
                        }
                    }
                    return false;
                }
            }""",
            // Codice B - metodo contains
            """
            public class ListSearcher {
                public boolean contiene(List<String> lista, String elemento) {
                    return lista.contains(elemento);
                }
            }""",
            "Entrambe le versioni sono corrette",
            "La versione B è più efficiente",
            "La versione B è più leggibile"
        ));

        // Confronto 3: Getter/Setter
        comparisons.add(new CodeComparison(
            "Implementare una classe Person con getter e setter",
            // Codice A - campi pubblici
            """
            public class Person {
                public String name;
                public int age;
                
                public Person(String name, int age) {
                    this.name = name;
                    this.age = age;
                }
            }""",
            // Codice B - campi privati con getter/setter
            """
            public class Person {
                private String name;
                private int age;
                
                public Person(String name, int age) {
                    this.name = name;
                    this.age = age;
                }
                
                public String getName() { return name; }
                public void setName(String name) { this.name = name; }
                public int getAge() { return age; }
                public void setAge(int age) { this.age = age; }
            }""",
            "Entrambe le versioni sono corrette",
            "Hanno la stessa efficienza",
            "La versione B è più leggibile"
        ));

        return comparisons;
    }

    private List<CodeComparison> getAdvancedComparisons() {
        List<CodeComparison> comparisons = new ArrayList<>();

        // Confronto 1: Filtraggio con Stream vs cicli
        comparisons.add(new CodeComparison(
            "Filtrare una lista di numeri per ottenere solo i pari maggiori di 10",
            // Codice A - cicli tradizionali
            """
            public class NumberFilter {
                public List<Integer> filterNumbers(List<Integer> numbers) {
                    List<Integer> result = new ArrayList<>();
                    for (Integer num : numbers) {
                        if (num != null && num > 10 && num % 2 == 0) {
                            result.add(num);
                        }
                    }
                    return result;
                }
            }""",
            // Codice B - Stream API
            """
            public class NumberFilter {
                public List<Integer> filterNumbers(List<Integer> numbers) {
                    return numbers.stream()
                            .filter(Objects::nonNull)
                            .filter(n -> n > 10)
                            .filter(n -> n % 2 == 0)
                            .collect(Collectors.toList());
                }
            }""",
            "Entrambe le versioni sono corrette",
            "La versione A è più efficiente",
            "La versione B è più leggibile"
        ));

        // Confronto 2: Gestione eccezioni
        comparisons.add(new CodeComparison(
            "Leggere un numero da una stringa con gestione errori",
            // Codice A - try-catch generico
            """
            public class NumberParser {
                public int parseNumber(String str) {
                    try {
                        return Integer.parseInt(str);
                    } catch (Exception e) {
                        return -1;
                    }
                }
            }""",
            // Codice B - try-catch specifico
            """
            public class NumberParser {
                public int parseNumber(String str) {
                    if (str == null || str.trim().isEmpty()) {
                        throw new IllegalArgumentException("String cannot be null or empty");
                    }
                    try {
                        return Integer.parseInt(str.trim());
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid number format: " + str, e);
                    }
                }
            }""",
            "Entrambe le versioni sono corrette",
            "Hanno la stessa efficienza",
            "La versione B è più leggibile"
        ));

        // Confronto 3: Elaborazione parallela
        comparisons.add(new CodeComparison(
            "Calcolare la somma dei quadrati di una grande lista di numeri",
            // Codice A - stream sequenziale
            """
            public class Calculator {
                public long sumOfSquares(List<Integer> numbers) {
                    return numbers.stream()
                            .mapToLong(n -> (long) n * n)
                            .sum();
                }
            }""",
            // Codice B - parallel stream
            """
            public class Calculator {
                public long sumOfSquares(List<Integer> numbers) {
                    return numbers.parallelStream()
                            .mapToLong(n -> (long) n * n)
                            .sum();
                }
            }""",
            "Entrambe le versioni sono corrette",
            "La versione B è più efficiente",
            "Sono equivalenti per leggibilità"
        ));

        return comparisons;
    }

    // Classi di supporto
    private static class CodeComparison {
        String problemDescription;
        String codeA;
        String codeB;
        String correctnessAnswer;
        String efficiencyAnswer;
        String readabilityAnswer;

        CodeComparison(String problemDescription, String codeA, String codeB, 
                      String correctnessAnswer, String efficiencyAnswer, String readabilityAnswer) {
            this.problemDescription = problemDescription;
            this.codeA = codeA;
            this.codeB = codeB;
            this.correctnessAnswer = correctnessAnswer;
            this.efficiencyAnswer = efficiencyAnswer;
            this.readabilityAnswer = readabilityAnswer;
        }
    }

    private static class UserResponse {
        ToggleGroup correctnessGroup;
        ToggleGroup efficiencyGroup;
        ToggleGroup readabilityGroup;
        TextArea motivationArea;

        boolean isComplete() {
            return correctnessGroup != null && correctnessGroup.getSelectedToggle() != null &&
                   efficiencyGroup != null && efficiencyGroup.getSelectedToggle() != null &&
                   readabilityGroup != null && readabilityGroup.getSelectedToggle() != null;
        }
    }
}