package blockchain;

import blockchain.Transaction;
import blockchain.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Block {
    private final String previousHash;
    private final long timeStamp;
    private final ArrayList<Transaction> transactions = new ArrayList<>();
    private String merkleRoot;
    private String hash;
    private int nonce;

    public String getMiner() {
        return miner;
    }

    public void setMiner(String miner) {
        this.miner = miner;
    }

    private String miner;


    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public String getHash() {
        return this.hash;
    }

    public String getPreviousHash() {
        return this.previousHash;
    }

    public String calculateHash() {
        return Utility.applySha256(previousHash + timeStamp + nonce + merkleRoot);
    }

    public void mineBlock(int difficulty) {
        merkleRoot = Utility.getMerkleRoot(transactions);
        String target = Utility.getDifficultyString(difficulty);

        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }

        System.out.println("block mined | " + hash);
    }

    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            return;
        }

        if (!Objects.equals(previousHash, "0")) {
            if (!transaction.processTransaction()) {
                System.out.println("transaction failed to process");
                return;
            }
        }

        transactions.add(transaction);
        System.out.println("transaction added to block");
    }
}