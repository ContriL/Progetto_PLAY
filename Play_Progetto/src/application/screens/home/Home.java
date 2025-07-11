package application.screens.home;

import application.core.BaseScreen;
import application.core.NavigationManager;
import application.components.NavigationBar;
import application.screens.auth.Main;
import application.UserProgress;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

// Schermata principale dell'applicazione
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
		progressButton.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-background-radius: 25; -fx-effect: dropshadow(gaussian, rgba(155, 89, 182, 0.4), 8, 0, 0, 4);");
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
		VBox content = new VBox();
		content.getStyleClass().add("content-container");
		content.getChildren().add(createMainContent());
		setCenter(content);
	}

	private VBox createMainContent() {
		VBox centerBox = new VBox(40);
		centerBox.setAlignment(Pos.CENTER);
		centerBox.setPadding(new Insets(30));

		HBox statsBox = createStatsSection();
		Button startButton = createMainActionButton();
		Text motivationText = createMotivationalQuote();

		centerBox.getChildren().addAll(statsBox, startButton, motivationText);
		return centerBox;
	}

	// Statistiche utente
	private HBox createStatsSection() {
		HBox statsBox = new HBox(25);
		statsBox.setAlignment(Pos.CENTER);
		Map<String, String> stats = calculateUserStats();

		VBox ex = createStatCard("🎯", "Esercizi", "Completati", stats.get("total"), "#e74c3c");
		VBox pct = createStatCard("🔥", "Percentuale", "Successo", stats.get("percentage") + "%", "#f39c12");
		VBox fav = createStatCard("💡", "Esercizio", "Preferito", stats.get("favorite"), "#9b59b6");
		VBox today = createStatCard("📅", "Oggi", "Sessioni", stats.get("today"), "#2ecc71");

		statsBox.getChildren().addAll(ex, pct, fav, today);
		return statsBox;
	}

	// Card singola statistica
	private VBox createStatCard(String icon, String title, String subtitle, String value, String borderColor) {
		VBox card = new VBox(12);
		card.getStyleClass().add("stats-container");
		card.setAlignment(Pos.CENTER);
		card.setPrefWidth(180);
		card.setPrefHeight(140);
		card.setStyle("-fx-border-color: " + borderColor + "; -fx-border-width: 0 0 4 0;");

		Text iconText = new Text(icon);
		iconText.setStyle("-fx-font-size: 36px;");
		Text valueText = new Text(value);
		valueText.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 3, 0, 0, 1);");
		Text titleText = new Text(title);
		titleText.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(255,255,255,0.9); -fx-font-weight: bold;");
		Text subtitleText = new Text(subtitle);
		subtitleText.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(255,255,255,0.8);");

		card.getChildren().addAll(iconText, valueText, titleText, subtitleText);
		return card;
	}

	// Pulsante "Inizia gli esercizi"
	private Button createMainActionButton() {
		Button btn = new Button("🚀 Inizia gli Esercizi");
		btn.setPrefWidth(320);
		btn.setPrefHeight(65);
		btn.getStyleClass().add("start-button");
		btn.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
		btn.setOnAction(e -> NavigationManager.getInstance().goToExerciseGrid());
		return btn;
	}

	// Citazione motivazionale casuale
	private Text createMotivationalQuote() {
		String[] quotes = {
				"\"Il successo è la somma di piccoli sforzi ripetuti giorno dopo giorno\" 💪",
				"\"Ogni esperto è stato una volta un principiante\" 🌱",
				"\"Il codice è poesia, ogni bug è una metafora\" 🎭",
				"\"Programmare è come risolvere puzzle infiniti\" 🧩",
				"\"L'errore di oggi è la conoscenza di domani\" 🔮"
		};
		Text t = new Text(quotes[(int) (Math.random() * quotes.length)]);
		t.setStyle("-fx-font-size: 16px; -fx-font-style: italic; -fx-text-fill: rgba(255,255,255,0.8);");
		t.setWrappingWidth(600);
		t.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
		return t;
	}


	// Calcola le statistiche dell'utente dai dati di progresso salvati.

	private Map<String, String> calculateUserStats() {
		Map<String, String> stats = new HashMap<>();

		try {
			List<String> data = UserProgress.getUserProgress(Main.getCurrentUser());
			int total = data.size(), correct = 0, totalQ = 0, today = 0;
			Map<String, Integer> types = new HashMap<>();
			Map<Integer, Integer> levels = new HashMap<>(); // Per contare i livelli
			String todayDate = java.time.LocalDate.now().toString();

			for (String row : data) {
				String[] p = row.split(",");
				if (p.length >= 6) {
					correct += Integer.parseInt(p[3]);
					totalQ += Integer.parseInt(p[4]);
					types.put(p[1], types.getOrDefault(p[1], 0) + 1);

					// Conta anche i livelli di difficoltà
					int level = Integer.parseInt(p[2]);
					levels.put(level, levels.getOrDefault(level, 0) + 1);

					// Controlla sessioni di oggi
					if (p.length >= 8 && p[7].startsWith(todayDate)) {
						today++;
					}
				}
			}

			// Trova esercizio preferito
			String favExercise = types.entrySet().stream()
					.max(Map.Entry.comparingByValue())
					.map(e -> getFriendlyExerciseName(e.getKey())).orElse("Nessuno");

			// Trova livello preferito
			String favLevel = levels.entrySet().stream()
					.max(Map.Entry.comparingByValue())
					.map(e -> getLevelName(e.getKey())).orElse("Principiante");

			int pct = totalQ > 0 ? (correct * 100 / totalQ) : 0;

			stats.put("total", String.valueOf(total));
			stats.put("percentage", String.valueOf(pct));
			stats.put("favorite", favExercise);
			stats.put("today", String.valueOf(today));

		} catch (Exception e) {
			stats.put("total", "0");
			stats.put("percentage", "0");
			stats.put("favorite", "Principiante");
			stats.put("today", "0");
		}

		return stats;
	}

	// Converte i numeri di livello in nomi leggibili.

	private String getLevelName(int level) {
		switch (level) {
			case 1: return "Principiante";
			case 2: return "Intermedio";
			case 3: return "Avanzato";
			default: return "Principiante";
		}
	}

	// Converte i nomi interni degli esercizi in nomi user-friendly.

	private String getFriendlyExerciseName(String name) {
		switch (name) {
			case "FindError": return "Trova Errori";
			case "OrderSteps": return "Ordina Passi";
			case "WhatPrints": return "Cosa Stampa";
			case "quizEP": return "Quiz EP";
			case "CompleteCode": return "Completa Codice";
			case "CompareCode": return "Confronta Codice";
			default: return "Misto";
		}
	}

	@Override
	protected void configureScene(Scene scene) {
		super.configureScene(scene);
		if (stage != null) stage.setTitle("PLAY - Home");
	}

	// Metodo factory per creare la schermata Home.

	public static Scene getScene(Stage stage, Scene loginScene) {
		return new Home(stage).createScene();
	}
}