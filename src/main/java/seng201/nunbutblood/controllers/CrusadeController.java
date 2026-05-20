package seng201.nunbutblood.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import seng201.nunbutblood.models.Saints;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.services.*;

import java.util.Random;


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

    @FXML private Label statChange;
    @FXML private Label eventQuestion;
    @FXML private VBox eventBox;
    @FXML private Button choiceAButton;
    @FXML private Button choiceBButton;

    private DiceRollAnimation diceAnimation;
    private CrusadeService crusadeService;
    private Covenant covenant;
    private Demon currentDemon;
    private ScreenNavigator navigator;
    private int bonus;
    private AudioService audio;
    private MidCrusadeEvents midCrusadeEvents;
    private MidCrusadeEvents.Event currentEvent;
    private final Random random = new Random();

    // Stores the narrative result of the last adventure choice, displayed at the top of the next event
    private String result;

    public void init(Covenant covenant, Demon demon, ScreenNavigator navigator) {
        this.covenant = covenant;
        this.currentDemon = demon;
        this.navigator = navigator;
        this.crusadeService = new CrusadeService();
        this.audio = new AudioService();
        this.midCrusadeEvents = new MidCrusadeEvents();

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

    /**
     * Shows the next adventure event panel.
     * Displays the result of the previous choice (if any) in the status label,
     * then loads a new random event with its two choices.
     */
    private void showNextAdventureEvent() {

        // Set for nullPointer
        eventQuestion.setText(result != null ? result : "");
        statChange.setText("");
        // Set to be visible
        eventBox.setOpacity(1);
        eventBox.setVisible(true);

        // Set's current event to be a random record containing description, choiceA, choiceB
        currentEvent = midCrusadeEvents.getRandomEvent();

        // Set desc + button names
        eventQuestion.setText(currentEvent.description());
        choiceAButton.setText(currentEvent.choiceA().text());
        choiceBButton.setText(currentEvent.choiceB().text());

        // Set onAction for each button
        choiceAButton.setOnAction(e -> handleAdventureChoice(currentEvent.choiceA()));
        choiceBButton.setOnAction(e -> handleAdventureChoice(currentEvent.choiceB()));

        refreshUI();
    }

    /**
     * Applies the player's chosen adventure choice, stores the result narrative,
     * logs it, then fades out the event panel before loading the next event.
     */
    private void handleAdventureChoice(MidCrusadeEvents.Choice choice) {
        // Store result into the class field (not a local variable) so showNextAdventureEvent can read it
        result = midCrusadeEvents.applyChoice(
                covenant.getActiveParty(),
                choice
        );

        eventLog.insertText(0, result + "\n");
        statChange.setText(result);
        FadeTransition fade = new FadeTransition(Duration.seconds(1), eventBox);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setDelay(Duration.seconds(1));
        fade.setOnFinished(event -> {
            eventBox.setVisible(false);
        });
        fade.play(); // FIX: fade.play() was missing — the animation never ran before

        refreshUI();
    }

    private void refreshUI() {
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
        partyContainer.setDisable(true);

        int bonus = selectedSaint.getAttributeValue(currentDemon.getWeakness());

            CrusadeService.StepResult stepResult = crusadeService.performStep(selectedSaint, currentDemon, covenant.getActiveParty());

        if (stepResult.hasDemonEvent()) {
            crusadeService.rollMidCrusadeEvent(covenant.getActiveParty())
                    .ifPresent(event -> eventLog.setText(event.description()));
        }

        diceAnimation.play(stepResult.roll(), stepResult.type(), bonus, () -> {
            refreshUI();

            rollResultLabel.getStyleClass().add("dice-" + stepResult.type());
            rollResultLabel.setText(String.valueOf(stepResult.roll() + bonus));
            narrativeLabel.setText(stepResult.narrative());



            if (crusadeService.checkVictory()) {
                postCrusade(true);
            } else if (crusadeService.checkGameOver(covenant.getActiveParty())) {
                postCrusade(false);
            } else {
                partyContainer.setDisable(false);
            }
            if (random.nextInt(10) < 5 ) {
                showNextAdventureEvent();
            }
        });
    }

    @FXML
    private void giveUp() {
        navigator.goToEndScreen(false, false, 0, 0);
    }

    private void postCrusade(boolean won) {
        if (won) {
            int reward = currentDemon.getFaithReward();
            int score = currentDemon.getDifficultyRating() * 10;
            navigator.goToEndScreen(true, covenant.isGameComplete(), reward, score);
        } else {
            covenant.postCrusade(0, 0, false);
            navigator.goToEndScreen(false, covenant.isGameComplete(), 0, 0);
        }
    }
}