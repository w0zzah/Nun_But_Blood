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


/**
 * <h1>Crussade Controller manages core gameplay loop.</h1>
 * <br>
 * <h2>FUNCTIONS : </h2>
 * <br>
 * <p><b>setupDemonDisplay() - </b>  Takes the selected demons stats + portrait from {@link seng201.nunbutblood.services.DemonService}
 * and creates a relevant info into {@link #demonContainer}</p>
 * <br>
 * <p><b>refreshUI() - </b> Updates the Saint's stamina after roll, the {@link #crusadeProgressBar}, and the {@link #eventLog}</p>
 * <br>
 * <p><b>handleRoll(Saints) - </b> Rolls the dice and checks for a success. Set's {@link  #rollResultLabel} and {@link #narrativeLabel}
 * Then checks for if the game is over or not</p>
 * <br>
 * <p><b>showEndDialog(String, String, boolean) -</b>  Pops up and alert declaring whether you successfully exorcized the demon</p>
 *
 */
public class CrusadeController {

    //    partyContainer(FlowPane): On the left representing your team
    //    demonContainer(VBox): On the right representing the demon
    //    rollResultLabel(Label): Represents the result of the roll
    //    narrativeLabel(Label): The most recent event
    //    crusadeProgressBar(ProgressBar): The current progress of the party out of 5
    //    eventLog(TextArea): The log of the combat

    @FXML private FlowPane partyContainer;
    @FXML private VBox demonContainer;
    @FXML private Label rollResultLabel;
    @FXML private Label narrativeLabel;
    @FXML private ProgressBar crusadeProgressBar;
    @FXML private TextArea eventLog;


    private CrusadeService manager;
    private Covenant covenant;
    private Demon currentDemon;
    private ScreenNavigator navigator;

    /**
     *
     * @param covenant
     * @param demon
     * @param navigator
     */
    public void init(Covenant covenant, Demon demon, ScreenNavigator navigator) {
        this.covenant = covenant;
        this.currentDemon = demon;
        this.navigator = navigator;
        this.manager = new CrusadeService();

        setupDemonDisplay();
        refreshUI();
    }

    /**
     *
     */
    private void setupDemonDisplay() {
        demonContainer.getChildren().clear();

        // Demon Portrait
        String path = "/images/evil/" + currentDemon.getPortraitKey();
        var resource = getClass().getResourceAsStream(path);

        ImageView demonImg = new ImageView();
        if (resource != null) {
            demonImg.setImage(new Image(resource));
        }

        else {
            System.err.println("MISSING IMAGE: Could not find demon portrait at " + path);
        }
        demonImg.setFitHeight(200);
        demonImg.setPreserveRatio(true);

        Label name = new Label(currentDemon.getName());
        name.getStyleClass().add("demon-name-label");

        Label difficulty = new Label("Difficulty: " + currentDemon.getDifficultyRating());

        demonContainer.getChildren().addAll(demonImg, name, difficulty);
    }

    /**
     *
     */
    private void refreshUI() {
        // 1. Refresh Party (Left Side)
        partyContainer.getChildren().clear();
        for (Saints saint : covenant.getActiveParty()) {

            Button rollBtn = new Button("SELECT TO ROLL");
            rollBtn.setDisable(saint.getCurrentStamina() < 20);
            rollBtn.setOnAction(e -> handleRoll(saint));

            VBox saintCard = SaintCardController.createSaintCard(saint, rollBtn, true);
            partyContainer.getChildren().add(saintCard);
        }

        double progress = manager.getCurrentStep() / 5.0;
        crusadeProgressBar.setProgress(progress);

        eventLog.setText(manager.getLog());
        eventLog.setScrollTop(Double.MAX_VALUE); // Keep scroll at bottom

    }

    /**
     *
     * @param selectedSaint
     */
    private void handleRoll(Saints selectedSaint) {
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

    /**
     *
     * @param title
     * @param message
     * @param won
     */

    private void showEndDialog(String title, String message, boolean won) {

        if (won) {
            int reward = manager.dropLoot(currentDemon, covenant.getActiveParty().size());
            covenant.addFaith(reward);
            navigator.goToHub();
            covenant.addScore(currentDemon.getDifficultyRating());
            covenant.advanceCrusade();
        } else {
            navigator.goToHub();
        }
    }

}