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

/**
 * Classe principale dell'applicazione PLAY che gestisce l'autenticazione degli utenti.
 * Fornisce interfaccia di login e registrazione con persistenza su file system.
 */
public class Main extends Application {

	private static String currentUser = "";
	public static File Utenti_registrati = getDataFile("Utenti_registrati.txt");

	private static File getDataFile(String filename) {
		// Usa SEMPRE la cartella resources di Play_Progetto
		File resourcesFile = new File("Play_Progetto/src/application/resources/" + filename);

		// Assicurati che la directory esista
		File parentDir = resourcesFile.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			parentDir.mkdirs();
		}

		return resourcesFile;
	}

	public static String getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(String username) {
		currentUser = username;
	}

	@Override
	public void start(Stage stage) {
		NavigationManager.getInstance().initialize(stage);


		stage.setWidth(1400);
		stage.setHeight(900);

		Scene loginScene = getLoginScene(stage);
		stage.setTitle("Pannello di Login");
		stage.setScene(loginScene);
		stage.show();
	}

	//Crea la schermata di login con interfaccia grafica stilizzata.

	public static Scene getLoginScene(Stage stage) {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1200, 800);
		StyleManager.applyMainStyles(scene);

		// Container principale
		VBox mainContainer = new VBox(30);
		mainContainer.setAlignment(Pos.CENTER);
		mainContainer.setPadding(new Insets(50));

		// Logo e sottotitolo
		Text logo = new Text("PLAY");
		logo.setStyle("-fx-font-size: 48px; -fx-font-weight: 800; -fx-text-fill: white; " +
				"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 3);");

		Text subtitle = new Text("Learn, Practice, Achieve");
		subtitle.setStyle("-fx-font-size: 18px; -fx-font-weight: 300; -fx-text-fill: rgba(255,255,255,0.8); " +
				"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);");

		VBox logoBox = new VBox(10);
		logoBox.setAlignment(Pos.CENTER);
		logoBox.getChildren().addAll(logo, subtitle);

		// Card di login
		VBox loginCard = new VBox(20);
		loginCard.getStyleClass().add("login-container");
		loginCard.setAlignment(Pos.CENTER);
		loginCard.setMaxWidth(400);
		loginCard.setPrefWidth(400);

		Text welcomeText = new Text("Benvenuto!");
		welcomeText.setStyle("-fx-font-size: 24px; -fx-font-weight: 600; -fx-text-fill: white;");

		// Campi di input
		TextField usernameField = new TextField();
		usernameField.setPromptText("✨ Il tuo nickname");
		usernameField.setMaxWidth(300);
		usernameField.setPrefHeight(45);

		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("🔒 La tua password");
		passwordField.setMaxWidth(300);
		passwordField.setPrefHeight(45);

		// Pulsanti
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

		// Event handlers
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

	// Verifica le credenziali utente confrontandole con i dati memorizzati nel file.

	public static boolean login(File file, String username, String password) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] l = line.split(",");
				if (l.length >= 4) {
					String nickname = l[2];
					String storedPassword = l[3];

					if (username.equals(nickname) && password.equals(storedPassword)) {
						return true;
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Errore durante il login: " + e.getMessage());
		}
		return false;
	}

	// Verifica se un nickname è già presente nel sistema.

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

	// Scrive contenuto in append al file specificato.

	public static void scriviFile(File file, String contenuto) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
			writer.write(contenuto);
			writer.newLine();
		} catch (IOException e) {
			System.err.println("Errore nella scrittura del file: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		launch();
	}
}