import Persons.Member;
import Persons.Person;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

public class Application {

    private final Person clueLess = new Member();
    private final Person ed = new Member();
    private Class clazz;
    private Object instance;
    private static final Application application = new Application();

    public static void main(String... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {

        application.loadClazzFromJavaArchive();
        application.provideInstanceOfClass();
        int i = 0;
        while (i == 0) {
            String in = null;
            String[] command = null;
            BufferedReader in1 = new BufferedReader(new InputStreamReader(System.in));
            in = in1.readLine();
            command = in.split(" ");
            if (Objects.equals(command[0], "launch http://www.trust-me.mcg/report.jar")) {
                application.launch();
            }

            else if (Objects.equals(command[0], "exchange")) {
                double amount = Double.parseDouble(command[1]);
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
                double amount = Double.parseDouble(command[1]);
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

    public void exchange(double amount){
        clueLess.exchange(amount);
        System.out.println("exchange "+ amount);
    }

    public void showBalance(){
        System.out.println("BankCredit: "+clueLess.getBankAccount().getCredit());
        System.out.println("BitcoinCredit: "+clueLess.getWallet().getCredit());
    }

    public void showRecipient(){
        System.out.println("showRecipient");
    }

    public void pay(double amount){
        clueLess.getWallet().transfer(amount);
        ed.getWallet().topUp(amount);
    }

    public void checkPayment(){
        if (ed.getWallet().transferSuccess()){
            application.executeMethodDirectlyWithoutPort("decryptAll");
            System.out.println("Files decrypted");
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
        System.out.println("--- executeMethodDirectlyWithoutPort");

        try {
            Method method = clazz.getDeclaredMethod(method1);
            System.out.println(method);
            String version = (String) method.invoke(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
    }
}
