package application.screens.user;

import application.core.BaseScreen;
import application.core.NavigationManager;
import application.components.NavigationBar;
import application.screens.auth.Main;
import application.UserProgress;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

// Schermata Profilo Utente: visualizza bio, avatar, statistiche e obiettivi
public class ProfileScreen extends BaseScreen {

    private static final String PROFILE_FILE = System.getProperty("user.dir") + "/src/application/resources/profiles.properties";

    private TextArea bioField;

    public ProfileScreen(Stage stage) {
        super(stage, 1200, 800);
    }

    @Override
    protected NavigationBar createNavigationBar() {
        return NavigationBar.forMainScreens();
    }

    @Override
    protected String getScreenTitle() {
        return "Il tuo Profilo 👤";
    }

    @Override
    protected String getScreenDescription() {
        return "Personalizza il tuo avatar e mostra i tuoi progressi! ✨";
    }

    @Override
    protected void initializeContent() {
        VBox mainContent = new VBox(40);
        mainContent.setPadding(new Insets(30));
        mainContent.setAlignment(Pos.TOP_CENTER);

        VBox profileSection = createProfileSection(); // avatar + bio
        HBox statsAchievements = new HBox(40, createRealStatsSection(), createSimpleAchievementsSection()); // stats + obiettivi
        statsAchievements.setAlignment(Pos.CENTER);

        mainContent.getChildren().addAll(profileSection, statsAchievements);

        VBox container = new VBox(mainContent);
        container.getStyleClass().add("content-container");
        setCenter(container);
    }

    // Sezione avatar, username, bio e pulsante salvataggio
    private VBox createProfileSection() {
        VBox section = new VBox(20);
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(20));
        section.getStyleClass().add("stats-container");
        section.setMaxWidth(800);

        VBox avatarBox = createSimpleAvatarSection();

