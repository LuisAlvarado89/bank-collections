package org.ies.tierno.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Bank {
    private String name;
    private List<Customer> customers;
    private Map<String, Account> accountsByIban;

    public Account findAccount(String iban) {
        return accountsByIban.get(iban);
    }

    public boolean deposit(String iban, double amount) {
        if (accountsByIban.containsKey(iban)) {
            var account = accountsByIban.get(iban);
            account.deposit(amount);
            return true;
        } else {
            System.out.println("No se ha encontrado la cuenta");
            return false;
        }
    }

    public List<Account> getCustomerAccounts(String nif) {
        Customer customer = findCustomer(nif);
        if (customer != null) {
            List<Account> customerAccounts = new ArrayList<>();
            for (var account : accountsByIban.values()) {
                if (account.getNif().equals(nif)) {
                    customerAccounts.add(account);
                }
            }
            return customerAccounts;
        } else {
            System.out.println("No se ha encontrado el cliente");
            return null;
        }
    }

    public Optional<List<Account>> getCustomerAccountsStreams(String nif) {
//        if(findCustomerStream(nif).isEmpty()) {
//            return Optional.empty();
//        } else {
//            return Optional.of(
//                    accountsByIban
//                            .values()
//                            .stream()
//                            .filter(account -> account.getNif().equals(nif))
//                            .toList()
//            );
//        }
        return findCustomerStream(nif)
                .map(customer ->
                        accountsByIban
                                .values()
                                .stream()
                                .filter(account -> account.getNif().equals(nif))
                                .toList()
                );
    }

    public Optional<Customer> findCustomerStream(String nif) {
        return customers.stream()
                .filter(customer -> customer.getNif().equals(nif))
                .findFirst();
    }


    public Customer findCustomer(String nif) {
        for (var customer : customers) {
            if (customer.getNif().equals(nif)) {
                return customer;
            }
        }
        return null;
    }

    public boolean withdraw(String iban, double amount) {
        if (accountsByIban.containsKey(iban)) {
            var account = accountsByIban.get(iban);
            return account.withdraw(amount);
        } else {
            System.out.println("No se ha encontrado la cuenta");
            return false;
        }
    }

    public boolean transfer(String originIban, String destinationIban, double amount) {
        if (accountsByIban.containsKey(originIban) && accountsByIban.containsKey(destinationIban)) {
            var origin = accountsByIban.get(originIban);
            if (origin.getBalance() >= amount) {
                var destination = accountsByIban.get(destinationIban);
                origin.withdraw(amount);
                destination.deposit(amount);
                return true;
            } else {
                System.out.println("No hay suficiente saldo");
                return false;
            }
        } else {
            System.out.println("No existe la cuenta de origen / destino");
            return false;
        }
    }

    public Set<String> getZipCodeCustomerNifs(int zipCode) {
        Set<String> nifs = new HashSet<>();
        for (var customer : customers) {
            if (customer.getZipCode() == zipCode) {
                nifs.add(customer.getNif());
            }
        }
        return nifs;
    }

    public List<Account> findZipCodeAccounts(int zipCode) {
        var customerNifs = getZipCodeCustomerNifs(zipCode);
        List<Account> accounts = new ArrayList<>();
        for (var account : accountsByIban.values()) {
            if (customerNifs.contains(account.getNif())) {
                accounts.add(account);
            }
        }
        return accounts;
    }

    public List<Account> findZipCodeAccountsStreams(int zipCode) {
        Set<String> nifs = getZipCodeCustomerNifsStreams(zipCode);

        return accountsByIban.values().stream()
                .filter(account -> nifs.contains(account.getNif()))
                .toList();
    }

    private Set<String> getZipCodeCustomerNifsStreams(int zipCode) {
        return customers.stream()
                .filter(customer -> customer.getZipCode() == zipCode)
                .map(customer -> customer.getNif())
                .collect(Collectors.toSet());
    }


}


