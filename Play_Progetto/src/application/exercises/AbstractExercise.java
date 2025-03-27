package application.exercises;

import java.util.List;

// Classe astratta base per implementare logiche comuni
public abstract class AbstractExercise implements Exercise {
    protected String title;
    protected String description;
    protected int difficulty;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getDifficulty() {
        return difficulty;
    }
}