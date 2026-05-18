package seng201.nunbutblood.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.services.DemonService;
import seng201.nunbutblood.gui.DemonCardController;

import java.util.List;
public class CrusadeSelectController {
    @FXML private Label crusadeLabel;
    @FXML private HBox demonCardBox;
    @FXML private Label hintLabel;
    @FXML private Button beginBtn;

    private Stage stage;
    private ScreenNavigator navigator;
    private Covenant covenant;
    private DemonService demonService;
    private Demon selectedDemon;

    public void init(Stage stage, ScreenNavigator navigator, Covenant covenant, DemonService demonService) {
        this.stage = stage;
        this.navigator = navigator;
        this.covenant = covenant;
        this.demonService = demonService;

        demonService.generateNewOptions(covenant.getCurrentCrusade());
        crusadeLabel.setText("Crusade " + covenant.getCurrentCrusade() + " of " + covenant.getTotalCrusades());
        renderDemonCards();
    }

    public void renderDemonCards() {
        demonCardBox.getChildren().clear();

        for (Demon demon : demonService.getCurrentOptions()) {
            // 1. Create the button (selectDemon is a Button/Node)
            Button selectDemon = new Button("Select");

            // 2. FIX: Pass 'selectDemon' (the Button), NOT 'selectedDemon' (the Demon model)
            VBox card = DemonCardController.buildDemonCard(demon, selectDemon);

            HBox.setHgrow(card, Priority.ALWAYS);
            demonCardBox.getChildren().add(card);

            selectDemon.setOnAction(e -> selectedDemon(demon, card, selectDemon));
        }
    }

    @FXML
    private void handleBegin() {
        if (selectedDemon == null) {
            hintLabel.setText("You must choose a demon to continue!");
            return;
        }
        navigator.goToCrusade(selectedDemon);
    }

    private void selectedDemon(Demon demon, VBox card, Button selectBtn) {
        demonCardBox.getChildren().forEach(node -> {
            node.getStyleClass().remove("demon-card-selected");
            if (node instanceof VBox vbox) {
                vbox.getChildren().stream()
                        .filter(c -> c instanceof Button)
                        .map(c -> (Button) c)
                        .forEach((b -> b.setText("Select")));
            }
        });

        selectedDemon = demon;
        card.getStyleClass().add("demon-card-selected");
        beginBtn.setText("Rock n' Roll!");
        hintLabel.setText("Facing " + demon.getName() + " Difficulty: " +
            demon.getDifficultyRating() + " Weakness: " + demon.getWeakness());
    }

    @FXML
    private void handleBack() {navigator.goToHub();}

}
