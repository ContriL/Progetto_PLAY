package application.screens.user;

import application.UserProgress;
import application.core.NavigationManager;
import application.core.StyleManager;
import application.screens.auth.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class UserProgressScreen {

    public static Scene getScene(Stage stage, Scene homeScene) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1200, 800);
        StyleManager.applyMainStyles(scene);

        // === NAVBAR BELLA ===
        HBox navBar = createModernNavBar();
        root.setTop(navBar);

        // === CONTENUTO PRINCIPALE ===
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox mainContent = createMainContent();
        scrollPane.setContent(mainContent);

        root.setCenter(scrollPane);

        stage.setTitle("PLAY - I tuoi Progressi");
        return scene;
    }

    private static HBox createModernNavBar() {
        HBox navBar = new HBox(20);
        navBar.setId("navBar");
        navBar.setPadding(new Insets(15, 25, 15, 25));
        navBar.setAlignment(Pos.CENTER_RIGHT);

        Button homeButton = new Button("🏠 Home");
        Button gridButton = new Button("🎯 Esercizi");
        Button logoutButton = new Button("👋 Logout");

        homeButton.getStyleClass().add("secondary");
        gridButton.getStyleClass().add("secondary");
        logoutButton.getStyleClass().add("secondary");

        homeButton.setOnAction(e -> NavigationManager.getInstance().goToHome());
        gridButton.setOnAction(e -> NavigationManager.getInstance().goToExerciseGrid());
        logoutButton.setOnAction(e -> NavigationManager.getInstance().logout());

        navBar.getChildren().addAll(homeButton, gridButton, logoutButton);
        return navBar;
    }

    private static VBox createMainContent() {
        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(30));
        mainContent.setAlignment(Pos.TOP_CENTER);

        // === HEADER BELLISSIMO ===
        VBox header = createHeader();

        // === STATISTICHE OVERVIEW ===
        HBox statsOverview = createStatsOverview();

        // === GRAFICI FIGHI ===
        HBox chartsSection = createChartsSection();

        // === TABELLA DETTAGLI ===
        VBox tableSection = createTableSection();

        mainContent.getChildren().addAll(header, statsOverview, chartsSection, tableSection);
        return mainContent;
    }

    private static VBox createHeader() {
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);

        Text title = new Text("📊 I tuoi Progressi");
        title.getStyleClass().add("main-title");

        Text subtitle = new Text("Ciao " + Main.getCurrentUser() + "! Ecco quanto sei migliorato! 🚀");
        subtitle.getStyleClass().add("section-title");
        subtitle.setStyle("-fx-font-size: 18px; -fx-text-fill: rgba(255,255,255,0.9);");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private static HBox createStatsOverview() {
        HBox statsBox = new HBox(25);
        statsBox.setAlignment(Pos.CENTER);

        // Calcola statistiche reali
        List<String> progressData = UserProgress.getUserProgress(Main.getCurrentUser());
        Map<String, Integer> stats = calculateStats(progressData);

        VBox totalCard = createStatCard("🎯", "Esercizi", "Completati", String.valueOf(stats.get("total")), "#e74c3c");
        VBox correctCard = createStatCard("✅", "Risposte", "Corrette", String.valueOf(stats.get("correct")), "#2ecc71");
        VBox streakCard = createStatCard("🔥", "Percentuale", "Successo", stats.get("percentage") + "%", "#f39c12");
        VBox levelCard = createStatCard("⭐", "Livello", "Medio", getLevelName(stats.get("avgLevel")), "#9b59b6");

        statsBox.getChildren().addAll(totalCard, correctCard, streakCard, levelCard);
        return statsBox;
    }

    private static VBox createStatCard(String icon, String title, String subtitle, String value, String color) {
        VBox card = new VBox(12);
        card.getStyleClass().add("stats-container");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        card.setPrefHeight(140);

        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 36px;");

        Text valueText = new Text(value);
        valueText.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 3, 0, 0, 1);");

        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(255,255,255,0.9); -fx-font-weight: bold;");

        Text subtitleText = new Text(subtitle);
        subtitleText.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(255,255,255,0.8);");

        // Bordo colorato
        card.setStyle(card.getStyle() + "-fx-border-color: " + color + "; -fx-border-width: 0 0 4 0;");

        card.getChildren().addAll(iconText, valueText, titleText, subtitleText);
        return card;
    }

    private static HBox createChartsSection() {
        HBox chartsBox = new HBox(30);
        chartsBox.setAlignment(Pos.CENTER);

        // === GRAFICO A TORTA ESERCIZI ===
        PieChart exerciseChart = createExerciseTypeChart();
        VBox pieChartContainer = createChartContainer("📈 Esercizi per Tipo", exerciseChart);

        // === GRAFICO A BARRE LIVELLI ===
        BarChart<String, Number> levelChart = createLevelChart();
        VBox barChartContainer = createChartContainer("📊 Prestazioni per Livello", levelChart);

        chartsBox.getChildren().addAll(pieChartContainer, barChartContainer);
        return chartsBox;
    }

    private static VBox createChartContainer(String title, javafx.scene.Node chart) {
        VBox container = new VBox(15);
        container.getStyleClass().add("content-container");
        container.setAlignment(Pos.CENTER);
        container.setPrefWidth(400);
        container.setPrefHeight(350);

        Text chartTitle = new Text(title);
        chartTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        container.getChildren().addAll(chartTitle, chart);
        return container;
    }

    private static PieChart createExerciseTypeChart() {
        List<String> progressData = UserProgress.getUserProgress(Main.getCurrentUser());
        Map<String, Integer> exerciseCount = new HashMap<>();

        // Conta esercizi per tipo
        for (String progress : progressData) {
            String[] parts = progress.split(",");
            if (parts.length >= 2) {
                String exerciseType = getExerciseDisplayName(parts[1]);
                exerciseCount.put(exerciseType, exerciseCount.getOrDefault(exerciseType, 0) + 1);
            }
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : exerciseCount.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Distribuzione Esercizi");
        chart.setLegendVisible(true);
        chart.setPrefSize(300, 250);

        return chart;
    }

    private static BarChart<String, Number> createLevelChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Livello Difficoltà");
        yAxis.setLabel("Percentuale Media");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Performance per Livello");
        chart.setPrefSize(300, 250);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Percentuale Successo");

        // Calcola performance per livello
        List<String> progressData = UserProgress.getUserProgress(Main.getCurrentUser());
        Map<String, List<Double>> levelPerformance = new HashMap<>();

        for (String progress : progressData) {
            String[] parts = progress.split(",");
            if (parts.length >= 6) {
                String level = "Livello " + parts[2];
                double percentage = Double.parseDouble(parts[6]);
                levelPerformance.computeIfAbsent(level, k -> new ArrayList<>()).add(percentage);
            }
        }

        for (Map.Entry<String, List<Double>> entry : levelPerformance.entrySet()) {
            double avgPercentage = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0);
            series.getData().add(new XYChart.Data<>(entry.getKey(), avgPercentage));
        }

        chart.getData().add(series);
        return chart;
    }

    private static VBox createTableSection() {
        VBox tableSection = new VBox(20);
        tableSection.setAlignment(Pos.CENTER);

        Text tableTitle = new Text("📋 Cronologia Dettagliata");
        tableTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);");

        TableView<ProgressEntry> table = createModernTable();

        VBox tableContainer = new VBox(15);
        tableContainer.getStyleClass().add("content-container");
        tableContainer.setPrefWidth(1000);
        tableContainer.getChildren().addAll(tableTitle, table);

        tableSection.getChildren().add(tableContainer);
        return tableSection;
    }

    @SuppressWarnings("unchecked")
    private static TableView<ProgressEntry> createModernTable() {
        TableView<ProgressEntry> table = new TableView<>();
        table.setPrefHeight(300);

        // Colonne stilizzate
        TableColumn<ProgressEntry, String> exerciseCol = new TableColumn<>("🎯 Esercizio");
        exerciseCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getExerciseType()));
        exerciseCol.setPrefWidth(150);

        TableColumn<ProgressEntry, String> levelCol = new TableColumn<>("⭐ Livello");
        levelCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDifficulty()));
        levelCol.setPrefWidth(100);

        TableColumn<ProgressEntry, String> scoreCol = new TableColumn<>("📊 Punteggio");
        scoreCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCorrectAnswers() + "/" + data.getValue().getTotalQuestions()));
        scoreCol.setPrefWidth(100);

        TableColumn<ProgressEntry, String> percentageCol = new TableColumn<>("📈 Percentuale");
        percentageCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPercentage()));
        percentageCol.setPrefWidth(120);

        TableColumn<ProgressEntry, String> statusCol = new TableColumn<>("🏆 Stato");
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        statusCol.setPrefWidth(120);

        TableColumn<ProgressEntry, String> dateCol = new TableColumn<>("📅 Data");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTimestamp()));
        dateCol.setPrefWidth(180);

        table.getColumns().addAll(exerciseCol, levelCol, scoreCol, percentageCol, statusCol, dateCol);

        // Carica dati
        List<String> progressData = UserProgress.getUserProgress(Main.getCurrentUser());
        ObservableList<ProgressEntry> entries = FXCollections.observableArrayList();

        for (String progress : progressData) {
            String[] parts = progress.split(",");
            if (parts.length >= 7) {
                entries.add(new ProgressEntry(
                        parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]),
                        Integer.parseInt(parts[4]), Double.parseDouble(parts[5]), parts[7]
                ));
            }
        }

        // Ordina per data (più recenti prima)
        entries.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        table.setItems(entries);

        return table;
    }

    // === METODI DI SUPPORTO ===

    private static Map<String, Integer> calculateStats(List<String> progressData) {
        Map<String, Integer> stats = new HashMap<>();
        int totalExercises = progressData.size();
        int totalCorrect = 0;
        int totalQuestions = 0;
        int totalLevels = 0;

        for (String progress : progressData) {
            String[] parts = progress.split(",");
            if (parts.length >= 6) {
                totalCorrect += Integer.parseInt(parts[3]);
                totalQuestions += Integer.parseInt(parts[4]);
                totalLevels += Integer.parseInt(parts[2]);
            }
        }

        int percentage = totalQuestions > 0 ? (totalCorrect * 100 / totalQuestions) : 0;
        int avgLevel = totalExercises > 0 ? (totalLevels / totalExercises) : 1;

        stats.put("total", totalExercises);
        stats.put("correct", totalCorrect);
        stats.put("percentage", percentage);
        stats.put("avgLevel", avgLevel);

        return stats;
    }

    private static String getExerciseDisplayName(String exerciseType) {
        switch (exerciseType) {
            case "FindError": return "🔍 Trova Errore";
            case "OrderSteps": return "🔄 Ordina Passi";
            case "WhatPrints": return "👁️ Cosa Stampa";
            case "quizEP": return "📝 Quiz EP";
            case "CompleteCode": return "💻 Completa Codice";
            default: return exerciseType;
        }
    }

    private static String getLevelName(int level) {
        switch (level) {
            case 1: return "Principiante";
            case 2: return "Intermedio";
            case 3: return "Avanzato";
            default: return "Base";
        }
    }

    // Classe per la tabella (già esistente nel codice originale)
    public static class ProgressEntry {
        private final String exerciseType;
        private final int difficulty;
        private final int correctAnswers;
        private final int totalQuestions;
        private final double percentage;
        private final String timestamp;

        public ProgressEntry(String exerciseType, int difficulty, int correctAnswers,
                             int totalQuestions, double percentage, String timestamp) {
            this.exerciseType = exerciseType;
            this.difficulty = difficulty;
            this.correctAnswers = correctAnswers;
            this.totalQuestions = totalQuestions;
            this.percentage = percentage;
            this.timestamp = timestamp;
        }

        public String getExerciseType() {
            return getExerciseDisplayName(exerciseType);
        }

        public String getDifficulty() {
            switch (difficulty) {
                case 1: return "🟢 Principiante";
                case 2: return "🟡 Intermedio";
                case 3: return "🔴 Avanzato";
                default: return String.valueOf(difficulty);
            }
        }

        public int getCorrectAnswers() { return correctAnswers; }
        public int getTotalQuestions() { return totalQuestions; }

        public String getPercentage() {
            return String.format("%.1f%%", percentage);
        }

        public String getTimestamp() { return timestamp; }

        public String getStatus() {
            if (percentage >= 80) return "🏆 Eccellente";
            else if (percentage >= 60) return "✅ Superato";
            else return "❌ Da migliorare";
        }
    }
}