package seng201.nunbutblood.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.services.MarketService;

public class EndScreenController {


    private Covenant covenant;
    private ScreenNavigator navigator;
    private boolean isVictory;
    private MarketService market;

    @FXML private Label crusadesCompleted;
    @FXML private Label score;
    @FXML private Label result;
    @FXML private Button goNext;


    public void init(Stage stage, ScreenNavigator screenNavigator, Covenant covenant, MarketService market, boolean isVictory, boolean gameOver) {
        this.covenant = covenant;
        this.navigator = screenNavigator;
        this.isVictory = isVictory;
        this.market = market;

        result(isVictory, gameOver);
    }
    public void result(boolean isVictory, boolean gameOver){
        if (gameOver) {
            crusadesCompleted.setText("You Completed: " + (covenant.getCurrentCrusade() - 1) + " / " + covenant.getTotalCrusades() + " Crusade's");
            goNext.setText("Play Again?");
            goNext.setOnAction(e -> goToSetup());
            score.setText("Score: " + covenant.getScore());
            if (isVictory) {
                result.setText("All Demon's Exorcised!!!");
            }
        } else {    
            goNext.setText("Back to Covenant");
            market.refresh(covenant.getCurrentCrusade());
            goNext.setOnAction(e -> goToHub());
            if (isVictory) {
                result.setText("Demon Exorcised!!!");
            }
            else {result.setText("YOU FAILED!");}
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
