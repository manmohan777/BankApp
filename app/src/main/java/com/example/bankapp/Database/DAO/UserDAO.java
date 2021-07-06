package com.example.bankapp.Database.DAO;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bankapp.Database.Room.User;

import java.util.List;

@Dao
public abstract class UserDAO  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void createOrUpdate(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(User user);

    @Query("select * from users where email=:email")
    public abstract User findByEmail(String email);

    @Query("select * from users where id=:id")
    public abstract User findById(String id);

    @Query("select * from users where accountNo=:account")
    public abstract User findByAccount(String account);

    @Query("select * from users where id like 'A%'")
    public abstract LiveData<List<User>> findAllAgent();


    public void mockData() {

        insertUser("001","user1@user.mail", "001", "password", "2025550111", "User 1");
        insertUser("002","user2@user.mail", "002", "password", "2025550112", "User 2");
        insertUser("003","user3@user.mail", "003", "password", "2025550113", "User 3");
        insertUser("004","user4@user.mail", "004", "password", "2025550114", "User 4");
        insertUser("005","user5@user.mail", "005", "password", "2025550115", "User 5");

        insertUser("A01","agent1@agent.mail", "1001", "password", "2025550121", "Agent 1");
        insertUser("A02","agent2@agent.mail", "1002", "password", "2025550122", "Agent 2");
        insertUser("A03","agent3@agent.mail", "1003", "password", "2025550123", "Agent 3");
        insertUser("A04","agent4@agent.mail", "1004", "password", "2025550124", "Agent 4");
        insertUser("A05","agent5@agent.mail", "1005", "password", "2025550125", "Agent 5");
    }

    private void insertUser(String id, String email, String account, String password, String phone, String name){
        User user = new User();
        user.setEmail(email);
        user.setId(id);
        user.setAccountNo(account);
        user.setPassword(password);
        user.setPhoneNumber(phone);
        user.setName(name);
        user.setBsrBal(10000);
        createOrUpdate(user);
    }
}
