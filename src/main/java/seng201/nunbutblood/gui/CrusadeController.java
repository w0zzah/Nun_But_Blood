package seng201.nunbutblood.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import seng201.nunbutblood.models.Saints; // or Saints depending on your model name
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.services.CrusadeService;

import java.util.List;

public class CrusadeController {

    @FXML private VBox partyContainer;
    @FXML private VBox demonContainer;
    @FXML private Label rollResultLabel;
    @FXML private Label narrativeLabel;
    @FXML private ProgressBar crusadeProgressBar;
    @FXML private TextArea eventLog;

    private CrusadeService manager;
    private Covenant covenant;
    private Demon currentDemon;
    private ScreenNavigator navigator;

    public void init(Covenant covenant, Demon demon, ScreenNavigator navigator) {
        this.covenant = covenant;
        this.currentDemon = demon;
        this.navigator = navigator;
        this.manager = new CrusadeService();

        setupDemonDisplay();
        refreshUI();
    }

    private void setupDemonDisplay() {
        demonContainer.getChildren().clear();

        // Demon Portrait
        String path = "/images/evil/" + currentDemon.getPortraitKey();
        ImageView demonImg = new ImageView(new Image(getClass().getResourceAsStream(path)));
        demonImg.setFitHeight(250);
        demonImg.setPreserveRatio(true);

        Label name = new Label(currentDemon.getName());
        name.getStyleClass().add("demon-name-label");

        Label difficulty = new Label("Difficulty: " + currentDemon.getDifficultyRating());

        demonContainer.getChildren().addAll(demonImg, name, difficulty);
    }

    private void refreshUI() {
        // 1. Refresh Party (Left Side)
        partyContainer.getChildren().clear();
        for (Saints saint : covenant.getActiveParty()) {

            // Create a "Roll" button for each saint
            Button rollBtn = new Button("SELECT TO ROLL");
            rollBtn.setDisable(saint.getCurrentStamina() < 20);
            rollBtn.setOnAction(e -> handleRoll(saint));

            // Reuse your existing card controller!
            VBox saintCard = SaintCardController.createSaintCard(saint, rollBtn, true);
            partyContainer.getChildren().add(saintCard);
        }

        // 2. Update Progress
        double progress = manager.getCurrentStep() / 5.0;
        crusadeProgressBar.setProgress(progress);

        // 3. Update Log
        eventLog.setText(manager.getLog());
        eventLog.setScrollTop(Double.MAX_VALUE); // Keep scroll at bottom
    }

    private void handleRoll(Saints selectedSaint) {
        // Perform the step logic we wrote earlier
        CrusadeService.StepResult result = manager.performStep(selectedSaint, currentDemon, covenant.getActiveParty());

        // Update Central Visuals
        rollResultLabel.setText(String.valueOf(result.roll()));
        narrativeLabel.setText(result.narrative());

        // Check for End Conditions
        if (manager.checkVictory()) {
            showEndDialog("DIVINE VICTORY", "The demon has been exorcised!", true);
        } else if (manager.checkGameOver(covenant.getActiveParty())) {
            showEndDialog("CRUSADE FAILED", "Your saints have collapsed from exhaustion...", false);
        } else {
            refreshUI();
        }
    }

    private void showEndDialog(String title, String message, boolean won) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        if (won) {
            int reward = manager.dropLoot(currentDemon, covenant.getActiveParty().size());
            covenant.addFaith(reward);
            navigator.goToHub();
        } else {
            navigator.goToHub();
        }
    }
}