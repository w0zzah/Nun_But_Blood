package seng201.nunbutblood.controllers;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import seng201.nunbutblood.models.Saint;
import javafx.scene.layout.HBox;
import seng201.nunbutblood.models.SaintRoster;

/**
 * Generate Saint Card Display.
 * Takes information from {@link SaintRoster} , and links the data such as
 * Name, Stats, PortraitID, Rarity to create a unique card for each Saint. <br>
 * <p>
 * {@link #createSaintCard(Saint, Node, boolean)} : Creates a distinct card for the Saint with a button to purchase the saint <br>
 * {@link #buildStatBars(Saint, boolean)} : Creates 3 rounded rectangles for each stat type <br>
 * {@link #buildStatRow(String, int, boolean, String, boolean)} : Assigns stats to each Saint card
 */
public class SaintCardController {
    /**
     * Creates the cards that display's Saint information.
     *
     * @param saint      the saint
     * @param actionNode the action node
     * @param isCompact  the is compact
     * @return the v box
     */
    public static VBox createSaintCard(Saint saint, Node actionNode, boolean isCompact) {

        int cardWidth = isCompact ? 200 : 230;
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

            // Assign image path to distinct portrait in SaintRoster
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

    private static VBox buildStatBars(Saint saint, boolean isCompact) {
        VBox bars = new VBox(3);
        bars.getStyleClass().add("stat-bars");

        GridPane grid = new GridPane();
        grid.setHgap(isCompact ? 4 : 6);
        grid.setVgap(isCompact ? 2 : 3);

        ColumnConstraints labelCol = new ColumnConstraints();
        labelCol.setHgrow(Priority.NEVER);

        ColumnConstraints barCol = new ColumnConstraints();
        barCol.setHgrow(Priority.ALWAYS);
        barCol.setFillWidth(true);

        ColumnConstraints numCol = new ColumnConstraints();
        numCol.setHgrow(Priority.NEVER);
        numCol.setMinWidth(isCompact ? 14 : 20);
        numCol.setHalignment(HPos.CENTER);

        grid.getColumnConstraints().addAll(labelCol, barCol, numCol);

        addStatRow(grid, 0, "STRENGTH",  saint.getStrength(),        saint.getStatType().equals("STR"), "#AA0000", isCompact);
        addStatRow(grid, 1, "WISDOM",    saint.getWisdom(),          saint.getStatType().equals("WIS"), "#790000", isCompact);
        addStatRow(grid, 2, "DEXTERITY", saint.getDexterity(),       saint.getStatType().equals("DEX"), "#530001", isCompact);
        addStatRow(grid, 3, "SANITY",    saint.getCurrentSanity(),   saint.getStatType().equals("STA"), "#4B0B26", isCompact);

        bars.getChildren().add(grid);
        return bars;
    }

    private static void addStatRow(GridPane grid, int rowIndex, String label, int value,
                                   boolean isPrimary, String colour, boolean isCompact) {
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
        statNum.setAlignment(Pos.CENTER);


        GridPane.setValignment(track, VPos.CENTER);
        GridPane.setValignment(lbl, VPos.CENTER);
        GridPane.setValignment(statNum, VPos.CENTER);

        grid.add(lbl,     0, rowIndex);
        grid.add(track,   1, rowIndex);
        grid.add(statNum, 2, rowIndex);
    }
}