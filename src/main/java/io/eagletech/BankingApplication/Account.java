package io.eagletech.BankingApplication;


import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Account implements Storable {
    @Getter
    private String acountName;
    private String accountNumber;
    private String customerBvn;
    private String bankName;
    private AccountType accountType;
    private List<Transaction> successfulTransactions;
    private int pin;



    public Account(Customer customer, String accountNumber, String bankName, AccountType accountType) {
        this.acountName = customer.getCustomerFirstName() + " " + customer.getCustomerLastName();
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        customerBvn = customer.getBvn();
        this.bankName = bankName;
        successfulTransactions = new ArrayList<>();


    }

    @Override
    public String toString() {
        String accountProfile = "";
        accountProfile += "Account Name: " + acountName + "\n";
        accountProfile += "Account Number: " + accountNumber + "\n";
        accountProfile += "Bank Name: " + bankName + "\n";
        accountProfile += "Account Type: " + accountType.toString() + "\n";
        accountProfile += "Customer BVN: " + customerBvn + "\n";
        //@todo remove account balance later, too sensitive to be in a toString method;
        accountProfile += "Account Balance: " + calculateAccountBalance().toString() + "\n";
        return accountProfile;

    }

    public BigDecimal calculateAccountBalance() {
        BigDecimal accountBalance = new BigDecimal(0);
        for(Transaction transaction: successfulTransactions){
            switch (transaction.getTransactionType()){
                case DEBIT -> accountBalance= accountBalance.add(transaction.getTransactionAmount());
                case CREDIT -> accountBalance = accountBalance.subtract(transaction.getTransactionAmount());
            }

        }
        return accountBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String getId() {
        return accountNumber;
    }

//    public void deposit(BigDecimal amountToDeposit) {
//
//        accountBalance = accountBalance.add(amountToDeposit);
//    }

    void updatePin(int oldPin, int newPin) {
        if (oldPin == pin) {
            setPin(newPin);
        }
    }

    public void withDraw(BigDecimal amountToWithdraw, int accountPin) {
        try {
            if (accountPin == getPin()) {
                if (amountToWithdraw.compareTo(calculateAccountBalance()) > 0) {
                    throw new WithdrawFailedException("Insufficient Funds");
                }
//                accountBalance = accountBalance.subtract(amountToWithdraw);
            } else {
                throw new WithdrawFailedException("Incorrect Pin");
            }


        } catch (InvalidPinException invalidPinException) {
            throw new WithdrawFailedException(invalidPinException.getMessage());
        }
    }

    private int getPin() {
        if (pin == 0) {
            throw new InvalidPinException("Pin not Set");
        } else {
            return pin;
        }
    }

    private void setPin(int newPin) {
        pin = newPin;
    }

    public List<Transaction> getTransaction() {
        return successfulTransactions;
    }
}