        Text usernameText = new Text(Main.getCurrentUser());
        usernameText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label bioLabel = new Label("📝 La tua bio:");
        bioLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.9); -fx-font-weight: bold;");

        bioField = new TextArea(loadUserBio());
        bioField.setPromptText("Scrivi qualcosa su di te...");
        bioField.setPrefRowCount(3);
        bioField.setWrapText(true);
        bioField.setMaxWidth(600);

        Button saveBtn = new Button("💾 Salva Bio");
        saveBtn.setOnAction(e -> {
            saveUserBio(bioField.getText());
            showSimpleAlert("Bio salvata! ✅");
        });

        section.getChildren().addAll(avatarBox, usernameText, bioLabel, bioField, saveBtn);
        return section;
    }

    // Sezione avatar: selezione emoji tra opzioni predefinite
    private VBox createSimpleAvatarSection() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        String currentAvatar = loadUserAvatar();
        Text avatarDisplay = new Text(currentAvatar);
        avatarDisplay.setStyle("-fx-font-size: 40px;");

        HBox selection = new HBox(10);
        selection.setAlignment(Pos.CENTER);
        String[] avatars = {"🎮", "👨‍💻", "👩‍💻", "🚀"};

        for (String av : avatars) {
            Button btn = new Button(av);
            btn.setStyle("-fx-font-size: 18px; -fx-background-radius: 50%; -fx-min-width: 40px; -fx-min-height: 40px;");
            if (av.equals(currentAvatar)) btn.getStyleClass().add("secondary");

            btn.setOnAction(e -> {
                avatarDisplay.setText(av);
                saveUserAvatar(av);
                selection.getChildren().forEach(n -> n.getStyleClass().remove("secondary"));
                btn.getStyleClass().add("secondary");
            });

            selection.getChildren().add(btn);
        }

        box.getChildren().addAll(avatarDisplay, selection);
        return box;
    }

    // Statistiche reali basate sui progressi utente
    private VBox createRealStatsSection() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));
        box.setPrefWidth(400);
        box.getStyleClass().add("stats-container");

        Text title = new Text("📊 Le tue Statistiche");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox stats = new VBox(10);
        List<String> progress = UserProgress.getUserProgress(Main.getCurrentUser());

        stats.getChildren().add(createStatRow("🎯", "Esercizi fatti", String.valueOf(progress.size())));

        // Conteggio esercizi superati
        long passed = progress.stream().filter(p -> {
            String[] parts = p.split(",");
            return parts.length >= 6 && Double.parseDouble(parts[5]) >= 60.0;
        }).count();
        stats.getChildren().add(createStatRow("🏆", "Livelli superati", String.valueOf(passed)));

        stats.getChildren().add(createStatRow("❤️", "Preferito", findFavoriteExercise(progress)));

        Button details = new Button("📈 Vedi Dettagli");
        details.getStyleClass().add("secondary");
        details.setOnAction(e -> NavigationManager.getInstance().goToUserProgress());

        box.getChildren().addAll(title, stats, details);
        return box;
    }

    // Sezione con obiettivi sbloccati o meno
    private VBox createSimpleAchievementsSection() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));
        box.setPrefWidth(400);
        box.getStyleClass().add("stats-container");

        Text title = new Text("🏆 Obiettivi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox rows = new VBox(10);
        calculateSimpleAchievements().forEach(a -> rows.getChildren().add(createSimpleAchievementRow(a)));

        box.getChildren().addAll(title, rows);
        return box;
    }

    // Riga singola per una statistica (es: esercizi completati)
    private HBox createStatRow(String icon, String label, String value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Text i = new Text(icon);
        i.setStyle("-fx-font-size: 14px;");
        Text l = new Text(label);
        l.setStyle("-fx-text-fill: rgba(255,255,255,0.8); -fx-font-size: 14px;");
        Text v = new Text(value);
        v.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.getChildren().addAll(i, l, spacer, v);
        return row;
    }

    // Riga per un singolo obiettivo (con stato sbloccato)
    private HBox createSimpleAchievementRow(SimpleAchievement a) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        Text icon = new Text(a.unlocked ? "✅" : "⭕");
        Text name = new Text(a.name);
        name.setStyle("-fx-text-fill: " + (a.unlocked ? "white" : "rgba(255,255,255,0.5)") + "; -fx-font-size: 13px;");
        row.getChildren().addAll(icon, name);
        return row;
    }

    // Crea e restituisce lista di obiettivi sbloccabili
    private List<SimpleAchievement> calculateSimpleAchievements() {
        List<String> progress = UserProgress.getUserProgress(Main.getCurrentUser());
        List<SimpleAchievement> list = new ArrayList<>();

        list.add(new SimpleAchievement("Primo esercizio", progress.size() >= 1));
        list.add(new SimpleAchievement("Supera un livello", progress.stream().anyMatch(p -> p.split(",").length >= 6 && Double.parseDouble(p.split(",")[5]) >= 60.0)));
        list.add(new SimpleAchievement("Trova Errori - Intermedio", UserProgress.hasPassedLevel(Main.getCurrentUser(), "FindError", 2)));
        list.add(new SimpleAchievement("Quiz EP - Principiante", UserProgress.hasPassedLevel(Main.getCurrentUser(), "quizEP", 1)));
        list.add(new SimpleAchievement("5 esercizi completati", progress.size() >= 5));

        // Verifica se ha provato almeno 3 tipi diversi
        Set<String> types = new HashSet<>();
        progress.forEach(p -> {
            String[] parts = p.split(",");
            if (parts.length >= 2) types.add(parts[1]);
        });
        list.add(new SimpleAchievement("Prova 3 tipi diversi", types.size() >= 3));

        return list;
    }

    // Trova il tipo di esercizio svolto più volte
    private String findFavoriteExercise(List<String> progress) {
        Map<String, Integer> map = new HashMap<>();
        for (String p : progress) {
            String[] parts = p.split(",");
            if (parts.length >= 2) map.put(parts[1], map.getOrDefault(parts[1], 0) + 1);
        }
        return map.entrySet().stream().max(Map.Entry.comparingByValue())
                .map(e -> getFriendlyName(e.getKey())).orElse("Nessuno");
    }

    private String getFriendlyName(String type) {
        switch (type) {
            case "FindError": return "Trova Errori";
            case "OrderSteps": return "Ordina Passi";
            case "WhatPrints": return "Cosa Stampa";
            case "quizEP": return "Quiz EP";
            case "CompleteCode": return "Completa Codice";
            default: return type;
        }
    }

    // Mostra un semplice alert informativo
    private void showSimpleAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("PLAY");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    // Carica la bio utente, con valore predefinito
    private String loadUserBio() {
        return loadUserProperty(".bio", "🚀 Studente di programmazione appassionato!");
    }

    private void saveUserBio(String bio) {
        saveUserProperty(".bio", bio);
    }

    private String loadUserAvatar() {
        return loadUserProperty(".avatar", "👨‍💻");
    }

    private void saveUserAvatar(String avatar) {
        saveUserProperty(".avatar", avatar);
    }

    // Carica una proprietà dal file utente
    private String loadUserProperty(String suffix, String defaultVal) {
        try {
            Properties props = new Properties();
            File f = new File(PROFILE_FILE);

            if (!f.exists()) {
                return defaultVal;
            }

            try (FileInputStream fis = new FileInputStream(f)) {
                props.load(fis);
            }

            String key = Main.getCurrentUser() + suffix;
            return props.getProperty(key, defaultVal);

        } catch (IOException e) {
            return defaultVal;
        }
    }

    // Salva una proprietà nel file, con debug avanzato
    private void saveUserProperty(String suffix, String value) {
        try {
            Properties props = new Properties();
            File file = new File(PROFILE_FILE);

            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    props.load(fis);
                }
            }

            String key = Main.getCurrentUser() + suffix;
            props.setProperty(key, value);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                props.store(fos, "User Profiles - Updated");
            }

        } catch (IOException e) {
        }
    }

    @Override
    protected void configureScene(Scene scene) {
        super.configureScene(scene);
        if (stage != null) stage.setTitle("PLAY - Il tuo Profilo");
    }

    // Modello dati per un obiettivo semplice (nome + stato sblocco)
    private static class SimpleAchievement {
        String name;
        boolean unlocked;

        SimpleAchievement(String name, boolean unlocked) {
            this.name = name;
            this.unlocked = unlocked;
        }
    }

}