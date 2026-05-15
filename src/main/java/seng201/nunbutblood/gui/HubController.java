package seng201.nunbutblood.gui;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import seng201.nunbutblood.models.Saints;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the Church Management (hub) screen.
 * Displays the active party, reserves, and inventory. Allows the player to:
 * bench/activate Saints, retire Saints, equip or use items, navigate to
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

    public void init(Stage stage, ScreenNavigator navigator, Covenant covenant) {
        this.stage = stage;
        this.navigator = navigator;
        this.covenant = covenant;
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
        for (Saints saint : covenant.getReserves()) {
            Button activateBtn = new Button("ACTIVATE");
            activateBtn.setOnAction(e -> {
                if (!covenant.activateApostle(saint)) {
                    setAlertBox("Active party is full \n (max 5).");
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
        for (Saints saint : covenant.getActiveParty()) {
            Button benchBtn = new Button("BENCH");
            benchBtn.setOnAction(e -> {
                if (!covenant.benchApostle(saint)) {
                    statusLabel.setText("Cannot bench: party must keep at least one Saint, or reserves are full.");
                } else {
                    statusLabel.setText(saint.getName() + " moved to reserves.");
                    refresh();
                }
            });

            Button retireBtn = new Button("RETIRE");
            retireBtn.setOnAction(e -> {
                if (!covenant.retireSaint(saint)) {
                    setAlertBox("Cannot retire the last Apostle.");
                } else {
                    setAlertBox(saint.getName() + " has retired.");
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

            Label itemLabel = new Label(item.getItemName() + " — " + item.getType().name());
            HBox row = new HBox(10, useBtn, itemLabel);
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            inventoryPane.getChildren().add(row);
        }
    }

    /**
     * Create's an overlay showing the alert
     * @param alert
     */
    private void setAlertBox(String alert) {
        // Set text & show box
        statusLabel.setText(alert);
        alertBox.setOpacity(1);
        alertBox.setVisible(true);

        // Fade transition
        FadeTransition fade = new FadeTransition(Duration.millis(600), alertBox);
        fade.setFromValue(1.0); fade.setToValue(0.0);
        fade.setDelay(Duration.millis(500));

        // Once finished make invisible so you can still interact
        fade.setOnFinished(event -> alertBox.setVisible(false));

        // play the scene
        fade.play();
    }

    /** Opens a ChoiceDialog allowing the player to select which Apostle to apply the item to. */
    private void handleUseItemDialog(Item item) {
        List<Saints> allSaints = new ArrayList<>(covenant.getActiveParty());
        allSaints.addAll(covenant.getReserves());

        if (allSaints.isEmpty()) {
            statusLabel.setText("You have no Saints to use this on.");
            return;
        }

        ChoiceDialog<Saints> dialog = new ChoiceDialog<>(allSaints.getFirst(), allSaints);
        dialog.setTitle("Use Item");
        dialog.setHeaderText("Applying: " + item.getItemName());
        dialog.setContentText("Select a Saint to use this on:");

        Optional<Saints> result = dialog.showAndWait();
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
        if (covenant.hasLost()) {
            navigator.goToEndScreen(false);
            return;
        }
        if (covenant.isGameComplete()) {
            navigator.goToEndScreen(true);
            return;
        }
        if (covenant.getActiveParty().isEmpty()) {
            setAlertBox("Active party is currently empty...");
            return;
        }
        navigator.goToCrusadeSelect();
    }

}