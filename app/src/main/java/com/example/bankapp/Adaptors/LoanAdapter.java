package com.example.bankapp.Adaptors;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapp.Database.Room.LoanData;
import com.example.bankapp.R;

import java.util.ArrayList;
import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.ViewHolder> {

    private final UpdateListener context;
    private List<LoanData> loanDataList = new ArrayList<>();

    public LoanAdapter(UpdateListener context) {
        this.context = context;
    }

    public void updateDataSet(List<LoanData> loanDataList) {
        this.loanDataList = loanDataList;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public LoanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_item, parent, false);
        return new LoanAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanAdapter.ViewHolder holder, final int position) {
        if (loanDataList == null) {
            holder.recyclerView.setVisibility(View.GONE);
            holder.noLoanHistory.setVisibility(View.VISIBLE);
            return;
        }
        LoanData loanData = loanDataList.get(position);
        if (loanData != null) {
            holder.appAmount.setText(String.valueOf(loanData.getLoanAmt()));
            holder.appDate.setText(String.valueOf(loanData.getApplDate()));
            holder.loanId.setText(loanData.getId());
            holder.loanType.setText(loanData.getLoanType());
            holder.period.setText(String.valueOf(loanData.getPeriod()));
            holder.userStateTxv.setText(LoanData.STATE_ARRAY[loanData.getState()]);

            if(context.isAdmin()){
                holder.ll_admin_controls.setVisibility(View.VISIBLE);
                holder.adminStateTxv.setText(LoanData.STATE_ARRAY[loanData.getState()]);
                switch (loanData.getState()){
                    case LoanData.STATE_PENDING:
                        holder.approveBtn.setEnabled(true);
                        holder.rejectBtn.setEnabled(true);

                        holder.approveBtn.setOnClickListener(view -> {

                            final Dialog d = new Dialog(context.getContext());
                            d.setContentView(R.layout.dialog);
                            Button b1 = (Button) d.findViewById(R.id.button1);
                            Button b2 = (Button) d.findViewById(R.id.button2);
                            final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                            np.setMaxValue(60);
                            np.setMinValue(0);
                            np.setWrapSelectorWheel(false);
                            b1.setOnClickListener(v -> {
                                context.approve(loanData.getId(), np.getValue());
                                d.dismiss();
                            });
                            b2.setOnClickListener(v -> d.dismiss());
                            d.show();

                        });
                        holder.rejectBtn.setOnClickListener(view -> {
                            cancelApplication(loanData);
                        });
                        break;
                    case LoanData.STATE_APPLIED:
                    case LoanData.STATE_APPROVED:
                    case LoanData.STATE_REJECTED:
                    default:
                        holder.approveBtn.setEnabled(false);
                        holder.rejectBtn.setEnabled(false);
                        break;
                }
            }else {
                holder.ll_admin_controls.setVisibility(View.GONE);
            }

            if(context.isAgent()){
                holder.ll_agent_controls.setVisibility(View.VISIBLE);
                holder.agentStateTxv.setText(LoanData.STATE_ARRAY[loanData.getState()]);
                switch (loanData.getState()){
                    case LoanData.STATE_APPLIED:
                        holder.acceptBtn.setEnabled(true);
                        holder.cancelBtn.setEnabled(true);

                        holder.cancelBtn.setOnClickListener(view -> {
                            cancelApplication(loanData);
                        });
                        holder.acceptBtn.setOnClickListener(view -> {
                            loanData.setState(LoanData.STATE_PENDING);
                            updateItem(loanData);
                        });
                        break;
                    case LoanData.STATE_PENDING:
                    case LoanData.STATE_APPROVED:
                    case LoanData.STATE_REJECTED:
                    default:
                        holder.acceptBtn.setEnabled(false);
                        holder.cancelBtn.setEnabled(false);
                        break;
                }
            }else {
                holder.ll_agent_controls.setVisibility(View.GONE);
            }

            if(!context.isAgent() && !context.isAdmin()){
                holder.ll_user_controls.setVisibility(View.VISIBLE);
                holder.agentStateTxv.setText(LoanData.STATE_ARRAY[loanData.getState()]);
                holder.inquiryBtn.setEnabled(true);

                holder.inquiryBtn.setOnClickListener(view -> {
                    context.inquiryApplication(loanData);
                });
            }else {
                holder.ll_user_controls.setVisibility(View.GONE);
            }
        }
    }

    private void cancelApplication(LoanData item){
        item.setState(LoanData.STATE_REJECTED);
        updateItem(item);
    }

    private void updateItem(LoanData item){
        context.update(item);
    }

    @Override
    public int getItemCount() {
        return loanDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView appDate;
        private final TextView loanType;
        private final TextView appAmount;
        private final TextView loanId;
        private final TextView period;
        private final TextView noLoanHistory;
        private final RecyclerView recyclerView;

        private final LinearLayout ll_agent_controls;
        private final TextView agentStateTxv;
        private final Button acceptBtn;
        private final Button cancelBtn;

        private final LinearLayout ll_user_controls;
        private final TextView userStateTxv;
        private final Button inquiryBtn;

        private final LinearLayout ll_admin_controls;
        private final TextView adminStateTxv;
        private final Button approveBtn;
        private final Button rejectBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.recyclerView);
            noLoanHistory = itemView.findViewById(R.id.noLoanHistory);
            appDate = itemView.findViewById(R.id.appDate);
            loanType = itemView.findViewById(R.id.loanType);
            appAmount = itemView.findViewById(R.id.appAmount);
            loanId = itemView.findViewById(R.id.loanId);
            period = itemView.findViewById(R.id.period);

            ll_agent_controls = itemView.findViewById(R.id.ll_agent_controls);
            agentStateTxv = itemView.findViewById(R.id.agentStateTxv);
            acceptBtn = itemView.findViewById(R.id.acceptBtn);
            cancelBtn = itemView.findViewById(R.id.cancelBtn);
            ll_admin_controls = itemView.findViewById(R.id.ll_admin_controls);
            adminStateTxv = itemView.findViewById(R.id.adminStateTxv);
            approveBtn = itemView.findViewById(R.id.approveBtn);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);

            ll_user_controls = itemView.findViewById(R.id.ll_user_controls);
            userStateTxv = itemView.findViewById(R.id.userStateTxv);
            inquiryBtn = itemView.findViewById(R.id.inquiryBtn);      }
    }

    public interface UpdateListener{

        boolean isAdmin();

        void update(LoanData item);

        void showToast(String msg);

        boolean isAgent();

        void approve(String id, int value);

        Context getContext();

        void inquiryApplication(LoanData insuranceData);

    }
}
