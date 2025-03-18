package application;

import java.io.File;



import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Home extends Main {
	

	public static Scene getScene(Stage stage, Scene s1) {
		BorderPane  root = new BorderPane();
		Scene home = new Scene(root,400,300);
		Text welcomeText = new Text("Benvenuto nella home!");
        Button logoutButton = new Button("Logout");

		stage.setTitle("Home");

		File css = new File("/Users/lorenzocontri/Documents/GitHub/Play_Progetto/src/application/application.css");
		home.getStylesheets().add("file://" + css.getAbsolutePath());

        root.setCenter(welcomeText);
        root.setBottom(logoutButton);

        
        logoutButton.setOnAction(e -> stage.setScene(s1));

        return home;
		
		
	}

	

	

}
