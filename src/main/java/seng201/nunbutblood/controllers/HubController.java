package seng201.nunbutblood.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng201.nunbutblood.models.AlertBox;
import seng201.nunbutblood.models.Saint;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Item;
import javafx.scene.control.Label;
import seng201.nunbutblood.services.AudioService;


import java.util.List;
import java.util.Optional;

/**
 * Controller for the Church Management (hub) screen.
 * Displays the active party, reserves, and inventory. Allows the player to:
 * bench/activate Saint, retire Saint, equip or use items, navigate to
 * the Market, or begin the next Crusade.
 */
public class HubController {

    @FXML private Label covenantTitle;
    @FXML private Label faithLabel;
    @FXML private Label expeditionLabel;
    @FXML private Label partyLabel;
    @FXML private Label statusLabel;
    @FXML private HBox alertBox;

    // Swapped ListViews for Layout Panes to hold our custom UI Cards
    @FXML private FlowPane activePane;
    @FXML private FlowPane reservePane;
    @FXML private HBox inventoryPane;

    private Stage stage;
    private ScreenNavigator navigator;
    private Covenant covenant;
    private AudioService audioService;

    /**
     * Init.
     *
     * @param stage     the stage
     * @param navigator the navigator
     * @param covenant  the covenant
     */
    public void init(Stage stage, ScreenNavigator navigator, Covenant covenant, AudioService audioService) {
        this.stage = stage;
        this.navigator = navigator;
        this.covenant = covenant;
        this.audioService = audioService;
        refresh();
    }


    /** Refreshes all displayed data from the Covenant model. */
    private void refresh() {
        covenantTitle.setText(covenant.getName().toUpperCase());
        faithLabel.setText("Faith: " + covenant.getFaith());
        expeditionLabel.setText("Crusade " + covenant.getCurrentCrusade()
                + " / " + covenant.getTotalCrusades());
        partyLabel.setText("Party: " + covenant.getActiveParty().size() + "/5");

        refreshActiveParty();
        refreshReserves();
        refreshInventory();
    }

    private void refreshReserves() {
        // Clears the pane
        reservePane.getChildren().clear();
        //
        for (Saint saint : covenant.getReserves()) {
            Button activateBtn = new Button("ACTIVATE");
            activateBtn.setOnAction(e -> {
                if (!covenant.activateSaint(saint)) {
                    AlertBox.showAlert(statusLabel, alertBox, "Active party is full \n (max 5).");
                }
                refresh();
            });

            Button retireBtn = new Button("RETIRE");
            retireBtn.setOnAction(e -> {
                covenant.retireSaint(saint); // No restriction on retiring from reserves
                statusLabel.setText(saint.getName() + " has retired.");
                refresh();
            });

            VBox actions = new VBox(5, activateBtn, retireBtn);
            actions.setAlignment(javafx.geometry.Pos.CENTER);

            VBox card = SaintCardController.createSaintCard(saint, actions, true);
            reservePane.getChildren().add(card);
        }
    }

    private void refreshActiveParty() {
        activePane.getChildren().clear();
        for (Saint saint : covenant.getActiveParty()) {
            Button benchBtn = new Button("BENCH");
            benchBtn.setOnAction(e -> {
                if (!covenant.benchSaint(saint)) {
                    statusLabel.setText("Cannot bench: party must keep at least one Saint, or reserves are full.");
                } else {
                    statusLabel.setText(saint.getName() + " moved to reserves.");
                    refresh();
                }
            });

            Button retireBtn = new Button("RETIRE");
            retireBtn.setOnAction(e -> {
                if (!covenant.retireSaint(saint)) {
                    AlertBox.showAlert(statusLabel, alertBox, "Cannot retire the last Apostle.");
                } else {
                    AlertBox.showAlert(statusLabel, alertBox, saint.getName() + " has retired.");
                    refresh();
                }
            });

            VBox actions = new VBox(5);
            actions.getChildren().addAll(benchBtn, retireBtn);
            actions.setAlignment(javafx.geometry.Pos.CENTER);

            VBox card = SaintCardController.createSaintCard(saint, actions, true);
            activePane.getChildren().add(card);
        }
    }

    private void refreshInventory() {
        inventoryPane.getChildren().clear();
        for (Item item : covenant.getInventory()) {
            Button useBtn = new Button("USE / EQUIP");
            useBtn.setOnAction(e -> handleUseItemDialog(item));
            useBtn.setStyle("-fx-font-size: 15");

            VBox card = ItemCardController.createItemCard(item, useBtn);
            inventoryPane.getChildren().add(card);
        }
    }

    /** Opens a ChoiceDialog allowing the player to select which Apostle to apply the item to. */
    private void handleUseItemDialog(Item item) {


        List<Saint> allSaints = covenant.getOwnedSaints();

        if (allSaints.isEmpty()) {
            statusLabel.setText("You have no Saint to use this on.");
            return;
        }

        ChoiceDialog<Saint> dialog = new ChoiceDialog<>(allSaints.getFirst(), allSaints);
        dialog.setTitle("Use Item");
        dialog.setHeaderText("Applying: " + item.getItemName());
        dialog.setContentText("Select a Saint to use this on:");

        Optional<Saint> result = dialog.showAndWait();
        result.ifPresent(saint -> {
            item.applyEffect(saint);
            covenant.removeItem(item);
            statusLabel.setText(item.getItemName() + " applied to " + saint.getName() + "!");
            refresh();
        });
    }

    @FXML
    private void handleMarket() {
        navigator.goToMarket();
    }

    @FXML
    private void handleCrusade() {
        if (covenant.getActiveParty().isEmpty()) {
            AlertBox.showAlert(statusLabel, alertBox, "Active party is currently empty...");
            return;
        }
        navigator.goToCrusadeSelect();
    }

}