package seng201.nunbutblood.models;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Utility class providing self-dismissing notification banners within the game's UI layout context.
 * Uses automated transition threads to cleanly manage visual layout opacity.
 */
public final class AlertBox {

    // Private constructor to prevent instantiation of utility class
    private AlertBox() {}

    /**
     * Renders a temporary notification message on screen and handles its automated fade transition.
     *
     * @param statusLabel target JavaFX Label element where the messaging text string will display
     * @param alertBox    parent container Layout wrapper managing the notification's visibility state
     * @param alert       the raw notification string message to pass into the display tree
     */
    public static void showAlert(Label statusLabel, Pane alertBox, String alert) {

        // Set text & show box
        statusLabel.setText(alert);
        alertBox.setOpacity(1);
        alertBox.setVisible(true);
        statusLabel.getStyleClass().add("status-label");

        // Fade transition
        FadeTransition fade = new FadeTransition(Duration.seconds(1), alertBox);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setDelay(Duration.seconds(1));

        // Once finished make invisible
        fade.setOnFinished(event -> alertBox.setVisible(false));

        fade.play();
    }
}