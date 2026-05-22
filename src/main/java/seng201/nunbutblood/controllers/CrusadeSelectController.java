package seng201.nunbutblood.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.services.DemonService;

/**
 * The type Crusade select controller.
 */
public class CrusadeSelectController {
    @FXML private Label crusadeLabel;
    @FXML private HBox demonCardBox;
    @FXML private Label hintLabel;

    private Stage stage;
    private ScreenNavigator navigator;
    private Covenant covenant;
    private DemonService demonService;

    /**
     * Init.
     *
     * @param stage        the stage
     * @param navigator    the navigator
     * @param covenant     the covenant
     * @param demonService the demon service
     */
    public void init(Stage stage, ScreenNavigator navigator, Covenant covenant, DemonService demonService) {
        this.stage = stage;
        this.navigator = navigator;
        this.covenant = covenant;
        this.demonService = covenant.getDemonService();

        int currentCrusade = covenant.getCurrentCrusade();
        int totalCrusades = covenant.getTotalCrusades();

        crusadeLabel.setText("Crusade " + currentCrusade+ " of " + totalCrusades);
        renderDemonCards();
    }

    /**
     * Render demon cards.
     */
    public void renderDemonCards() {
        demonCardBox.getChildren().clear();
        for (Demon demon : demonService.getCurrentOptions()) {

            Button selectDemon = new Button("EXORCISE");

            VBox card = DemonCardController.buildDemonCard(demon, selectDemon, true);

            HBox.setHgrow(card, Priority.ALWAYS);
            demonCardBox.getChildren().add(card);

            selectDemon.setOnAction(e -> handleBegin(demon));
        }
    }

    @FXML
    private void handleBegin(Demon demon) {
        if (demon == null) {
            hintLabel.setText("You must choose a demon to continue!");
            return;
        }
        navigator.goToCrusade(demon);
    }

    @FXML
    private void handleBack() {navigator.goToHub();}

}
