package seng201.nunbutblood.gui;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Item;
import seng201.nunbutblood.models.Saints;
import seng201.nunbutblood.services.MarketService;
import seng201.nunbutblood.services.RandomService;

public class MarketController {

    @FXML private Label faithLabel;
    @FXML private Label statusLabel;
    @FXML private FlowPane saintPane;
    @FXML private VBox itemPane;
    @FXML private VBox inventoryPane;

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
                    statusLabel.setText("Unable to hire, you lack faith!");
                } else {
                    statusLabel.setText(saint.getName() + " hired for " + saint.getCost() + "faith!");
                    refresh();
                }
            });

            // Create HBox containing hire button
            HBox action = new HBox(1);
            action.getChildren().add(hireBtn);

            VBox card = SaintCardController.createSaintCard(saint, action, true);
            saintPane.getChildren().add(card);
        }
        itemPane.getChildren().clear();

        // Purchase logic for the items
        for (Item item : marketService.getAvailableItems()) {
            Button buyBtn = new Button("Buy (" + item.getCost() + " Faith)");

            buyBtn.setOnAction(e -> {
                if (!marketService.buyItem(item, covenant)) {
                    statusLabel.setText("Not enough Faith to buy " + item.getItemName() + ".");
                } else {
                    statusLabel.setText(item.getItemName() + " purchased!");
                    refresh();
                }
            });

            // Row for the items to be stored
            Label descLabel = new Label(item.getDescription());
            HBox itemRow = new HBox(10, buyBtn, descLabel);
            itemRow.setAlignment(Pos.CENTER_LEFT);
            itemPane.getChildren().add(itemRow);
        }

        // Refresh player inventory
        inventoryPane.getChildren().clear();
        for (Item item : covenant.getInventory()) {
            int sellPrice = item.getCost() / 2;
            Button sellBtn = new Button("SELL (" + sellPrice + " faith)");
            sellBtn.setOnAction(e -> {
                if (!marketService.sellItem(item, covenant)) {
                    statusLabel.setText("Could not sell " + item.getItemName());
                } else {
                    statusLabel.setText("Sold " + item.getItemName() + " for " + sellPrice + " faith");
                    refresh();
                }
            });
            Label invLabel = new Label(item.getItemName() + " [" + item.getType() + "]");
            HBox invRow = new HBox(10, sellBtn, invLabel);
            invRow.setAlignment((Pos.CENTER_LEFT));
            inventoryPane.getChildren().add(invRow);
        }
    }
    @FXML
    private void handleBack() {
        navigator.goToHub();
    }
}
