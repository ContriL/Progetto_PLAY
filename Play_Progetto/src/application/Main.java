package application;
	

import java.io.*;
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


public class Main extends Application {
	
	//private static String password = "admin19";
	private static Integer tentativi = 0;
	//private static String username = "User1";

	private PasswordField pf = new PasswordField();
	private Button b = new Button("Accedi");
	private Text loginMsg = new Text();
	private	TextField name = new TextField();
	private Button r = new Button("Crea un account");
	public static File Utenti_registrati = new File("/Users/lorenzocontri/Desktop/Progetto_Programmazione/Progetto_PLAY/Play_Progetto/src/application/Utenti_registrati.txt");
	
	@Override
	public void start(Stage stage) {
		
		VBox root = new VBox();
		Scene s1 = new Scene(root,400,300);
		
		File css = new File("/Users/lorenzocontri/Documents/GitHub/Play_Progetto/src/application/application.css");
		s1.getStylesheets().add("file://" + css.getAbsolutePath());
		
		Text msg = new Text("Inserisci nickname");
		Text msg2 = new Text("Inserisci password");
		
		
		EventHandler<ActionEvent> accesso = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				tentativi++;
				boolean logged=login(Utenti_registrati);
				if(logged==true){
					loginMsg.setText("Login successfull!");
					Scene home = Home.getScene(stage, s1);
					stage.setScene(home);
					loginMsg.setFill(Color.GREEN);
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
		
		root.getChildren().addAll(msg,name,msg2, pf, b,r,loginMsg);
		
        stage.setTitle("Log in panel");
        stage.setScene(s1);
        stage.show();
				
				
	}

	 public  boolean login(File f){
		boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String [] l = line.split(",");
				String nickname = l[2];
				String password = l[3];
				if(pf.getText().equals(password) && name.getText().equals(nickname)){
					found=true;
				}			
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return found;
    }		
	
	public static void main(String[] args) {
		launch();
	}
}
