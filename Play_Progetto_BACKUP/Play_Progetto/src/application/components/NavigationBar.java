package application.components;

import application.core.NavigationManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Componente riutilizzabile per la barra di navigazione.
 * Elimina la duplicazione della navbar in tutte le schermate.
 */
public class NavigationBar extends HBox {

    private NavigationManager navigationManager;
    private Button homeButton;
    private Button progressButton;
    private Button logoutButton;
    private Button backButton;

    public NavigationBar() {
        this(true); // Di default mostra il pulsante indietro
    }

    public NavigationBar(boolean showBackButton) {
        this.navigationManager = NavigationManager.getInstance();
        initializeNavBar(showBackButton);
        setupEventHandlers();
    }

    private void initializeNavBar(boolean showBackButton) {
        // Configurazione del layout
        setSpacing(15);
        setId("navBar");
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER_RIGHT);
        setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        // Creazione dei pulsanti
        homeButton = new Button("Home");
        progressButton = new Button("I miei Progressi");
        logoutButton = new Button("Logout");

        // Aggiunta dei pulsanti principali
        getChildren().addAll(homeButton, progressButton, logoutButton);

        // Aggiunta condizionale del pulsante indietro
        if (showBackButton) {
            backButton = new Button("Indietro");
            getChildren().add(backButton);
        }
    }

    private void setupEventHandlers() {
        homeButton.setOnAction(e -> navigationManager.goToHome());
        progressButton.setOnAction(e -> navigationManager.goToUserProgress());
        logoutButton.setOnAction(e -> navigationManager.logout());
    }

    /**
     * Imposta l'azione personalizzata per il pulsante Indietro.
     * @param action L'azione da eseguire quando si clicca Indietro
     */
    public void setBackAction(Runnable action) {
        if (backButton != null) {
            backButton.setOnAction(e -> action.run());
        }
    }

    /**
     * Imposta l'azione personalizzata per il pulsante Indietro con navigazione predefinita.
     * @param destination La destinazione di default ("home", "selection", "login")
     */
    public void setBackAction(String destination) {
        if (backButton != null) {
            backButton.setOnAction(e -> {
                switch (destination.toLowerCase()) {
                    case "home":
                        navigationManager.goToHome();
                        break;
                    case "selection":
                        navigationManager.goToExerciseSelection();
                        break;
                    case "login":
                        navigationManager.goToLogin();
                        break;
                    default:
                        navigationManager.goToHome();
                        break;
                }
            });
        }
    }

    /**
     * Disabilita o abilita specifici pulsanti della navbar.
     */
    public void setButtonEnabled(String buttonName, boolean enabled) {
        switch (buttonName.toLowerCase()) {
            case "home":
                homeButton.setDisable(!enabled);
                break;
            case "progress":
                progressButton.setDisable(!enabled);
                break;
            case "logout":
                logoutButton.setDisable(!enabled);
                break;
            case "back":
                if (backButton != null) {
                    backButton.setDisable(!enabled);
                }
                break;
        }
    }

    /**
     * Nasconde o mostra specifici pulsanti della navbar.
     */
    public void setButtonVisible(String buttonName, boolean visible) {
        switch (buttonName.toLowerCase()) {
            case "home":
                homeButton.setVisible(visible);
                break;
            case "progress":
                progressButton.setVisible(visible);
                break;
            case "logout":
                logoutButton.setVisible(visible);
                break;
            case "back":
                if (backButton != null) {
                    backButton.setVisible(visible);
                }
                break;
        }
    }

    /**
     * Factory method per creare navbar specifiche per contesti diversi.
     */
    public static NavigationBar forLoginScreen() {
        NavigationBar navbar = new NavigationBar(false);
        navbar.setButtonVisible("home", false);
        navbar.setButtonVisible("progress", false);
        return navbar;
    }

    public static NavigationBar forMainScreens() {
        return new NavigationBar(false);
    }

    public static NavigationBar forSubScreens(String backDestination) {
        NavigationBar navbar = new NavigationBar(true);
        navbar.setBackAction(backDestination);
        return navbar;
    }
}