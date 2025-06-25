package application;

import application.core.NavigationManager;
import application.core.StyleManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

	// Percorso relativo per il file degli utenti registrati
	public static File Utenti_registrati = new File("Play_Progetto/src/application/resources/Utenti_registrati.txt");

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

	// Metodo per costruire la schermata di login (refactored)
	public static Scene getLoginScene(Stage stage) {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 700, 550);

		// Applica gli stili usando StyleManager
		StyleManager.applyMainStyles(scene);

		PasswordField pf = new PasswordField();
		TextField name = new TextField();
		Button loginButton = new Button("Accedi");
		Button registerButton = new Button("Crea un account");
		Text loginMsg = new Text();

		// Usa NavigationManager per la navigazione
		NavigationManager navManager = NavigationManager.getInstance();

		loginButton.setOnAction(event -> {
			tentativi++;
			boolean logged = login(Utenti_registrati, name.getText(), pf.getText());
			if (logged) {
				loginMsg.setText("Login successful!");
				loginMsg.setFill(Color.GREEN);
				setCurrentUser(name.getText());
				navManager.goToHome();
			} else {
				loginMsg.setText("Forgot your password? You tried " + tentativi + " times...");
				loginMsg.setFill(Color.RED);
			}
		});

		registerButton.setOnAction(event -> {
			Scene crea = CreaAccount.getScene(stage, scene, loginMsg);
			stage.setScene(crea);
		});

		Text titolo = new Text("Benvenuto!");
		titolo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
		Text msg = new Text("Nickname:");
		Text msg2 = new Text("Password:");

		name.setMaxWidth(250);
		pf.setMaxWidth(250);
		loginButton.setMaxWidth(250);
		registerButton.setMaxWidth(250);

		VBox loginBox = new VBox(10);
		loginBox.getChildren().addAll(titolo, msg, name, msg2, pf, loginButton, registerButton, loginMsg);
		loginBox.setStyle("-fx-padding: 30px; -fx-background-color: #f4f4f4; -fx-border-radius: 10px; -fx-background-radius: 10px;");
		loginBox.setMaxWidth(300);
		loginBox.setTranslateY(-30);

		root.setCenter(loginBox);
		BorderPane.setAlignment(loginBox, javafx.geometry.Pos.CENTER);

		return scene;
	}
	//login
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

					// CONFRONTO DIRETTO - NO HASH
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

	// Verifica se un nickname è già registrato (rimane invariato)
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

	// Scrittura dell'account nel file (rimane invariato)
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