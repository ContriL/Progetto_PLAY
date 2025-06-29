package application.exercises;

import java.util.ArrayList;
import java.util.List;

public class WhatPrintsExercise extends AbstractExercise {
    private List<String> codeSnippets; // Frammenti di codice per l'output
    private List<String> correctOutputs; // Risposte corrette

    public WhatPrintsExercise(int difficulty) {
        this.difficulty = difficulty;
        this.title = "Cosa Stampa?";

        switch(difficulty) {
            case 1:
                this.description = "Analizza il seguente codice Java e determina cosa verrà stampato.";
                initBeginnerExercise();
                break;
            case 2:
                this.description = "Analizza il seguente codice Java con strutture condizionali e cicli. Determina cosa verrà stampato.";
                initIntermediateExercise();
                break;
            case 3:
                this.description = "Analizza il seguente codice Java complesso con classi, metodi, e strutture dati. Determina cosa verrà stampato.";
                initAdvancedExercise();
                break;
            default:
                throw new IllegalArgumentException("Livello di difficoltà non valido");
        }
    }

    private void initBeginnerExercise() {
        codeSnippets = new ArrayList<>();
        correctOutputs = new ArrayList<>();

        // Esercizio 1: Output semplice
        codeSnippets.add(
                "public class Main {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        int x = 5;\n" +
                        "        int y = 10;\n" +
                        "        System.out.println(x + y);\n" +
                        "        System.out.println(\"x + y = \" + x + y);\n" +
                        "    }\n" +
                        "}"
        );
        correctOutputs.add("15\nx + y = 510");

        // Esercizio 2: Operazioni aritmetiche
        codeSnippets.add(
                "public class Main {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        int a = 10;\n" +
                        "        int b = 3;\n" +
                        "        System.out.println(a / b);\n" +
                        "        System.out.println(a % b);\n" +
                        "        System.out.println(a / (double) b);\n" +
                        "    }\n" +
                        "}"
        );
        correctOutputs.add("3\n1\n3.3333333333333335");

        // Esercizio 3: Incremento/decremento
        codeSnippets.add(
                "public class Main {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        int i = 5;\n" +
                        "        System.out.println(i++);\n" +
                        "        System.out.println(i);\n" +
                        "        System.out.println(++i);\n" +
                        "        System.out.println(i--);\n" +
                        "        System.out.println(i);\n" +
                        "    }\n" +
                        "}"
        );
        correctOutputs.add("5\n6\n7\n7\n6");
    }

    private void initIntermediateExercise() {
        codeSnippets = new ArrayList<>();
        correctOutputs = new ArrayList<>();

        // Esercizio 1: Ciclo for e if
        codeSnippets.add(
                "public class Main {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        for (int i = 1; i <= 10; i++) {\n" +
                        "            if (i % 2 == 0) {\n" +
                        "                System.out.print(i + \" \");\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "}"
        );
        correctOutputs.add("2 4 6 8 10 ");

        // Esercizio 2: Ciclo while e break
        codeSnippets.add(
                "public class Main {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        int sum = 0;\n" +
                        "        int i = 1;\n" +
                        "        \n" +
                        "        while (i <= 10) {\n" +
                        "            sum += i;\n" +
                        "            \n" +
                        "            if (sum > 20) {\n" +
                        "                break;\n" +
                        "            }\n" +
                        "            \n" +
                        "            i++;\n" +
                        "        }\n" +
                        "        \n" +
                        "        System.out.println(\"Sum: \" + sum);\n" +
                        "        System.out.println(\"Last i: \" + i);\n" +
                        "    }\n" +
                        "}"
        );
        correctOutputs.add("Sum: 21\nLast i: 6");

