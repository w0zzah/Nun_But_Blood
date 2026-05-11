package seng201.nunbutblood.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.services.AudioService;
import seng201.nunbutblood.services.MarketService;
import seng201.nunbutblood.services.RandomService;
import seng201.nunbutblood.services.DemonService;

import java.io.IOException;

/**
 * Centralised screen navigation helper.
 * <p>
 * Holds references to the primary {@link Stage}, the current {@link Covenant}
 * (game state), and shared services so they persist across scene transitions.
 * Each {@code goTo*()} method loads an FXML file, injects dependencies into the
 * controller via its {@code init()} method, and sets the scene on the stage.
 * </p>
 */
public class ScreenNavigator {

    /** The application's sole primary stage. */
    private final Stage stage;

    /** The player's Covenant — shared across all screens. */
    private Covenant covenant;

    /** Market service — refreshed after each crusade. */
    private MarketService marketService;

    /** Random event service — fires post-crusade. */
    private RandomService randomService;

    /** Demon factory — generates crusade options each round. */
    private final DemonService DemonService;

    /**
     * Audio service — loaded once and shared across all screens.
     * Persists here so clips stay in memory and play instantly.
     */
    private final AudioService audioService;

    /**
     * Constructs a ScreenNavigator bound to the given stage.
     *
     * @param stage the application's primary stage
     */
    public ScreenNavigator(Stage stage) {
        this.stage = stage;
        this.DemonService = new DemonService();
        this.audioService = new AudioService();
    }

    /**
     * Navigates to the game setup screen (name / expeditions / difficulty / starting apostles).
     */
    public void goToSetup() {
        loadScreen("/fxml/SetupInterface.fxml", controller -> {
            if (controller instanceof SetupController sc) {
                sc.init(stage, this);
            }
        });
    }
    /** Navigates to the Market screen. */
    public void goToMarket() {
        loadScreen("/fxml/MarketInterface.fxml", controller -> {
            if (controller instanceof MarketController mc) {
                mc.init(stage, this, covenant, marketService);
            }
        });
    }

    /** Navigates to the Crusade selection screen. */
    public void goToCrusadeSelect() {
        loadScreen("/fxml/CrusadeSelect.fxml", controller -> {
            if (controller instanceof CrusadeController cc) {
                 cc.init(stage, this, covenant, randomService, DemonService);
             }
        });
    }

    /** Navigates to the End Game summary screen. */
    public void goToEndScreen(boolean isVictory) {
        loadScreen("/fxml/EndScreen.fxml", controller -> {
            if (controller instanceof EndScreenController ec) {
                 ec.init(stage, this, covenant, isVictory);
             }
        });
    }
    /**
     * Navigates to the Church Management hub after setup is complete.
     *
     * @param covenant      the newly created Covenant
     * @param marketService the freshly initialised MarketService
     * @param randomSvc     the RandomEventService for this game
     */
    /** Navigates to the Church Management hub using the existing game state. */
    public void goToHubFirst(Covenant covenant, MarketService marketService, RandomService randomSvc) {
        this.covenant = covenant;
        this.marketService = marketService;
        this.randomService = randomSvc;
        goToHub();
        loadScreen("/fxml/testInterface.fxml", controller -> {
            if (controller instanceof HubController hc) {
                hc.init(stage, this, covenant);
            }
        });
    }


    /** Navigates to the Church Management hub using the existing game state. */
    public void goToHub() {
        loadScreen("/fxml/testInterface.fxml", controller -> {
            if (controller instanceof HubController hc) {
                hc.init(stage, this, covenant);
            }
        });
    }

    /**
     * Loads an FXML file, injects the controller via the provided consumer,
     * and swaps the stage's scene.
     *
     * @param fxmlPath   classpath resource path to the FXML file
     * @param injector   lambda that receives the raw controller object for injection
     */
    private void loadScreen(String fxmlPath, ControllerInjector injector) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Object controller = loader.getController();
            injector.inject(controller);
            Scene scene = new Scene(root, 860, 620);
            scene.getStylesheets().add(
                    getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Nun'But Blood");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load screen: " + fxmlPath, e);
        }
    }


    /** Functional interface for injecting dependencies into a loaded controller. */
    @FunctionalInterface
    private interface ControllerInjector {
        void inject(Object controller);
    }


    /** @return the current Covenant */
    public Covenant getCovenant() { return covenant; }

    /** @return the MarketService */
    public MarketService getMarketService() { return marketService; }

    /** @return the RandomEventService */
    public RandomService getRandomService() { return randomService; }

    /** @return the shared AudioService */
    public AudioService getAudioService() { return audioService; }
}
