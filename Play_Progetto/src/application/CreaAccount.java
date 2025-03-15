package application;

import java.io.File;
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

public class CreaAccount extends Application{



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
        // da creare action event per il bottone

        stage.setTitle("Registrati");

        root.getChildren().addAll(nome,n,cognome,c,nickname,nick,pswd,p,r);

        return crea;		
	}

    

    @Override
    public void start(Stage arg0) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'start'");
    }
    
}
