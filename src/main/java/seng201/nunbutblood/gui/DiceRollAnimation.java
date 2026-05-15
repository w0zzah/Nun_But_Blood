package seng201.nunbutblood.gui;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import seng201.nunbutblood.services.AudioService;
import java.util.Random;


public class DiceRollAnimation {
    private final StackPane diceContainer;
    private final Label dieLabel;
    private final Label outcomeLabel;
    private final AudioService audio;
    private final Random random = new Random();

    public DiceRollAnimation(StackPane diceContainer, Label dieLabel, Label outcomeLabel, AudioService audio) {
        this.diceContainer = diceContainer;
        this.dieLabel = dieLabel;
        this.outcomeLabel = outcomeLabel;
        this.audio = audio;
    }

    public void play(int finalValue, String resultType, Runnable onComplete) {
        audio.play(AudioService.Clip.ROLLING);

        Timeline cycling = new Timeline();
        int frames = 20;

        for (int i = 0; i< frames; i++) {
            // play random numbers
            int val = (i == frames -1) ? finalValue : random.nextInt(20) +1;
            cycling.getKeyFrames().add(new KeyFrame(Duration.millis(i * 40),
                e -> dieLabel.setText(String.valueOf(val))));
        }

        // Shake effect for the dice
        TranslateTransition shake = new TranslateTransition(Duration.millis(20), diceContainer);
        shake.setFromX(-10);
        shake.setToX(10);
        shake.setCycleCount(40);
        shake.setAutoReverse(true);

        // Syncs up shake with the numbers
        ParallelTransition rollPhase = new ParallelTransition(cycling, shake);

        rollPhase.setOnFinished(e -> {
            audio.stop(AudioService.Clip.ROLLING);
            applyLandingEffect(resultType, onComplete);
        });

        rollPhase.play();
    }

    /**
     * Once dice finished rolling make the dice bigger for impact then set colour to desired
     * (red for failure, green success), then fade out the outcome and return
     * @param type
     * @param onComplete
     */
    private void applyLandingEffect(String type, Runnable onComplete) {


        // Increase size of dice when finished to add more oomph
        ScaleTransition pop = new ScaleTransition(Duration.millis(200), diceContainer);
        pop.setFromX(1.0); pop.setToX(1.3); // increase size by 30%
        pop.setAutoReverse(true);
        pop.setCycleCount(2); // Grows then shrinks


        // we use css classes to handle colors. This keeps the java
        dieLabel.getStyleClass().removeAll("dice-crit", "dice-success", "dice-fail");
        dieLabel.getStyleClass().add("dice-" + type.toLowerCase());

        outcomeLabel.setText(type.toUpperCase());
        outcomeLabel.setOpacity(1.0);

        pop.setOnFinished(e -> {
            FadeTransition fade = new FadeTransition(Duration.millis(500), outcomeLabel);
            fade.setFromValue(1.0); fade.setToValue(0.0);
            fade.setDelay(Duration.millis(400)); // quick pause b4 fade

            fade.setOnFinished(ev -> onComplete.run());
            fade.play();
        });

        pop.play();
    }
}
