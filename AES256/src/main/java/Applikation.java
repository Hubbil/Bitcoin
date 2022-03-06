import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class Applikation {

    //static instance
    private static final Applikation instance = new Applikation();

    public Applikation() {
    }

    public static void main(String... args) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {

    }

    public void encryptAll() {
        String algorithm = "AES/CBC/PKCS5Padding";
        try {
            SecretKey sc = AES256.getKeyFromPassword("DHBW", "mos");
            IvParameterSpec iv = AES256.generateIvNotRandom();
            File folder = new File("Data/");
            for (final File files : folder.listFiles()) {
                AES256.encryptFile(algorithm, sc, iv, files);
                AES256.deleteFile(files);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void decryptAll() {
        String algorithm = "AES/CBC/PKCS5Padding";
        try {
            SecretKey sc = AES256.getKeyFromPassword("DHBW", "mos");
            IvParameterSpec iv = AES256.generateIvNotRandom();
            File folder = new File("Data/");
            for (final File files : folder.listFiles()) {
                AES256.decryptFile(algorithm, sc, iv, files);
                AES256.deleteFile(files);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteFiles(){
        try {
            File folder = new File("Data/");
            for (final File files : folder.listFiles()) {
                AES256.deleteFile(files);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static Applikation getInstance() {
        return instance;
    }
}
