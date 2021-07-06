package com.example.bankapp.Database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bankapp.Database.AppDatabase;
import com.example.bankapp.Database.Room.LoanData;
import com.example.bankapp.Database.Room.Transaction;
import com.example.bankapp.Database.Room.User;

import java.io.PrintStream;
import java.util.List;

@Dao
public abstract class LoanDataDAO {

    public static final int RESPONSE_CODE_SUCCESS = 0;
    public static final int RESPONSE_CODE_UNKNOWN_ERROR = 1;
    public static final int RESPONSE_CODE_INSUFFICIENT_BSR = 2;
    public static final int RESPONSE_CODE_NOT_FOUND = 3;
    public static final int RESPONSE_CODE_INVALID_STATE = 4;


    private final UserDAO userDAO;
    private final TransactionDAO transactionDAO;

    public LoanDataDAO(AppDatabase database) {
        this.userDAO = database.getUserDAO();
        this.transactionDAO = database.getTransactionDAO();
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void createOrUpdate(LoanData data);

    @Query("select * from loan_data where id=:id")
    public abstract LiveData<LoanData> findById(String id);

    @Query("select * from loan_data where id=:id")
    public abstract LoanData _findById(String id);

    @Query("select * from loan_data where userId=:id")
    public abstract LiveData<List<LoanData>> findByUserId(String id);

    @Query("select * from loan_data where agentId=:id")
    public abstract LiveData<List<LoanData>> findByAgentId(String id);

    @Query("select * from loan_data where state in (:state)")
    public abstract LiveData<List<LoanData>> findByState(Integer...state);

    public LiveData<List<LoanData>> findPending() {
        return findByState(LoanData.STATE_PENDING, LoanData.STATE_APPROVED, LoanData.STATE_REJECTED);
    }

    @Query("UPDATE loan_data SET state= :state")
    public abstract int updateAllState(int state);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(LoanData data);

    @androidx.room.Transaction
    public int approve(String id, int commissionRate) {
        LoanData item = _findById(id);
        if(item!=null){
            if(item.getState()==LoanData.STATE_PENDING){

                User user =  userDAO.findById(item.getUserId());
                User agent =  userDAO.findById(item.getAgentId());
                double applicationAmt = item.getLoanAmt();
                double userNewBsrBal = user.getBsrBal() + applicationAmt;
                double commissionAmt = (applicationAmt*commissionRate)/100;
                double businessAmt = applicationAmt - commissionAmt;
                Transaction commissionTransaction = Transaction.of(user.getId(), agent.getId(), commissionAmt, true);
                // A business transaction should be created Here to compensate application fees

                /*
                if(userNewBsrBal<2000){
                    Log.e(InsuranceDataDAO.class.getSimpleName(), "Rolling back transaction with an exception"+commissionRate);
                    throw new RuntimeException("Rolling back application validation");
                }
                //*/

                user.setBsrBal(userNewBsrBal);
                agent.setBsrBal(agent.getBsrBal()+commissionAmt);

                item.setState(LoanData.STATE_APPROVED);

                update(item);
                userDAO.update(agent);
                userDAO.update(user);
                transactionDAO.createOrUpdate(commissionTransaction);


                if(item.getState()== LoanData.STATE_APPROVED){
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
