package org.ies.tierno.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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

    @Test
    public void withdrawAccountNotFoundTest() {
        Bank bank = createTestBank();

        boolean success = bank.withdraw("ES33", 1200);

        Assertions.assertFalse(success);
    }

    @Test
    public void transferTest() {
        // Preparación
        Bank bank = createTestBank();

        // Test
        boolean transfer = bank.transfer("ES1", "ES2", 50);

        // Comprobación
        Account originAccount = bank.findAccount("ES1");
        Account destinationAccount = bank.findAccount("ES2");

        Assertions.assertTrue(transfer);
        Assertions.assertEquals(50, originAccount.getBalance());
        Assertions.assertEquals(60, destinationAccount.getBalance());
    }

    @Test
    public void transferFalseOriginTest() {
        // Preparación
        Bank bank = createTestBank();

        // Test
        boolean transfer = bank.transfer("ES8", "ES2", 300);

        // Comprobación
        Account destinationAccount = bank.findAccount("ES2");

        Assertions.assertFalse(transfer);
        Assertions.assertEquals(10, destinationAccount.getBalance());
    }

    @Test
    public void transferFalseDestinationTest() {
        // Preparación
        Bank bank = createTestBank();

        // Test
        boolean transfer = bank.transfer("ES1", "ES8", 50);

        // Comprobación
        Account originAccount = bank.findAccount("ES1");

        Assertions.assertFalse(transfer);
        Assertions.assertEquals(100, originAccount.getBalance());
    }

    @Test
    public void transferNotEnoughMoneyTest() {
        // Preparación
        Bank bank = createTestBank();

        // Test
        boolean transfer = bank.transfer("ES1", "ES2", 150);

        // Comprobación
        Account originAccount = bank.findAccount("ES1");
        Account destinationAccount = bank.findAccount("ES2");

        Assertions.assertFalse(transfer);

        Assertions.assertEquals(100, originAccount.getBalance());
        Assertions.assertEquals(10, destinationAccount.getBalance());
    }

    private Bank createTestBank() {
        List<Customer> customers = List.of(
                new Customer("1X", "Bob", "Esponja", 28000),
                new Customer("2X", "Peppa", "Pig", 28003),
                new Customer("3X", "George", "Pig", 28001),
                new Customer("4X", "Otro", "Persona", 28003)
        );

        Map<String, Account> accountsByIban = Map.of(
                "ES1", new Account("ES1", "1X", 100),
                "ES2", new Account("ES2", "1X", 10),
                "ES3", new Account("ES3", "2X", 1000),
                "ES4", new Account("ES4", "3X", 50),
                "ES5", new Account("ES5", "4X", 1050)
        );

        return new Bank("BBVA", customers, accountsByIban);
    }
}




