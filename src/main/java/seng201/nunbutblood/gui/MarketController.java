package seng201.nunbutblood.gui;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Item;
import seng201.nunbutblood.models.Saints;
import seng201.nunbutblood.services.MarketService;

public class MarketController {

    @FXML private Label faithLabel;
    @FXML private Label statusLabel;
    @FXML private FlowPane saintPane;
    @FXML private VBox itemPane;
    @FXML private VBox inventoryPane;
    @FXML private HBox alertBox;

    private Stage stage;
    private ScreenNavigator navigator;
    private Covenant covenant;
    private MarketService marketService;

    public void init(Stage stage, ScreenNavigator navigator, Covenant covenant, MarketService marketService) {
        this.stage = stage;
        this.navigator = navigator;
        this.covenant = covenant;
        this.marketService = marketService;
        refresh();
    }

    private void refresh() {
        faithLabel.setText("Faith: " + covenant.getFaith());
        saintPane.getChildren().clear();

        // purchase logic for the Saints
        for (Saints saint : marketService.getAvailableSaints()) {
            Button hireBtn = new Button("HIRE (" + saint.getCost() + " faith)");

            // Define Market Logic
            hireBtn.setOnAction(e -> {
                if (!marketService.buySaint(saint, covenant)) {
                    setAlertBox("Unable to hire, you lack faith!");
                } else {
                    setAlertBox(saint.getName() + " hired for " + saint.getCost() + "faith!");
                    refresh();
                }
            });

            // Create HBox containing hire button
            HBox action = new HBox(1);
            action.getChildren().add(hireBtn);

            VBox card = SaintCardController.createSaintCard(saint, action, false);
            saintPane.getChildren().add(card);
        }

        itemPane.getChildren().clear();
        // Purchase logic for the items
        for (Item item : marketService.getAvailableItems()) {
            Button buyBtn = new Button("Buy (" + item.getCost() + " Faith)");

            buyBtn.setOnAction(e -> {
                if (!marketService.buyItem(item, covenant)) {
                    setAlertBox("Not enough Faith");
                } else {
                    setAlertBox(item.getItemName() + " purchased!");
                    refresh();
                }
            });

            // Create the item
            VBox itemCard = ItemCardController.createItemCard(item, buyBtn);
            itemPane.getChildren().add(itemCard);
        }



        inventoryPane.getChildren().clear();
        for (Item playerItem : covenant.getInventory()) {
            Button sellBtn = new Button("Sell");

            sellBtn.setOnAction(e -> {
                marketService.sellItem(playerItem, covenant);
                setAlertBox(playerItem.getItemName() + " sold!");
                refresh();
            });

            Label invLabel = new Label(playerItem.getItemName() + " [" + playerItem.getDescription() + "]");
            VBox invRow = new VBox(10, invLabel, sellBtn);
            invRow.setAlignment(Pos.CENTER);
            inventoryPane.getChildren().add(invRow);
        }
    }

    /**
     * Creates a temporary pop-up notice that fades out cleanly.
     */
    private void setAlertBox(String alert) {
        statusLabel.setText(alert);
        alertBox.setOpacity(1.0);
        alertBox.setVisible(true);

        FadeTransition fade = new FadeTransition(Duration.millis(800), alertBox);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.millis(500));

        fade.setOnFinished(event -> alertBox.setVisible(false));
        fade.play();
    }

    @FXML
    private void handleBack() {
        navigator.goToHub();
    }
}