package org.ies.tierno.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTest {
    @Test
    public void depositTest() {
        // Preparación
        Account account = new Account("ES0001", "1X", 10d);

        // Test
        account.deposit(50d);

        // Aserciones
        Assertions.assertEquals(60d, account.getBalance(), 0.0000001);
    }

    @Test
    public void withdrawTest() {
        // Preparación
        Account account = new Account("ES1", "1X", 10d);

        // Test
        boolean res = account.withdraw(5d);

        // Aserciones
        Assertions.assertTrue(res);
        Assertions.assertEquals(5d, account.getBalance(), 0.0000001);
    }

    @Test
    public void withdrawNotEnoughBalanceTest() {
        // Preparación
        Account account = new Account("ES0001", "1X", 10d);

        // Test
        boolean res = account.withdraw(50d);

        // Aserciones
        Assertions.assertFalse(res);
        Assertions.assertEquals(10d, account.getBalance(), 0.0000001);
    }
}

