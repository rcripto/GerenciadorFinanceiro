package br.edu.ifspsaocarlos.gerenciadorfinanceiro.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import br.edu.ifspsaocarlos.gerenciadorfinanceiro.R;

public class TransactionActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
    }

    @Override
    public void onClick(View v) {

    }
}
