package BankAccount;

public class BankAccount {

    private double credit;

    public BankAccount(){}

    public void topUp(double amount){
        this.credit = this.credit + amount;
    }

    public void transfer(double amount){
        this.credit = this.credit - amount;
    }

    public double getCredit() {
        return credit;
    }
}