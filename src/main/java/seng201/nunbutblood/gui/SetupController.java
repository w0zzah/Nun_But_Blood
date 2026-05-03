package seng201.nunbutblood.gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Saints;
import seng201.nunbutblood.models.SaintsRoster;
import seng201.nunbutblood.services.MarketService;
import seng201.nunbutblood.services.RandomService;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the SetupInterface.fxml window
 * @author seng201 teaching team
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
        // Initialises new list of Saints with art for each
        saintsGrid.getChildren().clear();
        // for each Saint in the list...
        for (int i = 0; i < saintsList.size(); i++) {
            // get the ith saint and set to variable currentSaint
            Saints currentSaint = saintsList.get(i);
            // set that saint to a new vbox with built-in stats
            VBox card = buildSaintsCard(currentSaint);
            // Goes through and adds the Saints each by their index into a 3x2 grid
            saintsGrid.add(card, i % 3, i/ 3);
        }
    }

    private VBox buildSaintsCard(Saints a) {
        // create a new Vbox under name card with a 5px spacing vertically between children
        VBox card = new VBox(5);
        // adds CSS class saints-card & rarity to the node via the CSS with respect to its rarity
        card.getStyleClass().addAll("Saints-card", "rarity-" + a.getRarity().name().toLowerCase());
        // set width to 220 px
        card.setPrefWidth(220);

        // Portrait ID
        StackPane portrait = new StackPane();
        portrait.getStyleClass().addAll("Saint-portrait", "portrait-" + a.getPortraitKey());
        portrait.setPrefHeight(80);

        // Rarity colors
        Label rarityBadge = new Label(a.getRarity().getLabel().toUpperCase());
        rarityBadge.getStyleClass().addAll("rarity-badge", "rarity-badge-" + a.getRarity().name().toLowerCase());
        
        // Name and class 
        Label name = new Label(a.getName());
        name.getStyleClass().add("card-name");
        
        Label classLabel = new Label("[" + a.getStatType() + "specialist ]");
        classLabel.getStyleClass().add("card-type");
        
        // Stats 
        VBox statBars = buildStatBars(a);
        /**
         * Come here boi
         */
        // How much the cost each
        Label costLabel = new Label(String.format("Cost: Free | Pay: %d faith/round", a.getUpKeep()));
        costLabel.getStyleClass().add("card-cost");
        
        // select button 
        Button selectBtn = new Button("Select");
        selectBtn.getStyleClass().add("card-btn");
        selectBtn.setMaxWidth(Double.MAX_VALUE);
        selectBtn.setOnAction(e -> toggleSaintSelection(a, card, selectBtn));

        card.getChildren().addAll(portrait, rarityBadge, name, classLabel, statBars, costLabel, selectBtn);
        return card;
    }

    private VBox buildStatBars(Saints a) {
        VBox bars = new VBox(3);
        bars.getStyleClass().add("stat-bars");
        bars.getChildren().addAll(
            buildStatRow("STR", a.getStrength(), a.getStatType().equals("STR"), "red"),
            buildStatRow("WIS", a.getWisdom(), a.getStatType().equals("WIS"), "red"),
            buildStatRow("DEX", a.getDexterity(), a.getStatType().equals("DEX"), "red")
        );
        return bars;
    }

    private Node buildStatRow(String label, int value, boolean isPrimary, String colour) {
        HBox row = new HBox(6);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        Label lbl = new Label(label);
        lbl.getStyleClass().add(isPrimary ? "stat-label-primary" : "stat-label");
        lbl.setPrefWidth(30);
        
        // Background
        StackPane track = new StackPane();
        track.getStyleClass().add("stat-track");
        track.setPrefHeight(8);
        track.setMaxHeight(Double.MAX_VALUE);
        /**
         * Come back here and fix
         */
        // Fill in the bg
        double fillPct = value / 20.0;
        Region fill = new Region();
        fill.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: 4;", colour));
        fill.setPrefWidth(0); // will be set by layout
        fill.prefWidthProperty().bind(track.widthProperty().multiply(fillPct));

        track.getChildren().add(fill);
        javafx.scene.layout.StackPane.setAlignment(fill, javafx.geometry.Pos.CENTER_LEFT);

        Label valLbl = new Label(String.valueOf(value));
        valLbl.getStyleClass().add("stat-value-small");
        valLbl.setPrefWidth(22);

        HBox.setHgrow(track, javafx.scene.layout.Priority.ALWAYS);
        row.getChildren().addAll(lbl, track, valLbl);
        return row;
        
    }

    private void toggleSaintSelection(Saints a, VBox card, Button btn) {
        if (selectedSaints.contains(a)) {
            selectedSaints.remove(a);
            card.getStyleClass().remove("apostle-card-selected");
            btn.setText("SELECT");
        } else {
            if (selectedSaints.size() >= 3) {
                statusLabel.setText("You can only choose 3 starting Saints.");
                return;
            }
            selectedSaints.add(a);
            card.getStyleClass().add("apostle-card-selected");
            btn.setText("✓ CHOSEN");
        }
        saintSelectLabel.setText(selectedSaints.size() + " / 3 selected");
        statusLabel.setText(" ");
    }

    @FXML
    private void handleStart() {
        String name = nameField.getText().trim();
        if (name.length() < 3 || name.length() > 15) {
            nameError.setText("Name must be 3-15 characters");
            return;
        }
        if (!name.matches("^[a-zA-Z0-9] + $")) {
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
    }

}
