package blockchain;

import Persons.Miner;
import Persons.Wallet;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public enum Configuration {
    INSTANCE;

    public Transaction genesisTransaction;
    public HashMap<String, TransactionOutput> utx0Map = new HashMap<>();
    public HashMap<String, PublicKey> adressTable = new HashMap<>();
    float minimumTransaction = 0.1f;
    public ArrayList<Block> blockchain = new ArrayList<>();
    public int difficulty = 4;
    int transactionSequence = 0;
    public final ArrayList<Miner> miners = new ArrayList<>();

    public final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
    public final char ENCODED_ZERO = ALPHABET[0];
    public final int[] INDEXES = new int[128];

    public final String EC_GENERATOR_SPECIFICATION_ALGORITHM = "secp256r1";
    public final String MESSAGE_DIGEST_SHA_ALGORITHM = "SHA-256";
    public final String MESSAGE_DIGEST_MD5_ALGORITHM = "MD5";
}


