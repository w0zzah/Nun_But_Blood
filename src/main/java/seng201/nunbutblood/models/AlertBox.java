package seng201.nunbutblood.models;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public final class AlertBox {

    private AlertBox() {} // prevent instantiation

    public static void showAlert(Label statusLabel, Pane alertBox, String alert) {

        // Set text & show box
        statusLabel.setText(alert);
        alertBox.setOpacity(1);
        alertBox.setVisible(true);

        // Fade transition
        FadeTransition fade = new FadeTransition(Duration.millis(800), alertBox);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setDelay(Duration.millis(500));

        fade.setDelay(Duration.millis(500));
        // Once finished make invisible
        fade.setOnFinished(event -> alertBox.setVisible(false));

        fade.play();
    }
}
