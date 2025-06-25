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

public class CreaAccount extends Main {

    // Utilizza un percorso relativo per il file degli utenti registrati
    public static File Utenti_registrati = new File("Play_Progetto/src/application/resources/Utenti_registrati.txt");

    public static Scene getScene(Stage stage, Scene s1, Text loginMsg) {
        VBox root = new VBox(10); // Aggiunto spacing
        root.setPadding(new Insets(20)); // Aggiunto padding
        Scene crea = new Scene(root, 400, 400); // Aumentata l'altezza

        // Configurazione CSS usando percorso relativo
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
        Button backButton = new Button("Indietro");  // Nuovo pulsante Indietro

        // Contenitore per i pulsanti
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(r, backButton);

        Text errorMsg = new Text(); // Testo per messaggi di errore
        errorMsg.setFill(Color.RED);

        EventHandler<ActionEvent> pressed = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // Reset error message
                errorMsg.setText("");

                // Controllo per campi vuoti
                if (n.getText().trim().isEmpty() || c.getText().trim().isEmpty() ||
                        nick.getText().trim().isEmpty() || p.getText().trim().isEmpty()) {
                    errorMsg.setText("Tutti i campi sono obbligatori!");
                    return;
                }

                // Controllo per nickname duplicato
                if (isNicknameDuplicated(nick.getText(), loginMsg)) {
                    errorMsg.setText("Nickname già in uso!");
                    return;
                }

                try {
                    // Creazione dell'utente con validazione
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
                    // Gestione eccezioni di validazione
                    errorMsg.setText(e.getMessage());
                }
            }
        };

        // Azione per il pulsante Indietro
        backButton.setOnAction(e -> {
            // Torna alla schermata di login
            stage.setScene(s1);
        });

        r.setOnAction(pressed);

        stage.setTitle("Registrati");

        root.getChildren().addAll(
                title,
                nome, n,
                cognome, c,
                nickname, nick,
                pswd, p,
                buttonBox,  // Ora includiamo entrambi i pulsanti tramite l'HBox
                errorMsg
        );

        // Centrare il contenuto della pagina
        root.setAlignment(Pos.CENTER);

        return crea;
    }

    // Metodi isNicknameDuplicated e scriviFile rimangono invariati
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

    public static void scriviFile(File file, String contenuto) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            System.out.println("🐛 DEBUG: Tentativo scrittura file: " + file.getAbsolutePath());
            System.out.println("🐛 DEBUG: Contenuto da scrivere: " + contenuto);

            writer.write(contenuto);
            writer.newLine();

            System.out.println("✅ DEBUG: Scrittura completata!");
        } catch (IOException e) {
            System.err.println("❌ DEBUG: Errore nella scrittura: " + e.getMessage());
            e.printStackTrace();
        }
    }
}