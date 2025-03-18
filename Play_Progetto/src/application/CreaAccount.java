package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

public class CreaAccount extends Main{

    public static File Utenti_registrati = new File("/Users/lorenzocontri/Desktop/Progetto_Programmazione/Progetto_PLAY/Play_Progetto/src/application/Utenti_registrati.txt");
    
    
        public static Scene getScene(Stage stage, Scene s1) {
            VBox root = new VBox();
            Scene crea = new Scene(root,400,300);
            File css = new File("/Users/lorenzocontri/Documents/GitHub/Play_Progetto/src/application/application.css");
            crea.getStylesheets().add("file://" + css.getAbsolutePath());
    
            Text nome = new Text("Inserisci Nome");
            Text cognome = new Text("Inserisci Cognome");
            Text nickname = new Text("Inserisci nickname");
            Text pswd = new Text("Inserisci passsword");
    
            final TextField n = new TextField();
            final TextField c = new TextField();
            final TextField nick = new TextField();
            final PasswordField p = new PasswordField();
    
            Button r = new Button("Registrati");
            EventHandler<ActionEvent> pressed = new EventHandler<ActionEvent>(){
                public void handle(ActionEvent event){
                    User u = new User();
                    u.setNome(n.getText());
                    u.setCognome(c.getText());
                    u.setNick(nick.getText());
                    u.setPassword(p.getText());
    
                    String contenuto = u.toString();
                    scriviFile(Utenti_registrati,contenuto);
                    stage.setScene(s1);



            }

        };

        r.setOnAction(pressed);

        stage.setTitle("Registrati");

        root.getChildren().addAll(nome,n,cognome,c,nickname,nick,pswd,p,r);

        

        return crea;		
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

    

    

   
    
}
