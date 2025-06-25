package application.exercises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrderStepsExercise extends AbstractExercise {
    private List<List<String>> stepsList; // Lista di liste di passi per ogni esercizio
    private List<List<String>> correctOrdersList; // Lista degli ordini corretti dei passi

    public OrderStepsExercise(int difficulty) {
        this.difficulty = difficulty;
        this.title = "Ordina i Passi";

        switch(difficulty) {
            case 1:
                this.description = "Ordina i passi del seguente algoritmo nel giusto ordine di esecuzione.";
                initBeginnerExercise();
                break;
            case 2:
                this.description = "Ordina i passi del seguente algoritmo nel giusto ordine di esecuzione. Considera le dipendenze tra le operazioni.";
                initIntermediateExercise();
                break;
            case 3:
                this.description = "Ordina i passi del seguente algoritmo complesso nel giusto ordine di esecuzione. Considera le dipendenze e le condizioni.";
                initAdvancedExercise();
                break;
            default:
                throw new IllegalArgumentException("Livello di difficoltà non valido");
        }
    }

    private void initBeginnerExercise() {
        stepsList = new ArrayList<>();
        correctOrdersList = new ArrayList<>();

        // Esercizio 1: Calcolo della somma di due numeri
        List<String> steps1 = new ArrayList<>(Arrays.asList(
                "Dichiarare le variabili num1 e num2",
                "Assegnare valori a num1 e num2",
                "Dichiarare la variabile somma",
                "Calcolare num1 + num2 e assegnare il risultato a somma",
                "Stampare il valore di somma"
        ));

        List<String> correctOrder1 = new ArrayList<>(steps1); // L'ordine corretto è quello originale
        Collections.shuffle(steps1); // Mescoliamo i passi per l'esercizio

        stepsList.add(steps1);
        correctOrdersList.add(correctOrder1);

        // Esercizio 2: Verifica se un numero è pari o dispari
        List<String> steps2 = new ArrayList<>(Arrays.asList(
                "Dichiarare la variabile numero",
                "Assegnare un valore a numero",
                "Calcolare il resto della divisione di numero per 2",
                "Verificare se il resto è uguale a 0",
                "Stampare 'pari' se il resto è 0, altrimenti stampare 'dispari'"
        ));

        List<String> correctOrder2 = new ArrayList<>(steps2);
        Collections.shuffle(steps2);

        stepsList.add(steps2);
        correctOrdersList.add(correctOrder2);

        // Esercizio 3: Trovare il valore massimo tra tre numeri
        List<String> steps3 = new ArrayList<>(Arrays.asList(
                "Dichiarare le variabili a, b e c",
                "Assegnare valori alle variabili a, b e c",
                "Dichiarare la variabile max e inizializzarla con il valore di a",
                "Confrontare max con b e aggiornare max se b è maggiore",
                "Confrontare max con c e aggiornare max se c è maggiore",
                "Stampare il valore di max"
        ));

        List<String> correctOrder3 = new ArrayList<>(steps3);
        Collections.shuffle(steps3);

        stepsList.add(steps3);
        correctOrdersList.add(correctOrder3);
    }

    private void initIntermediateExercise() {
        stepsList = new ArrayList<>();
        correctOrdersList = new ArrayList<>();

        // Esercizio 1: Algoritmo di ordinamento per selezione
        List<String> steps1 = new ArrayList<>(Arrays.asList(
                "Dichiarare un array di numeri da ordinare",
                "Iniziare un ciclo esterno che va da 0 a lunghezza dell'array - 1",
                "Inizializzare minIndex con il valore dell'indice corrente i",
                "Iniziare un ciclo interno che va da i+1 a lunghezza dell'array - 1",
                "Confrontare l'elemento all'indice j con l'elemento all'indice minIndex",
                "Se l'elemento all'indice j è minore, aggiornare minIndex a j",
                "Scambiare gli elementi agli indici i e minIndex",
                "Ripetere finché tutti gli elementi non sono stati ordinati"
        ));

        List<String> correctOrder1 = new ArrayList<>(steps1);
        Collections.shuffle(steps1);

        stepsList.add(steps1);
        correctOrdersList.add(correctOrder1);

        // Esercizio 2: Calcolo del fattoriale
        List<String> steps2 = new ArrayList<>(Arrays.asList(
                "Dichiarare una variabile n per il numero di cui calcolare il fattoriale",
                "Verificare se n è uguale a 0 o 1",
                "Se n è 0 o 1, restituire 1 come risultato",
                "Altrimenti, dichiarare una variabile result inizializzata a 1",
                "Iniziare un ciclo da 2 a n",
                "Moltiplicare result per il valore corrente del ciclo",
                "Restituire il valore di result"
        ));

        List<String> correctOrder2 = new ArrayList<>(steps2);
        Collections.shuffle(steps2);

        stepsList.add(steps2);
        correctOrdersList.add(correctOrder2);

        // Esercizio 3: Verifica se una stringa è palindroma
        List<String> steps3 = new ArrayList<>(Arrays.asList(
                "Dichiarare una variabile stringa da verificare",
                "Convertire la stringa in minuscolo",
                "Rimuovere spazi e caratteri speciali dalla stringa",
                "Dichiarare due indici: uno che parte dall'inizio e uno dalla fine",
                "Confrontare i caratteri nelle posizioni dei due indici",
                "Se i caratteri sono diversi, la stringa non è palindroma",
                "Incrementare l'indice di inizio e decrementare quello di fine",
                "Ripetere finché gli indici non si incontrano",
                "Se tutti i confronti sono uguali, la stringa è palindroma"
        ));

        List<String> correctOrder3 = new ArrayList<>(steps3);
        Collections.shuffle(steps3);

        stepsList.add(steps3);
        correctOrdersList.add(correctOrder3);
    }

    private void initAdvancedExercise() {
        stepsList = new ArrayList<>();
        correctOrdersList = new ArrayList<>();

        // Esercizio 1: Implementazione di Quicksort
        List<String> steps1 = new ArrayList<>(Arrays.asList(
                "Verificare se l'array ha lunghezza 0 o 1, in tal caso è già ordinato",
                "Scegliere un elemento pivot dall'array (es. l'ultimo elemento)",
                "Dichiarare due array vuoti: leftArr e rightArr",
                "Scorrere l'array originale (escludendo il pivot)",
                "Se l'elemento corrente è minore del pivot, aggiungerlo a leftArr",
                "Se l'elemento corrente è maggiore o uguale al pivot, aggiungerlo a rightArr",
                "Applicare ricorsivamente quicksort a leftArr",
                "Applicare ricorsivamente quicksort a rightArr",
                "Concatenare leftArr ordinato, il pivot e rightArr ordinato",
                "Restituire l'array ordinato"
        ));

        List<String> correctOrder1 = new ArrayList<>(steps1);
        Collections.shuffle(steps1);

        stepsList.add(steps1);
        correctOrdersList.add(correctOrder1);

        // Esercizio 2: Algoritmo di ricerca binaria
        List<String> steps2 = new ArrayList<>(Arrays.asList(
                "Verificare che l'array sia ordinato",
                "Impostare left a 0 e right alla lunghezza dell'array - 1",
                "Mentre left <= right, calcolare il punto medio mid = (left + right) / 2",
                "Se l'elemento in posizione mid è uguale al target, restituire mid",
                "Se l'elemento in posizione mid è minore del target, aggiornare left = mid + 1",
                "Se l'elemento in posizione mid è maggiore del target, aggiornare right = mid - 1",
                "Se il ciclo termina senza trovare il target, restituire -1"
        ));

        List<String> correctOrder2 = new ArrayList<>(steps2);
        Collections.shuffle(steps2);

        stepsList.add(steps2);
        correctOrdersList.add(correctOrder2);

        // Esercizio 3: Implementazione del pattern Observer
        List<String> steps3 = new ArrayList<>(Arrays.asList(
                "Definire un'interfaccia Observer con un metodo update()",
                "Definire una classe astratta Subject che mantiene una lista di Observer",
                "Implementare in Subject i metodi per aggiungere e rimuovere Observer",
                "Implementare in Subject un metodo notifyObservers() che informa tutti gli Observer",
                "Creare una classe concreta ConcreteSubject che estende Subject",
                "Implementare un metodo setState() in ConcreteSubject che modifica lo stato e chiama notifyObservers()",
                "Creare una classe concreta ConcreteObserver che implementa Observer",
                "Implementare update() in ConcreteObserver per reagire ai cambiamenti di stato",
                "Registrare istanze di ConcreteObserver al ConcreteSubject"
        ));

        List<String> correctOrder3 = new ArrayList<>(steps3);
        Collections.shuffle(steps3);

        stepsList.add(steps3);
        correctOrdersList.add(correctOrder3);
    }

    @Override
    public List<String> getQuestions() {
        // Per semplicità, restituiamo le stringhe dei passi da ordinare
        // separate da newline
        List<String> formattedSteps = new ArrayList<>();

        for (List<String> steps : stepsList) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < steps.size(); i++) {
                sb.append((i + 1)).append(". ").append(steps.get(i)).append("\n");
            }
            formattedSteps.add(sb.toString());
        }

        return formattedSteps;
    }

    // Metodo per ottenere l'elenco di passi non formattati (utile per l'interfaccia)
    public List<String> getStepsForQuestion(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < stepsList.size()) {
            return new ArrayList<>(stepsList.get(questionIndex));
        }
        return new ArrayList<>();
    }

    @Override
    public boolean checkAnswer(int questionIndex, String userAnswer) {
        if (questionIndex < 0 || questionIndex >= correctOrdersList.size()) {
            return false;
        }

        // Parsing della risposta dell'utente
        // Ci aspettiamo una stringa con numeri separati da virgole o spazi
        // es. "1,3,2,5,4" o "1 3 2 5 4"
        List<Integer> userOrder = new ArrayList<>();
        String[] parts = userAnswer.replaceAll("\\s+", ",").split(",");

        try {
            for (String part : parts) {
                if (!part.trim().isEmpty()) {
                    userOrder.add(Integer.parseInt(part.trim()));
                }
            }
        } catch (NumberFormatException e) {
            return false; // Formato non valido
        }

        // Controlliamo che ci siano abbastanza numeri
        if (userOrder.size() != correctOrdersList.get(questionIndex).size()) {
            return false;
        }

        // Controlliamo che i numeri siano validi (1-based)
        for (int num : userOrder) {
            if (num < 1 || num > correctOrdersList.get(questionIndex).size()) {
                return false;
            }
        }

        // Confrontiamo l'ordine dell'utente con l'ordine corretto
        List<String> shuffledSteps = stepsList.get(questionIndex);
        List<String> userOrderedSteps = new ArrayList<>();

        for (int index : userOrder) {
            userOrderedSteps.add(shuffledSteps.get(index - 1));
        }

        return userOrderedSteps.equals(correctOrdersList.get(questionIndex));
    }

    @Override
    public int getTotalQuestions() {
        return stepsList.size();
    }
}