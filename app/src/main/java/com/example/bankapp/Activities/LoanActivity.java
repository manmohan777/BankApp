package com.example.bankapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapp.Adaptors.LoanAdapter;
import com.example.bankapp.Database.Room.LoanData;
import com.example.bankapp.Database.Room.User;
import com.example.bankapp.Database.ViewModel.LoanDataViewModel;
import com.example.bankapp.Database.ViewModel.UserViewModel;
import com.example.bankapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class LoanActivity extends BaseActivity implements LoanAdapter.UpdateListener{

    private Spinner loanTypeSpinner;
    private Button btnApply;
    private String value;
    private EditText etLoanAmt, etPeriod;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private ImageView logout;

    private User selectedAgent;
    private Spinner agentSpinner;
    private List<String> agentNames;
    UserViewModel userViewModel;
    LoanDataViewModel loanDataViewModel;
    private MaterialToolbar toolbar;
    private static final String TAG = "LoanActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " );
        setContentView(R.layout.activity_loan);
        initViews();
        setSupportActionBar(toolbar);
        initBottomNavMenu(bottomNavigationView, R.id.loan);
        initLogout(logout);

        userViewModel = UserViewModel.of(this);
        loanDataViewModel = LoanDataViewModel.of(this);

        getUserDAO().findAllAgent().observe(this, agentList -> {
            if(agentList==null || agentList.isEmpty()){
                showToast("No agent registered");
                onBackPressed();
            }
            agentNames = new ArrayList<>();
            for (User u: agentList){
                agentNames.add(u.getName());
            }

            String[] loanType = getResources().getStringArray(R.array.loan_type);
            ArrayAdapter adapter = new ArrayAdapter(LoanActivity.this, android.R.layout.simple_spinner_item, loanType);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            ArrayAdapter agentAdapter = new ArrayAdapter(LoanActivity.this, android.R.layout.simple_spinner_item, agentNames);
            agentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            loanTypeSpinner.setAdapter(adapter);
            agentSpinner.setAdapter(agentAdapter);

            agentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getId() == R.id.agentSpinner) {
                        selectedAgent = agentList.get(position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedAgent = null;
                }
            });

            loanTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getId() == R.id.loanTypeSpinner) {
                        value = parent.getItemAtPosition(position).toString();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    value = null;
                }
            });

            btnApply.setOnClickListener(v -> {
                String loanAmt, period;
                loanAmt = etLoanAmt.getText().toString();
                period = etPeriod.getText().toString();
                if (value.isEmpty()) {
                    showToast("Please Select Loan Type");
                    return;
                }
                if (loanAmt.isEmpty()) {
                    showToast("Please Enter Loan Amount");
                    return;
                }
                if (period.isEmpty()) {
                    showToast("Please Enter the Period");
                    return;
                }
                if (selectedAgent==null) {
                    showToast("Please Select An Agent");
                    return;
                }

                loanDataViewModel.applyLoan(this,
                        selectedAgent.getId(), getLoggedUserInfo().getEmail(),
                        loanAmt, value, period,(result, error, successful) -> {
                    etLoanAmt.setText("");
                    etPeriod.setText("");
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoanActivity.this)
                            .setTitle("Alert")
                            .setMessage("Loan Applied!!!")
                            .setPositiveButton("Okay", (dialogInterface, i) -> {
                            });
                    builder.create().show();

                });

            });

            LoanAdapter lAdapter = new LoanAdapter(LoanActivity.this);
            recyclerView.setAdapter(lAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(LoanActivity.this));

            loanDataViewModel.getLoanDataDAO().findByUserId(getLoggedUserInfo().getId()).observe(this, new Observer<List<LoanData>>() {
                @Override
                public void onChanged(List<LoanData> loanData) {
                    lAdapter.updateDataSet(loanData);
                }
            });
        });
        
    }

    private void initViews() {
        loanTypeSpinner = findViewById(R.id.loanTypeSpinner);
        agentSpinner = findViewById(R.id.agentSpinner);
        btnApply = findViewById(R.id.btnApply);
        etLoanAmt = findViewById(R.id.etLoanAmt);
        etPeriod = findViewById(R.id.etPeriod);
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        logout = findViewById(R.id.logout);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public void update(LoanData item) {
        throw new UnsupportedOperationException("Cannot invoke update() from InsuranceActivity");
    }

    @Override
    public void approve(String id, int value) {
        throw new UnsupportedOperationException("Cannot invoke approve() from InsuranceActivity");
    }

    @Override
    public void inquiryApplication(LoanData data) {
        loanDataViewModel.inquiryApplication(this, data, (chat, error, successful) -> {
            if(successful){
                Intent intent = new Intent(LoanActivity.this, MessageActivity.class);
                intent.putExtra("chatId", chat.getId());
                startActivity(intent);
            }else {
                showToast("Error");
                error.printStackTrace();
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }
}