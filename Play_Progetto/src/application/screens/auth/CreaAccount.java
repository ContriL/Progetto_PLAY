package application.screens.auth;

import application.User;
import application.core.StyleManager;
import application.screens.home.Home;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * Schermata di registrazione per nuovi utenti dell'applicazione PLAY.
 * Gestisce la creazione di account con validazione dei dati e persistenza su file.
 */
public class CreaAccount extends Main {

    public static File Utenti_registrati = new File(System.getProperty("user.dir") + "/src/application/resources/Utenti_registrati.txt");

    // Crea la schermata di registrazione con form di input e validazione.

    public static Scene getScene(Stage stage, Scene s1, Text loginMsg) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        Scene crea = new Scene(root, 1200, 800);

        StyleManager.applyMainStyles(crea);

        Text title = new Text("Registrazione Nuovo Utente");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Text nome = new Text("Inserisci Nome (almeno 2 caratteri)");
        Text cognome = new Text("Inserisci Cognome (almeno 2 caratteri)");
        Text nickname = new Text("Inserisci nickname (3-20 caratteri, solo lettere, numeri e underscore)");
        Text pswd = new Text("Inserisci password (min 8 caratteri, maiuscole, minuscole, numeri)");

        final TextField n = new TextField();
        final TextField c = new TextField();
        final TextField nick = new TextField();
        final PasswordField p = new PasswordField();

        Button r = new Button("Registrati");
        Button backButton = new Button("Indietro");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(r, backButton);

        Text errorMsg = new Text();
        errorMsg.setFill(Color.RED);

        EventHandler<ActionEvent> pressed = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                errorMsg.setText("");

                // Validazione campi obbligatori
                if (n.getText().trim().isEmpty() || c.getText().trim().isEmpty() ||
                        nick.getText().trim().isEmpty() || p.getText().trim().isEmpty()) {
                    errorMsg.setText("Tutti i campi sono obbligatori!");
                    return;
                }

                // Controllo unicità nickname
                if (isNicknameDuplicated(nick.getText(), loginMsg)) {
                    errorMsg.setText("Nickname già in uso!");
                    return;
                }

                try {
                    // Creazione e validazione utente
                    User u = new User();
                    u.setNome(n.getText());
                    u.setCognome(c.getText());
                    u.setNick(nick.getText());
                    u.setPassword(p.getText());

                    String contenuto = u.toString();
                    scriviFile(Utenti_registrati, contenuto);

                    loginMsg.setText("Registrazione completata!");
                    loginMsg.setFill(Color.GREEN);
                    Scene homeScreen = Home.getScene(stage, crea);
                    stage.setScene(homeScreen);

                } catch (IllegalArgumentException e) {
                    errorMsg.setText(e.getMessage());
                }
            }
        };

        backButton.setOnAction(e -> stage.setScene(s1));
        r.setOnAction(pressed);

        stage.setTitle("Registrati");

        root.getChildren().addAll(
                title,
                nome, n,
                cognome, c,
                nickname, nick,
                pswd, p,
                buttonBox,
                errorMsg
        );

        root.setAlignment(Pos.CENTER);
        return crea;
    }

    // Verifica se un nickname è già presente nel sistema.

    public static boolean isNicknameDuplicated(String nickname, Text loginMsg) {
        try (BufferedReader br = new BufferedReader(new FileReader(Utenti_registrati))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] l = line.split(",");
                String existingNickname = l[2];
                if (existingNickname.equals(nickname)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Scrive i dati utente nel file di registrazione.

    public static void scriviFile(File file, String contenuto) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(contenuto);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Errore nella scrittura: " + e.getMessage());
            e.printStackTrace();
        }
    }
}