package persons;

import blockchain.*;

import java.security.PublicKey;
import java.util.ArrayList;

public class Person {

    private Wallet wallet;
    private BankAccount account;

    public Person(){
        wallet = new Wallet();
        account = new BankAccount();
    }

    public void pay(float amount, String bitcoinAdress){
        Utility.processLog("Transaction initiated");
        Utility.processLog("TX Broadcast to miners");
        PublicKey publicKey = Configuration.INSTANCE.adressTable.get(bitcoinAdress);
        Utility.processLog("Structating block");
        Block block = new Block(Configuration.INSTANCE.blockchain.get(Configuration.INSTANCE.blockchain.size()).getHash());
        block.addTransaction(wallet.sendFunds(publicKey,amount));
        Utility.processLog("PoW Miner selection");
        Miner miner = Blockchain.pickRandomMiner(Configuration.INSTANCE.miners);
        miner.mine(block);
    }

    public void exchangeForBitcoin(int amount){
        Utility.processLog("Transaction initiated");
        Utility.processLog("TX Broadcast to miners");
        Wallet coinbase = new Wallet();
        ArrayList<TransactionInput> inputs = new ArrayList<>();
        TransactionOutput output = new TransactionOutput(wallet.getPublicKey(),amount / 0.000019f,Configuration.INSTANCE.blockchain.get(Configuration.INSTANCE.blockchain.size()).getTransactions().get(0).getId());
        TransactionInput input = new TransactionInput(output.getID());
        inputs.add(input);
        Configuration.INSTANCE.utx0Map.put(output.getID(), output);
        Transaction transaction = new Transaction(coinbase.getPublicKey(), wallet.getPublicKey(),amount / 0.000019f,inputs );
        transaction.setExchangeTransaction(true);
        transaction.generateSignature(coinbase.getPrivateKey());
        Utility.processLog("Structating block");
        Block block = new Block(Configuration.INSTANCE.blockchain.get(Configuration.INSTANCE.blockchain.size()).getHash());
        block.addTransaction(transaction);
        Utility.processLog("PoW Miner selection");
        Miner miner = Blockchain.pickRandomMiner(Configuration.INSTANCE.miners);
        miner.mine(block);
    }

    public void initializeBlockchain() {
        Utility.processLog("Initialise Blockchain");
        Wallet coinbase = new Wallet();
        Configuration.INSTANCE.genesisTransaction = new Transaction(coinbase.getPublicKey(), new Wallet().getPublicKey(), 100f, null);
        Configuration.INSTANCE.genesisTransaction.generateSignature(coinbase.getPrivateKey());
        Configuration.INSTANCE.genesisTransaction.setId("0");
        Configuration.INSTANCE.genesisTransaction.getOutputs().add(
                new TransactionOutput(Configuration.INSTANCE.genesisTransaction.getRecipient(),
                        Configuration.INSTANCE.genesisTransaction.getValue(), Configuration.INSTANCE.genesisTransaction.getId())
        );

        Configuration.INSTANCE.utx0Map.put(
                Configuration.INSTANCE.genesisTransaction.getOutputs().get(0).getID(),
                Configuration.INSTANCE.genesisTransaction.getOutputs().get(0));


        Block genesisBlock = new Block("0");
        genesisBlock.addTransaction(Configuration.INSTANCE.genesisTransaction);
        Configuration.INSTANCE.blockchain.add(genesisBlock);
        genesisBlock.mineBlock(Configuration.INSTANCE.difficulty);
    }
}
