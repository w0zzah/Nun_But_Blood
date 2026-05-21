package seng201.nunbutblood.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    private Item consumableItem;
    private Item equipmentItem;
    private Saint mockSaint;

    @BeforeEach
    void setUp() {
        consumableItem = new Item("Blood Vial", Item.ItemType.CONSUMABLE, StatType.STRENGTH, 5, 150, "Unholy Power", "bloodVial.png");
        equipmentItem = new Item("Sacred Circlet", Item.ItemType.EQUIPMENT, StatType.WISDOM, 3, 250, "Divine Focus", "circlet.png");
        mockSaint = new Executioner("MockSaint", "portrait.png", 10, 10, 10, 20);
    }

    @Test
    void testGettersAndFormatting() {
        assertEquals("[CONSUMABLE] Blood Vial", consumableItem.getItemName());
        assertEquals(Item.ItemType.CONSUMABLE, consumableItem.getType());
        assertEquals(StatType.STRENGTH, consumableItem.getStatAffected());
        assertEquals(5, consumableItem.getBonusValue());
        assertEquals(150, consumableItem.getCost());
        assertEquals("bloodVial.png", consumableItem.getPortraitKey());

        String expectedDesc = "+5 STRENGTH | Cost: 150 Faith | Unholy Power";
        assertEquals(expectedDesc, consumableItem.getDescription());
    }

    @Test
    void testApplyEffectConsumable() {
        int initialStr = mockSaint.getStrength();
        consumableItem.applyEffect(mockSaint);
        assertEquals(initialStr + 5, mockSaint.getStrength());
    }

    @Test
    void testApplyEffectEquipment() {
        int initialWis = mockSaint.getWisdom();
        equipmentItem.applyEffect(mockSaint);
        assertEquals(initialWis + 3, mockSaint.getWisdom());
    }

    @Test
    void testAfterCrusadeEffects() {
        int equipWis = mockSaint.getWisdom();
        int conStrength = mockSaint.getStrength();
        consumableItem.applyEffect(mockSaint);
        equipmentItem.applyEffect(mockSaint);
        assertEquals(equipWis + 3, mockSaint.getWisdom());
        assertEquals(conStrength + 5, mockSaint.getStrength());
        mockSaint.clearConsumable();
        assertEquals(equipWis + 3, mockSaint.getWisdom());
        assertEquals(conStrength, mockSaint.getStrength(), "Potion Stayed After Saint.clearConsumable");
    }


    @Test
    void applyEffect() {
        Executioner saint = new Executioner(
                "Test Saint",
                "saint.png",
                10,
                5,
                3,
                20
        );

        Item item = new Item(
                "Holy Water",
                Item.ItemType.CONSUMABLE,
                StatType.STRENGTH,
                5,
                20,
                "Increases strength",
                "holywater.png"
        );

        item.applyEffect(saint);

        assertEquals(15, saint.getStrength());
    }

    @Test
    void getPortraitKey() {
        Item item = new Item(
                "Holy Water",
                Item.ItemType.CONSUMABLE,
                StatType.STRENGTH,
                5,
                20,
                "Increases strength",
                "holywater.png"
        );

        assertEquals("holywater.png", item.getPortraitKey());
    }

    @Test
    void getItemName() {
        Item item = new Item(
                "Holy Water",
                Item.ItemType.CONSUMABLE,
                StatType.STRENGTH,
                5,
                20,
                "Increases strength",
                "holywater.png"
        );

        assertEquals("[CONSUMABLE] Holy Water", item.getItemName());
    }

    @Test
    void getType() {
        Item item = new Item(
                "Holy Water",
                Item.ItemType.CONSUMABLE,
                StatType.STRENGTH,
                5,
                20,
                "Increases strength",
                "holywater.png"
        );

        assertEquals(Item.ItemType.CONSUMABLE, item.getType());
    }

    @Test
    void getStatAffected() {
        Item item = new Item(
                "Holy Water",
                Item.ItemType.CONSUMABLE,
                StatType.STRENGTH,
                5,
                20,
                "Increases strength",
                "holywater.png"
        );

        assertEquals(StatType.STRENGTH, item.getStatAffected());
    }

    @Test
    void getBonusValue() {
        Item item = new Item(
                "Holy Water",
                Item.ItemType.CONSUMABLE,
                StatType.STRENGTH,
                5,
                20,
                "Increases strength",
                "holywater.png"
        );

        assertEquals(5, item.getBonusValue());
    }

    @Test
    void getCost() {
        Item item = new Item(
                "Holy Water",
                Item.ItemType.CONSUMABLE,
                StatType.STRENGTH,
                5,
                20,
                "Increases strength",
                "holywater.png"
        );

        assertEquals(20, item.getCost());
    }

    @Test
    void getDescription() {
        Item item = new Item(
                "Holy Water",
                Item.ItemType.CONSUMABLE,
                StatType.STRENGTH,
                5,
                20,
                "Increases strength",
                "holywater.png"
        );

        assertEquals(
                "+5 STRENGTH | Cost: 20 Faith | Increases strength",
                item.getDescription()
        );
    }
}