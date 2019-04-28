package br.edu.ifspsaocarlos.gerenciadorfinanceiro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.gerenciadorfinanceiro.R;
import br.edu.ifspsaocarlos.gerenciadorfinanceiro.model.Transaction;

public class AccountDetailListAdapter extends ArrayAdapter<Transaction> {
    private LayoutInflater layoutInflater;

    public AccountDetailListAdapter(Context context, List<Transaction> accountDetailList) {
        super(context, R.layout.layout_account_adapter, accountDetailList);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_account_detail, null);

            holder = new Holder();
            holder.transactionDateTextView = convertView.findViewById(R.id.dateTransaction);
            holder.valueTransactionTextView = convertView.findViewById(R.id.transactionValue);
            holder.transactionType = convertView.findViewById(R.id.transactionType);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Transaction transaction = getItem(position);

        holder.transactionDateTextView.setText(transaction.getTransactionDate());
        holder.transactionType.setText(transaction.getTransactionType());
        holder.valueTransactionTextView.setText("R$" + transaction.getValue());

        if (transaction.getCredit()) {
            holder.valueTransactionTextView.setTextColor(Color.GREEN);
        } else {
            holder.valueTransactionTextView.setTextColor(Color.RED);
        }

        return convertView;
    }

    private class Holder {
        public TextView transactionDateTextView;
        public TextView valueTransactionTextView;
        public TextView transactionType;
    }
}
