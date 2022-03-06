package Persons;

public class BankAccount {

    private float credit;

    public BankAccount(){
        credit = 5000f;
    }

    public double getCredit() {
        return credit;
    }

    public void setNewCredit(float amount){
        credit = credit + amount;
    }
}