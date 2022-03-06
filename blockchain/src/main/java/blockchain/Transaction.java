package blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
    private final PublicKey sender;
    private final PublicKey recipient;
    private final float value;
    private final ArrayList<TransactionOutput> outputs = new ArrayList<>();
    private final ArrayList<TransactionInput> inputs;
    private String id;
    private byte[] signature;
    private boolean exchangeTransaction;

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
        Configuration.INSTANCE.transactionSequence++;
        return Utility.applySha256(Utility.getStringFromKey(sender) + Utility.getStringFromKey(recipient)
                + value + Configuration.INSTANCE.transactionSequence);
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = Utility.getStringFromKey(sender) + Utility.getStringFromKey(recipient) + value;
        signature = Utility.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = Utility.getStringFromKey(sender) + Utility.getStringFromKey(recipient) + value;
        return !Utility.verifyECDSASig(sender, data, signature);

    }

    public boolean processTransaction() {
        if (verifySignature()) {
            System.out.println("#transaction signature failed to verify");
            return false;
        }

        for (TransactionInput i : inputs) {
            i.setUtx0(Configuration.INSTANCE.utx0Map.get(i.getId()));
        }

        if (getInputsValue() < Configuration.INSTANCE.minimumTransaction) {
            System.out.println("#transaction input to small | " + getInputsValue());
            return false;
        }

        float leftOver = getInputsValue() - value;
        id = calculateHash();
        outputs.add(new TransactionOutput(recipient, value, id));
        outputs.add(new TransactionOutput(sender, leftOver, id));

        for (TransactionOutput o : outputs) {
            Configuration.INSTANCE.utx0Map.put(o.getID(), o);
        }

        for (TransactionInput i : inputs) {
            if (i.getUTX0() == null) {
                continue;
            }
            Configuration.INSTANCE.utx0Map.remove(i.getUTX0().getID());
        }

        return true;
    }

    public float getInputsValue() {
        float total = 0;

        for (TransactionInput i : inputs) {
            if (i.getUTX0() == null) {
                continue;
            }
            total += i.getUTX0().getValue();
        }

        return total;
    }

    public float getOutputsValue() {
        float total = 0;

        for (TransactionOutput o : outputs) {
            total += o.getValue();
        }

        return total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PublicKey getSender() {
        return sender;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public float getValue() {
        return value;
    }

    public ArrayList<TransactionInput> getInputs() {
        return inputs;
    }

    public ArrayList<TransactionOutput> getOutputs() {
        return outputs;
    }

    public void setExchangeTransaction(boolean set) {
        exchangeTransaction = set;
    }

    public boolean isExchangeTransaction() {
        return exchangeTransaction;
    }
}