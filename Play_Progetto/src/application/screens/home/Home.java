package application.screens.home;

import application.core.BaseScreen;
import application.core.NavigationManager;
import application.components.NavigationBar;
import application.core.StyleManager;
import application.screens.auth.Main;
import application.UserProgress;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Schermata Home dell'applicazione PLAY
 */
public class Home extends BaseScreen {

	public Home(Stage stage) {
		super(stage, 1200, 800); 
	}

	@Override
	protected NavigationBar createNavigationBar() {
		
		NavigationBar navbar = new NavigationBar(false); 

		
		Button homeButton = new Button("🏠 Home");
		Button progressButton = new Button("📊 I miei Progressi");
		Button logoutButton = new Button("👋 Logout");

		homeButton.getStyleClass().add("secondary");
		progressButton.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; " +
				"-fx-background-radius: 25; -fx-effect: dropshadow(gaussian, rgba(155, 89, 182, 0.4), 8, 0, 0, 4);");
		logoutButton.getStyleClass().add("secondary");

		
		progressButton.setOnAction(e -> NavigationManager.getInstance().goToUserProgress());
		logoutButton.setOnAction(e -> NavigationManager.getInstance().logout());

		return navbar;
	}

	@Override
	protected String getScreenTitle() {
		return "Ciao " + Main.getCurrentUser() + "! 👋";
	}

	@Override
	protected String getScreenDescription() {
		return "Pronto per una nuova avventura di programmazione? 🚀";
	}

	@Override
	protected void initializeContent() {
		VBox mainContent = createBeautifulHomeContent();

		
		VBox contentContainer = new VBox();
		contentContainer.getStyleClass().add("content-container");
		contentContainer.getChildren().add(mainContent);

		setCenter(contentContainer);
	}

	/**
	 * Crea il contenuto principale della Home
	 */
	private VBox createBeautifulHomeContent() {
		VBox centerBox = new VBox(40);
		centerBox.setAlignment(Pos.CENTER);
		centerBox.setPadding(new Insets(30));

		HBox statsBox = createStatsSection();
		
		Button startButton = createMainActionButton();

		Text motivationText = createMotivationalQuote();

		centerBox.getChildren().addAll(statsBox, startButton, motivationText);
		return centerBox;
	}

	
	private HBox createStatsSection() {
		HBox statsBox = new HBox(25);
		statsBox.setAlignment(Pos.CENTER);

		Map<String, String> userStats = calculateUserStats();

		VBox exercisesCard = createStatCard("🎯", "Esercizi", "Completati", userStats.get("total"), "#e74c3c");
		VBox streakCard = createStatCard("🔥", "Percentuale", "Successo", userStats.get("percentage") + "%", "#f39c12");
		VBox levelCard = createStatCard("⭐", "Livello", "Preferito", userStats.get("favorite"), "#9b59b6");
		VBox todayCard = createStatCard("📅", "Oggi", "Sessioni", userStats.get("today"), "#2ecc71");

		statsBox.getChildren().addAll(exercisesCard, streakCard, levelCard, todayCard);
		return statsBox;
	}

	
	private VBox createStatCard(String icon, String title, String subtitle, String value, String borderColor) {
		VBox card = new VBox(12);
		card.getStyleClass().add("stats-container");
		card.setAlignment(Pos.CENTER);
		card.setPrefWidth(180);
		card.setPrefHeight(140);

		
		card.setStyle(card.getStyle() + "-fx-border-color: " + borderColor + "; -fx-border-width: 0 0 4 0;");

		Text iconText = new Text(icon);
		iconText.setStyle("-fx-font-size: 36px;");

		Text valueText = new Text(value);
		valueText.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white; " +
				"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 3, 0, 0, 1);");

		Text titleText = new Text(title);
		titleText.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(255,255,255,0.9); -fx-font-weight: bold;");

		Text subtitleText = new Text(subtitle);
		subtitleText.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(255,255,255,0.8);");

		card.getChildren().addAll(iconText, valueText, titleText, subtitleText);
		return card;
	}

	
	private Button createMainActionButton() {
		Button startButton = new Button("🚀 Inizia gli Esercizi");
		startButton.setPrefWidth(320);
		startButton.setPrefHeight(65);
		startButton.getStyleClass().add("start-button");
		startButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		startButton.setOnAction(e -> NavigationManager.getInstance().goToExerciseGrid());

		return startButton;
	}

	private Text createMotivationalQuote() {
		String[] quotes = {
				"\"Il successo è la somma di piccoli sforzi ripetuti giorno dopo giorno\" 💪",
				"\"Ogni esperto è stato una volta un principiante\" 🌱",
				"\"Il codice è poesia, ogni bug è una metafora\" 🎭",
				"\"Programmare è come risolvere puzzle infiniti\" 🧩",
				"\"L'errore di oggi è la conoscenza di domani\" 🔮"
		};

		String randomQuote = quotes[(int) (Math.random() * quotes.length)];

		Text motivationText = new Text(randomQuote);
		motivationText.setStyle("-fx-font-size: 16px; -fx-font-style: italic; " +
				"-fx-text-fill: rgba(255,255,255,0.8); -fx-text-alignment: center;");
		motivationText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
		motivationText.setWrappingWidth(600);

		return motivationText;
	}

	
	private Map<String, String> calculateUserStats() {
		Map<String, String> stats = new HashMap<>();

		try {
			List<String> progressData = UserProgress.getUserProgress(Main.getCurrentUser());

			
			int totalExercises = progressData.size();
			int totalCorrect = 0;
			int totalQuestions = 0;
			Map<String, Integer> exerciseCount = new HashMap<>();
			int todaySessions = 0;

			String today = java.time.LocalDate.now().toString();

			for (String progress : progressData) {
				String[] parts = progress.split(",");
				if (parts.length >= 6) {
					totalCorrect += Integer.parseInt(parts[3]);
					totalQuestions += Integer.parseInt(parts[4]);

				
					String type = parts[1];
					exerciseCount.put(type, exerciseCount.getOrDefault(type, 0) + 1);

					
					if (parts.length >= 7 && parts[6].startsWith(today)) {
						todaySessions++;
					}
				}
			}

			
			String favoriteExercise = exerciseCount.entrySet().stream()
					.max(Map.Entry.comparingByValue())
					.map(entry -> getFriendlyExerciseName(entry.getKey()))
					.orElse("Nessuno");

			int percentage = totalQuestions > 0 ? (totalCorrect * 100 / totalQuestions) : 0;

			stats.put("total", String.valueOf(totalExercises));
			stats.put("percentage", String.valueOf(percentage));
			stats.put("favorite", favoriteExercise);
			stats.put("today", String.valueOf(todaySessions));

		} catch (Exception e) {
			
			stats.put("total", "0");
			stats.put("percentage", "0");
			stats.put("favorite", "Quiz");
			stats.put("today", "0");
		}

		return stats;
	}

	private String getFriendlyExerciseName(String internalName) {
		switch (internalName) {
			case "FindError": return "Debug";
			case "OrderSteps": return "Logica";
			case "WhatPrints": return "Output";
			case "quizEP": return "Quiz";
			case "CompleteCode": return "Coding";
			default: return "Misto";
		}
	}

	@Override
	protected void configureScene(Scene scene) {
		super.configureScene(scene);
		if (stage != null) {
			stage.setTitle("PLAY - Home");
		}
	}

	
	public static Scene getScene(Stage stage, Scene loginScene) {
		Home homeScreen = new Home(stage);
		return homeScreen.createScene();
	}
}