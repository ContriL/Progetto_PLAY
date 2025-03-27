package application.exercises;

import java.util.List;

// Interfaccia base per tutti gli esercizi
public interface Exercise {
    String getTitle();
    String getDescription();
    int getDifficulty(); // 1 (principiante), 2 (intermedio), 3 (avanzato)
    List<String> getQuestions();
    boolean checkAnswer(int questionIndex, String userAnswer);
    int getTotalQuestions();
}