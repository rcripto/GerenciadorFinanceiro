package br.edu.ifspsaocarlos.gerenciadorfinanceiro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {

    private String name;
    private String amount;

    private List<Transaction> transactions;

    public Account() {}

    public Account(String name, String amount) {
        this.name = name;
        this.amount = amount;
        this.transactions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public void addTransaction (Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void executeTransaction(Boolean isCredit, String value) {
        Double amountDouble = Double.parseDouble(this.amount);
        Double valueDouble = Double.parseDouble(value);
        Double total = 0.0;
        if (isCredit) {
            total = amountDouble + valueDouble;
        } else {
            total = amountDouble - valueDouble;
        }

        this.amount = total.toString();
    }
}