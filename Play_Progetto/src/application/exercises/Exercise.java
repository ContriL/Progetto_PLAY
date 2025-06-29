package application.exercises;

import java.util.List;

// Interfaccia base per tutti gli esercizi
public interface Exercise {
    String getTitle();
    String getDescription();
    int getDifficulty();
    List<String> getQuestions();
    boolean checkAnswer(int questionIndex, String userAnswer);
    int getTotalQuestions();
}