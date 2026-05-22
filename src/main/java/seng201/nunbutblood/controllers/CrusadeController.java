package seng201.nunbutblood.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import seng201.nunbutblood.models.Saint;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.models.StatType;
import seng201.nunbutblood.services.*;

import java.util.Random;


/**
 * Crusade Controller manages core gameplay loop.
 * <br>
 * <h2>FUNCTIONS : </h2>
 * <br>
 * <p><b>setupDemonDisplay() - </b>  Takes the selected demons stats + portrait
 * and creates a relevant info into {@link #demonContainer}</p>
 * <br>
 * <p><b>refreshUI() - </b> Updates the Saint's sanity after roll, the {@link #crusadeProgressBar}, and the {@link #eventLog}</p>
 * <br>
 * <p><b>handleRoll(Saint) - </b> Rolls the dice, plays the animation, and checks for a success.
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
    @FXML private Label demonName;


    // Dice Animation Fields
    @FXML private StackPane diceBox;
    @FXML private Label diceLabel;
    @FXML private Label outcomeLabel;

    @FXML private Label statChange;
    @FXML private Label eventQuestion;
    @FXML private VBox eventBox;
    @FXML private Button choiceAButton;
    @FXML private Button choiceBButton;
    @FXML private Label statTypeLabel;


    private DiceRollAnimation diceAnimation;
    private CrusadeService crusadeService;
    private Covenant covenant;
    private Demon currentDemon;
    private ScreenNavigator navigator;
    private int bonus;
    private AudioService audioService;
    private MidCrusadeEvents midCrusadeEvents;
    private MidCrusadeEvents.Event currentEvent;
    private final Random random = new Random();
    private StatType rollStatType;
    // Stores the narrative result of the last adventure choice, displayed at the top of the next event
    private String result;


    /**
     * Init.
     *
     * @param covenant  the covenant
     * @param demon     the demon
     * @param navigator the navigator
     */
    public void init(Covenant covenant, Demon demon, ScreenNavigator navigator, AudioService audioService) {
        this.covenant = covenant;
        this.currentDemon = demon;
        this.navigator = navigator;
        this.crusadeService = new CrusadeService();
        this.audioService = audioService;
        this.midCrusadeEvents = new MidCrusadeEvents();

        this.diceAnimation = new DiceRollAnimation(
                diceBox,
                diceLabel,
                outcomeLabel,
                navigator.getAudioService(),
                bonus
        );
        audioService.stopMusic();
        audioService.playMusic(AudioService.Track.CRUSADE);
        setupDemonDisplay();
        refreshUI();
    }

    private void setupDemonDisplay() {
        Button emptyDemonButton = new Button("");
        VBox card = DemonCardController.buildDemonCard(currentDemon, emptyDemonButton, false);
        HBox.setHgrow(card, Priority.ALWAYS);
        demonContainer.getChildren().add(card);
        demonName.setText(currentDemon.getName());
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
        currentEvent = midCrusadeEvents.getRandomEvent(covenant.getActiveParty());

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
        fade.play();
        refreshUI();
    }

    private void refreshUI() {
        partyContainer.getChildren().clear();
        for (Saint saint : covenant.getActiveParty()) {
            Button rollBtn = new Button("ROLL");
            rollBtn.getStyleClass().add("saint-button-fight");
            rollBtn.setDisable(saint.getCurrentSanity() < 5);
            rollBtn.setOnAction(e -> handleRoll(saint));

            VBox saintCard = SaintCardController.createSaintCard(saint, rollBtn, true);
            partyContainer.getChildren().add(saintCard);
        }

        double stepsLeft = (double) (crusadeService.getTotalSteps() - crusadeService.getCurrentStep());
        double progress = stepsLeft / crusadeService.getTotalSteps();
        progress = Math.max(0.0, Math.min(1.0, progress));

        // Next Stat type to check n set label
        rollStatType = StatType.getRandomOf(StatType.STRENGTH, StatType.WISDOM, StatType.DEXTERITY);
        statTypeLabel.setText(rollStatType.toString());
        crusadeProgressBar.setProgress(progress);

        eventLog.setText(crusadeService.getLog());
        eventLog.setScrollTop(Double.MAX_VALUE);
    }

    private void handleRoll(Saint selectedSaint) {
        partyContainer.setDisable(true);

        if (currentDemon.getWeakness().equals(rollStatType)) {
            bonus = selectedSaint.getAttributeValue(rollStatType) + 3;
        } else {bonus = selectedSaint.getAttributeValue(rollStatType);}

        CrusadeService.StepResult stepResult = crusadeService.performStep(selectedSaint, currentDemon, covenant.getActiveParty(), rollStatType);

        diceAnimation.play(stepResult.roll(), stepResult.type(), bonus, () -> {
            rollResultLabel.setText(stepResult.roll() +" + " + bonus);
            refreshUI();
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
        covenant.postCrusade(0,0,false);
        navigator.goToEndScreen(false, false);
    }

    private void postCrusade(boolean won) {
        if (won) {
            int reward = currentDemon.getFaithReward();
            int score = currentDemon.getDifficultyRating() * 10;
            covenant.postCrusade(reward, score, true);
            navigator.goToEndScreen(true, covenant.isGameComplete());
        } else {
            covenant.postCrusade(0, 0, false);
            navigator.goToEndScreen(false, covenant.isGameComplete());
        }
    }
}