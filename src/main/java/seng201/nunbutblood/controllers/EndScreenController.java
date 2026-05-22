package seng201.nunbutblood.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.services.AudioService;
import seng201.nunbutblood.services.MarketService;
import seng201.nunbutblood.services.PostCrusadeService;

import java.util.List;

/**
 * The type End screen controller.
 */
public class EndScreenController {


    private Covenant covenant;
    private ScreenNavigator navigator;
    private boolean isVictory;
    private MarketService market;
    private PostCrusadeService postCrusadeEvents;
    private AudioService audioService;

    @FXML private Label crusadesCompleted;
    @FXML private Label score;
    @FXML private Label result;
    @FXML private Button goNext;
    @FXML private ListView<String> eventsList;


    /**
     * Init.
     *
     * @param stage           the stage
     * @param screenNavigator the screen navigator
     * @param covenant        the covenant
     * @param market          the market
     * @param isVictory       the is victory
     * @param gameOver        the game over
     */
    public void init(Stage stage, ScreenNavigator screenNavigator, Covenant covenant, MarketService market,
            AudioService audioService, boolean isVictory, boolean gameOver) {

        this.covenant = covenant;
        this.navigator = screenNavigator;
        this.isVictory = isVictory;
        this.market = market;
        this.postCrusadeEvents = new PostCrusadeService(covenant.getDifficulty());
        this.audioService = audioService;
        audioService.stopMusic();
        audioService.playMusic(AudioService.Track.HUB);
        result(isVictory, gameOver);

    }

    /**
     * Result.
     *
     * @param isVictory the is victory
     * @param gameOver  the game over
     */
    public void result(boolean isVictory, boolean gameOver) {
        if (gameOver || covenant.hasLost()) {
            crusadesCompleted.setText("You Completed: " + covenant.getCurrentCrusade() + " / " + covenant.getTotalCrusades() + " Crusades");
            score.setText("Score: " + covenant.getScore());
            result.setText(isVictory ? covenant.getName() + " Has pushed the Demons back to Hell!" : covenant.getName() + " falls...");
            goNext.setText("Play Again?");
            goNext.setOnAction(e -> goToSetup());
        } else {
            market.refresh(covenant.getCurrentCrusade());
            result.setText(isVictory ? "Demon Vanquished" : "The Demon Flees...");
            List<String> events = postCrusadeEvents.triggerEvents(covenant);
            if (events.isEmpty()) {
                eventsList.getItems().setAll("No random events occur...");
            } else {
                eventsList.getItems().setAll(events);
            }
            goNext.setText("Back to Covenant");
            goNext.setOnAction(e -> goToHub());
        }
    }


    @FXML
    private void goToSetup() {
        navigator.goToSetup();
    }

    @FXML
    private void goToHub() {
        navigator.goToHub();}

}
