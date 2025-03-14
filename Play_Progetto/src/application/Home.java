package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Home {
	
	public static Scene getScene1(Stage stage,Scene s1) {
		BorderPane  root = new BorderPane();
		Scene home = new Scene(root,400,300);
		Text welcomeText = new Text("Benvenuto nella home!");
        Button logoutButton = new Button("Logout");

        root.setCenter(welcomeText);
        root.setBottom(logoutButton);

        
        logoutButton.setOnAction(e -> stage.setScene(s1));

        return home;
	}

	public static Scene getScene(Stage stage, Scene s1) {
		
		return getScene1(stage,s1);
	}

}
