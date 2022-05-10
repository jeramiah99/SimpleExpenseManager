/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.app.Application;
import android.test.ApplicationTestCase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTest extends ApplicationTestCase<Application> {

    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    public ApplicationTest() {
        super(Application.class);
    }

    @Before
    public void setUp(){
        DatabaseHelper db = new DatabaseHelper(ApplicationProvider.getApplicationContext());
        accountDAO = new PersistentAccountDAO(db);
        transactionDAO = new PersistentTransactionDAO(db);
        //addAccounts();
    }

    public void addAccounts(){
        accountDAO.addAccount(new Account("111", "Star City", "Oliver Queen", 1000));
        accountDAO.addAccount(new Account("112", "Central City", "Barry Allen", 500));
        accountDAO.addAccount(new Account("113", "Gotham", "Bruce Wayne", 900));
        accountDAO.addAccount(new Account("114", "Metropolitan", "Clark Kent", 100));
        accountDAO.addAccount(new Account("115", "Coast City", "Hal Jordan", 50));
    }

    public boolean compareAccountVsAccountData(String accountNo, String bankName, String accountHolderName, double balance, Account account){
        return account.getAccountNo().equals(accountNo) && account.getAccountHolderName().equals(accountHolderName) && account.getBankName().equals(bankName) && account.getBalance()==balance;
    }

    //@Test
    public void A_getAccount(){
        try{
            Account account = accountDAO.getAccount("111");
            assert  compareAccountVsAccountData("111", "Star City", "Oliver Queen", 1000, account);
        }catch (InvalidAccountException e){
            assert false;
        }
    }

    // @Test
    public void B_getAccountsList(){
        List<Account> accountList = accountDAO.getAccountsList();
        String[] accountNumbers = {"111","112","113","114","115"};
        for (String accountNumber:accountNumbers) {
            boolean found = false;
            for (Account account:accountList) {
                if(account.getAccountNo().equals(accountNumber)){
                    found = true;
                    break;
                }
            }
            assert found;
        }
    }

    @Test
    public void test(){
        assert true;
    }

    // @Test
    public void D_removeAccount(){
        assert false;
        try{
            accountDAO.removeAccount("111");
            List<Account> accountList = accountDAO.getAccountsList();
            for(Account account:accountList){
                if(account.getAccountNo().equals("111"))    assert false;
            }
        }catch(InvalidAccountException err){
            assert false;
        }


    }

    public void logTransactions(){
        Date date = new Date();
        transactionDAO.logTransaction(date,"111", ExpenseType.INCOME,500);
        transactionDAO.logTransaction(date,"111",ExpenseType.EXPENSE,200);
        transactionDAO.logTransaction(date,"111",ExpenseType.EXPENSE,300);
    }

    //@Test
    public void C_getAllTransactions(){
        logTransactions();
        List<Transaction> transactionList =  transactionDAO.getAllTransactionLogs();
        Transaction[] transactions = {
                new Transaction(new Date(), "111", ExpenseType.INCOME, 500),
                new Transaction(new Date(), "111", ExpenseType.EXPENSE, 200),
                new Transaction(new Date(), "111", ExpenseType.EXPENSE, 300),
        };
        for (Transaction transaction:transactions) {
            boolean found = false;
            for (Transaction transaction1:transactionList) {
                if((transaction.getAccountNo().equals(transaction1.getAccountNo())) && (transaction.getAmount()==transaction1.getAmount()) && (transaction.getExpenseType()==transaction1.getExpenseType())){
                    found = true;
                    break;
                }
            }
            assert found;
        }
    }





}