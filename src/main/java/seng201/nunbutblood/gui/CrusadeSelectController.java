package seng201.nunbutblood.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Demon;

import java.util.List;
public class CrusadeSelectController {
    @FXML private Label crusadeLabel;
    @FXML private HBox demonCardBox;
    @FXML private Label hintLabel;
    @FXML private Button beginBtn;

    private Stage stage;
    private ScreenNavigator navigator;
    private Covenant covenant;
    private List<Demon> demons;
    private Demon selectedDemon;

    public void init(Stage stage, ScreenNavigator navigator, Covenant covenant, List<Demon> demons) {
        this.stage = stage;
        this.navigator = navigator;
        this.demons = demons;
        crusadeLabel.setText("Crusade " + covenant.getCurrentCrusade() + " of " + covenant.getTotalCrusades());
        renderDemonCards();
    }
    public void renderDemonCards() {
        demonCardBox.getChildren().clear();
        for (Demon demon : demons) {
            VBox card = buildDemonCard(demon);
            HBox.setHgrow(card, Priority.ALWAYS);
            demonCardBox.getChildren().add(card);
        }

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
    private void handleBegin() {
        if (selectedDemon == null) {
            hintLabel.setText("You must chose a demon to continue");
            return;
        }
    }

    @FXML
    private void handleBack() {navigator.goToHub();}

}