        // Esercizio 3: Array e ciclo for-each
        codeSnippets.add(
                "public class Main {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        int[] numbers = {1, 4, 7, 2, 5};\n" +
                        "        int max = numbers[0];\n" +
                        "        \n" +
                        "        for (int num : numbers) {\n" +
                        "            if (num > max) {\n" +
                        "                max = num;\n" +
                        "            }\n" +
                        "        }\n" +
                        "        \n" +
                        "        System.out.println(\"Max value: \" + max);\n" +
                        "        \n" +
                        "        for (int i = 0; i < numbers.length; i++) {\n" +
                        "            if (numbers[i] == max) {\n" +
                        "                System.out.println(\"Index of max: \" + i);\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "}"
        );
        correctOutputs.add("Max value: 7\nIndex of max: 2");
    }

    private void initAdvancedExercise() {
        codeSnippets = new ArrayList<>();
        correctOutputs = new ArrayList<>();

        // Esercizio 1: Classi e metodi
        codeSnippets.add(
                "class Person {\n" +
                        "    private String name;\n" +
                        "    private int age;\n" +
                        "    \n" +
                        "    public Person(String name, int age) {\n" +
                        "        this.name = name;\n" +
                        "        this.age = age;\n" +
                        "    }\n" +
                        "    \n" +
                        "    public void birthday() {\n" +
                        "        this.age++;\n" +
                        "    }\n" +
                        "    \n" +
                        "    @Override\n" +
                        "    public String toString() {\n" +
                        "        return name + \", \" + age;\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "public class Main {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        Person p1 = new Person(\"Alice\", 25);\n" +
                        "        Person p2 = new Person(\"Bob\", 30);\n" +
                        "        \n" +
                        "        System.out.println(p1);\n" +
                        "        p1.birthday();\n" +
                        "        System.out.println(p1);\n" +
                        "        System.out.println(p2);\n" +
                        "    }\n" +
                        "}"
        );
        correctOutputs.add("Alice, 25\nAlice, 26\nBob, 30");

        // Esercizio 2: Ereditarietà e polimorfismo
        codeSnippets.add(
                "class Shape {\n" +
                        "    public double area() {\n" +
                        "        return 0;\n" +
                        "    }\n" +
                        "    \n" +
                        "    @Override\n" +
                        "    public String toString() {\n" +
                        "        return \"Area: \" + area();\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "class Circle extends Shape {\n" +
                        "    private double radius;\n" +
                        "    \n" +
                        "    public Circle(double radius) {\n" +
                        "        this.radius = radius;\n" +
                        "    }\n" +
                        "    \n" +
                        "    @Override\n" +
                        "    public double area() {\n" +
                        "        return Math.PI * radius * radius;\n" +
                        "    }\n" +
                        "    \n" +
                        "    @Override\n" +
                        "    public String toString() {\n" +
                        "        return \"Circle, \" + super.toString();\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "class Rectangle extends Shape {\n" +
                        "    private double width;\n" +
                        "    private double height;\n" +
                        "    \n" +
                        "    public Rectangle(double width, double height) {\n" +
                        "        this.width = width;\n" +
                        "        this.height = height;\n" +
                        "    }\n" +
                        "    \n" +
                        "    @Override\n" +
                        "    public double area() {\n" +
                        "        return width * height;\n" +
                        "    }\n" +
                        "    \n" +
                        "    @Override\n" +
                        "    public String toString() {\n" +
                        "        return \"Rectangle, \" + super.toString();\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "public class Main {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        Shape[] shapes = {\n" +
                        "            new Circle(2),\n" +
                        "            new Rectangle(3, 4),\n" +
                        "            new Circle(1)\n" +
                        "        };\n" +
                        "        \n" +
                        "        for (Shape shape : shapes) {\n" +
                        "            System.out.println(shape);\n" +
                        "        }\n" +
                        "    }\n" +
                        "}"
        );
        // Nota: usiamo un valore approssimato per l'area del cerchio
        correctOutputs.add("Circle, Area: 12.566370614359172\nRectangle, Area: 12.0\nCircle, Area: 3.141592653589793");

        // Esercizio 3: Eccezioni e gestione degli errori
        codeSnippets.add(
                "public class Main {\n" +
                        "    public static int divide(int a, int b) {\n" +
                        "        try {\n" +
                        "            return a / b;\n" +
                        "        } catch (ArithmeticException e) {\n" +
                        "            System.out.println(\"Errore: \" + e.getMessage());\n" +
                        "            return 0;\n" +
                        "        } finally {\n" +
                        "            System.out.println(\"Operazione di divisione completata\");\n" +
                        "        }\n" +
                        "    }\n" +
                        "    \n" +
                        "    public static void main(String[] args) {\n" +
                        "        System.out.println(\"Risultato 1: \" + divide(10, 2));\n" +
                        "        System.out.println(\"Risultato 2: \" + divide(8, 0));\n" +
                        "        System.out.println(\"Risultato 3: \" + divide(15, 3));\n" +
                        "    }\n" +
                        "}"
        );
        correctOutputs.add(
                "Operazione di divisione completata\n" +
                        "Risultato 1: 5\n" +
                        "Errore: / by zero\n" +
                        "Operazione di divisione completata\n" +
                        "Risultato 2: 0\n" +
                        "Operazione di divisione completata\n" +
                        "Risultato 3: 5"
        );
    }

    @Override
    public List<String> getQuestions() {
        return codeSnippets;
    }

    @Override
    public boolean checkAnswer(int questionIndex, String userAnswer) {
        if (questionIndex < 0 || questionIndex >= correctOutputs.size()) {
            return false;
        }

        // Normalizziamo le risposte per il confronto
        String normalizedUserAnswer = userAnswer.trim().replaceAll("\\r\\n", "\n");
        String normalizedCorrectOutput = correctOutputs.get(questionIndex).trim().replaceAll("\\r\\n", "\n");

        return normalizedUserAnswer.equals(normalizedCorrectOutput);
    }

    @Override
    public int getTotalQuestions() {
        return codeSnippets.size();
    }
}