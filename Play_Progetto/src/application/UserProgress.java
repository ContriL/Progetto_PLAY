package application;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe per gestire e salvare i progressi degli utenti nel sistema PLAY.
 * Fornisce funzionalità per il salvataggio, recupero e analisi dei risultati degli esercizi.
 */
public class UserProgress {

    /**
     * File di riferimento per il salvataggio dei progressi utente
     */
    public static File progressFile = getDataFile("Utenti_Progressi.txt");

    private static File getDataFile(String filename) {

        File resourcesFile = new File("Play_Progetto/src/application/resources/" + filename);

        File parentDir = resourcesFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        return resourcesFile;
    }

    /**
     * Salva il risultato di un esercizio completato da un utente.
     */
    public static boolean saveProgress(String username, String exerciseType, int difficulty,
                                       int correctAnswers, int totalQuestions) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Errore: username vuoto, salvataggio annullato");
            return false;
        }
        try {
            // Verifica esistenza del file e lo crea se necessario
            if (!progressFile.exists()) {
                progressFile.createNewFile();
            }

            // Calcola la percentuale di successo
            double percentage = (double) correctAnswers / totalQuestions * 100;

            // Crea timestamp per salvare quando è stato fatto l'esercizio
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            // Crea la riga da salvare nel file
            String progressLine = String.format("%s,%s,%d,%d,%d,%.2f,%s",
                    username, exerciseType, difficulty, correctAnswers, totalQuestions, percentage, timestamp);

            // Scrive nel file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(progressFile, true))) {
                writer.write(progressLine);
                writer.newLine();
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recupera tutti i progressi di un utente specifico.
     */
    public static List<String> getUserProgress(String username) {
        List<String> userProgressList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(progressFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Controlla se la riga appartiene all'utente
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
     * Recupera l'ultimo livello di difficoltà per un tipo di esercizio.
     */
    public static int getDifficultyForExercise(String username, String exerciseType) {
        int lastDifficulty = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(progressFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Controlla se corrisponde a utente e tipo esercizio
                if (parts.length >= 3 && parts[0].equals(username) && parts[1].equals(exerciseType)) {
                    // Prende l'ultimo valore trovato
                    lastDifficulty = Integer.parseInt(parts[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lastDifficulty;
    }

    /**
     * Recupera il numero di risposte corrette per un esercizio specifico.
     */
    public static int getCorrectAnswers(String username, String exerciseType, int difficulty) {
        int correctAnswers = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(progressFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Controlla se corrispondono tutti i parametri
                if (parts.length >= 4 &&
                        parts[0].equals(username) &&
                        parts[1].equals(exerciseType) &&
                        Integer.parseInt(parts[2]) == difficulty) {

                    // Prende l'ultimo risultato
                    correctAnswers = Integer.parseInt(parts[3]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return correctAnswers;
    }

    /**
     * Calcola il numero totale di esercizi completati con successo.
     */
    public static int getTotalExercisesCompleted(String username) {
        List<String> userProgress = getUserProgress(username);
        int completedCount = 0;

        for (String progress : userProgress) {
            String[] parts = progress.split(",");
            if (parts.length >= 6) {
                double percentage = Double.parseDouble(parts[5]);
                // Considera superato se >= 60%
                if (percentage >= 60.0) {
                    completedCount++;
                }
            }
        }

        return completedCount;
    }

    /**
     * Verifica se un utente ha superato un determinato livello.
     */
    public static boolean hasPassedLevel(String username, String exerciseType, int level) {
        try (BufferedReader reader = new BufferedReader(new FileReader(progressFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Controlla se corrispondono i parametri
                if (parts.length >= 6 &&
                        parts[0].equals(username) &&
                        parts[1].equals(exerciseType) &&
                        Integer.parseInt(parts[2]) == level) {

                    // Controlla se ha superato la soglia del 60%
                    double percentage = Double.parseDouble(parts[5]);
                    if (percentage >= 60.0) {
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