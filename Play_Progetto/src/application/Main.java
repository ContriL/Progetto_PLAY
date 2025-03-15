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


public class Main extends Application {
	
	private static String password = "admin19";
	private static Integer tentativi = 0;
	private static String username = "User1";
	
	@Override
	public void start(Stage stage) {
		
		VBox root = new VBox();
		Scene s1 = new Scene(root,400,300);
		
		File css = new File("/Users/lorenzocontri/Documents/GitHub/Play_Progetto/src/application/application.css");
		s1.getStylesheets().add("file://" + css.getAbsolutePath());
		
		Text t = new Text("Inserisci password");
		
		
		final PasswordField pf = new PasswordField();
		final Button b = new Button("Accedi");
		final Text loginMsg = new Text();
		final PasswordField un = new PasswordField();
		final Button r = new Button("Crea un account");
		
		
		
		
		EventHandler<ActionEvent> accesso = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				tentativi++;
				
				if (pf.getText().equals(password) && un.getText().equals(username)) {
		    			loginMsg.setText("Login successfull!");
		    			loginMsg.setFill(Color.GREEN);
						Scene home = Home.getScene(stage, s1);
		    			stage.setScene(home);
		    		}
		    		else {
		    			loginMsg.setText("Forgot your password? You tried "  + tentativi + " times...");
		    			loginMsg.setFill(Color.RED);
		    			}
            	}

			
		};

		EventHandler<ActionEvent> register = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event){
				Scene Crea = CreaAccount.getScene(stage,s1);
				stage.setScene(Crea);
			}
		};
		
		
		b.setOnAction(accesso);
		r.setOnAction(register);
		
		root.getChildren().addAll(t, pf, b, loginMsg,r);
		
    stage.setTitle("Admin panel");
    stage.setScene(s1);
    stage.show();
				
				
			};
			
	
	public static void main(String[] args) {
		launch();
	}
}
