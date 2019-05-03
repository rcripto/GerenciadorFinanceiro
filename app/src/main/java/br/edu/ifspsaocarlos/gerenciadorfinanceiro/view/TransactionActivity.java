package br.edu.ifspsaocarlos.gerenciadorfinanceiro.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.edu.ifspsaocarlos.gerenciadorfinanceiro.R;
import br.edu.ifspsaocarlos.gerenciadorfinanceiro.model.Account;
import br.edu.ifspsaocarlos.gerenciadorfinanceiro.model.Transaction;

public class TransactionActivity extends AppCompatActivity implements View.OnClickListener {

    public static String EXTRA_TRANSACTION = "EXTRA_TRANSACTION";

    private List<Account> accountListArray;

    private View expenseType;
    private Spinner accountsSpinner;
    private Spinner expenseTypeSpinner;
    private EditText transactionValue;
    private Button creditOption;
    private Button debitOption;
    private EditText descriptionTransaction;
    private Button cancelButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        getSupportActionBar().setTitle(R.string.create_transaction_title);

        // Layout reference
        accountsSpinner = findViewById(R.id.selecteAccountSpinner);
        expenseType = findViewById(R.id.expenseType);
        expenseTypeSpinner = findViewById(R.id.selecteExpenseTypeSpinner);
        transactionValue = findViewById(R.id.transactionValue);
        creditOption = findViewById(R.id.credit_type);
        debitOption = findViewById(R.id.debit_type);
        descriptionTransaction = findViewById(R.id.descriptionTransaction);
        cancelButton = findViewById(R.id.cancelButton);
        saveButton = findViewById(R.id.saveButton);

        // Setting listeners
        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        creditOption.setOnClickListener(this);
        debitOption.setOnClickListener(this);

        // Starts with no selection type
        creditOption.setSelected(false);
        debitOption.setSelected(false);

        // Retrieve value from activity
        accountListArray = new ArrayList<>();
        Intent intent = getIntent();
        accountListArray = (List<Account>) intent.getSerializableExtra(EXTRA_TRANSACTION);

        // Array to load accounts name
        String[] accountsName = new String[accountListArray.size()];
        String[] expensesType = {"Alimentação","Moradia","Transporte","Entretenimento","Outros"};

        // Load all accounts to array
        int i = 0;
        for (Account accont : accountListArray) {
            accountsName[i] = accont.getName();
            i++;
        }

        // Spinner config
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,R.layout.spinner,accountsName);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner);
        accountsSpinner.setAdapter(spinnerAdapter);

        ArrayAdapter<String> spinnerExpenseType = new ArrayAdapter<>(this,R.layout.spinner,expensesType);
        spinnerExpenseType.setDropDownViewResource(R.layout.spinner);
        expenseTypeSpinner.setAdapter(spinnerExpenseType);
    }

    @Override
    public void onClick(View v) {
        // Advertise alertview
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Load field values
        String amountValue = transactionValue.getText().toString();
        String description = descriptionTransaction.getText().toString();

        // Check type of transaction
        String accountName = accountsSpinner.getSelectedItem().toString();
        String transactionType = expenseTypeSpinner.getSelectedItem().toString();
        Boolean isCreditSelected = creditOption.isSelected();
        Boolean isDebitSelected = debitOption.isSelected();
        Boolean optionIsCredit = null;

        if (isCreditSelected || isDebitSelected) {
            if (isDebitSelected) {
                optionIsCredit = false;
            } else {
                optionIsCredit = true;
            }
        }

        switch (v.getId()) {
            case R.id.credit_type:
                v.setBackgroundColor(Color.BLUE);
                ((Button)v).setTextColor(Color.parseColor("#FFFFFF"));
                v.setSelected(true);
                debitOption.setBackgroundColor(Color.LTGRAY);
                debitOption.setSelected(false);
                debitOption.setTextColor(Color.parseColor("#000000"));

                expenseType.setVisibility(View.GONE);

                break;
            case R.id.debit_type:
                v.setBackgroundColor(Color.BLUE);
                ((Button)v).setTextColor(Color.parseColor("#FFFFFF"));
                v.setSelected(true);
                creditOption.setBackgroundColor(Color.LTGRAY);
                creditOption.setSelected(false);
                creditOption.setTextColor(Color.parseColor("#000000"));

                expenseType.setVisibility(View.VISIBLE);

                break;

            case R.id.cancelButton:
                // Behavior if the user taps to cancel
                if (!amountValue.matches("") || !description.matches("")) {

                    String title = this.getString(R.string.title_alertView);
                    String message = this.getString(R.string.title_alertView_cancel_message);
                    String okbuttonText = this.getString(R.string.ok_button_text)   ;
                    String cancelButtonText = this.getString(R.string.cancel_button_text);

                    alertDialog.setTitle(title);
                    alertDialog.setMessage(message);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, okbuttonText,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, cancelButtonText,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    alertDialog.show();
                } else {
                    finish();
                }

                break;

            case R.id.saveButton:
                // Behavior if the user taps to save
                if (amountValue.matches("") || description.matches("") || optionIsCredit == null) {

                    String errorTitle = this.getString(R.string.error_save_title);
                    String messageError = this.getString(R.string.error_save_transaction);

                    alertDialog.setTitle(errorTitle);
                    alertDialog.setMessage(messageError);
                    alertDialog.show();
                } else {

                    Transaction transaction = new Transaction();
                    transaction.setTransactionDate(getCurrentDate());
                    transaction.setAccountName(accountName);
                    transaction.setValue(amountValue);
                    transaction.setTransactionDescription(description);
                    transaction.setCredit(optionIsCredit);

                    if (optionIsCredit) {
                        transaction.setTransactionType("-");
                    } else {
                        transaction.setTransactionType(transactionType);
                    }

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MainActivity.EXTRA_TRANSACTION, transaction);
                    resultIntent.setAction(MainActivity.EXTRA_TRANSACTION);
                    setResult(RESULT_OK, resultIntent);

                    finish();
                    break;
                }

                break;
        }
    }

    public String getCurrentDate() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return dateFormat.format(currentDate);
    }
}