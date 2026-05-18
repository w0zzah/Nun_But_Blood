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
    @FXML private VBox sellItemPane;
    @FXML private FlowPane sellSaintPane;
    @FXML private HBox alertBox;
    @FXML private Button refresh;

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
        sellSaintPane.getChildren().clear();
        itemPane.getChildren().clear();
        sellItemPane.getChildren().clear();


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



        for (Item playerItem : covenant.getInventory()) {
            Button sellBtn = new Button("Sell");

            sellBtn.setOnAction(e -> {
                marketService.sellItem(playerItem, covenant);
                setAlertBox(playerItem.getItemName() + " sold!");
                refresh();
            });

            VBox itemCard = ItemCardController.createItemCard(playerItem, sellBtn);
            sellItemPane.getChildren().add(itemCard);
        }



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

            VBox card = SaintCardController.createSaintCard(saint, action, true);
            saintPane.getChildren().add(card);
            }

        for (Saints saint : covenant.getOwnedSaints()) {

            Button sellbtn = new Button("Sell (" + (saint.getCost() / 2) + " faith)");

            sellbtn.setOnAction(e -> {
                if (!marketService.sellSaint(saint, covenant)) {
                    setAlertBox("You can't sell your last Saint!");
                } else {
                    setAlertBox(saint.getName() + " sold for " + (saint.getCost() / 2) + "faith!");
                    refresh();}
            });

            // Create HBox containing hire button
            HBox action = new HBox(1);
            action.getChildren().add(sellbtn);

            VBox card = SaintCardController.createSaintCard(saint, action, true);
            sellSaintPane.getChildren().add(card);
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

    @FXML
    private void shopRefresh() {
        if (covenant.spendFaith(50)) {
            marketService.refresh(covenant.getCurrentCrusade());
            setAlertBox("Refreshing...!");
        } else {
            setAlertBox("Not enough Faith!");
        }
    }
}