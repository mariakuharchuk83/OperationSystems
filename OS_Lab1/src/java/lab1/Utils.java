package lab1;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {
    private static Scanner input = new Scanner(System.in);

    public static int getX() {
        System.out.print("Enter x: ");
        int x = input.nextInt();
        input.nextLine();
        return x;
    }

    public static void runMenuEsc() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(e.getMessage());
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
    }
}
