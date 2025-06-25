package application.exercises;

import java.util.ArrayList;
import java.util.List;

public class FindErrorExercise extends AbstractExercise {
    private List<String> codeSnippets; // Frammenti di codice con errori
    private List<String> correctAnswers; // Le risposte corrette

    public FindErrorExercise(int difficulty) {
        this.difficulty = difficulty;
        this.title = "Trova l'errore";

        switch(difficulty) {
            case 1:
                this.description = "Trova l'errore di sintassi nel seguente codice Java.";
                initBeginnerExercise();
                break;
            case 2:
                this.description = "Trova gli errori di sintassi e logica nel seguente codice Java.";
                initIntermediateExercise();
                break;
            case 3:
                this.description = "Analizza il codice e trova tutti gli errori di sintassi, logica e performance.";
                initAdvancedExercise();
                break;
            default:
                throw new IllegalArgumentException("Livello di difficoltà non valido");
        }
    }

    private void initBeginnerExercise() {
        codeSnippets = new ArrayList<>();
        correctAnswers = new ArrayList<>();

        // Esempio 1: Manca il punto e virgola
        codeSnippets.add(
                "public class Hello {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        System.out.println(\"Hello, World!\")\n" +
                        "    }\n" +
                        "}"
        );
        correctAnswers.add("System.out.println(\"Hello, World!\");");

        // Esempio 2: Parentesi non bilanciata
        codeSnippets.add(
                "public class MathCalculator {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        int result = (5 + 3 * (2 - 1;\n" +
                        "        System.out.println(result);\n" +
                        "    }\n" +
                        "}"
        );
        correctAnswers.add("int result = (5 + 3 * (2 - 1));");

        // Esempio 3: Uso di variabile non dichiarata
        codeSnippets.add(
                "public class VariableDemo {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        count = 10;\n" +
                        "        System.out.println(count);\n" +
                        "    }\n" +
                        "}"
        );
        correctAnswers.add("int count = 10;");
    }

