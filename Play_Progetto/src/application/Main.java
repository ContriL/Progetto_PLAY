package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

	private static Integer tentativi = 0;
	private static String currentUser = ""; // Per tenere traccia dell'utente corrente

	private PasswordField pf = new PasswordField();
	private Button b = new Button("Accedi");
	private Text loginMsg = new Text();
	private TextField name = new TextField();
	private Button r = new Button("Crea un account");
	public static File Utenti_registrati = new File("C:/Users/dadas/IdeaProjects/Progetto_PLAY/Play_Progetto/src/application/Utenti_registrati.txt");

	/**
	 * Ottiene il nome utente correntemente loggato
	 * @return Nome utente corrente
	 */
	public static String getCurrentUser() {
		return currentUser;
	}

	/**
	 * Imposta il nome utente corrente
	 * @param username Nome utente
	 */
	public static void setCurrentUser(String username) {
		currentUser = username;
	}

	@Override
	public void start(Stage stage) {
		VBox root = new VBox();
		Scene s1 = new Scene(root, 400, 300);

		File css = new File("C:/Users/dadas/IdeaProjects/Progetto_PLAY/Play_Progetto/src/application/application.css");
		s1.getStylesheets().add("file://" + css.getAbsolutePath());

		Text msg = new Text("Inserisci nickname");
		Text msg2 = new Text("Inserisci password");

		// Gestione accesso
		EventHandler<ActionEvent> accesso = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				tentativi++;
				boolean logged = login(Utenti_registrati);
				if (logged) {
					setLoginMessage("Login successful!", Color.GREEN);
					// Imposta l'utente corrente
					setCurrentUser(name.getText());
					Scene home = Home.getScene(stage, s1);
					stage.setScene(home);
				} else {
					setLoginMessage("Forgot your password? You tried " + tentativi + " times...", Color.RED);
				}
			}
		};

		// Gestione registrazione
		EventHandler<ActionEvent> register = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Scene Crea = CreaAccount.getScene(stage, s1, loginMsg);
				stage.setScene(Crea);
			}
		};

		b.setOnAction(accesso);
		r.setOnAction(register);

		root.getChildren().addAll(msg, name, msg2, pf, b, r, loginMsg);

		stage.setTitle("Log in panel");
		stage.setScene(s1);
		stage.show();
	}

	// Metodo per aggiornare il messaggio di login
	public void setLoginMessage(String message, Color color) {
		loginMsg.setText(message);
		loginMsg.setFill(color);
	}

	// Funzione di login
	public boolean login(File f) {
		boolean found = false;
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] l = line.split(",");
				String nickname = l[2];
				String password = l[3];
				if (pf.getText().equals(password) && name.getText().equals(nickname)) {
					found = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return found;
	}

	// Funzione per controllare il nickname duplicato
	public static boolean isNicknameDuplicated(String nickname, Text loginMsg) {
		try (BufferedReader br = new BufferedReader(new FileReader(Utenti_registrati))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] l = line.split(",");
				String existingNickname = l[2]; // Il nickname è nel terzo campo
				if (existingNickname.equals(nickname)) {
					// Aggiorniamo il messaggio di errore se il nickname è duplicato
					loginMsg.setText("Nickname già in uso! Scegli un altro nickname.");
					loginMsg.setFill(Color.RED);
					return true; // Nickname già presente
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false; // Nickname non trovato
	}

	// Funzione per la registrazione dell'utente
	public static void scriviFile(File file, String contenuto) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
			writer.write(contenuto);
			writer.newLine();
			System.out.println("Scrittura completata.");
		} catch (IOException e) {
			System.err.println("Errore nella scrittura del file: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		launch();
	}
}