package seng201.nunbutblood.controllers;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import seng201.nunbutblood.models.Item;

import java.io.InputStream;

/**
 * Generate item Card Display.
 * Takes information from {@link seng201.nunbutblood.models.Item} , and links the data such as
 * Name, Stats, PortraitID, Rarity to create a unique card for each item. <br>
 * <p>
 */
public class ItemCardController {
    /**
     * Creates the cards that display's items information.
     *
     * @param item       the item
     * @param actionNode the action node
     * @return the v box
     */
    public static VBox createItemCard(Item item, Node actionNode) {


        VBox card = new VBox(2);
        // add CSS link dependent on rarity of card
        card.getStyleClass().addAll("item-card");
        card.setPrefWidth(500);
        card.setPrefHeight(100);


        // Assign the portrait image to a new stack pane within card
        StackPane portrait = new StackPane();
        portrait.getStyleClass().add("item-portrait");

        String imagePath = "/images/items/" + item.getPortraitKey();
        InputStream stream = ItemCardController.class.getResourceAsStream(imagePath);

        // Try to find the image (if not print cant find img)
        if (stream != null) {
            Image img = new Image(stream, 100, 75, true, false);
            ImageView portraitView = new ImageView(img);
            portraitView.setPreserveRatio(true);
            portrait.getChildren().add(portraitView);
        } else {
            System.err.println("Could not find image: " + imagePath);
        }

        Label name = new Label(item.getItemName());
        if (item.getType() == Item.ItemType.EQUIPMENT) {
            card.getStyleClass().add("item-card-equipment");
        } else { card.getStyleClass().add("item-card-potion");}

        Label descLabel = new Label(item.getDescription());
        descLabel.getStyleClass().add("item-card-desc");
        actionNode.getStyleClass().add("item-card-btn");

        card.getChildren().addAll(name, portrait, descLabel, actionNode);
        return card;
    }
}