    private void initIntermediateExercise() {
        codeSnippets = new ArrayList<>();
        correctAnswers = new ArrayList<>();

        // Esempio 1: Divisione per zero
        codeSnippets.add(
                "public class Calculator {\n" +
                        "    public static int divide(int a, int b) {\n" +
                        "        return a / b;\n" +
                        "    }\n" +
                        "    \n" +
                        "    public static void main(String[] args) {\n" +
                        "        int result = divide(10, 0);\n" +
                        "        System.out.println(result);\n" +
                        "    }\n" +
                        "}"
        );
        correctAnswers.add("if (b != 0) { return a / b; } else { throw new ArithmeticException(\"Divisione per zero\"); }");

        // Esempio 2: Confronto tra stringhe con ==
        codeSnippets.add(
                "public class StringCompare {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        String s1 = new String(\"Hello\");\n" +
                        "        String s2 = new String(\"Hello\");\n" +
                        "        \n" +
                        "        if (s1 == s2) {\n" +
                        "            System.out.println(\"Le stringhe sono uguali\");\n" +
                        "        } else {\n" +
                        "            System.out.println(\"Le stringhe sono diverse\");\n" +
                        "        }\n" +
                        "    }\n" +
                        "}"
        );
        correctAnswers.add("if (s1.equals(s2))");

        // Esempio 3: Problema di concorrenza
        codeSnippets.add(
                "import java.util.ArrayList;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "public class ConcurrencyIssue {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        List<String> items = new ArrayList<>();\n" +
                        "        items.add(\"Item 1\");\n" +
                        "        items.add(\"Item 2\");\n" +
                        "        items.add(\"Item 3\");\n" +
                        "        \n" +
                        "        for (String item : items) {\n" +
                        "            if (item.equals(\"Item 2\")) {\n" +
                        "                items.remove(item);\n" +
                        "            }\n" +
                        "        }\n" +
                        "        \n" +
                        "        System.out.println(items);\n" +
                        "    }\n" +
                        "}"
        );
        correctAnswers.add("ConcurrentModificationException");
    }

    private void initAdvancedExercise() {
        codeSnippets = new ArrayList<>();
        correctAnswers = new ArrayList<>();

        // Esempio 1: Memory leak
        codeSnippets.add(
                "import java.util.HashMap;\n" +
                        "import java.util.Map;\n" +
                        "\n" +
                        "public class CacheClass {\n" +
                        "    private static final Map<Key, String> cache = new HashMap<>();\n" +
                        "    \n" +
                        "    public static void addToCache(Key key, String value) {\n" +
                        "        cache.put(key, value);\n" +
                        "    }\n" +
                        "    \n" +
                        "    public static String getFromCache(Key key) {\n" +
                        "        return cache.get(key);\n" +
                        "    }\n" +
                        "    \n" +
                        "    public static class Key {\n" +
                        "        private String id;\n" +
                        "        \n" +
                        "        public Key(String id) {\n" +
                        "            this.id = id;\n" +
                        "        }\n" +
                        "    }\n" +
                        "}"
        );
        correctAnswers.add("Non ha implementato equals() e hashCode() nella classe Key");

        // Esempio 2: Deadlock
        codeSnippets.add(
                "public class DeadlockExample {\n" +
                        "    private static final Object lock1 = new Object();\n" +
                        "    private static final Object lock2 = new Object();\n" +
                        "    \n" +
                        "    public static void method1() {\n" +
                        "        synchronized (lock1) {\n" +
                        "            System.out.println(\"Thread 1: Holding lock 1...\");\n" +
                        "            try { Thread.sleep(100); } catch (Exception e) {}\n" +
                        "            System.out.println(\"Thread 1: Waiting for lock 2...\");\n" +
                        "            synchronized (lock2) {\n" +
                        "                System.out.println(\"Thread 1: Holding lock 1 & 2...\");\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "    \n" +
                        "    public static void method2() {\n" +
                        "        synchronized (lock2) {\n" +
                        "            System.out.println(\"Thread 2: Holding lock 2...\");\n" +
                        "            try { Thread.sleep(100); } catch (Exception e) {}\n" +
                        "            System.out.println(\"Thread 2: Waiting for lock 1...\");\n" +
                        "            synchronized (lock1) {\n" +
                        "                System.out.println(\"Thread 2: Holding lock 1 & 2...\");\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "    \n" +
                        "    public static void main(String[] args) {\n" +
                        "        new Thread(() -> method1()).start();\n" +
                        "        new Thread(() -> method2()).start();\n" +
                        "    }\n" +
                        "}"
        );
        correctAnswers.add("I metodi ottengono i lock in ordine diverso causando deadlock");

        // Esempio 3: SQL Injection
        codeSnippets.add(
                "import java.sql.Connection;\n" +
                        "import java.sql.DriverManager;\n" +
                        "import java.sql.SQLException;\n" +
                        "import java.sql.Statement;\n" +
                        "\n" +
                        "public class LoginSystem {\n" +
                        "    public static boolean authenticateUser(String username, String password) {\n" +
                        "        try {\n" +
                        "            Connection conn = DriverManager.getConnection(\"jdbc:mysql://localhost:3306/mydb\", \"user\", \"password\");\n" +
                        "            Statement stmt = conn.createStatement();\n" +
                        "            String query = \"SELECT * FROM users WHERE username = '\" + username + \"' AND password = '\" + password + \"'\";\n" +
                        "            return stmt.executeQuery(query).next();\n" +
                        "        } catch (SQLException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "            return false;\n" +
                        "        }\n" +
                        "    }\n" +
                        "}"
        );
        correctAnswers.add("Vulnerabilità SQL Injection");
    }

    @Override
    public List<String> getQuestions() {
        return codeSnippets;
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
        return codeSnippets.size();
    }
}