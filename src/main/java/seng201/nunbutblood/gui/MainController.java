package seng201.nunbutblood.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seng201.nunbutblood.services.CounterService;

/*

    Last Edited 0350 02/04/26 @author cwi184 - "UI bridge logic"

        MainController Class --- Links FXML to Models
        TODO:
          Methods:
            ~ handleStartGame() : Validates name (3-15) and setup
            ~ updateDisplay() : Refreshes labels and party lists
            ~ switchScreen() : Navigates between Church/Market/Crusade
*/

/**
 * Controller for the SetupInterface.fxml window
 * @author seng201 teaching team
 */
public class MainController {

    @FXML
    private Label defaultLabel;

    @FXML
    private Button defaultButton;

    private CounterService counterService;

    /**
     * Initialize the window
     *
     * @param stage Top level container for this window
     */
    public void init(Stage stage) {
        counterService = new CounterService();
    }

    /**
     * Method to call when our counter button is clicked
     *
     */
    @FXML
    public void onButtonClicked() {
        System.out.println("Button has been clicked");
        counterService.incrementCounter();

        int count = counterService.getCurrentCount();
        defaultLabel.setText(Integer.toString(count));
    }
}
