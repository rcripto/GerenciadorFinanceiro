package br.edu.ifspsaocarlos.gerenciadorfinanceiro.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import br.edu.ifspsaocarlos.gerenciadorfinanceiro.R;
import br.edu.ifspsaocarlos.gerenciadorfinanceiro.adapter.AccountListAdapter;
import br.edu.ifspsaocarlos.gerenciadorfinanceiro.model.Account;
import br.edu.ifspsaocarlos.gerenciadorfinanceiro.model.Transaction;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private final int NEW_ACCOUNT_CODE = 0;
    private final int NEW_TRANSACTION = 1;

    // Constant to get value from another activity
    public static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
    public static String EXTRA_TRANSACTION = "EXTRA_TRANSACTION";

    private ListView accountsListView;
    private List<Account> accountListArray;
    private AccountListAdapter accountsListAdapter;

    private View createButtonAndTotalView;
    private Button createTransactionButton;
    private View placeHolderImageView;

    // save values in sharepreferences
    private SharedPreferences sharedPreferences;
    private final String SHARED_PREFERENCES = "SHARED_PREFERENCES";
    private final String SHARED_PREFERENCES_KEY = "sdmKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Minhas Contas");

        // ListView reference
        createButtonAndTotalView = findViewById(R.id.createTransactionAndTotalView);
        placeHolderImageView = findViewById(R.id.placeholderImage);

        accountsListView = findViewById(R.id.accountsListView);
        accountListArray = new ArrayList<>();

        accountsListAdapter = new AccountListAdapter(this, accountListArray);
        accountsListView.setAdapter(accountsListAdapter);
        registerForContextMenu(accountsListView);

        accountsListView.setOnItemClickListener(this);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);

        retrieveValues();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (accountListArray.isEmpty()) {
            placeHolderImageView.setVisibility(View.VISIBLE);
            createButtonAndTotalView.setVisibility(View.GONE);
        } else {
            placeHolderImageView.setVisibility(View.GONE);
            createButtonAndTotalView.setVisibility(View.VISIBLE);
        }

        Double total = 0.0;
        for (Account account : accountListArray) {
            Double accountAmount = Double.parseDouble(account.getAmount());
            total = total + accountAmount;
        }

        loadTotalAmount(total.toString());

        saveValues();
    }

    // Menu methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent createAccountIntent = new Intent(this, AccountCreateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_ACCOUNT, (Serializable) accountListArray);
        createAccountIntent.putExtras(bundle);
        startActivityForResult(createAccountIntent,NEW_ACCOUNT_CODE);
        return true;
    }

    // Navigation
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {
            case NEW_ACCOUNT_CODE:
                if (resultCode == RESULT_OK) {
                    Account newAccount = (Account) data.getSerializableExtra(EXTRA_ACCOUNT);

                    if (newAccount != null) {
                        accountListArray.add(newAccount);
                        accountsListAdapter.notifyDataSetChanged();
                        Toast.makeText(this, R.string.new_account_created_message, Toast.LENGTH_SHORT).show();
                    }
                }

                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this,R.string.canceled_account_message,Toast.LENGTH_SHORT).show();
                }

                break;
            case NEW_TRANSACTION:
                if (resultCode == RESULT_OK) {
                    Transaction newTransaction = (Transaction) data.getSerializableExtra(EXTRA_TRANSACTION);

                    if (newTransaction != null) {
                        int position = 0;
                        for (Account account : accountListArray) {
                            if (account.getName().matches(newTransaction.getAccountName())) {

                                account.addTransaction(newTransaction);
                                account.executeTransaction(newTransaction.getCredit(), newTransaction.getValue());

                                View accountCell = accountsListView.getChildAt(position);
                                TextView amountAccount = accountCell.findViewById(R.id.amountTextView);

                                double amount = Double.parseDouble(account.getAmount());
                                DecimalFormat formatter = new DecimalFormat("#,###.00");
                                amountAccount.setText("R$ " + formatter.format(amount));
                            }
                            position++;
                        }
                    }
                }
                break;
        }
    }

    // Other Methods
    public void loadTotalAmount(String totalAmount) {
        // Set the title of the Total cell and the amount
        View totalView = createButtonAndTotalView.findViewById(R.id.totalAmountView);
        TextView totalTextView = totalView.findViewById(R.id.accountNameTextView);
        TextView amountTextView = totalView.findViewById(R.id.amountTextView);
        totalTextView.setText(R.string.total_string);

        double amount = Double.parseDouble(totalAmount);
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        amountTextView.setText("R$ " + formatter.format(amount));

        // get createButton Reference
        createTransactionButton = createButtonAndTotalView.findViewById(R.id.createTransactionButton);
        createTransactionButton.setOnClickListener(this);
    }

    protected void saveValues() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(accountListArray);
        Log.v("teste","SAVE VALUES");
        Log.v("teste",json);
        editor.putString(SHARED_PREFERENCES_KEY, json);
        editor.commit();
    }

    protected void retrieveValues() {

        Gson gson = new Gson();
        String json = sharedPreferences.getString(SHARED_PREFERENCES_KEY, null);
        Type type = new TypeToken<ArrayList<Account>>() {}.getType();
        gson.fromJson(json, type);

        if (json != null) {
            Log.v("teste","RETRIEVE VALUES");
            Log.v("teste",json);
            try {
                JSONArray accountsObjects = new JSONArray(json);
                for (int i = 0; i < accountsObjects.length(); i++) {
                    JSONObject accountObject = new JSONObject(accountsObjects.get(i).toString());
                    Account account = new Account(accountObject.getString("name"),accountObject.getString("amount"));

                    String transactionsJson = accountObject.getString("transactions");
                    JSONArray transactionsObjects = new JSONArray(transactionsJson);
                    for (int j = 0; j < transactionsObjects.length(); j++) {

                        JSONObject transactionObject = new JSONObject(transactionsObjects.get(j).toString());
                        Transaction transaction = new Transaction();

                        transaction.setAccountName(transactionObject.getString("accountName"));
                        transaction.setValue(transactionObject.getString("value"));
                        transaction.setCredit(transactionObject.getBoolean("isCredit"));
                        transaction.setTransactionDescription(transactionObject.getString("transactionDescription"));
                        transaction.setTransactionDate(transactionObject.getString("transactionDate"));
                        transaction.setTransactionType(transactionObject.getString("transactionType"));

                        account.addTransaction(transaction);
                    }

                    accountListArray.add(account);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Interface implementations
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Account account = accountListArray.get(position);
        if (account.getTransactions().size() > 0) {
            Intent accountDetailIntent = new Intent(this,AccountDetailActivity.class);

            accountDetailIntent.putExtra(EXTRA_ACCOUNT, account);
            startActivity(accountDetailIntent);
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            String errorMessage = this.getString(R.string.empty_transactions);
            String alertTitle = this.getString(R.string.title_alertView);

            alertDialog.setTitle(alertTitle);
            alertDialog.setMessage(errorMessage);
            alertDialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.createTransactionButton) {
            Intent createTransaction = new Intent(this, TransactionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXTRA_TRANSACTION, (Serializable) accountListArray);
            createTransaction.putExtras(bundle);
            startActivityForResult(createTransaction,NEW_TRANSACTION);
        }
    }
}