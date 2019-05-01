package br.edu.ifspsaocarlos.gerenciadorfinanceiro.view;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import br.edu.ifspsaocarlos.gerenciadorfinanceiro.R;

import br.edu.ifspsaocarlos.gerenciadorfinanceiro.model.Account;
import br.edu.ifspsaocarlos.gerenciadorfinanceiro.adapter.AccountListAdapter;

public class MainActivity extends AppCompatActivity {

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
    }
}