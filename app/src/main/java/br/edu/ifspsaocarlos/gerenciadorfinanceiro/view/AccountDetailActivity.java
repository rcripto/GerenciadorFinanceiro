package br.edu.ifspsaocarlos.gerenciadorfinanceiro.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.gerenciadorfinanceiro.R;
import br.edu.ifspsaocarlos.gerenciadorfinanceiro.adapter.AccountDetailListAdapter;
import br.edu.ifspsaocarlos.gerenciadorfinanceiro.model.Transaction;
import br.edu.ifspsaocarlos.gerenciadorfinanceiro.model.Account;

public class AccountDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
    private ListView accountDetailListView;
    private List<Transaction> accountListTransactionsArray;
    private AccountDetailListAdapter accountDetailListAdapter;
    private Spinner statementType;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        // Fill title Activity with the name of account
        account = new Account();
        Intent intent = getIntent();
        account = (Account) intent.getSerializableExtra(EXTRA_ACCOUNT);
        getSupportActionBar().setTitle(account.getName());

        // ListView Reference
        accountDetailListView = findViewById(R.id.accountDetailListView);
        accountListTransactionsArray = new ArrayList<>();
        accountListTransactionsArray = account.getTransactions();

        accountDetailListAdapter = new AccountDetailListAdapter(this,accountListTransactionsArray);
        accountDetailListView.setAdapter(accountDetailListAdapter);

        // setting spinner to show the bank statement
        statementType = findViewById(R.id.selectStatementTypeSpinner);
        String[] expensesType = {"Por período","Por natureza","Por tipo"};
        ArrayAdapter<String> spinnerExpenseType = new ArrayAdapter<>(this,R.layout.spinner,expensesType);
        spinnerExpenseType.setDropDownViewResource(R.layout.spinner);
        statementType.setAdapter(spinnerExpenseType);
        statementType.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                accountDetailListAdapter = new AccountDetailListAdapter(this,accountListTransactionsArray);
                accountDetailListView.setAdapter(accountDetailListAdapter);
                break;
            case 1:
                List<Transaction> auxArray = new ArrayList<>();
                List<Transaction> internalAuxArray = new ArrayList<>();
                for (Transaction tr : accountListTransactionsArray) {
                    if (tr.getCredit()) {
                        auxArray.add(tr);
                    } else {
                        internalAuxArray.add(tr);
                    }
                }

                auxArray.addAll(internalAuxArray);
                accountDetailListAdapter = new AccountDetailListAdapter(this,auxArray);
                accountDetailListView.setAdapter(accountDetailListAdapter);
                break;
            case 2:
                List<Transaction> mainArray = new ArrayList<>();
                List<Transaction> alimentacaoArray = new ArrayList<>();
                List<Transaction> moradiaArray = new ArrayList<>();
                List<Transaction> transporteArray = new ArrayList<>();
                List<Transaction> entretenimentoArray = new ArrayList<>();
                List<Transaction> outrosArray = new ArrayList<>();
                List<Transaction> noTypeArray = new ArrayList<>();
                for (Transaction tr : accountListTransactionsArray) {
                    if (tr.getTransactionType().equals("Alimentação")) {
                        alimentacaoArray.add(tr);
                    } else
                    if (tr.getTransactionType().equals("Moradia")) {
                        moradiaArray.add(tr);
                    } else
                    if (tr.getTransactionType().equals("Transporte")) {
                        transporteArray.add(tr);
                    } else
                    if (tr.getTransactionType().equals("Entretenimento")) {
                        entretenimentoArray.add(tr);
                    } else
                    if (tr.getTransactionType().equals("Outros")) {
                        outrosArray.add(tr);
                    } else {
                        noTypeArray.add(tr);
                    }
                }

                mainArray.addAll(alimentacaoArray);
                mainArray.addAll(moradiaArray);
                mainArray.addAll(transporteArray);
                mainArray.addAll(entretenimentoArray);
                mainArray.addAll(outrosArray);
                mainArray.addAll(noTypeArray);

                accountDetailListAdapter = new AccountDetailListAdapter(this,mainArray);
                accountDetailListView.setAdapter(accountDetailListAdapter);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.print("");
    }
}