package seng201.nunbutblood.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import seng201.nunbutblood.models.Saints;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.services.AudioService;
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
    private CrusadeService crusadeService;
    private Covenant covenant;
    private Demon currentDemon;
    private ScreenNavigator navigator;
    private int bonus;
    private AudioService audio;

    public void init(Covenant covenant, Demon demon, ScreenNavigator navigator) {

        this.covenant = covenant;
        this.currentDemon = demon;
        this.navigator = navigator;
        this.crusadeService = new CrusadeService();
        this.audio = new AudioService();

        // Initialize the animation theater
        this.diceAnimation = new DiceRollAnimation(
                diceBox,
                diceLabel,
                outcomeLabel,
                navigator.getAudioService(),
                bonus
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
        demonImg.setSmooth(false);

        Label name = new Label(currentDemon.getName());
        name.getStyleClass().add("demon-name-label");

        Label difficulty = new Label("Difficulty: " + currentDemon.getDifficultyRating());

        demonContainer.getChildren().addAll(demonImg, name, difficulty);
    }

    private void refreshUI() {
        // 1. Refresh Party (Left Side)
        partyContainer.getChildren().clear();
        for (Saints saint : covenant.getActiveParty()) {

            Button rollBtn = new Button("EXORCISE");
            rollBtn.setDisable(saint.getCurrentStamina() < 5);
            rollBtn.setOnAction(e -> handleRoll(saint));

            VBox saintCard = SaintCardController.createSaintCard(saint, rollBtn, true);
            partyContainer.getChildren().add(saintCard);
        }


        double progress = crusadeService.getCurrentStep() / 5.0;
        crusadeProgressBar.setProgress(progress);

        eventLog.setText(crusadeService.getLog());
        eventLog.setScrollTop(Double.MAX_VALUE);
    }

    private void handleRoll(Saints selectedSaint) {

        // Disable Saints Container
        partyContainer.setDisable(true);
        int bonus = selectedSaint.getAttributeValue(currentDemon.getWeakness());

        // Advance to the next step + diceAnimation
        CrusadeService.StepResult result = crusadeService.performStep(selectedSaint, currentDemon, covenant.getActiveParty());
        diceAnimation.play(result.roll(), result.type(), bonus, () -> {

            // Set Text below diceAnimation
            rollResultLabel.getStyleClass().add("dice-" + result.type());
            rollResultLabel.setText(String.valueOf(result.roll() + bonus));

            narrativeLabel.setText(result.narrative());

            // Check if demon has been exorcised,
            if (crusadeService.checkVictory()) {
                showEndDialog(true);
            } else if (crusadeService.checkGameOver(covenant.getActiveParty())) {
                showEndDialog(false);
            } else {
                partyContainer.setDisable(false);
                refreshUI();
            }
        });
    }

    @FXML
    private void giveUp() {
        navigator.goToEndScreen(false, false);
    }

    private void showEndDialog(boolean won) {
        if (won) {
            int reward = crusadeService.dropLoot(currentDemon);
            int score = currentDemon.getDifficultyRating() * 10;
            covenant.postCrusade(reward, score);
            navigator.goToEndScreen(true, covenant.isGameComplete());
        } else {
            covenant.postCrusade(0, 0);
            navigator.goToEndScreen(false, covenant.isGameComplete());
        }
    }
}