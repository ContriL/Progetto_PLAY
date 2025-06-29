package application.screens.exercises;

import application.components.NavigationBar;
import application.screens.auth.Main;
import application.UserProgress;
import application.screens.user.UserProgressScreen;
import application.core.StyleManager;
import application.exercises.Exercise;
import application.exercises.FindErrorExercise;
import application.exercises.OrderStepsExercise;
import application.exercises.WhatPrintsExercise;
import application.screens.home.Home;
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

import java.util.ArrayList;
import java.util.List;

public class ExerciseScreen {

    private static int currentQuestionIndex = 0;
    private static int correctAnswers = 0;

    public static Scene getScene(Stage stage, Scene selectionScene, Exercise exercise) {
        
        currentQuestionIndex = 0;
        correctAnswers = 0;

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1200, 800);

        
        StyleManager.applyMainStyles(scene);

        
        NavigationBar navBar = NavigationBar.forSubScreens("grid");

        VBox topContainer = new VBox();
        topContainer.getChildren().add(navBar);

        
        Text headerText = new Text(exercise.getTitle() + " - Livello " + exercise.getDifficulty());
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Text descriptionText = new Text(exercise.getDescription());

        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15));
        headerBox.getChildren().addAll(headerText, descriptionText);

        topContainer.getChildren().add(headerBox);
        root.setTop(topContainer);

        
        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(15));

        
        Label questionCountLabel = new Label("Domanda " + (currentQuestionIndex + 1) + " di " + exercise.getTotalQuestions());
        questionCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        
        TextArea codeSnippetArea = new TextArea();
        codeSnippetArea.setEditable(false);
        codeSnippetArea.setPrefRowCount(12);
        codeSnippetArea.setFont(Font.font("Monospaced", 13));
        codeSnippetArea.setWrapText(true);

        
        List<String> questions = exercise.getQuestions();
        if (!questions.isEmpty()) {
            codeSnippetArea.setText(questions.get(currentQuestionIndex));
        }

        
        VBox answerBox = new VBox(10);
        Label answerLabel = new Label("La tua risposta:");

        
        final Control[] answerControlRef = new Control[1];

        
        if (exercise instanceof WhatPrintsExercise) {
            TextArea answerArea = new TextArea();
            answerArea.setPromptText("Scrivi l'output atteso, una riga per ogni println");
            answerArea.setWrapText(true);
            answerArea.setPrefRowCount(4);
            answerControlRef[0] = answerArea;
        } else if (exercise instanceof OrderStepsExercise) {
            
            answerControlRef[0] = createOrderStepsControl((OrderStepsExercise) exercise, currentQuestionIndex);
        } else {
            TextField answerField = new TextField();
            answerField.setPromptText("Inserisci la tua risposta qui");
            answerControlRef[0] = answerField;
        }

        answerBox.getChildren().addAll(answerLabel, answerControlRef[0]);

        
        Text resultText = new Text();
        resultText.setFill(Color.GREEN);

        
        contentBox.getChildren().addAll(questionCountLabel, codeSnippetArea, answerBox, resultText);
        root.setCenter(contentBox);

        
        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(15));

        Button submitButton = new Button("Verifica");
        Button retryButton = new Button("Riprova");
        Button nextButton = new Button("Prossima domanda");
        Button backButton = new Button("Torna alla selezione");

        nextButton.setDisable(true);
        retryButton.setDisable(true);

        
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
                    
                    String item = listView.getItems().get(i);
                    int dotIndex = item.indexOf('.');
                    if (dotIndex > 0) {
                        sb.append(item.substring(0, dotIndex));
                    } else {
                        sb.append(item); 
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
                
                codeSnippetArea.setText(questions.get(currentQuestionIndex));
                questionCountLabel.setText("Domanda " + (currentQuestionIndex + 1) + " di " + exercise.getTotalQuestions());

                
                if (answerControlRef[0] instanceof TextField) {
                    ((TextField) answerControlRef[0]).clear();

                    
                    TextField textField = (TextField) answerControlRef[0];
                    textField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (submitButton.isDisabled() && !textField.getText().isEmpty()) {
                            submitButton.setDisable(false);
                        }
                    });

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
                nextButton.setDisable(true);
                retryButton.setDisable(true);

            } else {
                
                contentBox.getChildren().clear();

                
                String exerciseType = getExerciseType(exercise);

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
                if (percentage >= 60) {
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

                
                submitButton.setDisable(true);
                nextButton.setDisable(true);
                retryButton.setDisable(true);
            }
        });

        
        backButton.setOnAction(e -> {
            showExitConfirmation(() -> {
                
                if (currentQuestionIndex > 0 || correctAnswers > 0) {
                    savePartialProgress(exercise, correctAnswers, currentQuestionIndex + 1);
                }
                stage.setScene(selectionScene);
            });
        });

        
        buttonBar.getChildren().addAll(submitButton, retryButton, nextButton, backButton);
        root.setBottom(buttonBar);

        return scene;
    }

    
    private static void showExitConfirmation(Runnable onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Uscita");
        alert.setHeaderText("⚠️ Attenzione: stai per uscire dall'esercizio");
        alert.setContentText("I tuoi progressi verranno salvati automaticamente.\n\n" +
                "Vuoi continuare e uscire dall'esercizio?");

        
        ButtonType confirmButton = new ButtonType("✅ Sì, salva ed esci", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("❌ No, continua esercizio", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        
        alert.getDialogPane().getStylesheets().add(
                ExerciseScreen.class.getResource("/application/application.css").toExternalForm()
        );

        alert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                onConfirm.run();
            }
            
        });
    }

    // Salva progresso parziale con percentuale
    private static void savePartialProgress(Exercise exercise, int correctAnswers, int questionsAttempted) {
        String exerciseType = getExerciseType(exercise);
        String currentUser = Main.getCurrentUser();

        
        boolean saved = UserProgress.saveProgress(
                currentUser,
                exerciseType,
                exercise.getDifficulty(),
                correctAnswers,                    
                exercise.getTotalQuestions()      
        );

        if (saved) {
            System.out.println("✅ Progresso parziale salvato: " + correctAnswers + "/" + exercise.getTotalQuestions() +
                    " (fatto " + questionsAttempted + " domande)");
        }
    }

    // Estrae il tipo di esercizio 
    private static String getExerciseType(Exercise exercise) {
        if (exercise instanceof FindErrorExercise) {
            return "FindError";
        } else if (exercise instanceof OrderStepsExercise) {
            return "OrderSteps";
        } else if (exercise instanceof WhatPrintsExercise) {
            return "WhatPrints";
        }
        return "Unknown";
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
                        setText(null);
                    }
                }
            };

            
            cell.setOnDragDetected(event -> {
                if (!cell.isEmpty()) {
                    Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(cell.getItem());
                    db.setContent(content);

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

                        if (thisIdx > draggedIdx) {
                            thisIdx--;
                        }

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