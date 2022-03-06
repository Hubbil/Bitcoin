package persons;

public class BankAccount {

    private float credit;

    public BankAccount(){}

    public double getCredit() {
        return credit;
    }

    public void setNewCredit(float amount){
        credit = credit + amount;
    }
}