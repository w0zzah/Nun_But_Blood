package seng201.nunbutblood.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Item;
import seng201.nunbutblood.models.Saint;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MarketServiceTest {

    private MarketService marketService;
    private Covenant covenant;

    @BeforeEach
    void init() {
        marketService = new MarketService(new Random(1));
        covenant = new Covenant("Blood Covenant", 5, "Normal");
    }

    @Test
    void refresh() {

        marketService.refresh(1);

        assertFalse(marketService.getAvailableSaints().isEmpty());
        assertFalse(marketService.getAvailableItems().isEmpty());
    }

    @Test
    void buySaint() {

        covenant.addFaith(10_000);

        Saint saint = marketService.getAvailableSaints().getFirst();

        int beforeFaith = covenant.getFaith();

        boolean result = marketService.buySaint(saint, covenant);

        assertTrue(result);
        assertTrue(covenant.getOwnedSaints().contains(saint));
        assertFalse(marketService.getAvailableSaints().contains(saint));
        assertTrue(covenant.getFaith() < beforeFaith);
    }

    @Test
    void buySaintFailsIfSaintNotInMarket() {

        Saint fakeSaint = covenant.getOwnedSaints().isEmpty()
                ? null
                : covenant.getOwnedSaints().getFirst();

        if (fakeSaint != null) {
            assertFalse(marketService.buySaint(fakeSaint, covenant));
        }
    }

    @Test
    void sellSaint() {

        covenant.addFaith(10_000);

        Saint saint1 = marketService.getAvailableSaints().get(0);
        Saint saint2 = marketService.getAvailableSaints().get(1);

        assertTrue(marketService.buySaint(saint1, covenant));
        assertTrue(marketService.buySaint(saint2, covenant));

        int beforeFaith = covenant.getFaith();

        boolean result = marketService.sellSaint(saint1, covenant);

        assertTrue(result);
        assertFalse(covenant.getOwnedSaints().contains(saint1));
        assertTrue(covenant.getFaith() > beforeFaith);
    }

    @Test
    void buyItem() {

        Item item = marketService.getAvailableItems().getFirst();

        int beforeFaith = covenant.getFaith();

        boolean result = marketService.buyItem(item, covenant);

        assertTrue(result);

        assertTrue(covenant.getInventory().contains(item));

        assertFalse(marketService.getAvailableItems().contains(item));

        assertTrue(covenant.getFaith() < beforeFaith);
    }

    @Test
    void sellItem() {

        Item item = marketService.getAvailableItems().getFirst();

        marketService.buyItem(item, covenant);

        int beforeFaith = covenant.getFaith();

        boolean result = marketService.sellItem(item, covenant);

        assertTrue(result);

        assertFalse(covenant.getInventory().contains(item));

        assertTrue(covenant.getFaith() > beforeFaith);
    }

    @Test
    void getAvailableSaints() {

        assertNotNull(marketService.getAvailableSaints());

        assertFalse(marketService.getAvailableSaints().isEmpty());
    }

    @Test
    void getAvailableItems() {

        assertNotNull(marketService.getAvailableItems());

        assertFalse(marketService.getAvailableItems().isEmpty());
    }
}