package seng201.nunbutblood.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Saints;
import seng201.nunbutblood.controllers.DiceRollAnimation;
import seng201.nunbutblood.controllers.ScreenNavigator;

import java.util.Random;

public class LootingController {

    @FXML private StackPane diceBox;
    @FXML private Label diceLabel;
    @FXML private TextArea lootFound;
    @FXML private VBox lootingContainer;
    @FXML private VBox lootingProgress;
    @FXML private ProgressBar lootingTotalStamina;
    @FXML private Label narrativeLabel;
    @FXML private Label outcomeLabel;
    @FXML private FlowPane partyContainer;
    @FXML private Button prayForBlessing;
    @FXML private Label rollResultLabel;
    @FXML private Button searchCorpse;
    @FXML private Button searchSurrondings;
    @FXML private Button returnToCovenant;

    private Covenant covenant;
    private ScreenNavigator navigator;
    private DiceRollAnimation diceAnimation;
    private final Random random = new Random();

    private int totalStartingLootStamina;
    private int lootFoundAmount = 0;
    private int faithFoundAmount = 0;

    public void init(Covenant covenant, ScreenNavigator navigator) {
        this.covenant = covenant;
        this.navigator = navigator;

        this.totalStartingLootStamina = getTotalRemainingStamina();

        this.diceAnimation = new DiceRollAnimation(
                diceBox,
                diceLabel,
                outcomeLabel,
                navigator.getAudioService(),
                0
        );

        lootFound.setText("");
        narrativeLabel.setText("The crusade is over. Search the ruins using your saints' remaining stamina.");
        rollResultLabel.setText("");

        refreshUI();
    }

    private void refreshUI() {
        partyContainer.getChildren().clear();

        for (Saints saint : covenant.getActiveParty()) {
            Button statusButton = new Button("STAMINA: " + saint.getCurrentStamina());
            statusButton.setDisable(true);

            VBox saintCard = SaintCardController.createSaintCard(saint, statusButton, true);
            partyContainer.getChildren().add(saintCard);
        }

        if (totalStartingLootStamina <= 0) {
            lootingTotalStamina.setProgress(0);
        } else {
            lootingTotalStamina.setProgress(getTotalRemainingStamina() / (double) totalStartingLootStamina);
        }

        boolean canLoot = getTotalRemainingStamina() > 0;

        searchCorpse.setDisable(!canLoot);
        searchSurrondings.setDisable(!canLoot);
        prayForBlessing.setDisable(!canLoot);
    }

    private int getTotalRemainingStamina() {
        int total = 0;

        for (Saints saint : covenant.getActiveParty()) {
            total += saint.getCurrentStamina();
        }

        return total;
    }

    private Saints getBestAvailableSaint() {
        Saints best = null;

        for (Saints saint : covenant.getActiveParty()) {
            if (saint.getCurrentStamina() > 0) {
                if (best == null || saint.getCurrentStamina() > best.getCurrentStamina()) {
                    best = saint;
                }
            }
        }

        return best;
    }

    private void spendStamina(int amount) {
        int remaining = amount;

        while (remaining > 0) {
            Saints saint = getBestAvailableSaint();

            if (saint == null) {
                return;
            }

            saint.setCurrentStamina(saint.getCurrentStamina() - 1);
            remaining--;
        }
    }

    @FXML
    private void searchCorpse() {
        performLootAction(
                "Searching the fallen corpse...",
                5,
                "You found relic coins among the dead.",
                "The corpse had already been stripped clean."
        );
    }

    @FXML
    private void searchSurrondings() {
        performLootAction(
                "Searching the cursed surroundings...",
                8,
                "You found supplies hidden in the ruins.",
                "The ruins were empty and silent."
        );
    }

    @FXML
    private void prayForBlessing() {
        performLootAction(
                "Praying for a final blessing...",
                10,
                "Your prayer was answered. Faith was restored.",
                "Only silence answered your prayer."
        );
    }

    private void performLootAction(String startText, int staminaCost, String successText, String failText) {
        if (getTotalRemainingStamina() <= 0) {
            narrativeLabel.setText("Your saints are too exhausted to continue looting.");
            refreshUI();
            return;
        }

        partyContainer.setDisable(true);
        searchCorpse.setDisable(true);
        searchSurrondings.setDisable(true);
        prayForBlessing.setDisable(true);

        int actualCost = Math.min(staminaCost, getTotalRemainingStamina());
        spendStamina(actualCost);

        int roll = random.nextInt(20) + 1;
        int bonus = actualCost / 2;
        int total = roll + bonus;

        narrativeLabel.setText(startText);

        String type = total >= 12 ? "success" : "fail";

        diceAnimation.play(roll, type, bonus, () -> {
            rollResultLabel.setText(String.valueOf(total));

            if (total >= 12) {
                int reward = random.nextInt(15) + 10 + bonus;
                lootFoundAmount += reward;

                if (startText.contains("Praying")) {
                    faithFoundAmount += reward;
                    lootFound.appendText("Faith +" + reward + "\n");
                } else {
                    lootFound.appendText("Loot +" + reward + "\n");
                }

                narrativeLabel.setText(successText);
            } else {
                lootFound.appendText("Nothing found.\n");
                narrativeLabel.setText(failText);
            }

            partyContainer.setDisable(false);
            refreshUI();

            if (getTotalRemainingStamina() <= 0) {
                narrativeLabel.setText("Your saints are exhausted. Return to the covenant.");
            }
        });
    }

    @FXML
    private void returnToCovenant() {
        covenant.postCrusade(faithFoundAmount, lootFoundAmount);
        navigator.goToCovenantScreen();
    }




}