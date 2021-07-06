package com.example.bankapp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.bankapp.Database.Room.User;
import com.example.bankapp.Database.SharedPreferences.AdminPreferences;
import com.example.bankapp.Database.ViewModel.UserViewModel;
import com.example.bankapp.R;
import com.example.bankapp.model.Users;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class AgentHome extends BaseActivity {

    private MaterialToolbar toolbar;
    private ImageView logout;
    private TextView tvBsrBal, tvAccount, tvBal;
    private BottomNavigationView bottomNavigationView;
    private Spinner currencySpinner;
    UserViewModel userViewModel;

    private Button send;
    private EditText receiverAcc, sendingAmount;
    private int bal;

    //vars
   private String name,email;

   private Long accountNumber;

   DatabaseReference databaseReference;
    private static final String TAG = "AgentHome";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " );
        setContentView(R.layout.activity_agent);
        initViews();
        setSupportActionBar(toolbar);
        initBottomNavMenu(bottomNavigationView, R.id.agentHome);
        initLogout(logout);

        userViewModel = UserViewModel.of(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Agents");

       // User.Info info = getLoggedUserInfo();

        getIntentData();


         userViewModel.userByEmail(this, email, (user, error, successful) -> {

            String[] currency = getResources().getStringArray(R.array.currency);

            ArrayAdapter adapter = new ArrayAdapter(AgentHome.this, android.R.layout.simple_spinner_item, currency);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            currencySpinner.setAdapter(adapter);

            currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getId() == R.id.currencySpinner) {
                        double BSRRate = AdminPreferences.getInstance().getBSRRate();
                        String value = parent.getItemAtPosition(position).toString();
                        switch (value) {
                            case "INR":
                              tvBal.setText(String.format("Balance : ₹%s", (bal * BSRRate * 75.11)));
                                break;
                            case "JPY":
                              tvBal.setText(String.format("Balance : ¥%s", (bal * BSRRate * 108.15)));
                                break;
                            case "GBP":
                             tvBal.setText(String.format("Balance : £%s", (bal * BSRRate * 0.72)));
                                break;
                            case "AED":
                            tvBal.setText(String.format("Balance : د.إ%s", (bal * BSRRate * 3.67)));
                                break;
                            case "AUD":
                                tvBal.setText(String.format("Balance : A$%s", (bal * BSRRate * 1.29)));
                                break;
                            default:
                                tvBal.setText(String.format("Balance : $%s", (bal * BSRRate)));
                                break;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
              tvBal.setText(String.format("Balance : $%s", (bal * 100)));
                }
            });

            toolbar.setTitle("Welcome, " + name);
            tvAccount.setText(String.format("Account Number : %s", accountNumber));
            tvBsrBal.setText(String.format("BSR Balance : %s", bal));

            send.setOnClickListener(v -> {
                String amt = sendingAmount.getText().toString();
                String acc = receiverAcc.getText().toString();
                if (amt.isEmpty()) {
                    showToast("Please enter the amount to be sent");
                    return;
                }
                if (user.getBsrBal() < Double.parseDouble(amt)) {
                    showToast("Insufficient Balance");
                    return;
                }
                if (acc.isEmpty()) {
                    showToast("Please enter receiver's account number");
                    return;
                }
                if (acc.equals(user.getAccountNo())) {
                    showToast("Invalid receiver");
                    return;
                }
                userViewModel.sendMoney(AgentHome.this, user, acc, amt, (result, error1, successful1) -> {
                    if(successful1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(AgentHome.this)
                                .setTitle("Alert")
                                .setMessage("Money Sent")
                                .setPositiveButton("Okay", (dialogInterface, i) -> {
                                    receiverAcc.setText("");
                                    sendingAmount.setText("");
                            //        bal = result.getBsrBal();
                                    tvBsrBal.setText(String.format("BSR Balance : %s", result.getBsrBal()));
                                });
                        builder.create().show();
                    }else {
                        showToast(error1.getMessage());
                    }
                });

            });
        });

    }

    private void getIntentData() {

       name = getIntent().getStringExtra("name");
       email = getIntent().getStringExtra("email");
       //bal=getIntent().getIntExtra("balance",0);
       accountNumber=getIntent().getLongExtra("account_number",0);

       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
               for (DataSnapshot shot : snapshot.getChildren()) {
                   Users users = shot.getValue(Users.class);
                   bal = (int) users.getBsrBal();
               }
           }

           @Override
           public void onCancelled(@NonNull @NotNull DatabaseError error) {

           }
       });

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        logout = findViewById(R.id.logout);
        tvAccount = findViewById(R.id.tvAccount);
        tvBsrBal = findViewById(R.id.tvBsrBal);
        tvBal = findViewById(R.id.tvBal);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        currencySpinner = findViewById(R.id.currencySpinner);

        send = findViewById(R.id.send);
        sendingAmount = findViewById(R.id.sendingAmount);
        receiverAcc = findViewById(R.id.receiverAcc);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please Logout", Toast.LENGTH_SHORT).show();
    }
}