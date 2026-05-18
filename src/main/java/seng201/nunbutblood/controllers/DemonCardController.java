package seng201.nunbutblood.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import seng201.nunbutblood.models.Demon;

public class DemonCardController {

    public static VBox buildDemonCard(Demon demon, Button actionNode) {

        String path = "/images/evil/" + demon.getPortraitKey();
        var resource = DemonCardController.class.getResourceAsStream(path);

        ImageView demonImg = new ImageView();
        if (resource != null) {
            demonImg.setImage(new Image(resource));
        }
        else {
            System.err.println("MISSING IMAGE: Could not find demon portrait at " + path);
        }
        demonImg.setFitHeight(250);
        demonImg.setPreserveRatio(true);
        demonImg.setSmooth(false);

        VBox card = new VBox(3);
        card.getStyleClass().add("demon-card");
        card.setMaxWidth(Double.MAX_VALUE);

        Label name = new Label(demon.getName());
        name.getStyleClass().add("demon-card-name");
        name.setWrapText(true);

        Label weakness = new Label("Weak to: " + demon.getWeakness());
        weakness.getStyleClass().add("demon-card-type");

        Label difficulty = new Label("Difficulty: " + demon.getDifficultyRating());
        difficulty.getStyleClass().add("demon-card-stats");

        Label reward = new Label("Reward: " + demon.getFaithReward());
        reward.getStyleClass().add("demon-card-type");

        card.getChildren().addAll(demonImg, name, weakness, difficulty, reward, actionNode);
        return card;
    }
}
