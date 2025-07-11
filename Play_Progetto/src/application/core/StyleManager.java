package application.core;

import javafx.scene.Scene;

/**
 * Gestisce l'applicazione centralizzata degli stili CSS per l'applicazione PLAY.
 * Fornisce metodi per applicare fogli di stile e gestire la cache delle risorse CSS.
 */
public class StyleManager {
    private static final String CSS_PATH = "/application/application.css";
    private static String cachedCssUrl = null;

    //Applica il foglio di stile principale alla scena specificata.

    public static boolean applyMainStyles(Scene scene) {
        if (scene == null) {
            System.err.println("Impossibile applicare stili: scene is null");
            return false;
        }

        try {
            String cssUrl = getCssUrl();
            if (cssUrl != null && !scene.getStylesheets().contains(cssUrl)) {
                scene.getStylesheets().add(cssUrl);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Errore nel caricamento del file CSS: " + e.getMessage());
        }

        return false;
    }

    /**
     * Ottiene l'URL del file CSS, utilizzando la cache se disponibile.
     * @return L'URL del file CSS o null se non trovato
     */
    private static String getCssUrl() {
        if (cachedCssUrl == null) {
            try {
                var resource = StyleManager.class.getResource(CSS_PATH);
                if (resource != null) {
                    cachedCssUrl = resource.toExternalForm();
                } else {
                    System.err.println("File CSS non trovato: " + CSS_PATH);
                }
            } catch (Exception e) {
                System.err.println("Errore durante il caricamento del CSS: " + e.getMessage());
            }
        }
        return cachedCssUrl;
    }

    public static void applyInlineStyles(javafx.scene.Node styleableNode, String styles) {
        if (styleableNode != null && styles != null && !styles.trim().isEmpty()) {
            styleableNode.setStyle(styles);
        }
    }

    // Pulisce la cache degli URL CSS, forzando il ricaricamento al prossimo utilizzo.

    public static void clearCache() {
        cachedCssUrl = null;
    }

    /**
     * Verifica se il file CSS è disponibile e accessibile.
     * true se il file CSS è accessibile
     */
    public static boolean isCssAvailable() {
        return getCssUrl() != null;
    }
}