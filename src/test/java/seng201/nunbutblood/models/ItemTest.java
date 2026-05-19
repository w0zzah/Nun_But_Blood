package seng201.nunbutblood.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

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
                Item.ItemType.UPGRADE,
                "strength",
                5,
                20,
                "Increases strength",
                "holywater.png"
        );

        item.applyEffect(saint);

        assertEquals(15, saint.getStrength());
    }

    @Test
    void applySkillTomeEffect() {
        Executioner saint = new Executioner(
                "Test Saint",
                "saint.png",
                10,
                5,
                3,
                20
        );

        Item item = new Item(
                "Tome of Fire",
                Item.ItemType.SKILL_TOME,
                "wisdom",
                0,
                30,
                "Fire Blast",
                "tome.png"
        );

        item.applyEffect(saint);

        assertEquals("Fire Blast", saint.getSpecialAbility());
    }

    @Test
    void getPortraitKey() {
        Item item = new Item(
                "Holy Water",
                Item.ItemType.UPGRADE,
                "strength",
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
                Item.ItemType.UPGRADE,
                "strength",
                5,
                20,
                "Increases strength",
                "holywater.png"
        );

        assertEquals("[UPGRADE] Holy Water", item.getItemName());
    }

    @Test
    void getType() {
        Item item = new Item(
                "Holy Water",
                Item.ItemType.UPGRADE,
                "strength",
                5,
                20,
                "Increases strength",
                "holywater.png"
        );

        assertEquals(Item.ItemType.UPGRADE, item.getType());
    }

    @Test
    void getStatAffected() {
        Item item = new Item(
                "Holy Water",
                Item.ItemType.UPGRADE,
                "strength",
                5,
                20,
                "Increases strength",
                "holywater.png"
        );

        assertEquals("strength", item.getStatAffected());
    }

    @Test
    void getBonusValue() {
        Item item = new Item(
                "Holy Water",
                Item.ItemType.UPGRADE,
                "strength",
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
                Item.ItemType.UPGRADE,
                "strength",
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
                Item.ItemType.UPGRADE,
                "strength",
                5,
                20,
                "Increases strength",
                "holywater.png"
        );

        assertEquals(
                "+5 strength | Cost: 20 Faith | Increases strength",
                item.getDescription()
        );
    }
}