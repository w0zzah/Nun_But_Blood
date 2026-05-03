package seng201.nunbutblood.gui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entry point for the JavaFX application.
 * Delegates all scene management to {@link ScreenNavigator}.
 *
 * @author seng201 teaching team (modified by team-10)
 */
public class MainWindow extends Application {

    /**
     * Initialises the primary stage and launches the setup screen.
     *
     * @param primaryStage the JavaFX-managed primary stage
     */
    @Override
    public void start(Stage primaryStage) {
        ScreenNavigator navigator = new ScreenNavigator(primaryStage);
        navigator.goToSetup();
        primaryStage.setFullScreen(true);
    }

    /**
     * Launches the JavaFX application. Must be called from a non-JavaFX class.
     *
     * @param args command-line arguments
     */
    public static void launchWrapper(String[] args) {
        launch(args);
    }
}
