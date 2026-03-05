package org.ies.tierno.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankTest {

    @Test
    public void depositTest() {
        Bank bank = createTestBank();

        boolean res = bank.deposit("ES1", 100);

        Assertions.assertTrue(res);
        Assertions.assertEquals(200, bank.getAccountsByIban().get("ES1").getBalance());
    }

    @Test
    public void depositNoAccountTest() {
        Bank bank = createTestBank();

        boolean res = bank.deposit("ES1345345", 100);

        Assertions.assertFalse(res);
    }

    @Test
    public void customerAccountsTest() {
        Bank bank = createTestBank();

        Set<Account> res = bank.getCustomerAccounts("1X").stream().collect(Collectors.toSet());

        Assertions.assertEquals(
                Set.of(
                        new Account("ES1", "1X", 100),
                        new Account("ES2", "1X", 10)
                ),
                res
        );
    }

    @Test
    public void customerAccountsNoCustomerTest() {
        Bank bank = createTestBank();

        List<Account> res = bank.getCustomerAccounts("134X");

        Assertions.assertNull(res);
    }

    @Test
    public void withdrawTest() {
        Bank bank = createTestBank();

        boolean success = bank.withdraw("ES3", 200);

        Assertions.assertTrue(success);

        Account account = bank.findAccount("ES3");
        Assertions.assertEquals(800, account.getBalance());
    }

    @Test
    public void withdrawNotEnougthBalanceTest() {
        Bank bank = createTestBank();

        boolean success = bank.withdraw("ES3", 1200);

        Assertions.assertFalse(success);

        Account account = bank.findAccount("ES3");
        Assertions.assertEquals(1000, account.getBalance());
    }

    @Test
    public void withdrawAccountNotFoundTest() {
        Bank bank = createTestBank();

        boolean success = bank.withdraw("ES33", 1200);

        Assertions.assertFalse(success);
    }
    public void transferTest(){
        Bank bank = createTestBank();
    }


    private Bank createTestBank() {
        List<Customer> customers = List.of(
                new Customer("1X", "Juan", "Perez", 28054),
                new Customer("2E", "Maria", "Molina", 28005),
                new Customer("4X", "Pedro", "Ayala", 28002),
                new Customer("5R", "Soledad", "García", 28053),
                new Customer("2T", "Pablo", "Martinez", 28054)
        );
        Map<String, Account> accountsByIban = Map.of(
                "ES1", new Account("ES1", "1X", 120),
                "ES2", new Account("ES2", "2E", 345),
                "ES3", new Account("ES3", "4X", 580),
                "ES4", new Account("ES4", "2E", 1500),
                "ES5", new Account("ES5", "5R", 3800),
                "ES6", new Account("ES6", "2T", 1780)

        );
        return new Bank("Caixa", customers, accountsByIban);
    }

}


