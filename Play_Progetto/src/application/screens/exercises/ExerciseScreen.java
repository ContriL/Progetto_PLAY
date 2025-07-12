package application.screens.exercises;

import application.components.NavigationBar;
import application.components.FinalResultScreen;
import application.screens.auth.Main;
import application.UserProgress;
import application.core.StyleManager;
import application.core.NavigationManager;
import application.exercises.Exercise;
import application.exercises.FindErrorExercise;
import application.exercises.OrderStepsExercise;
import application.exercises.WhatPrintsExercise;
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
import application.core.DialogUtils;

import java.util.ArrayList;
import java.util.List;

// Schermata per i tre esercizi base del progetto
// Gestisce trova errore, ordina passi e cosa stampa
public class ExerciseScreen {

    public static Scene getScene(Stage stage, Scene selectionScene, Exercise exercise) {
        // Evito problemi con le variabili statiche usando array
        final int[] currentQuestionIndex = {0};
        final int[] correctAnswers = {0};

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1200, 800);

        StyleManager.applyMainStyles(scene);

        // Navbar personalizzata con gestione uscita durante esercizio
        NavigationBar navBar = new NavigationBar(true);

        // Se prova ad uscire durante l'esercizio chiedo conferma
        navBar.setBackAction(() -> {
            handleNavigation(exercise, currentQuestionIndex[0], correctAnswers[0], () -> {
                NavigationManager.getInstance().goToExerciseGrid();
            });
        });

        // Tutti i pulsanti della navbar devono chiedere conferma se esercizio in corso
        navBar.setButtonAction("home", () ->
                handleNavigation(exercise, currentQuestionIndex[0], correctAnswers[0], () ->
                        NavigationManager.getInstance().goToHome()));

        navBar.setButtonAction("progress", () ->
                handleNavigation(exercise, currentQuestionIndex[0], correctAnswers[0], () ->
                        NavigationManager.getInstance().goToUserProgress()));

        navBar.setButtonAction("profile", () ->
                handleNavigation(exercise, currentQuestionIndex[0], correctAnswers[0], () ->
                        NavigationManager.getInstance().goToProfile()));

        navBar.setButtonAction("logout", () ->
                handleNavigation(exercise, currentQuestionIndex[0], correctAnswers[0], () ->
                        NavigationManager.getInstance().logout()));

        VBox topContainer = new VBox();
        topContainer.getChildren().add(navBar);

        // Header dell'esercizio
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

        // Contatore domande
        Label questionCountLabel = new Label("Domanda " + (currentQuestionIndex[0] + 1) + " di " + exercise.getTotalQuestions());
        questionCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Qui mostro il codice dell'esercizio
        TextArea codeSnippetArea = new TextArea();
        codeSnippetArea.setEditable(false);
        codeSnippetArea.setPrefRowCount(12);
        codeSnippetArea.setFont(Font.font("Monospaced", 13));
        codeSnippetArea.setWrapText(true);

        List<String> questions = exercise.getQuestions();
        if (!questions.isEmpty()) {
            codeSnippetArea.setText(questions.get(currentQuestionIndex[0]));
        }

        VBox answerBox = new VBox(10);
        Label answerLabel = new Label("La tua risposta:");

        final Control[] answerControlRef = new Control[1];

        // Creo controlli diversi per ogni tipo di esercizio
        if (exercise instanceof WhatPrintsExercise) {
            TextArea answerArea = new TextArea();
            answerArea.setPromptText("Scrivi l'output atteso, una riga per ogni println");
            answerArea.setWrapText(true);
            answerArea.setPrefRowCount(4);
            answerControlRef[0] = answerArea;
        } else if (exercise instanceof OrderStepsExercise) {
            answerControlRef[0] = createOrderStepsControl((OrderStepsExercise) exercise, currentQuestionIndex[0]);
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

        // Barra dei pulsanti in basso
        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(15));

        Button submitButton = new Button("Verifica");
        Button retryButton = new Button("Riprova");
        Button nextButton = new Button("Prossima domanda");
        Button backButton = new Button("Torna alla selezione");

        nextButton.setDisable(true);
        retryButton.setDisable(true);

