package seng201.nunbutblood.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.services.*;

import java.io.IOException;

/**
 * The type Screen navigator.
 */
public class ScreenNavigator {

    private final Stage stage;
    private final Scene scene;

    private Covenant covenant;
    private MarketService marketService;
    private PostCrusadeService randomService;
    private final DemonService demonService;
    private final AudioService audioService;
    private CovenantService covenantService;

    /**
     * Instantiates a new Screen navigator.
     *
     * @param stage the stage
     */
    public ScreenNavigator(Stage stage) {
        this.stage = stage;
        this.demonService = new DemonService();
        this.audioService = new AudioService();

        this.scene = new Scene(new Pane(), 1280, 720);

        String style = getClass().getResource("/css/style.css").toExternalForm();
        this.scene.getStylesheets().add(style);

        this.stage.setScene(this.scene);
        this.stage.setTitle("Nun'But Blood");
    }

    /**
     * Start of the game flow
     */
    public void goToSetup() {
        loadScreen("/fxml/SetupInterface.fxml", controller -> {
            if (controller instanceof SetupController sc) {
                sc.init(stage, this);
            }
        });
    }

    /**
     * Initial Hub entry. Use this only once when finishing Setup
     * to "hook" your services into the navigator.
     *
     * @param covenant      the covenant
     * @param marketService the market service
     */
    public void startCovenant(Covenant covenant, MarketService marketService) {
        this.covenant = covenant;
        this.marketService = marketService;
        goToHub();
    }

    /**
     * Main navigation hub
     */
    public void goToHub() {
        loadScreen("/fxml/HubInterface.fxml", controller -> {
            if (controller instanceof HubController hc) {
                hc.init(stage, this, covenant);
            }
        });
    }

    /**
     * Go to market.
     */
    public void goToMarket() {
        loadScreen("/fxml/ShopInterface.fxml", controller -> {
            if (controller instanceof MarketController mc) {
                mc.init(stage, this, covenant, marketService);
            }
        });
    }

    /**
     * Step 1: Select a Demon
     */
    public void goToCrusadeSelect() {
        loadScreen("/fxml/SelectDemonInterface.fxml", controller -> {
            if (controller instanceof CrusadeSelectController csc) {
                csc.init(stage, this, covenant, demonService);
            }
        });
    }

    /**
     * Step 2: The actual battle/dice roll screen  @param chosenDemon the chosen demon
     */
    public void goToCrusade(Demon chosenDemon) {
        loadScreen("/fxml/CrusadeInterface.fxml", controller -> {
            if (controller instanceof CrusadeController cc) {
                cc.init(covenant, chosenDemon, this);
            }
        });
    }

    /**
     * Final Score / Game Over Screen  @param isVictory the is victory
     *
     * @param isGameOver the is game over
     */
    public void goToEndScreen(boolean isVictory, boolean isGameOver) {
        loadScreen("/fxml/EndScreen.fxml", controller -> {
            if (controller instanceof EndScreenController ec) {
                ec.init(stage, this, covenant, marketService, isVictory, isGameOver);
            }
        });
    }

    /**
     *
     *
     */
    private void loadScreen(String fxmlPath, ControllerInjector injector) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();
            injector.inject(controller);

            scene.setRoot(root);

            stage.setFullScreen(true);
        } catch (IOException e) {
            throw new RuntimeException("CRITICAL: Failed to load FXML: " + fxmlPath, e);
        }
    }

    /**
     * Gets audio service.
     *
     * @return the audio service
     */
    public AudioService getAudioService() {
        return this.audioService;
    }



    @FunctionalInterface
    private interface ControllerInjector {
        /**
         * Inject.
         *
         * @param controller the controller
         */
        void inject(Object controller);
    }
}