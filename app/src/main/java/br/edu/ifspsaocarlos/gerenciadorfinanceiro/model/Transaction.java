package br.edu.ifspsaocarlos.gerenciadorfinanceiro.model;

import java.io.Serializable;

public class Transaction implements Serializable {

    private String accountName;
    private String value;
    private Boolean isCredit;
    private String transactionDescription;
    private String transactionDate;
    private String transactionType;

    public Transaction() {}

    public Transaction(String value, Boolean isCredit, String transactionDescription, String creationDate) {
        this.value = value;
        this.isCredit = isCredit;
        this.transactionDescription = transactionDescription;
        this.transactionDate = creationDate;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getCredit() {
        return isCredit;
    }

    public void setCredit(Boolean credit) {
        isCredit = credit;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}