package application;

import application.exercises.Exercise;
import application.exercises.OrderStepsExercise;
import application.exercises.WhatPrintsExercise;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExerciseScreen {

    private static int currentQuestionIndex = 0;
    private static int correctAnswers = 0;

    public static Scene getScene(Stage stage, Scene selectionScene, Exercise exercise) {
        // Reset contatori per una nuova sessione di esercizi
        currentQuestionIndex = 0;
        correctAnswers = 0;

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 700, 550);

        // Configurazione CSS con percorso relativo
        try {
            String cssPath = ExerciseScreen.class.getResource("/application/application.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Errore nel caricamento del file CSS: " + e.getMessage());
        }

            // Barra di navigazione superiore
        HBox navBar = new HBox(15);
        navBar.setId("navBar");
        navBar.setPadding(new Insets(10));
        navBar.setAlignment(Pos.CENTER_RIGHT);
        navBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        // Pulsanti di navigazione
        Button homeButton = new Button("Home");
        Button progressButton = new Button("I miei Progressi");
        Button logoutButton = new Button("Logout");
        

        // Gestione eventi dei pulsanti di navigazione
        homeButton.setOnAction(e -> {
            Scene homeScene = Home.getScene(stage, selectionScene);
            stage.setScene(homeScene);
        });

        progressButton.setOnAction(e -> {
            Scene progressScene = UserProgressScreen.getScene(stage, selectionScene);
            stage.setScene(progressScene);
        });

        logoutButton.setOnAction(e -> {
            Main.setCurrentUser("");
	        Scene loginScene = Main.getLoginScene(stage);
	        stage.setScene(loginScene);
        });


        navBar.getChildren().addAll(homeButton, progressButton, logoutButton);

        VBox topContainer = new VBox();
        topContainer.getChildren().add(navBar);

        // Intestazione
        Text headerText = new Text(exercise.getTitle() + " - Livello " + exercise.getDifficulty());
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Text descriptionText = new Text(exercise.getDescription());

        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15));
        headerBox.getChildren().addAll(headerText, descriptionText);

        topContainer.getChildren().add(headerBox);
        root.setTop(topContainer);

        // Contenuto principale
        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(15));

        // Contatore domande
        Label questionCountLabel = new Label("Domanda " + (currentQuestionIndex + 1) + " di " + exercise.getTotalQuestions());
        questionCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Area per visualizzare l'esercizio
        TextArea codeSnippetArea = new TextArea();
        codeSnippetArea.setEditable(false);
        codeSnippetArea.setPrefRowCount(12);
        codeSnippetArea.setFont(Font.font("Monospaced", 13));
        codeSnippetArea.setWrapText(true);

        // Carica il primo esercizio
        List<String> questions = exercise.getQuestions();
        if (!questions.isEmpty()) {
            codeSnippetArea.setText(questions.get(currentQuestionIndex));
        }

        // Area per la risposta dell'utente (varia in base al tipo di esercizio)
        VBox answerBox = new VBox(10);
        Label answerLabel = new Label("La tua risposta:");

        // Riferimento finale a Control (usato nei lambda)
        final Control[] answerControlRef = new Control[1];

        // Diverse interfacce per diversi tipi di esercizi
        if (exercise instanceof WhatPrintsExercise) {
            TextArea answerArea = new TextArea();
            answerArea.setPromptText("Scrivi l'output atteso, una riga per ogni println");
            answerArea.setWrapText(true);
            answerArea.setPrefRowCount(4);
            answerControlRef[0] = answerArea;
        } else if (exercise instanceof OrderStepsExercise) {
            // Crea il controllo per l'ordinamento dei passi
            answerControlRef[0] = createOrderStepsControl((OrderStepsExercise) exercise, currentQuestionIndex);
        } else {
            TextField answerField = new TextField();
            answerField.setPromptText("Inserisci la tua risposta qui");
            answerControlRef[0] = answerField;
        }

        answerBox.getChildren().addAll(answerLabel, answerControlRef[0]);

        // Area per i messaggi di risultato
        Text resultText = new Text();
        resultText.setFill(Color.GREEN);

        // Aggiunta dei componenti all'area del contenuto
        contentBox.getChildren().addAll(questionCountLabel, codeSnippetArea, answerBox, resultText);
        root.setCenter(contentBox);

        // Barra dei pulsanti
        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(15));

        Button submitButton = new Button("Verifica");
        Button retryButton = new Button("Riprova");
        Button nextButton = new Button("Prossima domanda");
        Button backButton = new Button("Torna alla selezione");

        nextButton.setDisable(true);
        retryButton.setDisable(true);

        // Aggiungi listener per abilitare il pulsante Verifica quando si cambia il testo
        if (answerControlRef[0] instanceof TextField) {
            TextField textField = (TextField) answerControlRef[0];
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (submitButton.isDisabled() && !textField.getText().isEmpty()) {
                    submitButton.setDisable(false);
                }
            });
        }

        submitButton.setOnAction(e -> {
            String userAnswer = "";

            if (answerControlRef[0] instanceof TextField) {
                userAnswer = ((TextField) answerControlRef[0]).getText().trim();
            } else if (answerControlRef[0] instanceof TextArea) {
                userAnswer = ((TextArea) answerControlRef[0]).getText().trim();
            } else if (answerControlRef[0] instanceof ListView) {
                @SuppressWarnings("unchecked")
                ListView<String> listView = (ListView<String>) answerControlRef[0];

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < listView.getItems().size(); i++) {
                    if (i > 0) sb.append(",");
                    // Estrai solo il numero dal formato "X. testo"
                    String item = listView.getItems().get(i);
                    int dotIndex = item.indexOf('.');
                    if (dotIndex > 0) {
                        sb.append(item.substring(0, dotIndex));
                    } else {
                        sb.append(item); // Fallback
                    }
                }
                userAnswer = sb.toString();
            }

            boolean isCorrect = exercise.checkAnswer(currentQuestionIndex, userAnswer);


            if (isCorrect) {
                resultText.setText("Corretto!");
                resultText.setFill(Color.GREEN);
                correctAnswers++;
                retryButton.setDisable(true);
            } else {
                resultText.setText("Sbagliato. Riprova o passa alla prossima domanda.");
                resultText.setFill(Color.RED);
                retryButton.setDisable(false);
            }

            submitButton.setDisable(true);
            nextButton.setDisable(false);
        });

        retryButton.setOnAction(e -> {
            if (answerControlRef[0] instanceof TextField) {
                ((TextField) answerControlRef[0]).clear();
            } else if (answerControlRef[0] instanceof TextArea) {
                ((TextArea) answerControlRef[0]).clear();
            } else if (answerControlRef[0] instanceof ListView) {
                VBox parent = (VBox) answerControlRef[0].getParent();
                int controlIndex = parent.getChildren().indexOf(answerControlRef[0]);
                parent.getChildren().remove(answerControlRef[0]);

                answerControlRef[0] = createOrderStepsControl((OrderStepsExercise) exercise, currentQuestionIndex);
                parent.getChildren().add(controlIndex, answerControlRef[0]);
            }

            resultText.setText("");
            submitButton.setDisable(false);
            retryButton.setDisable(true);
        });


        nextButton.setOnAction(e -> {
            currentQuestionIndex++;

            if (currentQuestionIndex < exercise.getTotalQuestions()) {
                // Carica la prossima domanda
                codeSnippetArea.setText(questions.get(currentQuestionIndex));
                questionCountLabel.setText("Domanda " + (currentQuestionIndex + 1) + " di " + exercise.getTotalQuestions());

                // Reset dell'area di risposta
                if (answerControlRef[0] instanceof TextField) {
                    ((TextField) answerControlRef[0]).clear();

                    // Ricolleghiamo il listener per questa nuova istanza
                    TextField textField = (TextField) answerControlRef[0];
                    textField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (submitButton.isDisabled() && !textField.getText().isEmpty()) {
                            submitButton.setDisable(false);
                        }
                    });

                } else if (answerControlRef[0] instanceof TextArea) {
                    ((TextArea) answerControlRef[0]).clear();

                } else if (answerControlRef[0] instanceof ListView) {
                    // Aggiorna la ListView per la nuova domanda
                    VBox parent = (VBox) answerControlRef[0].getParent();
                    int controlIndex = parent.getChildren().indexOf(answerControlRef[0]);
                    parent.getChildren().remove(answerControlRef[0]);

                    answerControlRef[0] = createOrderStepsControl((OrderStepsExercise) exercise, currentQuestionIndex);
                    parent.getChildren().add(controlIndex, answerControlRef[0]);
                }


                resultText.setText("");
                submitButton.setDisable(false);
                nextButton.setDisable(true);
                retryButton.setDisable(true);

            } else {
                // Esercizio completato
                contentBox.getChildren().clear();

                // Ottieni il tipo di esercizio
                String exerciseType = "";
                if (exercise instanceof application.exercises.FindErrorExercise) {
                    exerciseType = "FindError";
                } else if (exercise instanceof application.exercises.OrderStepsExercise) {
                    exerciseType = "OrderSteps";
                } else if (exercise instanceof application.exercises.WhatPrintsExercise) {
                    exerciseType = "WhatPrints";
                }

                // Salva il progresso dell'utente (usando il nickname dell'utente corrente)
                String currentUser = Main.getCurrentUser();
                boolean saved = UserProgress.saveProgress(
                        currentUser,
                        exerciseType,
                        exercise.getDifficulty(),
                        correctAnswers,
                        exercise.getTotalQuestions()
                );

                Text completionText = new Text("Esercizio completato!");
                completionText.setFont(Font.font("Arial", FontWeight.BOLD, 18));

                Text scoreText = new Text("Hai risposto correttamente a " + correctAnswers +
                        " domande su " + exercise.getTotalQuestions() + ".");
                scoreText.setFill(Color.GREEN);

                Text progressText = new Text(saved ?
                        "I tuoi progressi sono stati salvati." :
                        "Non è stato possibile salvare i progressi.");
                progressText.setFill(saved ? Color.GREEN : Color.RED);

                // Calcola la percentuale di successo
                double percentage = (double) correctAnswers / exercise.getTotalQuestions() * 100;
                Text percentageText = new Text(String.format("Percentuale di successo: %.1f%%", percentage));

                // Messaggio sul risultato del livello
                Text resultLevelText = new Text();
                if (percentage >= 70) {
                    resultLevelText.setText("Congratulazioni! Hai superato questo livello.");
                    resultLevelText.setFill(Color.GREEN);
                } else {
                    resultLevelText.setText("Continua a esercitarti per migliorare il tuo punteggio.");
                    resultLevelText.setFill(Color.ORANGE);
                }

                Button newExerciseButton = new Button("Scegli un nuovo esercizio");
                newExerciseButton.setOnAction(event -> stage.setScene(selectionScene));

                contentBox.getChildren().addAll(
                        completionText,
                        scoreText,
                        percentageText,
                        resultLevelText,
                        progressText,
                        newExerciseButton
                );

                // Disabilita i pulsanti non necessari
                submitButton.setDisable(true);
                nextButton.setDisable(true);
                retryButton.setDisable(true);
            }
        });

        backButton.setOnAction(e -> stage.setScene(selectionScene));

        // Aggiungiamo tutti i pulsanti, incluso il pulsante Riprova
        buttonBar.getChildren().addAll(submitButton, retryButton, nextButton, backButton);
        root.setBottom(buttonBar);

        return scene;
    }

    // Crea un controllo per l'esercizio "Ordina i Passi"
    private static ListView<String> createOrderStepsControl(OrderStepsExercise exercise, int questionIndex) {
        ListView<String> stepsListView = new ListView<>();
        stepsListView.setPrefHeight(200);

        List<String> steps = exercise.getStepsForQuestion(questionIndex);
        List<String> numberedSteps = new ArrayList<>();

        for (int i = 0; i < steps.size(); i++) {
            numberedSteps.add((i + 1) + ". " + steps.get(i));
        }

        // Assicurati di svuotare la lista prima di aggiungere i nuovi passi
        stepsListView.getItems().clear();
        stepsListView.getItems().addAll(numberedSteps);

        stepsListView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Label icon = new Label("≡");
                        icon.setStyle("-fx-font-weight: bold; -fx-padding: 0 10 0 0;");
                        Label text = new Label(item);
                        HBox box = new HBox(5, icon, text);
                        setGraphic(box);
                        setText(null); // Importante: non impostare il testo ma solo il grafico
                    }
                }
            };

            // Gestione del drag & drop
            cell.setOnDragDetected(event -> {
                if (!cell.isEmpty()) {
                    Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(cell.getItem());
                    db.setContent(content);

                    // Salva l'indice dell'elemento che si sta spostando
                    stepsListView.getProperties().put("draggedIndex", cell.getIndex());

                    event.consume();
                }
            });

            cell.setOnDragOver(event -> {
                if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
                }
            });

            cell.setOnDragEntered(event -> {
                if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
                    cell.setStyle("-fx-background-color: lightgray;");
                }
            });

            cell.setOnDragExited(event -> {
                cell.setStyle("");
            });

            cell.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;

                if (db.hasString()) {
                    Integer draggedIdx = (Integer) stepsListView.getProperties().get("draggedIndex");
                    int thisIdx = cell.getIndex();

                    if (draggedIdx != null && draggedIdx != thisIdx) {
                        String draggedItem = stepsListView.getItems().remove(draggedIdx.intValue());

                        // Se l'elemento è trascinato più in basso, aggiustare l'indice
                        if (thisIdx > draggedIdx) {
                            thisIdx--;
                        }

                        // Inserisci nel punto esatto
                        stepsListView.getItems().add(thisIdx, draggedItem);
                        stepsListView.getSelectionModel().select(thisIdx);
                        success = true;
                    }
                }

                event.setDropCompleted(success);
                event.consume();
            });

            return cell;
        });

        return stepsListView;
    }
}