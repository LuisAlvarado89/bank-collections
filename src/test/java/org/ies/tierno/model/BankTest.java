package org.ies.tierno.model;

import org.ies.tierno.models.Account;
import org.ies.tierno.models.Bank;
import org.ies.tierno.models.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class BankTest {

    @Test
    public void depositTest() {
        Bank bank = createTestBank();

        boolean res = bank.deposit("ES1", 150);

        Assertions.assertTrue(res);
        Assertions.assertEquals(250, bank.getAccountsByIban().get("ES1").getBalance());
    }

    @Test
    public void depositNoAccountTest() {
        Bank bank = createTestBank();

        boolean res = bank.deposit("ES1345345", 150);

        Assertions.assertFalse(res);
        Assertions.assertEquals(250, bank.getAccountsByIban().get("ES1").getBalance());
    }

    @Test
    public void customerAccountTest() {
        Bank bank = createTestBank();

        List<Account> res = bank.getCustomerAccounts("1X");
        res.sort(Comparator.comparing(Account::getIban));

        Assertions.assertEquals(
                List.of(
                        new Account("ES1", "2E", 345),
                        new Account("ES4", "2E", 1500)
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
        return new Bank("Caixa", accountsByIban, customers);
    }

}
