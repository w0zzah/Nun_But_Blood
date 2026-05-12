package seng201.nunbutblood.gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Saints;
import seng201.nunbutblood.models.SaintsRoster;
import seng201.nunbutblood.services.MarketService;
import seng201.nunbutblood.services.RandomService;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

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
    @FXML private Label nameError;
    @FXML private Slider expeditionSlider;
    @FXML private Label expeditionValue;
    @FXML private ToggleButton easyMode;
    @FXML private ToggleButton hardMode;
    @FXML private GridPane saintsGrid;
    @FXML private Label saintSelectLabel;
    @FXML private Label statusLabel;
    @FXML private Button startButton;
    @FXML private Label diffError;

    private Stage stage;

    private ScreenNavigator navigator;

    // The first 6 Saint, 2 of each class
    private final List<Saints> saintsList = new ArrayList<>();

    // The list of chosen Saints
    private final List<Saints> selectedSaints = new ArrayList<>();

    // Toggle difficulty select
    private final ToggleGroup diffGroup = new ToggleGroup();


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
    }

    /**
     * Set's up the amount of expeditions the player will play through with a slider.
     * The lambda expression takes The observable value / the property of the slider at current. The old value (original)
     * and then the new value, the final value the player leaves it on.
     *
     *
     */
    private void setupSlider() {
        expeditionSlider.valueProperty().addListener((obs, old, val) -> {
            int v = val.intValue();
            expeditionSlider.setValue(v);
            expeditionValue.setText(String.valueOf(v));
        });
    }

    private void buildSaintsList() {
        // Create an Array list of all possible Saints
        List<SaintsRoster.Entry> all = new ArrayList<>(SaintsRoster.getAllSaints());
        // Initialise a count of each to ensure 2 of each Executioner, Oracle and Inquisitor are picked
        int exe = 0, ora = 0, inq = 0;
        for (SaintsRoster.Entry e : all) {
            if (exe < 2 && e.type().equals("EXE")) {saintsList.add(SaintsRoster.create(e)); exe++; }
            else if (ora < 2 && e.type().equals("ORA")) {saintsList.add(SaintsRoster.create(e)); ora++; }
            else if (inq < 2 && e.type().equals("INQ")) {saintsList.add(SaintsRoster.create(e)); inq++; }

            if (exe == 2 && ora == 2 && inq == 2) break;
        }
    }

    private void createSaintCards() {
        saintsGrid.getChildren().clear();
        for (int i = 0; i < saintsList.size(); i++) {
            Saints currentSaint = saintsList.get(i);

            // Define the button for Setup
            Button btn = new Button("SELECT");
            HBox action = new HBox(1);
            action.getChildren().add(btn);


            // We need a reference to the card to change its style on click
            // So we create the card first, then set the button action
            VBox card = SaintCardController.createSaintCard(currentSaint, action, false);

            btn.setOnAction(e -> toggleSaintSelection(currentSaint, card, btn));

            saintsGrid.add(card, i, 0);
        }
    }


    /**
     * Toggle's through saint dispositions during assignation (ass)
     *
     * @param clickedSaint
     * @param card
     * @param btn
     */
    private void toggleSaintSelection(Saints clickedSaint, VBox card, Button btn) {

        // Check if the current selected list of saints contains clickedSaint
        if (selectedSaints.contains(clickedSaint)) {

            // if so remove it from the list of selected saints and reset the style
            selectedSaints.remove(clickedSaint);
            card.getStyleClass().remove("saint-card-selected");
            btn.setText("SELECT");

            // If clickedSaint is not in the selectedSaints list
        } else {
            // If SelectedSaints is greater than 3 throw error message and return
            if (selectedSaints.size() >= 3) {
                statusLabel.setText("You can only choose 3 starting Saints.");
                return;
            }

            // Else, assign clickedSaint to selectedSaints
            selectedSaints.add(clickedSaint);
            card.getStyleClass().add("saint-card-selected");
            btn.setText("CHOSEN");
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
            nameError.setText("Name must be 3-15 characters");
            return;
        }
        if (!name.matches("^[a-zA-Z0-9 ]+$")) {
            nameError.setText("Only letters, numbers and spaces allowed");
            return;
        }
        nameError.setText(" ");

        if (selectedSaints.size() != 3) {
            statusLabel.setText("Please selected exactly 3 Saints");
            return;
        }

        int expeditions = (int) expeditionSlider.getValue();
        String difficulty = hardMode.isSelected() ? "Hard" : "Easy";

        Covenant covenant = new Covenant(name, expeditions, difficulty);
        for (Saints a : selectedSaints) {
            covenant.addSaints(a);
        }

        MarketService market = new MarketService();
        RandomService randomSvc = new RandomService(difficulty);
        navigator.startCovenant(covenant, market, randomSvc);
    }

}
