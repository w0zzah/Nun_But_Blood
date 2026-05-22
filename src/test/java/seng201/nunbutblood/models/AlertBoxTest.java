package seng201.nunbutblood.models;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class AlertBoxTest {

    @BeforeAll
    static void startJavaFx() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.startup(latch::countDown);

        latch.await();
    }

    @Test
    void showAlertSetsTextAndMakesBoxVisible() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            Label label = new Label();
            Pane pane = new Pane();

            AlertBox.showAlert(label, pane, "CRITICAL SUCCESS");

            assertEquals("CRITICAL SUCCESS", label.getText());
            assertTrue(pane.isVisible());
            assertEquals(1, pane.getOpacity());

            latch.countDown();
        });

        latch.await();
    }
}