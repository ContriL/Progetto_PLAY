package application.exercises;

public class ExerciseFactory {

    public static Exercise createExercise(String type, int difficulty) {
        switch(type) {
            case "FindError":
                return new FindErrorExercise(difficulty);
            case "OrderSteps":
                return new OrderStepsExercise(difficulty);
            case "WhatPrints":
                return new WhatPrintsExercise(difficulty);
            default:
                throw new IllegalArgumentException("Tipo di esercizio non supportato: " + type);
        }
    }
}