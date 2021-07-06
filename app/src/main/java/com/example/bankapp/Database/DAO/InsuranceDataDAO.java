package com.example.bankapp.Database.DAO;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bankapp.Database.AppDatabase;
import com.example.bankapp.Database.Room.InsuranceData;
import com.example.bankapp.Database.Room.LoanData;
import com.example.bankapp.Database.Room.Transaction;
import com.example.bankapp.Database.Room.User;

import java.util.List;

@Dao
public abstract class InsuranceDataDAO {

    public static final int RESPONSE_CODE_SUCCESS = 0;
    public static final int RESPONSE_CODE_UNKNOWN_ERROR = 1;
    public static final int RESPONSE_CODE_INSUFFICIENT_BSR = 2;
    public static final int RESPONSE_CODE_NOT_FOUND = 3;
    public static final int RESPONSE_CODE_INVALID_STATE = 4;

    private final UserDAO userDAO;
    private final TransactionDAO transactionDAO;

    public InsuranceDataDAO(AppDatabase database) {
        this.userDAO = database.getUserDAO();
        this.transactionDAO = database.getTransactionDAO();
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void createOrUpdate(InsuranceData data);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(InsuranceData data);

    @Query("select * from insurance_data where id=:id")
    public abstract LiveData<InsuranceData> findById(String id);

    @Query("select * from insurance_data where id=:id")
    public abstract InsuranceData _findById(String id);

    @Query("select * from insurance_data where userId=:id")
    public abstract LiveData<List<InsuranceData>> findByUserId(String id);

    @Query("select * from insurance_data where agentId=:id")
    public abstract LiveData<List<InsuranceData>> findByAgentId(String id);

    @Query("select * from insurance_data where state in (:state)")
    public abstract LiveData<List<InsuranceData>> findByState(Integer...state);

    public LiveData<List<InsuranceData>> findPending() {
        return findByState(InsuranceData.STATE_PENDING, InsuranceData.STATE_APPROVED, InsuranceData.STATE_REJECTED);
    }

    @Query("UPDATE insurance_data SET state= :state")
    public abstract int updateAllState(int state);

    public int reset() {
        return updateAllState(InsuranceData.STATE_APPLIED);
    }

    @androidx.room.Transaction
    public int approve(String id, int commissionRate) {
        InsuranceData item = _findById(id);
        if(item!=null){
            if(item.getState()==InsuranceData.STATE_PENDING){

                    User user =  userDAO.findById(item.getUserId());
                    User agent =  userDAO.findById(item.getAgentId());
                    double applicationAmt = item.getInsuranceAmt();
                    double userNewBsrBal = user.getBsrBal() - applicationAmt;
                    double commissionAmt = (applicationAmt*commissionRate)/100;
                    double businessAmt = applicationAmt - commissionAmt;
                    Transaction commissionTransaction = Transaction.of(user.getId(), agent.getId(), commissionAmt, true);
                    // A business transaction should be created Here to compensate application fees

                    if(userNewBsrBal<2000){
                        Log.e(InsuranceDataDAO.class.getSimpleName(), "Rolling back transaction with an exception"+commissionRate);
                        throw new RuntimeException("Insufficient BSR" + userNewBsrBal);
                    }

                    user.setBsrBal(userNewBsrBal);
                    agent.setBsrBal(agent.getBsrBal()+commissionAmt);

                    item.setState(LoanData.STATE_APPROVED);
                    update(item);
                    userDAO.update(agent);
                    userDAO.update(user);
                    transactionDAO.createOrUpdate(commissionTransaction);


                if(item.getState()== InsuranceData.STATE_APPROVED){
                    return RESPONSE_CODE_SUCCESS;
                }else {
                    return RESPONSE_CODE_INSUFFICIENT_BSR;
                }

            }else {
                return RESPONSE_CODE_INVALID_STATE;
            }
        }else {
            return RESPONSE_CODE_NOT_FOUND;
        }
        // return RESPONSE_CODE_UNKNOWN_ERROR;
    }
}
