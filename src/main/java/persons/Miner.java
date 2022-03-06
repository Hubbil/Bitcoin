package persons;

import blockchain.*;

public class Miner {
    private String name;
    private Wallet wallet;

    public Miner(String name) {
        this.name = name;
        this.wallet = new Wallet();
    }

    public String getName() {
        return name;
    }
    public Wallet getWallet() {
        return wallet;
    }

    public void mine(Block newBlock) {
        if (!newBlock.getTransactions().get(0).isExchangeTransaction()){
            TransactionOutput output = new TransactionOutput(wallet.getPublicKey(), 0.025f, Configuration.INSTANCE.blockchain.get(Configuration.INSTANCE.blockchain.size()).getTransactions().get(0).getId());
            Configuration.INSTANCE.utx0Map.put(output.getID(), output);
        }
        newBlock.mineBlock(Configuration.INSTANCE.difficulty);
        Configuration.INSTANCE.blockchain.add(newBlock);
        Utility.toBlockchainJSON(Configuration.INSTANCE.blockchain);
        Utility.processLog("Block broadcast");
        Utility.processLog("Block verification");
        Utility.processLog("Block added");
    }
}
