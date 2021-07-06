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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapp.Adaptors.InsuranceAdapter;
import com.example.bankapp.Database.Room.Chat;
import com.example.bankapp.Database.Room.InsuranceData;
import com.example.bankapp.Database.Room.User;
import com.example.bankapp.Database.ViewModel.CompletionListener;
import com.example.bankapp.Database.ViewModel.InsuranceDataViewModel;
import com.example.bankapp.Database.ViewModel.UserViewModel;
import com.example.bankapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class InsuranceActivity extends BaseActivity implements InsuranceAdapter.UpdateListener{

    private Spinner insuranceTypeSpinner;
    private Button btnApply;
    private String value;
    private EditText etInsuranceAmt, etNominee;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private ImageView logout;

    private User selectedAgent;
    private Spinner agentSpinner;
    private List<String> agentNames;
    private UserViewModel userViewModel;
    private InsuranceDataViewModel insuranceDataViewModel;
    private MaterialToolbar toolbar;
    private static final String TAG = "InsuranceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " );
        setContentView(R.layout.activity_insurance);
        initViews();
        setSupportActionBar(toolbar);
        initBottomNavMenu(bottomNavigationView, R.id.insurance);
        initLogout(logout);
        userViewModel = UserViewModel.of(this);
        insuranceDataViewModel = InsuranceDataViewModel.of(this);

        getUserDAO().findAllAgent().observe(this, agentList -> {
            if(agentList==null || agentList.isEmpty()){
                Toast.makeText(this, "No agent registered", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
            agentNames = new ArrayList<>();
            for (User u: agentList){
                agentNames.add(u.getName());
            }

            String[] insuranceType = getResources().getStringArray(R.array.insurance_type);
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, insuranceType);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            ArrayAdapter agentAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, agentNames);
            agentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            insuranceTypeSpinner.setAdapter(adapter);
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

            insuranceTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getId() == R.id.insuranceTypeSpinner) {
                        value = parent.getItemAtPosition(position).toString();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    value = null;
                }
            });

            btnApply.setOnClickListener(v -> {
                String insuranceAmt, nominee;
                insuranceAmt = etInsuranceAmt.getText().toString();
                nominee = etNominee.getText().toString();
                if (value.isEmpty()) {
                    Toast.makeText(this, "Please Select Insurance Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (insuranceAmt.isEmpty()) {
                    Toast.makeText(this, "Please Enter Insurance Amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (nominee.isEmpty()) {
                    Toast.makeText(this, "Please Enter Your Nominee", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedAgent==null) {
                    Toast.makeText(this, "Please Select An Agent", Toast.LENGTH_SHORT).show();
                    return;
                }

                insuranceDataViewModel.applyInsurance(this, selectedAgent.getId(), getLoggedUserInfo().getEmail(), insuranceAmt, value, nominee,(result, error, successful) -> {
                    if(successful){
                        etInsuranceAmt.setText("");
                        etNominee.setText("");
                        AlertDialog.Builder builder = new AlertDialog.Builder(InsuranceActivity.this)
                                .setTitle("Alert")
                                .setMessage("Insurance Applied!!!")
                                .setPositiveButton("Okay", (dialogInterface, i) -> {
                                });
                        builder.create().show();
                    }else {
                        showToast(error.getMessage());
                    }
                });

            });

            InsuranceAdapter iAdapter = new InsuranceAdapter(this);
            recyclerView.setAdapter(iAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            insuranceDataViewModel.getInsuranceDataDAO().findByUserId(getLoggedUserInfo().getId()).observe(this, insuranceData -> iAdapter.updateDataSet(insuranceData));
        });
    }

    private void initViews() {
        insuranceTypeSpinner = findViewById(R.id.insuranceTypeSpinner);
        agentSpinner = findViewById(R.id.agentSpinner);
        btnApply = findViewById(R.id.btnApply);
        etInsuranceAmt = findViewById(R.id.etInsuranceAmt);
        etNominee = findViewById(R.id.etNominee);
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        logout = findViewById(R.id.logout);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public void update(InsuranceData item) {
        throw new UnsupportedOperationException("Cannot invoke update() from InsuranceActivity");
    }

    @Override
    public void approve(String id, int value) {
        throw new UnsupportedOperationException("Cannot invoke approve() from InsuranceActivity");
    }

    @Override
    public void inquiryApplication(InsuranceData insuranceData) {
        insuranceDataViewModel.inquiryApplication(this, insuranceData, new CompletionListener<Chat>() {
            @Override
            public void onCompletion(Chat chat, Throwable error, boolean successful) {
                if(successful){
                    Intent intent = new Intent(InsuranceActivity.this, MessageActivity.class);
                    intent.putExtra("chatId", chat.getId());
                    startActivity(intent);
                }else {
                    showToast("Error");
                    error.printStackTrace();
                }
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }
}