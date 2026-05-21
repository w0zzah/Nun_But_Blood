package seng201.nunbutblood.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import seng201.nunbutblood.models.*;
import seng201.nunbutblood.services.MarketService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * Javadocs:
 * Image: https://openjfx.io/javadoc/11/javafx.graphics/javafx/scene/image/Image.html
 *
 */
public class SetupController {

    @FXML
    private Label defaultLabel;
    @FXML private TextField nameField;
    @FXML private Slider expeditionSlider;
    @FXML private Label expeditionValue;
    @FXML private ToggleButton easyMode;
    @FXML private ToggleButton hardMode;
    @FXML private GridPane saintsGrid;
    @FXML private Label saintSelectLabel;
    @FXML private Button startButton;
    @FXML private Label diffError;
    @FXML private HBox alertBox;
    @FXML private Label statusLabel;
    @FXML private Button enter;
    @FXML private VBox entryBox;

    private Stage stage;

    private ScreenNavigator navigator;

    // The first 6 Saint, 2 of each class
    private final List<Saint> saintList = new ArrayList<>();

    // The list of chosen Saint
    private final List<Saint> selectedSaints = new ArrayList<>();

    // Toggle difficulty select
    private final ToggleGroup diffGroup = new ToggleGroup();


    /**
     * Init.
     *
     * @param stage     the stage
     * @param navigator the navigator
     */
    public void init(Stage stage, ScreenNavigator navigator) {
        this.stage = stage;
        this.navigator = navigator;
        setupDifficultyToggle();
        setupSlider();
        buildSaintsList();
        createSaintCards();
    }


    private void setupDifficultyToggle() {
        easyMode.setToggleGroup(diffGroup);
        hardMode.setToggleGroup(diffGroup);
        easyMode.setSelected(true);

        // Enforce that at least one difficulty is always selected
        diffGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                oldValue.setSelected(true);
            }
        });
    }

    /**
     * Set's up the amount of expeditions the player will play through with a slider.
     * The lambda expression takes The observable value / the property of the slider at current. The old value (original)
     * and then the new value, the final value the player leaves it on.
     *
     *
     */
    private void setupSlider() {
        expeditionSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int v = oldValue.intValue();
            expeditionSlider.setValue(v);
            expeditionValue.setText(String.valueOf(v));
        });
    }

    private void buildSaintsList() {
        saintList.clear();

        List<SaintRoster.Entry> commonEntries = SaintRoster.getAllSaints().stream()
                .filter(entry -> SaintRoster.create(entry).getRarity() == Rarity.COMMON)
                .limit(6).toList();

        for (SaintRoster.Entry entry : commonEntries) {
            saintList.add(SaintRoster.create(entry));
        }
    }

    @FXML
    private void enter() {
        entryBox.setOpacity(1);
        entryBox.setVisible(true);

        // Fade transition
        FadeTransition fade = new FadeTransition(Duration.millis(1000), entryBox);
        fade.setFromValue(1.0); fade.setToValue(0.0);

        // Once finished make invisible so you can still interact
        fade.setOnFinished(event -> entryBox.setVisible(false));

        // Play the scene
        fade.play();
    }

    private void createSaintCards() {
        saintsGrid.getChildren().clear();
        for (int i = 0; i < saintList.size(); i++) {
            Saint currentSaint = saintList.get(i);

            // Define the button for Setup
            Button btn = new Button("SELECT");
            HBox action = new HBox(1);
            action.getChildren().add(btn);

            VBox card = SaintCardController.createSaintCard(currentSaint, action, false);

            btn.setOnAction(e -> toggleSaintSelection(currentSaint, card, btn));

            saintsGrid.add(card, i, 0);
        }
    }


    /**
     * Toggle's through saint dispositions during assignation (ass)
     *
     * @param clickedSaint Saint Selected
     * @param card The Saint card,  contains all information including HireButton
     * @param SelectBtn When clicked adds Saint to the selectedSaints
     */
    private void toggleSaintSelection(Saint clickedSaint, VBox card, Button SelectBtn) {

        // Check if the current selected list of saints contains clickedSaint
        if (selectedSaints.contains(clickedSaint)) {

            // if so remove it from the list of selected saints and reset the style
            selectedSaints.remove(clickedSaint);
            card.getStyleClass().remove("saint-card-active");
            SelectBtn.setText("SELECT");

            // If clickedSaint is not in the selectedSaints list
        } else {
            // If SelectedSaints is greater than 3 throw error message and return
            if (selectedSaints.size() >= 3) {
                AlertBox.showAlert(statusLabel, alertBox, "You can only choose 3 starting Saint.");
                return;
            }

            // Else, assign clickedSaint to selectedSaints
            selectedSaints.add(clickedSaint);
            card.getStyleClass().add("saint-card-active");
            SelectBtn.setText("CHOSEN");
        }
        // update selectedSaints UX
        saintSelectLabel.setText(selectedSaints.size() + " / 3 selected");
        statusLabel.setText(" ");
    }

    /**
     * Checks all relevant info before the start of the game.
     * - Checks if name is between 3-15, and only letters numbers or spaces
     * - Checks if 3 starting saints have been selected
     * - Checks between 5-15 expeditions have been selected
     * - Checks difficulty
     *
     */


    @FXML
    private void handleStart() {

        String name = nameField.getText().trim();
        if (name.length() < 3 || name.length() > 15) {
              AlertBox.showAlert(statusLabel, alertBox,"Name must be 3-15 characters");
            return;
        }
        if (!name.matches("^[a-zA-Z0-9 ]+$")) {
              AlertBox.showAlert(statusLabel, alertBox,"Only letters, numbers and spaces allowed");
            return;
        }

        if (selectedSaints.size() != 3) {
              AlertBox.showAlert(statusLabel, alertBox,"Please select exactly 3 Saint");
            return;
        }

        int crusades = (int) expeditionSlider.getValue();

        String difficulty = hardMode.isSelected() ? "Hard" : "Easy";

        Covenant covenant = new Covenant(name, crusades, difficulty);
        for (Saint a : selectedSaints) {
            covenant.addSaint(a);
        }

        MarketService market = new MarketService(new Random());
        navigator.startCovenant(covenant, market);
    }

}
