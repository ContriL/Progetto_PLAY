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
    protected int width = 700;
    protected int height = 550;

    public BaseScreen(Stage stage) {
        this.stage = stage;
        initialize();
    }

    // AGGIUNGI QUESTO COSTRUTTORE:
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

    /**
     * Inizializza la barra di navigazione.
     * Le sottoclassi possono sovrascrivere questo metodo per personalizzare la navbar.
     */
    protected void initializeNavigation() {
        navbar = createNavigationBar();

        topContainer = new VBox();
        topContainer.getChildren().add(navbar);
    }

    /**
     * Crea la barra di navigazione specifica per questa schermata.
     * Le sottoclassi devono implementare questo metodo.
     */
    protected abstract NavigationBar createNavigationBar();

    /**
     * Inizializza l'header della schermata.
     */
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

    /**
     * Metodo astratto per inizializzare il contenuto specifico della schermata.
     */
    protected abstract void initializeContent();

    /**
     * Imposta il layout finale della schermata.
     */
    protected void setupLayout() {
        setTop(topContainer);
    }

    /**
     * Crea e configura la Scene per questa schermata.
     */
    public Scene createScene() {
        Scene scene = new Scene(this, width, height);
        StyleManager.applyMainStyles(scene);
        configureScene(scene);
        return scene;
    }

    /**
     * Permette alle sottoclassi di configurare ulteriormente la Scene.
     */
    protected void configureScene(Scene scene) {
        // Implementazione di default vuota
        // Le sottoclassi possono sovrascrivere per aggiungere configurazioni specifiche
    }

    /**
     * Metodi astratti che le sottoclassi devono implementare.
     */
    protected abstract String getScreenTitle();

    protected String getScreenDescription() {
        return null; // Di default nessuna descrizione
    }

    /**
     * Metodi di utilità per le sottoclassi.
     */
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
            // Crea la descrizione se non esisteva
            descriptionText = new Text(newDescription);
            descriptionText.setFont(Font.font("Arial", 16));
            headerBox.getChildren().add(descriptionText);
        }
    }

    /**
     * Metodi per gestire il lifecycle della schermata.
     */
    public void onShow() {
        // Chiamato quando la schermata viene mostrata
        // Le sottoclassi possono sovrascrivere per inizializzazioni specifiche
    }

    public void onHide() {
        // Chiamato quando la schermata viene nascosta
        // Le sottoclassi possono sovrascrivere per cleanup
    }

    /**
     * Mostra questa schermata sul stage.
     */
    public void show() {
        Scene scene = createScene();
        stage.setScene(scene);
        onShow();
    }
}