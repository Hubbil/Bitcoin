package Persons;

import blockchain.*;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    public HashMap<String, TransactionOutput> utx0Map = new HashMap<>();
    private transient PrivateKey privateKey;
    private transient PublicKey publicKey;
    private transient String bitcoinAdress;
    private float amount;

    public Wallet() {
        generateKeyPair();
        Configuration.INSTANCE.adressTable.put(bitcoinAdress,publicKey);
        amount = 0.0f;
    }

    private String adjustTo64(String string) {
        return switch (string.length()) {
            case 62 -> "00" + string;
            case 63 -> "0" + string;
            case 64 -> string;
            default -> throw new IllegalArgumentException("not a valid key | " + string);
        };
    }

    public String bytesToHex(byte[] bytes) {
        byte[] hexArray = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
        byte[] hexChars = new byte[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars, StandardCharsets.UTF_8);
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(ecSpec, random);
            KeyPair kp = keyGen.generateKeyPair();

            privateKey = kp.getPrivate();
            publicKey = kp.getPublic();
            ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
            String result = adjustTo64(ecPrivateKey.getS().toString(16)).toUpperCase();
            ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
            ECPoint ecPoint = ecPublicKey.getW();
            String sx = adjustTo64(ecPoint.getAffineX().toString(16)).toUpperCase();
            String sy = adjustTo64(ecPoint.getAffineY().toString(16)).toUpperCase();
            String bcPublicKey = "04" + sx + sy;

            MessageDigest shaMessageDigest01 = MessageDigest.getInstance(Configuration.INSTANCE.MESSAGE_DIGEST_SHA_ALGORITHM);
            byte[] resultSHAMessageDigest = shaMessageDigest01.digest(bcPublicKey.getBytes(StandardCharsets.UTF_8));

            MessageDigest md5MessageDigest01 = MessageDigest.getInstance(Configuration.INSTANCE.MESSAGE_DIGEST_MD5_ALGORITHM);
            byte[] md5MessageDigest02 = md5MessageDigest01.digest(resultSHAMessageDigest);
            byte[] md5MessageDigest03 = new byte[md5MessageDigest02.length + 1];
            md5MessageDigest03[0] = 0;
            System.arraycopy(md5MessageDigest02, 0, md5MessageDigest03, 1, md5MessageDigest02.length);

            byte[] shaMessageDigest02 = shaMessageDigest01.digest(md5MessageDigest03);

            byte[] shaMessageDigest03 = shaMessageDigest01.digest(shaMessageDigest02);

            byte[] temp = new byte[25];
            System.arraycopy(md5MessageDigest02, 0, temp, 0, md5MessageDigest02.length);
            System.arraycopy(shaMessageDigest03, 0, temp, 20, 5);
            bitcoinAdress = Base58.encode(temp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public float getBalance() {
        float total = 0;

        for (Map.Entry<String, TransactionOutput> item : Configuration.INSTANCE.utx0Map.entrySet()) {
            TransactionOutput utx0 = item.getValue();
            if (utx0.isMine(publicKey)) {
                utx0Map.put(utx0.getID(), utx0);
                total += utx0.getValue();
            }
        }

        return total;
    }

    public Transaction sendFunds(PublicKey recipient, float value) {
        if (getBalance() < value) {
            return null;
        }

        ArrayList<TransactionInput> inputs = new ArrayList<>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : utx0Map.entrySet()) {
            TransactionOutput utx0 = item.getValue();
            total += utx0.getValue();
            inputs.add(new TransactionInput(utx0.getID()));
            if (total > value) {
                break;
            }
        }

        Transaction transaction = new Transaction(publicKey, recipient, value, inputs);
        transaction.generateSignature(privateKey);

        for (TransactionInput input : inputs) {
            utx0Map.remove(input.getId());
        }

        return transaction;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public String getBitcoinAdress(){
        return bitcoinAdress;
    }

    public float getAmount() {
        return amount;
    }

    public void setNewAmount(float amount){
        this.amount = this.amount + amount;
    }
}