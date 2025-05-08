package application;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe per gestire e salvare i progressi degli utenti nel sistema PLAY.
 */
public class UserProgress {

    //private static final String PROGRESS_FILE_PATH = "C:/Users/dadas/IdeaProjects/Progetto_PLAY/Play_Progetto/src/application/UtentiProgressi.txt";
    private static final String PROGRESS_FILE_PATH = "/Users/lorenzocontri/Desktop/Progetto_Programmazione/Progetto_PLAY/Play_Progetto/src/application/UtentiProgressi.txt";
    private static final File progressFile = new File(PROGRESS_FILE_PATH);

    /**
     * Salva il risultato di un esercizio completato da un utente.
     *
     * @param username Nome utente
     * @param exerciseType Tipo di esercizio (es. "FindError", "OrderSteps", "WhatPrints")
     * @param difficulty Livello di difficoltà dell'esercizio (1-3)
     * @param correctAnswers Numero di risposte corrette
     * @param totalQuestions Numero totale di domande
     * @return true se il salvataggio è avvenuto con successo, false altrimenti
     */
    public static boolean saveProgress(String username, String exerciseType, int difficulty,
                                       int correctAnswers, int totalQuestions) {
        try {
            // Verifica se il file esiste, altrimenti lo crea
            if (!progressFile.exists()) {
                progressFile.createNewFile();
            }

            // Calcola la percentuale di successo
            double percentage = (double) correctAnswers / totalQuestions * 100;

            // Ottieni la data e l'ora correnti
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            // Formatta la riga di progresso: username,exerciseType,difficulty,correctAnswers,totalQuestions,percentage,timestamp
            String progressLine = String.format("%s,%s,%d,%d,%d,%.2f,%s",
                    username, exerciseType, difficulty, correctAnswers, totalQuestions, percentage, timestamp);

            // Scrive la riga nel file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(progressFile, true))) {
                writer.write(progressLine);
                writer.newLine();
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Rimuovi questa riga: non verrà mai raggiunta
        // return false;
    }

    /**
     * Recupera tutti i progressi di un utente specifico.
     *
     * @param username Nome utente
     * @return Lista di stringhe rappresentanti i progressi dell'utente
     */
    public static List<String> getUserProgress(String username) {
        List<String> userProgressList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(progressFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Se la prima parte corrisponde allo username, aggiungi alla lista
                if (parts.length > 0 && parts[0].equals(username)) {
                    userProgressList.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userProgressList;
    }

    /**
     * Recupera il livello massimo raggiunto da un utente per un tipo di esercizio.
     *
     * @param username Nome utente
     * @param exerciseType Tipo di esercizio
     * @return Il livello massimo raggiunto (1-3), o 0 se non è stato completato alcun esercizio
     */
    

    public static int getDifficultyForExercise(String username, String exerciseType) {
        int lastDifficulty = 0;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(progressFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
    
                // Verifica se la riga appartiene all'utente e al tipo di esercizio
                if (parts.length >= 3 && parts[0].equals(username) && parts[1].equals(exerciseType)) {
                    // Aggiorna sempre all'ultimo valore trovato
                    lastDifficulty = Integer.parseInt(parts[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return lastDifficulty;
    }
    
    public static int getCorrectAnswers(String username, String exerciseType, int difficulty) {
        int correctAnswers = 0;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(progressFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
    
                // Verifica se la riga appartiene all'utente, al tipo di esercizio e al livello
                if (parts.length >= 4 &&
                    parts[0].equals(username) &&
                    parts[1].equals(exerciseType) &&
                    Integer.parseInt(parts[2]) == difficulty) {
                    
                    // Usa sempre l'ultima entry valida trovata
                    correctAnswers = Integer.parseInt(parts[3]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return correctAnswers;
    }
    

    /**
     * Controlla se un utente ha completato con successo un determinato livello di un esercizio.
     * Il successo è definito come almeno il 70% di risposte corrette.
     *
     * @param username Nome utente
     * @param exerciseType Tipo di esercizio
     * @param level Livello dell'esercizio
     * @return true se l'utente ha completato con successo il livello, false altrimenti
     */
    public static boolean hasPassedLevel(String username, String exerciseType, int level) {
        try (BufferedReader reader = new BufferedReader(new FileReader(progressFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Verifica se la riga appartiene all'utente, al tipo di esercizio e al livello
                if (parts.length >= 6 &&
                        parts[0].equals(username) &&
                        parts[1].equals(exerciseType) &&
                        Integer.parseInt(parts[2]) == level) {

                    // Controlla se la percentuale di successo è almeno 70%
                    double percentage = Double.parseDouble(parts[5]);
                    if (percentage >= 70.0) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}