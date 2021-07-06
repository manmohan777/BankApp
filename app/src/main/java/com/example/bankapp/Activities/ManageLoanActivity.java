package com.example.bankapp.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapp.Adaptors.LoanAdapter;
import com.example.bankapp.Database.DAO.InsuranceDataDAO;
import com.example.bankapp.Database.Room.LoanData;
import com.example.bankapp.Database.ViewModel.LoanDataViewModel;
import com.example.bankapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ManageLoanActivity extends BaseActivity implements LoanAdapter.UpdateListener{

    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private ImageView logout;
    LoanDataViewModel loanDataViewModel;
    private MaterialToolbar toolbar;
    private static final String TAG = "ManageLoanActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " );
        setContentView(R.layout.activity_manage_loan);
        initViews();
        setSupportActionBar(toolbar);
        initBottomNavMenu(bottomNavigationView, R.id.managerLoan);
        initLogout(logout);

        loanDataViewModel = LoanDataViewModel.of(this);

        LoanAdapter lAdapter = new LoanAdapter(this);

        if(isAdmin()){
            getLoanDAO().findPending().observe(this, loanData -> {
                lAdapter.updateDataSet(loanData);
            });
        }else{
            getLoanDAO().findByAgentId(getLoggedUserInfo().getId()).observe(this, loanData -> {
                lAdapter.updateDataSet(loanData);
            });
        }

        recyclerView.setAdapter(lAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        logout = findViewById(R.id.logout);
    }

    @Override
    public void update(LoanData item) {
        loanDataViewModel.update(this, item,  (result, error, successful) -> {
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
        loanDataViewModel.approve(this, id, value,  (returnCode, error, successful) -> {
            if(successful){
                switch (returnCode){
                    case InsuranceDataDAO.RESPONSE_CODE_SUCCESS:
                        showToast("Done");
                        break;
                    case InsuranceDataDAO.RESPONSE_CODE_INSUFFICIENT_BSR:
                        showToast("insufficient BSR");
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
                error.printStackTrace();
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void inquiryApplication(LoanData insuranceData) {
        throw new UnsupportedOperationException("Cannot invoke inquiryApplication() from ManageLoanActivity");
    }
}