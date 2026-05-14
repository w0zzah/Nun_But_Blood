package seng201.nunbutblood.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import seng201.nunbutblood.models.Covenant;

public class EndScreenController {

    private Covenant covenant;
    private ScreenNavigator navigator;
    private boolean isVictory;
    @FXML private Label score;
    @FXML private Label result;


    public void init(Stage stage, ScreenNavigator screenNavigator, Covenant covenant, boolean isVictory) {
        this.covenant = covenant;
        this.navigator = screenNavigator;
        this.isVictory = isVictory;

        score(covenant, isVictory);
    }

    public void score(Covenant covenant, boolean isVictory){
        score.setText("Score: " + covenant.getScore());
        if (isVictory) {
            result.setText("Victory!");
        } else {
            result.setText("GAME OVER");
        }

    }

    @FXML
    private void goToSetup() {navigator.goToSetup();}
}