        // Attivo verifica solo quando ha scritto qualcosa
        if (answerControlRef[0] instanceof TextField) {
            TextField textField = (TextField) answerControlRef[0];
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (submitButton.isDisabled() && !textField.getText().isEmpty()) {
                    submitButton.setDisable(false);
                }
            });
        }

        // Quando clicca verifica controllo la risposta
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

            boolean isCorrect = exercise.checkAnswer(currentQuestionIndex[0], userAnswer);

            if (isCorrect) {
                resultText.setText("Corretto!");
                resultText.setFill(Color.GREEN);
                correctAnswers[0]++;
                retryButton.setDisable(true);
            } else {
                resultText.setText("Sbagliato. Riprova o passa alla prossima domanda.");
                resultText.setFill(Color.RED);
                retryButton.setDisable(false);
            }

            submitButton.setDisable(true);
            nextButton.setDisable(false);
        });

        // Reset per riprovare la domanda
        retryButton.setOnAction(e -> {
            if (answerControlRef[0] instanceof TextField) {
                ((TextField) answerControlRef[0]).clear();
            } else if (answerControlRef[0] instanceof TextArea) {
                ((TextArea) answerControlRef[0]).clear();
            } else if (answerControlRef[0] instanceof ListView) {
                VBox parent = (VBox) answerControlRef[0].getParent();
                int controlIndex = parent.getChildren().indexOf(answerControlRef[0]);
                parent.getChildren().remove(answerControlRef[0]);

                answerControlRef[0] = createOrderStepsControl((OrderStepsExercise) exercise, currentQuestionIndex[0]);
                parent.getChildren().add(controlIndex, answerControlRef[0]);
            }

            resultText.setText("");
            submitButton.setDisable(false);
            retryButton.setDisable(true);
        });

        // Passo alla prossima domanda o mostro risultati finali
        nextButton.setOnAction(e -> {
            currentQuestionIndex[0]++;

            if (currentQuestionIndex[0] < exercise.getTotalQuestions()) {
                // Carico la prossima domanda
                codeSnippetArea.setText(questions.get(currentQuestionIndex[0]));
                questionCountLabel.setText("Domanda " + (currentQuestionIndex[0] + 1) + " di " + exercise.getTotalQuestions());

                // Pulisco i controlli per la prossima domanda
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

                    answerControlRef[0] = createOrderStepsControl((OrderStepsExercise) exercise, currentQuestionIndex[0]);
                    parent.getChildren().add(controlIndex, answerControlRef[0]);
                }

                resultText.setText("");
                submitButton.setDisable(false);
                nextButton.setDisable(true);
                retryButton.setDisable(true);

            } else {
                // Finito! Uso la nuova classe per la schermata finale
                String exerciseType = getExerciseType(exercise);
                String currentUser = Main.getCurrentUser();
                boolean saved = UserProgress.saveProgress(
                        currentUser,
                        exerciseType,
                        exercise.getDifficulty(),
                        correctAnswers[0],
                        exercise.getTotalQuestions()
                );

                // Sostituisco tutto con la schermata finale
                root.getChildren().clear();
                root.setTop(topContainer); // Tengo solo la navbar

                ScrollPane finalScreen = FinalResultScreen.createFinalScreen(exercise, correctAnswers[0], exercise.getTotalQuestions(), saved);
                FinalResultScreen.setupActions(finalScreen, exercise, stage, selectionScene);

                root.setCenter(finalScreen);
                // NON aggiungo più buttonBar in basso!
            }
        });

        // Pulsante indietro con conferma se esercizio in corso
        backButton.setOnAction(e -> {
            handleNavigation(exercise, currentQuestionIndex[0], correctAnswers[0], () -> {
                stage.setScene(selectionScene);
            });
        });

        buttonBar.getChildren().addAll(submitButton, retryButton, nextButton, backButton);
        root.setBottom(buttonBar);

        return scene;
    }

    // Roba originale che c'era già

    // Popup di conferma quando esce durante esercizio
    private static void showExitConfirmation(Exercise exercise, int currentQuestionIndex,
                                             int correctAnswers, Runnable onConfirm) {
        boolean isCompleted = (currentQuestionIndex >= exercise.getTotalQuestions());

        DialogUtils.showExerciseExitConfirmation(
                exercise,
                isCompleted,
                correctAnswers,
                onConfirm
        );
    }

    // Controllo se deve chiedere conferma prima di navigare
    private static void handleNavigation(Exercise exercise, int currentQuestionIndex,
                                         int correctAnswers, Runnable navigationAction) {
        boolean isCompleted = (currentQuestionIndex >= exercise.getTotalQuestions());

        if (isCompleted) {
            // Se ha finito può navigare liberamente
            navigationAction.run();
        } else {
            // Altrimenti chiedo conferma
            showExitConfirmation(exercise, currentQuestionIndex, correctAnswers, navigationAction);
        }
    }

    // Converto l'esercizio in stringa per salvarlo
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

    // Lista con drag and drop per l'esercizio ordina passi
    private static ListView<String> createOrderStepsControl(OrderStepsExercise exercise, int questionIndex) {
        ListView<String> stepsListView = new ListView<>();
        stepsListView.setPrefHeight(200);

        List<String> steps = exercise.getStepsForQuestion(questionIndex);
        List<String> numberedSteps = new ArrayList<>();

        // Metto i numeri davanti ai passi
        for (int i = 0; i < steps.size(); i++) {
            numberedSteps.add((i + 1) + ". " + steps.get(i));
        }

        stepsListView.getItems().clear();
        stepsListView.getItems().addAll(numberedSteps);

        // Imposto drag and drop per riordinare
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

            // Tutto il casino per il drag and drop
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