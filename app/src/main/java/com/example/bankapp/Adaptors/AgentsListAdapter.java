package com.example.bankapp.Adaptors;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapp.R;
import com.example.bankapp.model.AgentListData;
import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.List;


public class AgentsListAdapter extends RecyclerView.Adapter<AgentsListAdapter.ViewHolder> {

    private Context mContaxt;
    private List<AgentListData> agentListDataList;



    public AgentsListAdapter(Context mContaxt, List<AgentListData> agentListDataList) {
        this.mContaxt = mContaxt;
        this.agentListDataList = agentListDataList;


    }

    @NonNull
    @Override
    public AgentsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContaxt);
        View view = inflater.inflate(R.layout.agent_show_adapter, null);

        return new AgentsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AgentsListAdapter.ViewHolder holder, int position) {

        final AgentListData agentListData = agentListDataList.get(position);

        holder.agentNameTV.setText(agentListData.getName());

        holder.agentAccountNumber.setText(agentListData.getAccount_no()+"");

        holder.agentBalance.setText("₹"+ agentListData.getBalance()+"");



        String colors [] = {"#ff4444","#00C851","#ffbb33","#33b5e5","#2BBBAD","#4285F4","#aa66cc","#3F729B"};
        int color = position % colors.length;
        int intColour = Color.parseColor(colors[color]);
        holder.agentNameLtr.setLetter(agentListData.getName());
        holder.agentNameLtr.setShapeColor(intColour);





    }

    @Override
    public int getItemCount() {
        return agentListDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView agentNameTV,agentAccountNumber,agentBalance;

        private MaterialLetterIcon agentNameLtr;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            agentNameTV=itemView.findViewById(R.id.agentNameTV);

            agentAccountNumber=itemView.findViewById(R.id.agentAccountNumber);

            agentBalance=itemView.findViewById(R.id.agentBalance);


            agentNameLtr=itemView.findViewById(R.id.userNameLtr);



        }

    }

}


