package com.coffeemachine;

import java.io.IOException;
import java.util.Scanner;

import com.coffeemachine.coffeemachine.AdvancedCoffeeMachine;
import com.coffeemachine.coffeemachine.BasicCoffeeMachine;
import com.coffeemachine.coffeemachine.CoffeeMachine;

public class App {

    public static final Scanner scanner = new Scanner(System.in);

    public static final String rootDir = System.getProperty("user.dir");
    public static final String storageDir = App.rootDir + "/storage";

    public static void test() {
        return;
    }

    public static void main(String[] args) throws IOException {
        int coffeeMachineIndex;
        CoffeeMachine coffeeMachine = null;

        while (true) {
            System.out.println("Select Coffee Machine: ");
            System.out.println("0: Test Mode");
            System.out.println("1: Basic Coffee Machine");
            System.out.println("2: Advanced Coffee Machine");
            coffeeMachineIndex = scanner.nextInt();
            if (coffeeMachineIndex >= 0 && coffeeMachineIndex <= 2) {
                break;
            } else {
                System.out.println("That is not a valid mode");
            }
        }

        switch (coffeeMachineIndex) {
            case 1:
                coffeeMachine = new BasicCoffeeMachine();
                break;
            case 2:
                coffeeMachine = new AdvancedCoffeeMachine();
                break;
            case 0:
                App.test();
                break;
            default:
                break;
        }

        if (coffeeMachine != null) {
            coffeeMachine.operate();
        }
    }
}
