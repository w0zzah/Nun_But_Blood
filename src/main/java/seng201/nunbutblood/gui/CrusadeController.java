package seng201.nunbutblood.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import seng201.nunbutblood.models.Saints;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.services.CrusadeService;

/**
 * <h1>Crusade Controller manages core gameplay loop.</h1>
 * <br>
 * <h2>FUNCTIONS : </h2>
 * <br>
 * <p><b>setupDemonDisplay() - </b>  Takes the selected demons stats + portrait
 * and creates a relevant info into {@link #demonContainer}</p>
 * <br>
 * <p><b>refreshUI() - </b> Updates the Saint's stamina after roll, the {@link #crusadeProgressBar}, and the {@link #eventLog}</p>
 * <br>
 * <p><b>handleRoll(Saints) - </b> Rolls the dice, plays the animation, and checks for a success.
 * Sets {@link  #rollResultLabel} and {@link #narrativeLabel}. Then checks for if the game is over.</p>
 * <br>
 * <p><b>showEndDialog(String, String, boolean) -</b>  Transitions out of the screen declaring victory or defeat.</p>
 */
public class CrusadeController {

    @FXML private FlowPane partyContainer;
    @FXML private VBox demonContainer;
    @FXML private Label rollResultLabel;
    @FXML private Label narrativeLabel;
    @FXML private ProgressBar crusadeProgressBar;
    @FXML private TextArea eventLog;

    // Dice Animation Fields
    @FXML private StackPane diceBox;
    @FXML private Label diceLabel;
    @FXML private Label outcomeLabel;

    private DiceRollAnimation diceAnimation;
    private CrusadeService manager;
    private Covenant covenant;
    private Demon currentDemon;
    private ScreenNavigator navigator;

    public void init(Covenant covenant, Demon demon, ScreenNavigator navigator) {
        this.covenant = covenant;
        this.currentDemon = demon;
        this.navigator = navigator;
        this.manager = new CrusadeService();

        // Initialize the animation theater
        this.diceAnimation = new DiceRollAnimation(
                diceBox,
                diceLabel,
                outcomeLabel,
                navigator.getAudioService()
        );

        setupDemonDisplay();
        refreshUI();
    }

    private void setupDemonDisplay() {
        demonContainer.getChildren().clear();

        // Demon Portrait
        String path = "/images/evil/" + currentDemon.getPortraitKey();
        var resource = getClass().getResourceAsStream(path);

        ImageView demonImg = new ImageView();
        if (resource != null) {
            demonImg.setImage(new Image(resource));
        } else {
            System.err.println("MISSING IMAGE: Could not find demon portrait at " + path);
        }

        demonImg.setFitHeight(200);
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

            Button rollBtn = new Button("1");
            rollBtn.setDisable(saint.getCurrentStamina() < 20);
            rollBtn.setOnAction(e -> handleRoll(saint));

            VBox saintCard = SaintCardController.createSaintCard(saint, rollBtn, true);
            partyContainer.getChildren().add(saintCard);
        }

        double progress = manager.getCurrentStep() / 5.0;
        crusadeProgressBar.setProgress(progress);

        eventLog.setText(manager.getLog());
        eventLog.setScrollTop(Double.MAX_VALUE);
    }

    private void handleRoll(Saints selectedSaint) {
        // 1. LOCK UI: Prevent spam clicking while animating
        partyContainer.setDisable(true);

        // 2. GET RESULT: Process logic instantly behind the scenes
        CrusadeService.StepResult result = manager.performStep(selectedSaint, currentDemon, covenant.getActiveParty());

        // 3. PLAY ANIMATION: Pass result type ("success", "crit", "fail") to generate colors
        diceAnimation.play(result.roll(), result.type(), () -> {

            // 4. CALLBACK: This runs ONLY after the die finishes shaking and landing
            rollResultLabel.setText(String.valueOf(result.roll()));
            narrativeLabel.setText(result.narrative());

            // 5. UPDATE BACKEND/UI CONDITIONS
            if (manager.checkVictory()) {
                showEndDialog("DIVINE VICTORY", "The demon has been exorcised!", true);
            } else if (manager.checkGameOver(covenant.getActiveParty())) {
                showEndDialog("CRUSADE FAILED", "Your saints have collapsed...", false);
            } else {
                partyContainer.setDisable(false);
                refreshUI();
            }
        });
    }


    private void showEndDialog(String title, String message, boolean won) {
        if (won) {
            int reward = manager.dropLoot(currentDemon);
            covenant.addFaith(reward);
            covenant.payActiveParty();
            navigator.goToHub();
            covenant.addScore(currentDemon.getDifficultyRating());
            covenant.advanceCrusade();
        } else {
            navigator.goToHub();
        }
    }
}