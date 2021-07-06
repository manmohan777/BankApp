package com.example.bankapp.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapp.Adaptors.InsuranceAdapter;
import com.example.bankapp.Database.DAO.InsuranceDataDAO;
import com.example.bankapp.Database.Room.InsuranceData;
import com.example.bankapp.Database.ViewModel.InsuranceDataViewModel;
import com.example.bankapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ManageInsuranceActivity extends BaseActivity implements InsuranceAdapter.UpdateListener{

    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private ImageView logout;
    private InsuranceDataViewModel insuranceDataViewModel;
    private MaterialToolbar toolbar;
    private static final String TAG = "ManageInsuranceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " );
        setContentView(R.layout.activity_manage_insurance);
        initViews();
        setSupportActionBar(toolbar);
        initBottomNavMenu(bottomNavigationView, R.id.managerInsurance);
        initLogout(logout);
        insuranceDataViewModel = InsuranceDataViewModel.of(this);
        InsuranceAdapter iAdapter = new InsuranceAdapter(this);
        recyclerView.setAdapter(iAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(isAdmin()){
            getInsuranceDAO().findPending().observe(this, insuranceData -> {
                iAdapter.updateDataSet(insuranceData);
            });
        }else{
            getInsuranceDAO().findByAgentId(getLoggedUserInfo().getId()).observe(this, insuranceData -> {
                iAdapter.updateDataSet(insuranceData);
            });
        }

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        logout = findViewById(R.id.logout);
    }

    @Override
    public void update(InsuranceData item) {
        insuranceDataViewModel.update(this, item,  (result, error, successful) -> {
            if(successful){
                showToast("Done");
            }else {
                showToast("Error");
                error.printStackTrace();
            }
        });
    }

    @Override
    public void approve(String id, int value) {
        insuranceDataViewModel.approve(this, id, value,  (returnCode, error, successful) -> {
            if(successful){
                switch (returnCode){
                    case InsuranceDataDAO.RESPONSE_CODE_SUCCESS:
                        showToast("Done");
                        break;
                    case InsuranceDataDAO.RESPONSE_CODE_INSUFFICIENT_BSR:
                        showToast("Insufficient BSR");
                        break;
                    case InsuranceDataDAO.RESPONSE_CODE_NOT_FOUND:
                        showToast("Not found");
                        break;
                    case InsuranceDataDAO.RESPONSE_CODE_INVALID_STATE:
                        showToast("Invalid state");
                        break;
                    default:
                        showToast("Error");
                        break;
                }
            }else {
                showToast(error.getMessage());
                error.printStackTrace();
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void inquiryApplication(InsuranceData insuranceData) {
        throw new UnsupportedOperationException("Cannot invoke inquiryApplication() from ManageInsuranceActivity");
    }
}