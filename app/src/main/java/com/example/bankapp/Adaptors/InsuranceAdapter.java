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

import com.example.bankapp.Database.Room.InsuranceData;
import com.example.bankapp.R;

import java.util.ArrayList;
import java.util.List;

public class InsuranceAdapter extends RecyclerView.Adapter<InsuranceAdapter.ViewHolder> {

    private final UpdateListener context;
    private List<InsuranceData> insuranceDataList = new ArrayList<>();


    public InsuranceAdapter(UpdateListener context) {
        this.context = context;
    }

    public void updateDataSet(List<InsuranceData> insuranceDataList) {
        this.insuranceDataList = insuranceDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InsuranceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.insurance_item, parent, false);
        return new InsuranceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InsuranceAdapter.ViewHolder holder, final int position) {
        if (insuranceDataList == null) {
            holder.recyclerView.setVisibility(View.GONE);
            holder.noInsuranceHistory.setVisibility(View.VISIBLE);
            return;
        }
        InsuranceData insuranceData = insuranceDataList.get(position);
        if (insuranceData != null) {
            //holder.recyclerView.setVisibility(View.VISIBLE);
            //holder.noInsuranceHistory.setVisibility(View.GONE);
            holder.appAmount.setText(String.valueOf(insuranceData.getInsuranceAmt()));
            holder.appDate.setText(String.valueOf(insuranceData.getApplDate()));
            holder.insuranceId.setText(insuranceData.getId());
            holder.insuranceType.setText(insuranceData.getInsuranceType());
            holder.nominee.setText(insuranceData.getNominee());
            holder.userStateTxv.setText(InsuranceData.STATE_ARRAY[insuranceData.getState()]);

            if(context.isAdmin()){
                holder.ll_admin_controls.setVisibility(View.VISIBLE);
                holder.adminStateTxv.setText(InsuranceData.STATE_ARRAY[insuranceData.getState()]);
                switch (insuranceData.getState()){
                    case InsuranceData.STATE_PENDING:
                        holder.approveBtn.setEnabled(true);
                        holder.rejectBtn.setEnabled(true);

                        holder.approveBtn.setOnClickListener(view -> {

                            final Dialog d = new Dialog(context.getContext());
                            d.setContentView(R.layout.dialog);
                            Button b1 = d.findViewById(R.id.button1);
                            Button b2 = d.findViewById(R.id.button2);
                            final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                            np.setMaxValue(60);
                            np.setMinValue(0);
                            np.setWrapSelectorWheel(false);
                            b1.setOnClickListener(v -> {
                                context.approve(insuranceData.getId(), np.getValue());
                                d.dismiss();
                            });
                            b2.setOnClickListener(v -> d.dismiss());
                            d.show();

                        });
                        holder.rejectBtn.setOnClickListener(view -> {
                            cancelApplication(insuranceData);
                        });
                        break;
                    case InsuranceData.STATE_APPLIED:
                    case InsuranceData.STATE_APPROVED:
                    case InsuranceData.STATE_REJECTED:
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
                holder.agentStateTxv.setText(InsuranceData.STATE_ARRAY[insuranceData.getState()]);
                switch (insuranceData.getState()){
                    case InsuranceData.STATE_APPLIED:
                        holder.acceptBtn.setEnabled(true);
                        holder.cancelBtn.setEnabled(true);

                        holder.cancelBtn.setOnClickListener(view -> {
                            cancelApplication(insuranceData);
                        });
                        holder.acceptBtn.setOnClickListener(view -> {
                            insuranceData.setState(InsuranceData.STATE_PENDING);
                            updateItem(insuranceData);
                        });
                        break;
                    case InsuranceData.STATE_PENDING:
                    case InsuranceData.STATE_APPROVED:
                    case InsuranceData.STATE_REJECTED:
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
                holder.agentStateTxv.setText(InsuranceData.STATE_ARRAY[insuranceData.getState()]);
                holder.inquiryBtn.setEnabled(true);

                holder.inquiryBtn.setOnClickListener(view -> {
                    context.inquiryApplication(insuranceData);
                });
            }else {
                holder.ll_user_controls.setVisibility(View.GONE);
            }
        }
    }
    
    private void cancelApplication(InsuranceData item){
        item.setState(InsuranceData.STATE_REJECTED);
        updateItem(item);
    }

    private void updateItem(InsuranceData item){
        context.update(item);
        context.showToast("done");
    }

    @Override
    public int getItemCount() {
        return insuranceDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView appDate;
        private final TextView insuranceType;
        private final TextView appAmount;
        private final TextView insuranceId;
        private final TextView nominee;
        private final TextView noInsuranceHistory;
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
            noInsuranceHistory = itemView.findViewById(R.id.noInsuranceHistory);
            appDate = itemView.findViewById(R.id.appDate);
            insuranceType = itemView.findViewById(R.id.insuranceType);
            appAmount = itemView.findViewById(R.id.appAmount);
            insuranceId = itemView.findViewById(R.id.insuranceId);
            nominee = itemView.findViewById(R.id.nominee);

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
            inquiryBtn = itemView.findViewById(R.id.inquiryBtn);
        }
    }

    public interface UpdateListener{

        boolean isAdmin();

        void update(InsuranceData item);
        
        void showToast(String msg);

        boolean isAgent();

        void approve(String id, int value);

        Context getContext();

        void inquiryApplication(InsuranceData insuranceData);
    }
}