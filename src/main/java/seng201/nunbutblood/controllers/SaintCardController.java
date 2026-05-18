package seng201.nunbutblood.controllers;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import seng201.nunbutblood.models.Saints;
import javafx.scene.layout.HBox;

/**
 * Generate Saint Card Display.
 * Takes information from {@link seng201.nunbutblood.models.SaintsRoster} , and links the data such as
 * Name, Stats, PortraitID, Rarity to create a unique card for each Saint. <br>
 *
 * {@link #createSaintCard(Saints, Node, boolean)} : Creates a distinct card for the Saint with a button to purchase the saint <br>
 * {@link #buildStatBars(Saints, boolean)} : Creates 3 rounded rectangles for each stat type <br>
 * {@link #buildStatRow(String ,int ,boolean ,String ,boolean ))} : Assigns stats to each Saint card
 */
public class SaintCardController {
    /**
     * Creates the cards that display's Saints information.
     * @param saint
     * @param actionNode
     * @return
     */
    public static VBox createSaintCard(Saints saint, Node actionNode, boolean isCompact) {

        int cardWidth = isCompact ? 180 : 200;
        int cardHeight = isCompact ? 150 : 100;
        int portraitBoxHeight = isCompact ? 150 : 200;
        int imageFitHeight = isCompact ? 120 : 150;
        int spacing = isCompact ? 2 : 3;

        VBox card = new VBox(spacing);
        // add CSS link dependent on rarity of card
        card.getStyleClass().addAll("saint-card", "rarity-" + saint.getRarity().name().toLowerCase());
        card.setPrefWidth(cardWidth);
        card.setPrefHeight(cardHeight);


        // Assign the portrait image to a new stack pane within card
        StackPane portrait = new StackPane();
        portrait.getStyleClass().add("Saint-portrait");
        portrait.setPrefHeight(portraitBoxHeight);

        // Try to find the image (if not print cant find img)
        try {

            // Assign image path to distinct portrait in SaintsRoster
            String imagePath = "/images/Saints/" + saint.getPortraitKey();
            Image img = new Image(SaintCardController.class.getResourceAsStream(imagePath), 300, 0, true, true);

            // set to ImageView to allow for easy resizing
            ImageView portraitView = new ImageView(img);
            portraitView.setFitHeight(imageFitHeight);
            portraitView.setPreserveRatio(true);

            // add the image to portrait.
            portrait.getChildren().add(portraitView);

        } catch (Exception e) {
            System.err.println("Could not find image: " + saint.getPortraitKey());
        }

        Label rarityBadge = new Label(saint.getRarity().getLabel().toUpperCase());
        rarityBadge.getStyleClass().add("rarity-" + saint.getRarity().getLabel().toLowerCase());

        Label name = new Label(saint.getName());
        name.getStyleClass().add("card-name");

        VBox statBars = buildStatBars(saint, isCompact);

        Label costLabel = new Label(String.format("Pay: %d faith/round", saint.getUpKeep()));
        costLabel.getStyleClass().add("card-cost");
        actionNode.getStyleClass().add(isCompact ? "card-btn-com" : "card-btn");

        card.getChildren().addAll(portrait, name, rarityBadge, statBars, costLabel, actionNode);
        return card;
    }

    private static VBox buildStatBars(Saints saint, boolean isCompact) {
        VBox bars = new VBox(3);
        bars.getStyleClass().add("stat-bars");
        bars.getChildren().addAll(
                buildStatRow("STR", saint.getStrength(), saint.getStatType().equals("STR"), "#AF4130", isCompact),
                buildStatRow("WIS", saint.getWisdom(), saint.getStatType().equals("WIS"), "#346687", isCompact),
                buildStatRow("DEX", saint.getDexterity(), saint.getStatType().equals("DEX"), "#609953", isCompact),
                buildStatRow("STA", saint.getCurrentStamina(), saint.getStatType().equals("STA"), "#a38934", isCompact)
                );
        return bars;
    }

    private static Node buildStatRow(String label, int value, boolean isPrimary, String colour, boolean isCompact) {
        int spacing = isCompact ? 2 : 6;

        HBox row = new HBox(spacing);
        row.setAlignment(Pos.CENTER_LEFT);
        Label lbl = new Label(label);
        lbl.getStyleClass().add(isPrimary ? "stat-label-primary" : "stat-label");

        StackPane track = new StackPane();
        track.getStyleClass().add("stat-track");
        track.setPrefHeight(isCompact ? 4 : 8);

        double fillPct = value / 20.0;
        Region fill = new Region();
        fill.maxWidthProperty().bind(track.widthProperty().multiply(fillPct));
        fill.setStyle(String.format("-fx-background-color: %s; -fx-background-radius: 4;", colour));

        track.getChildren().add(fill);
        StackPane.setAlignment(fill, Pos.CENTER_LEFT);

        Label statNum = new Label(String.valueOf(value));
        statNum.getStyleClass().add("stat-value-small");

        HBox.setHgrow(track, Priority.ALWAYS);
        row.getChildren().addAll(lbl, track, statNum);
        return row;
    }
}