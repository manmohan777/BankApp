package com.example.bankapp.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapp.Activities.BaseActivity;
import com.example.bankapp.Database.Room.Transaction;
import com.example.bankapp.Database.Room.User;
import com.example.bankapp.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final BaseActivity context;
    private final List<Transaction> transactions;


    public TransactionAdapter(BaseActivity context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        User.Info user = context.getLoggedUserInfo();
        /*
        if (transactions.get(position) == null) {
            holder.recyclerView.setVisibility(View.GONE);
            holder.noTransaction.setVisibility(View.VISIBLE);
        }//*/
        Transaction transaction = transactions.get(position);
        if (transaction != null) {
            // holder.recyclerView.setVisibility(View.VISIBLE);
            // holder.noTransaction.setVisibility(View.GONE);
            holder.transactionId.setText(transaction.getId());
            holder.sentAmount.setText(String.valueOf(transaction.getAmount()));
            holder.sentDate.setText(String.valueOf(transaction.getCreatedDate()));
            if (transaction.getReceiver().equals(user.getId())) {
                // holder.toAccNo.setText(transaction.getSender());
                holder.toAccNo.setText(transaction.getSenderName());
                holder.toAccName.setText(transaction.getSenderAccNo());
                holder.sentOrReceived.setText("Received");
            } else if (transaction.getReceiver().equals("ADMIN")){
                // holder.toAccNo.setText(transaction.getReceiver());
//                if (transaction.getReceiverName().equals("ADMIN")){
//                    holder.toAccNo.setText(transaction.getSenderName());
//                }else {


                holder.toAccNo.setText(transaction.getReceiverName());
                holder.toAccName.setText(transaction.getReceiverAccNo());
                holder.sentOrReceived.setText("Received");

                // }
            }else {
                holder.toAccNo.setText(transaction.getReceiverName());
                holder.toAccName.setText(transaction.getReceiverAccNo());
                holder.sentOrReceived.setText("Sent");
            }
            if (user.getId().equals("")) {
                if (transaction.getAmount() > 0) {
                    holder.toAccNo.setText(transaction.getSenderName());
                    holder.toAccName.setText(transaction.getSenderAccNo());
                    holder.sentOrReceived.setText("Sent");

                }else {
                    holder.toAccNo.setText(transaction.getReceiverName());
                    holder.toAccName.setText(transaction.getReceiverAccNo());
                    holder.sentOrReceived.setText("Deducted");
                }
            }
            if (transaction.getAmount() < 0){
                holder.sentOrReceived.setText("Deducted");
            }

        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView sentAmount;
        private final TextView toAccNo;
        private final TextView transactionId;
        private final TextView sentDate;
        private final TextView sentOrReceived;
        private final TextView toAccName;
        // private final RecyclerView recyclerView;
        //private final TextView noTransaction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // recyclerView = itemView.findViewById(R.id.recyclerView);
            // noTransaction = itemView.findViewById(R.id.noTransaction);
            sentAmount = itemView.findViewById(R.id.sentAmount);
            toAccNo = itemView.findViewById(R.id.toAccNo);
            transactionId = itemView.findViewById(R.id.transactionId);
            sentDate = itemView.findViewById(R.id.sentDate);
            sentOrReceived = itemView.findViewById(R.id.sentOrReceived);
            toAccName = itemView.findViewById(R.id.toAccName);
        }
    }
}