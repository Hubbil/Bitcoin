package Persons;

import BankAccount.BankAccount;
import Wallet.Wallet;

public abstract class Person {

    protected Wallet wallet;

    protected BankAccount bankAccount;

    public Person(){
        this.wallet = new Wallet();
        this.bankAccount = new BankAccount();
    }

//    public void addWallet(Wallet wallet){
//        this.wallet = wallet;
//    }
//
//    public void addBankAccount(BankAccount bankAccount){
//        this.bankAccount = bankAccount;
//    }

    public Wallet getWallet() {
        return wallet;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void exchange(double bitcoinAmount){
        double euroAmount = bitcoinAmount / (0.000019);
        this.bankAccount.transfer(euroAmount);
        this.wallet.topUp(bitcoinAmount);
    }

    public void showBalance(){
        System.out.println("Your Bankaccount Credit is now "+this.bankAccount.getCredit()+" Euro");
        System.out.println("Your Wallet Credit is now "+this.wallet.getCredit()+" BTC");
    }
}