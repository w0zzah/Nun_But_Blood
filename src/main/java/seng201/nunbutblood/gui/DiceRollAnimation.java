package seng201.nunbutblood.gui;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import seng201.nunbutblood.services.AudioService;
import java.util.Random;


public class DiceRollAnimation {
    private final StackPane diceContainer;
    private final int bonus;
    private final Label dieLabel;
    private final Label outcomeLabel;
    private final AudioService audio;
    private final Random random = new Random();

    public DiceRollAnimation(StackPane diceContainer, Label dieLabel, Label outcomeLabel, AudioService audio, int bonus) {
        this.diceContainer = diceContainer;
        this.dieLabel = dieLabel;
        this.outcomeLabel = outcomeLabel;
        this.audio = audio;
        this.bonus = bonus;
    }

    public void play(int finalValue, String resultType, int attributeValue, Runnable onComplete) {

        // 1. Reset Phase
        String[] styleClasses = {"dice-success", "dice-fail", "dice-critical-success", "dice-critical-fail"};
        dieLabel.getStyleClass().removeAll(styleClasses);
        outcomeLabel.getStyleClass().removeAll(styleClasses);
        outcomeLabel.setText("");
        outcomeLabel.setOpacity(0.0);

        audio.play(AudioService.Clip.ROLLING);

        // Creates an empty animation timeline where we will map out specific events at specific milliseconds
        Timeline cycling = new Timeline();
        // How many times the number will change
        int totalFrames = 25;
        // Running timer that we increase the longer it rolls
        double currentDelayMs = 1;
        // Starting duration between numbers
        double frameDuration = 15;
        // How fast the dice slows down (1.5 means it takes 50% longer than the one before)
        double slowdownFactor = 1.14;

        if (resultType.equals("success") || resultType.equals("fail")) {
            finalValue += attributeValue;
        }
        // 3. Frame Generation Phase
        for (int i = 0; i < totalFrames; i++) {
            // Calculate number and toggle the shake direction (up on even frames, down on odd)
            int val = (i == totalFrames - 1) ? finalValue : random.nextInt(20) + 1;
            int shakeOffset = (i % 2 == 0) ? 10 : -10;

            cycling.getKeyFrames().add(new KeyFrame(
                    Duration.millis(currentDelayMs),
                    e -> {
                        dieLabel.setText(String.valueOf(val));
                        diceContainer.setTranslateY(shakeOffset); // Synced perfectly with the numbers!
                    }
            ));

            frameDuration *= slowdownFactor;
            currentDelayMs += frameDuration;
        }

        // 4. Completion Phase
        cycling.setOnFinished(e -> {
            diceContainer.setTranslateY(0);
            audio.stop(AudioService.Clip.ROLLING);
            applyLandingEffect(resultType, onComplete, attributeValue);
        });

        cycling.play();
    }

    /**
     * Once dice finished rolling make the dice bigger for impact then set colour to desired
     * (red for failure, green success), then fade out the outcome and return
     * @param type
     * @param onComplete
     */
    private void applyLandingEffect(String type, Runnable onComplete, int bonus) {

        ScaleTransition pop = new ScaleTransition(Duration.millis(350), diceContainer);
        // Increase size of dice when finished to add more oomph + play sound
        switch (type) {

            case "critical-success":
                audio.play(AudioService.Clip.CRITICAL_SUCCESS);
                pop.setFromX(1.0); pop.setToX(1.5);pop.setFromY(1.0); pop.setToY(1.5);
                break;

            case "critical-fail":
                audio.play(AudioService.Clip.CRITICAL_FAIL);
                pop.setFromX(1.0); pop.setToX(1.5);pop.setFromY(1.0); pop.setToY(1.5);
                break;

            case "success":
                audio.play(AudioService.Clip.SUCCESS);
                pop.setFromX(1.0); pop.setToX(1.3);pop.setFromY(1.0); pop.setToY(1.3);
                break;

            case "fail":
                audio.play(AudioService.Clip.FAIL);
                pop.setFromX(1.0); pop.setToX(1.3);pop.setFromY(1.0); pop.setToY(1.3);
                break;
        }
        pop.setAutoReverse(true);
        pop.setCycleCount(2); // Grows then shrinks

        String[] styleClasses = {"dice-success", "dice-fail", "dice-critical-success", "dice-critical-fail"};
        outcomeLabel.getStyleClass().removeAll(styleClasses);

        dieLabel.getStyleClass().add("dice-" + type);
        outcomeLabel.getStyleClass().add("dice-" + type);

        String readableType = type.replace("-", " ").toUpperCase();
        outcomeLabel.setText("Bonus : " + bonus + "\n" + readableType);
        outcomeLabel.setOpacity(1.0);


        pop.setOnFinished(e -> {
            FadeTransition fade = new FadeTransition(Duration.millis(500), outcomeLabel);
            fade.setFromValue(1.0); fade.setToValue(0.0);
            fade.setDelay(Duration.millis(400)); // quick pause b4 fade

            fade.setOnFinished(event -> onComplete.run());
            fade.play();
        });

        pop.play();
    }
}
