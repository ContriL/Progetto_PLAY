package application.screens.home;

import application.core.BaseScreen;
import application.core.NavigationManager;
import application.components.NavigationBar;
import application.core.StyleManager;
import application.screens.auth.Main;
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

/**
 * Schermata Home dell'applicazione PLAY.
 * REFACTORATO per usare BaseScreen ed eliminare duplicazioni.
 */
public class Home extends BaseScreen {

	public Home(Stage stage) {
		super(stage, 600, 400); // Dimensioni originali mantenute
	}

	@Override
	protected NavigationBar createNavigationBar() {
		return NavigationBar.forMainScreens();
	}

	@Override
	protected String getScreenTitle() {
		return "Benvenuto a PLAY!";
	}

	@Override
	protected String getScreenDescription() {
		return "Play, Learn, Assess Yourself - Impara a programmare divertendoti";
	}

	@Override
	protected void initializeContent() {
		VBox centerBox = createHomeContent();
		setCenter(centerBox);
	}

	/**
	 * Crea il contenuto centrale della home
	 */
	private VBox createHomeContent() {
		VBox centerBox = new VBox(20);
		centerBox.setAlignment(Pos.CENTER);
		centerBox.setPadding(new Insets(20));

		// Pulsante principale per iniziare gli esercizi
		Button startButton = new Button("Inizia gli Esercizi");
		startButton.setPrefWidth(200);
		startButton.setPrefHeight(40);
		startButton.setFont(Font.font("Arial", 14));

		startButton.setOnAction(e -> {
			// Usa il NavigationManager per andare alla griglia degli esercizi
			NavigationManager.getInstance().goToExerciseGrid();
		});

		centerBox.getChildren().add(startButton);
		return centerBox;
	}

	@Override
	protected void configureScene(Scene scene) {
		super.configureScene(scene);
		if (stage != null) {
			stage.setTitle("PLAY - Home");
		}
	}

	/// Modifica per Home.java - Schermata Home più bella

	public static Scene getScene(Stage stage, Scene loginScene) {
		BorderPane root = new BorderPane();
		Scene home = new Scene(root, 900, 700);

		StyleManager.applyMainStyles(home);

		// === NAVBAR GLASSMORPHISM ===
		HBox navBar = new HBox(20);
		navBar.setId("navBar");
		navBar.setPadding(new Insets(15, 25, 15, 25));
		navBar.setAlignment(Pos.CENTER_RIGHT);

		Button homeButton = new Button("🏠 Home");
		Button progressButton = new Button("📊 I miei Progressi");
		Button logoutButton = new Button("👋 Logout");

		homeButton.getStyleClass().add("secondary");
		progressButton.setStyle("-fx-background-color: linear-gradient(135deg, #667eea, #764ba2);");
		logoutButton.getStyleClass().add("secondary");

		progressButton.setOnAction(e -> NavigationManager.getInstance().goToUserProgress());
		logoutButton.setOnAction(e -> NavigationManager.getInstance().logout());

		navBar.getChildren().addAll(homeButton, progressButton, logoutButton);
		root.setTop(navBar);

		// === CONTENUTO CENTRALE BELLISSIMO ===
		VBox centerBox = new VBox(40);
		centerBox.setAlignment(Pos.CENTER);
		centerBox.setPadding(new Insets(50));

		// Hero Section
		VBox heroSection = new VBox(20);
		heroSection.setAlignment(Pos.CENTER);

		Text welcomeText = new Text("Ciao " + Main.getCurrentUser() + "! 👋");
		welcomeText.getStyleClass().add("main-title");

		Text heroSubtitle = new Text("Pronto per una nuova avventura di programmazione?");
		heroSubtitle.getStyleClass().add("section-title");
		heroSubtitle.setStyle("-fx-font-size: 20px; -fx-text-fill: rgba(255,255,255,0.9);");

		// Statistiche rapide in cards
		HBox statsBox = new HBox(20);
		statsBox.setAlignment(Pos.CENTER);

		VBox exercisesCard = createStatCard("🎯", "Esercizi", "Completati", "12");
		VBox streakCard = createStatCard("🔥", "Giorni", "Consecutivi", "5");
		VBox levelCard = createStatCard("⭐", "Livello", "Attuale", "Intermedio");

		statsBox.getChildren().addAll(exercisesCard, streakCard, levelCard);

		// Pulsante principale elegante
		Button startButton = new Button("🚀 Inizia gli Esercizi");
		startButton.setPrefWidth(300);
		startButton.setPrefHeight(60);
		startButton.setStyle("-fx-font-size: 18px; -fx-font-weight: 700; " +
				"-fx-background-color: linear-gradient(135deg, #ff6b6b, #ee5a24); " +
				"-fx-effect: dropshadow(gaussian, rgba(238, 90, 36, 0.5), 15, 0, 0, 8);");

		startButton.setOnAction(e -> NavigationManager.getInstance().goToExerciseGrid());

		// Motivational quote
		Text motivationText = new Text("\"Il successo è la somma di piccoli sforzi ripetuti giorno dopo giorno\" 💪");
		motivationText.setStyle("-fx-font-size: 16px; -fx-font-style: italic; " +
				"-fx-text-fill: rgba(255,255,255,0.8); -fx-text-alignment: center;");
		motivationText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

		heroSection.getChildren().addAll(welcomeText, heroSubtitle);
		centerBox.getChildren().addAll(heroSection, statsBox, startButton, motivationText);

		// Container con glassmorphism
		VBox contentContainer = new VBox();
		contentContainer.getStyleClass().add("content-container");
		contentContainer.getChildren().add(centerBox);

		root.setCenter(contentContainer);
		stage.setTitle("PLAY - Home");

		return home;
	}

	private static VBox createStatCard(String icon, String title, String subtitle, String value) {
		VBox card = new VBox(8);
		card.getStyleClass().add("stats-container");
		card.setAlignment(Pos.CENTER);
		card.setPrefWidth(150);
		card.setPrefHeight(120);

		Text iconText = new Text(icon);
		iconText.setStyle("-fx-font-size: 32px;");

		Text valueText = new Text(value);
		valueText.getStyleClass().add("stat-value");
		valueText.setStyle("-fx-font-size: 24px;");

		Text titleText = new Text(title);
		titleText.getStyleClass().add("stat-label");

		Text subtitleText = new Text(subtitle);
		subtitleText.getStyleClass().add("stat-label");
		subtitleText.setStyle("-fx-font-size: 12px;");

		card.getChildren().addAll(iconText, valueText, titleText, subtitleText);
		return card;
	}
}