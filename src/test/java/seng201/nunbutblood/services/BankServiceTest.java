package seng201.nunbutblood.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankServiceTest {

    private BankService testBank;

    @BeforeEach
    void setup() {
        this.testBank = new BankService("Hard");
        testBank.spendFaith(100); // set faith to zero
    }

    @Test
    void spendMoreFaithThenOwnedTest() {
        assertFalse(testBank.spendFaith(1));
    }

    @Test
    void spendNegativeDoesntAlterValue() {
        assertEquals(0, testBank.getFaith());
        testBank.spendFaith(-100);
        assertEquals(0, testBank.getFaith(), "Negative faith should change faith value");
    }

}
