package seng201.nunbutblood.controllers;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import seng201.nunbutblood.models.AlertBox;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Item;
import seng201.nunbutblood.models.Saint;
import seng201.nunbutblood.services.MarketService;

/**
 * The type Market controller.
 */
public class MarketController {

    @FXML private Label faithLabel;
    @FXML private Label statusLabel;
    @FXML private FlowPane saintPane;
    @FXML private VBox itemPane;
    @FXML private VBox sellItemPane;
    @FXML private FlowPane sellSaintPane;
    @FXML private HBox alertBox;

    private Stage stage;
    private ScreenNavigator navigator;
    private Covenant covenant;
    private MarketService marketService;

    /**
     * Init.
     *
     * @param stage         the stage
     * @param navigator     the navigator
     * @param covenant      the covenant
     * @param marketService the market service
     */
    public void init(Stage stage, ScreenNavigator navigator, Covenant covenant, MarketService marketService) {
        this.stage = stage;
        this.navigator = navigator;
        this.covenant = covenant;
        this.marketService = marketService;
        refresh();
    }

    private void refresh() {
        faithLabel.setText("Faith: " + covenant.getFaith());

        clearAllPanes();
        populateBuyItems();
        populateSellItems();
        populateBuySaints();
        populateSellSaints();
    }

    private Button getHireBtn(Saint saint) {
        Button hireBtn = new Button("HIRE (" + saint.getCost() + " faith)");

        // Define Market Logic
        hireBtn.setOnAction(e -> {
            if (!marketService.buySaint(saint, covenant)) {
                AlertBox.showAlert(statusLabel, alertBox, "Unable to hire, you lack faith!");
            } else {
                AlertBox.showAlert(statusLabel, alertBox, saint.getName() + " hired for " + saint.getCost() + "faith!");
                refresh();
            }
        });
        return hireBtn;
    }

    private void clearAllPanes() {
        saintPane.getChildren().clear();
        sellSaintPane.getChildren().clear();
        itemPane.getChildren().clear();
        sellItemPane.getChildren().clear();
    }

    /**
     * Populate buy items.
     */
    public void populateBuyItems() {
        for (Item item : marketService.getAvailableItems()) {
            Button buyBtn = new Button("Buy (" + item.getCost() + " Faith)");

            buyBtn.setOnAction(e -> {
                if (!marketService.buyItem(item, covenant)) {
                    AlertBox.showAlert(statusLabel, alertBox, "Not enough Faith");
                } else {
                    AlertBox.showAlert(statusLabel, alertBox, item.getItemName() + " purchased!");
                    refresh();
                }
            });

            // Create the item
            VBox itemCard = ItemCardController.createItemCard(item, buyBtn);
            itemPane.getChildren().add(itemCard);
        }
    }

    /**
     * Populate sell items.
     */
    public void populateSellItems() {
        for (Item playerItem : covenant.getInventory()) {
            Button sellBtn = new Button("Sell");

            sellBtn.setOnAction(e -> {
                marketService.sellItem(playerItem, covenant);
                AlertBox.showAlert(statusLabel, alertBox, playerItem.getItemName() + " sold!");
                refresh();
            });

            VBox itemCard = ItemCardController.createItemCard(playerItem, sellBtn);
            sellItemPane.getChildren().add(itemCard);
        }

    }

    /**
     * Populate buy saints.
     */
    public void populateBuySaints() {
        // purchase logic for the Saint
        for (Saint saint : marketService.getAvailableSaints()) {
            Button hireBtn = getHireBtn(saint);
            hireBtn.getStyleClass().add("saint-button");

            // Create HBox containing hire button
            HBox action = new HBox(1);

            action.getChildren().add(hireBtn);

            VBox card = SaintCardController.createSaintCard(saint, action, true);
            saintPane.getChildren().add(card);
        }
    }

    /**
     * Populate sell saints.
     */
    public void populateSellSaints() {
        for (Saint saint : covenant.getOwnedSaints()) {

            Button sellBtn = new Button("Sell (" + (saint.getCost() / 2) + " faith)");

            sellBtn.setOnAction(e -> {
                if (!marketService.sellSaint(saint, covenant)) {
                    AlertBox.showAlert(statusLabel, alertBox, "You can't sell your last Saint!");
                } else {
                    AlertBox.showAlert(statusLabel, alertBox, saint.getName() + " sold for " + (saint.getCost() / 2) + "faith!");
                    refresh();}
            });
            sellBtn.getStyleClass().add("saint-button");
            // Create HBox containing hire button
            HBox action = new HBox(1);
            action.getChildren().add(sellBtn);
            VBox card = SaintCardController.createSaintCard(saint, action, true);
            sellSaintPane.getChildren().add(card);
        }
    }



    @FXML
    private void handleBack() {
        navigator.goToHub();
    }

    @FXML
    private void shopRefresh() {
        if (covenant.spendFaith(50)) {

            AlertBox.showAlert(statusLabel, alertBox, "Refreshing...!");
            marketService.refresh(covenant.getCurrentCrusade());
            PauseTransition refresh = new PauseTransition(Duration.seconds(2));
            refresh.setOnFinished(e -> {
                navigator.goToMarket();
            });

            refresh.play();
        } else {
            AlertBox.showAlert(statusLabel, alertBox, "Not enough Faith!");
        }
    }
}