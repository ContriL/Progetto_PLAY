package application.screens.auth;

import application.core.NavigationManager;
import application.core.StyleManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

	private static Integer tentativi = 0;
	private static String currentUser = "";

	
	 public static File Utenti_registrati = new File("src/application/resources/Utenti_registrati.txt");

	public static String getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(String username) {
		currentUser = username;
	}

	@Override
	public void start(Stage stage) {
		debugPaths();

		// Inizializza il NavigationManager
		NavigationManager.getInstance().initialize(stage);

		Scene loginScene = getLoginScene(stage);
		stage.setTitle("Pannello di Login");
		stage.setScene(loginScene);
		stage.show();
	}

	

	public static Scene getLoginScene(Stage stage) {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1200, 800);

		
		StyleManager.applyMainStyles(scene);

		// Container principale 
		VBox mainContainer = new VBox(30);
		mainContainer.setAlignment(Pos.CENTER);
		mainContainer.setPadding(new Insets(50));

		
		Text logo = new Text("PLAY");
		logo.setStyle("-fx-font-size: 48px; -fx-font-weight: 800; -fx-text-fill: white; " +
				"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 3);");

		Text subtitle = new Text("Learn, Practice, Achieve");
		subtitle.setStyle("-fx-font-size: 18px; -fx-font-weight: 300; -fx-text-fill: rgba(255,255,255,0.8); " +
				"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);");

		VBox logoBox = new VBox(10);
		logoBox.setAlignment(Pos.CENTER);
		logoBox.getChildren().addAll(logo, subtitle);

		
		VBox loginCard = new VBox(20);
		loginCard.getStyleClass().add("login-container");
		loginCard.setAlignment(Pos.CENTER);
		loginCard.setMaxWidth(400);
		loginCard.setPrefWidth(400);

		
		Text welcomeText = new Text("Benvenuto!");
		welcomeText.setStyle("-fx-font-size: 24px; -fx-font-weight: 600; -fx-text-fill: white;");

		
		TextField usernameField = new TextField();
		usernameField.setPromptText("✨ Il tuo nickname");
		usernameField.setMaxWidth(300);
		usernameField.setPrefHeight(45);

		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("🔒 La tua password");
		passwordField.setMaxWidth(300);
		passwordField.setPrefHeight(45);

		
		Button loginButton = new Button("🚀 Accedi");
		loginButton.setMaxWidth(300);
		loginButton.setPrefHeight(45);
		loginButton.setStyle("-fx-font-size: 16px; -fx-font-weight: 600;");

		Button registerButton = new Button("✨ Crea un Account");
		registerButton.getStyleClass().add("secondary");
		registerButton.setMaxWidth(300);
		registerButton.setPrefHeight(45);
		registerButton.setStyle("-fx-font-size: 16px; -fx-font-weight: 600;");

		
		Text loginMsg = new Text();
		loginMsg.setStyle("-fx-font-size: 14px; -fx-font-weight: 500;");

		
		NavigationManager navManager = NavigationManager.getInstance();

		loginButton.setOnAction(event -> {
			boolean logged = login(Utenti_registrati, usernameField.getText(), passwordField.getText());
			if (logged) {
				loginMsg.setText("🎉 Login effettuato con successo!");
				loginMsg.getStyleClass().add("success");
				setCurrentUser(usernameField.getText());
				navManager.goToHome();
			} else {
				loginMsg.setText("❌ Credenziali non valide. Riprova!");
				loginMsg.getStyleClass().add("error");
			}
		});

		registerButton.setOnAction(event -> {
			Scene registerScene = CreaAccount.getScene(stage, scene, loginMsg);
			stage.setScene(registerScene);
		});

		
		loginCard.getChildren().addAll(
				welcomeText,
				usernameField,
				passwordField,
				loginButton,
				registerButton,
				loginMsg
		);

		mainContainer.getChildren().addAll(logoBox, loginCard);
		root.setCenter(mainContainer);

		return scene;
	}
	
	public static boolean login(File file, String username, String password) {
		System.out.println("🐛 PERCORSO FILE LOGIN: " + file.getAbsolutePath());
		System.out.println("🐛 FILE EXISTS: " + file.exists());

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println("🐛 DEBUG LOGIN: Riga letta: " + line);

				String[] l = line.split(",");
				if (l.length >= 4) {
					String nickname = l[2];
					String storedPassword = l[3];

					
					if (username.equals(nickname) && password.equals(storedPassword)) {
						System.out.println("✅ DEBUG LOGIN: Match trovato!");
						return true;
					}
				}
			}
		} catch (IOException e) {
			System.err.println("❌ DEBUG LOGIN: " + e.getMessage());
		}
		return false;
	}

	
	public static boolean isNicknameDuplicated(String nickname, Text loginMsg) {
		try (BufferedReader br = new BufferedReader(new FileReader(Utenti_registrati))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] l = line.split(",");
				if (l.length >= 3 && l[2].equals(nickname)) {
					loginMsg.setText("Nickname già in uso! Scegli un altro nickname.");
					loginMsg.setFill(Color.RED);
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	
	public static void scriviFile(File file, String contenuto) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
			writer.write(contenuto);
			writer.newLine();
			System.out.println("Scrittura completata.");
		} catch (IOException e) {
			System.err.println("Errore nella scrittura del file: " + e.getMessage());
		}
	}

	public static void debugPaths() {
		System.out.println("=== DEBUG PERCORSI ===");
		System.out.println("user.dir: " + System.getProperty("user.dir"));

		File file1 = new File("Utenti_registrati.txt");
		System.out.println("File root: " + file1.getAbsolutePath() + " - exists: " + file1.exists());

		File file2 = new File(System.getProperty("user.dir") + "/src/application/Utenti_registrati.txt");
		System.out.println("File src: " + file2.getAbsolutePath() + " - exists: " + file2.exists());

		File file3 = new File(System.getProperty("user.dir") + "/src/application/resources/data/Utenti_registrati.txt");
		System.out.println("File resources: " + file3.getAbsolutePath() + " - exists: " + file3.exists());

		System.out.println("======================");
	}

	public static void main(String[] args) {
		launch();
	}
}