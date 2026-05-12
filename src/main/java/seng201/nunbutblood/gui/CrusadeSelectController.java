package seng201.nunbutblood.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.services.DemonService;

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
        this.covenant = covenant; // Make sure this.covenant is assigned!
        this.demonService = demonService;

        // Generate new demons only if we don't have current options
        if (demonService.getCurrentOptions().isEmpty()) {
            demonService.generateNewOptions(covenant.getCurrentCrusade());
        }

        crusadeLabel.setText("Crusade " + covenant.getCurrentCrusade() + " of " + covenant.getTotalCrusades());
        renderDemonCards();
    }

    public void renderDemonCards() {
        demonCardBox.getChildren().clear();
        // Call the method from the service, not the service itself
        for (Demon demon : demonService.getCurrentOptions()) {
            VBox card = buildDemonCard(demon);
            HBox.setHgrow(card, Priority.ALWAYS);
            demonCardBox.getChildren().add(card);
        }
    }

    @FXML
    private void handleBegin() {
        if (selectedDemon == null) {
            hintLabel.setText("You must choose a demon to continue!");
            hintLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        // 2. Tell the navigator to launch the battle screen
        navigator.goToCrusade(selectedDemon);
    }

    public VBox buildDemonCard(Demon demon) {

        VBox card = new VBox(10);
        card.getStyleClass().add("demon-card");
        card.setMaxWidth(Double.MAX_VALUE);


        Label name = new Label(demon.getName());
        name.getStyleClass().add("card-name");
        name.setWrapText(true);

        Label weakness = new Label("Weak to: " + demon.getWeakness());
        weakness.getStyleClass().add("card-type");

        Label difficulty = new Label("Difficulty: " + demon.getDifficultyRating());
        difficulty.getStyleClass().add("card-stats");

        Label reward = new Label("Reward: " + demon.getFaithReward());
        difficulty.getStyleClass().add("card-type");

        Button selectBtn = new Button("Exorcise");
        selectBtn.getStyleClass().add("glow-button");
        selectBtn.setMaxWidth(Double.MAX_VALUE);
        selectBtn.setOnAction(e -> selectedDemon(demon, card, selectBtn));

        card.getChildren().addAll(name, weakness, difficulty, reward, selectBtn);
        return card;
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
