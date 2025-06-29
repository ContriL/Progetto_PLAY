package application.core;

import application.components.NavigationBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Classe base per tutte le schermate dell'applicazione.
 * Fornisce struttura comune e gestione degli stili.
 */
public abstract class BaseScreen extends BorderPane {

    protected NavigationBar navbar;
    protected VBox topContainer;
    protected VBox headerBox;
    protected Text headerText;
    protected Text descriptionText;
    protected Stage stage;
    protected int width = 1200;
    protected int height = 800;

    public BaseScreen(Stage stage) {
        this.stage = stage;
        initialize();
    }

    
    public BaseScreen(Stage stage, int width, int height) {
        this.stage = stage;
        this.width = width;
        this.height = height;
        initialize();
    }

    private void initialize() {
        initializeNavigation();
        initializeHeader();
        initializeContent();
        setupLayout();
    }


    
    protected void initializeNavigation() {
        navbar = createNavigationBar();

        topContainer = new VBox();
        topContainer.getChildren().add(navbar);
    }

   
    protected abstract NavigationBar createNavigationBar();

    
    protected void initializeHeader() {
        headerText = new Text(getScreenTitle());
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        String description = getScreenDescription();
        if (description != null && !description.isEmpty()) {
            descriptionText = new Text(description);
            descriptionText.setFont(Font.font("Arial", 16));
        }

        headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20, 0, 20, 0));
        headerBox.getChildren().add(headerText);

        if (descriptionText != null) {
            headerBox.getChildren().add(descriptionText);
        }

        topContainer.getChildren().add(headerBox);
    }

    
    protected abstract void initializeContent();

    
    protected void setupLayout() {
        setTop(topContainer);
    }

    
    public Scene createScene() {
        Scene scene = new Scene(this, width, height);
        StyleManager.applyMainStyles(scene);
        configureScene(scene);
        return scene;
    }

    
    protected void configureScene(Scene scene) {
        
    }

    
    protected abstract String getScreenTitle();

    protected String getScreenDescription() {
        return null; 
    }

   
    protected void setHeaderStyle(String style) {
        if (headerText != null) {
            headerText.setStyle(style);
        }
    }

    protected void setDescriptionStyle(String style) {
        if (descriptionText != null) {
            descriptionText.setStyle(style);
        }
    }

    protected void updateTitle(String newTitle) {
        if (headerText != null) {
            headerText.setText(newTitle);
        }
    }

    protected void updateDescription(String newDescription) {
        if (descriptionText != null) {
            descriptionText.setText(newDescription);
        } else if (newDescription != null && !newDescription.isEmpty()) {
            
            descriptionText = new Text(newDescription);
            descriptionText.setFont(Font.font("Arial", 16));
            headerBox.getChildren().add(descriptionText);
        }
    }

    /**
     * Metodi per gestire il lifecycle della schermata.
     */
    public void onShow() {
        
    }

    public void onHide() {
        
    }

    
    public void show() {
        Scene scene = createScene();
        stage.setScene(scene);
        onShow();
    }
}