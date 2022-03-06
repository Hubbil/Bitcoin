package Wallet;

public class Wallet {

    private double credit;
    private boolean success = false;
    private double amountDiff;

    public Wallet(){

    }

    public double getCredit() {
        return credit;
    }

    public void topUp(double amount){
        this.credit = this.credit + amount;
        if (amount >= 0.02755){
            this.success = true;
        }
    }

    public void transfer(double amount){
        this.credit = this.credit - amount;
    }

    public boolean transferSuccess() {
        return success;
    }
}