package seng201.nunbutblood.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.services.AudioService;
import seng201.nunbutblood.services.MarketService;
import seng201.nunbutblood.services.RandomService;
import seng201.nunbutblood.services.DemonService;

import java.io.IOException;

public class ScreenNavigator {

    private final Stage stage;
    private Covenant covenant;
    private MarketService marketService;
    private RandomService randomService;
    private final DemonService demonService;
    private final AudioService audioService;

    public ScreenNavigator(Stage stage) {
        this.stage = stage;
        this.demonService = new DemonService();
        this.audioService = new AudioService();
    }

    /** Start of the game flow */
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
     */
    public void startCovenant(Covenant covenant, MarketService marketService, RandomService randomSvc) {
        this.covenant = covenant;
        this.marketService = marketService;
        this.randomService = randomSvc;
        goToHub();
    }

    /** Main navigation hub */
    public void goToHub() {
        loadScreen("/fxml/HubInterface.fxml", controller -> {
            if (controller instanceof HubController hc) {
                hc.init(stage, this, covenant);
            }
        });
    }

    public void goToMarket() {
        loadScreen("/fxml/ShopInterface.fxml", controller -> {
            if (controller instanceof MarketController mc) {
                mc.init(stage, this, covenant, marketService);
            }
        });
    }

    /** Step 1: Select a Demon */
    public void goToCrusadeSelect() {
        loadScreen("/fxml/SelectDemonInterface.fxml", controller -> {
            if (controller instanceof CrusadeSelectController csc) {
                csc.init(stage, this, covenant, demonService);
            }
        });
    }

    /** Step 2: The actual battle/dice roll screen */
    public void goToCrusade(Demon chosenDemon) {
        loadScreen("/fxml/CrusadeInterface.fxml", controller -> {
            if (controller instanceof CrusadeController cc) {
                cc.init(covenant, chosenDemon, this);
            }
        });
    }

    /** Final Score / Game Over Screen */
    public void goToEndScreen(boolean isVictory, boolean isGameOver) {
        loadScreen("/fxml/EndScreen.fxml", controller -> {
            if (controller instanceof EndScreenController ec) {
                ec.init(stage, this, covenant, isVictory, isGameOver);
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

            // You can adjust the default resolution here
            Scene scene = new Scene(root, 1280, 720);

            // Apply global CSS (including your tab and dialog styles)
            String style = getClass().getResource("/css/style.css").toExternalForm();
            scene.getStylesheets().add(style);

            stage.setScene(scene);
            stage.setTitle("Nun'But Blood");
            stage.show();
            stage.setFullScreen(true);
        } catch (IOException e) {
            throw new RuntimeException("CRITICAL: Failed to load FXML: " + fxmlPath, e);
        }
    }

    public AudioService getAudioService() {
        return this.audioService;
    }



    @FunctionalInterface
    private interface ControllerInjector {
        void inject(Object controller);
    }
}