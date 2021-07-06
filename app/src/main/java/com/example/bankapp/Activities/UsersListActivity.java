package com.example.bankapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.bankapp.Adaptors.AgentsListAdapter;
import com.example.bankapp.R;
import com.example.bankapp.model.AgentListData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity {

    private static final String TAG ="UsersListActivity";
    //wed
    private ShimmerRecyclerView agentListRV;


    //vars

    List<AgentListData> agentListDataList;


    AgentListData agentListData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
        setContentView(R.layout.activity_users_list);

        initComponents();



        getAgentList();
    }

    private void getAgentList() {

        Query query = FirebaseDatabase.getInstance().getReference("Users").child("user1").child("Agents");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: "+dataSnapshot.exists()+"\n"+dataSnapshot.getChildren()+"\n"+dataSnapshot.getKey()+"\n"+query);
                if(dataSnapshot.exists())
                {
                    agentListDataList=new ArrayList<>();
                    for(DataSnapshot i : dataSnapshot.getChildren())
                    {

                        agentListData = i.getValue(AgentListData.class);
                        Log.e(TAG, "data: "+agentListData);
                        agentListDataList.add(agentListData);
                    }
                    AgentsListAdapter adapter = new AgentsListAdapter(UsersListActivity.this,agentListDataList);
                    agentListRV.setAdapter(adapter);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UsersListActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initComponents() {

        agentListRV=findViewById(R.id.agentListRV);
        agentListRV.setHasFixedSize(true);
        agentListRV.setLayoutManager(new LinearLayoutManager(UsersListActivity.this,LinearLayoutManager.VERTICAL,false));

    }
}