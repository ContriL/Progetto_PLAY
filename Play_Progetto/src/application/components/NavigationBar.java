package application.components;

import application.core.NavigationManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;


public class NavigationBar extends HBox {

    private NavigationManager navigationManager;
    private Button homeButton;
    private Button progressButton;
    private Button profileButton;
    private Button logoutButton;
    private Button backButton;

    public NavigationBar() {
        this(true);
    }

    public NavigationBar(boolean showBackButton) {
        this.navigationManager = NavigationManager.getInstance();
        initializeNavBar(showBackButton);
        setupEventHandlers();
    }

    private void initializeNavBar(boolean showBackButton) {
        // Configurazione del layout
        setSpacing(20);
        setId("navBar");
        setPadding(new Insets(15, 25, 15, 25));
        setAlignment(Pos.CENTER_RIGHT);

        // Creazione dei pulsanti
        homeButton = new Button("🏠 Home");
        progressButton = new Button("📊 I miei Progressi");
        profileButton = new Button("👤 Profilo");
        logoutButton = new Button("👋 Logout");


        getChildren().addAll(homeButton, progressButton, profileButton, logoutButton);


        if (showBackButton) {
            backButton = new Button("⬅️ Indietro");
            getChildren().add(backButton);
        }
    }

    private void setupEventHandlers() {
        homeButton.setOnAction(e -> navigationManager.goToHome());
        progressButton.setOnAction(e -> navigationManager.goToUserProgress());
        profileButton.setOnAction(e -> navigationManager.goToProfile());
        logoutButton.setOnAction(e -> navigationManager.logout());
    }


    public void setBackAction(Runnable action) {
        if (backButton != null) {
            backButton.setOnAction(e -> action.run());
        }
    }


    public void setBackAction(String destination) {
        if (backButton != null) {
            backButton.setOnAction(e -> {
                switch (destination.toLowerCase()) {
                    case "home":
                        navigationManager.goToHome();
                        break;
                    case "selection":
                        navigationManager.goToExerciseGrid();
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


    public void setButtonEnabled(String buttonName, boolean enabled) {
        switch (buttonName.toLowerCase()) {
            case "home":
                homeButton.setDisable(!enabled);
                break;
            case "progress":
                progressButton.setDisable(!enabled);
                break;
            case "profile":
                profileButton.setDisable(!enabled);
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


    public void setButtonVisible(String buttonName, boolean visible) {
        switch (buttonName.toLowerCase()) {
            case "home":
                homeButton.setVisible(visible);
                break;
            case "progress":
                progressButton.setVisible(visible);
                break;
            case "profile":
                profileButton.setVisible(visible);
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


    public static NavigationBar forLoginScreen() {
        NavigationBar navbar = new NavigationBar(false);
        navbar.setButtonVisible("home", false);
        navbar.setButtonVisible("progress", false);
        navbar.setButtonVisible("profile", false);
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

    /**
     * Imposta azione personalizzata per un bottone specifico.
     * Utile per intercettare la navigazione negli esercizi.
     */
    public void setButtonAction(String buttonName, Runnable action) {
        switch (buttonName.toLowerCase()) {
            case "home":
                if (homeButton != null) {
                    homeButton.setOnAction(e -> action.run());
                }
                break;
            case "progress":
                if (progressButton != null) {
                    progressButton.setOnAction(e -> action.run());
                }
                break;
            case "profile":
                if (profileButton != null) {
                    profileButton.setOnAction(e -> action.run());
                }
                break;
            case "logout":
                if (logoutButton != null) {
                    logoutButton.setOnAction(e -> action.run());
                }
                break;
            default:
                System.err.println(" Bottone non riconosciuto: " + buttonName);
                break;
        }
    }

    //Ripristina le azioni di default dei bottoni

    public void resetDefaultActions() {
        setupEventHandlers(); // Richiama il metodo esistente
    }
}