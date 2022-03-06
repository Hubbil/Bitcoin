package blockchain;

import persons.Miner;
import persons.Wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Blockchain {

    public Blockchain(){

        Miner sam = new Miner("Sam");
        Miner eve = new Miner("Eve");
        Miner bob = new Miner("Bob");

        Configuration.INSTANCE.miners.add(sam);
        Configuration.INSTANCE.miners.add(eve);
        Configuration.INSTANCE.miners.add(bob);
    }

    public static void isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = Utility.getDifficultyString(Configuration.INSTANCE.difficulty);
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<>();
        tempUTXOs.put(Configuration.INSTANCE.genesisTransaction.getOutputs().get(0).getID(), Configuration.INSTANCE.genesisTransaction.getOutputs().get(0));

        for (int i = 1; i < Configuration.INSTANCE.blockchain.size(); i++) {
            currentBlock = Configuration.INSTANCE.blockchain.get(i);
            previousBlock = Configuration.INSTANCE.blockchain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("#current hashes not equal");
                return;
            }

            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("#previous hashes not equal");
                return;
            }

            if (!currentBlock.getHash().substring(0, Configuration.INSTANCE.difficulty).equals(hashTarget)) {
                System.out.println("#block not mined");
                return;
            }

            TransactionOutput tempOutput;
            for (int t = 0; t < currentBlock.getTransactions().size(); t++) {
                Transaction currentTransaction = currentBlock.getTransactions().get(t);

                if (currentTransaction.verifySignature()) {
                    System.out.println("#Signature on blockchain.Transaction(" + t + ") is Invalid");
                    return;
                }

                if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are not equal to outputs on blockchain.Transaction(" + t + ")");
                    return;
                }

                for (TransactionInput input : currentTransaction.getInputs()) {
                    tempOutput = tempUTXOs.get(input.getId());

                    if (tempOutput == null) {
                        System.out.println("#referenced input on transaction(" + t + ") is missing");
                        return;
                    }

                    if (input.getUTX0().getValue() != tempOutput.getValue()) {
                        System.out.println("#referenced input on transaction(" + t + ") value invalid");
                        return;
                    }

                    tempUTXOs.remove(input.getId());
                }

                for (TransactionOutput output : currentTransaction.getOutputs()) {
                    tempUTXOs.put(output.getID(), output);
                }

                if (currentTransaction.getOutputs().get(0).getRecipient() != currentTransaction.getRecipient()) {
                    System.out.println("#transaction(" + t + ") output recipient is invalid");
                    return;
                }

                if (currentTransaction.getOutputs().get(1).getRecipient() != currentTransaction.getSender()) {
                    System.out.println("#transaction(" + t + ") output 'change' is not sender");
                    return;
                }
            }
        }
        System.out.println("blockchain valid");
    }

    public static Miner pickRandomMiner(ArrayList<Miner> miners) {
        int min = 1;
        int max = 3;
        int getRandomValue = 0;
        for(int i = min; i <=max; i++) {
            getRandomValue = (int) (Math.random()*(max-min)) + min;
        }
        return Configuration.INSTANCE.miners.get(getRandomValue - 1);
    }
}
