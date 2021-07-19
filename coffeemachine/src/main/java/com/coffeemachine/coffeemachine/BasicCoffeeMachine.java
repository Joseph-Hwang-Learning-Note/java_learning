package com.coffeemachine.coffeemachine;

import java.util.Collections;
import java.util.Arrays;

import com.coffeemachine.App;
import com.coffeemachine.coffee.BasicCoffee;
import com.coffeemachine.coffee.Coffee;

public class BasicCoffeeMachine implements CoffeeMachine {

    @Override
    public void operate() {
        int mode;
        while (true) {
            System.out.println("Select mode: ");
            System.out.println("1: Make Coffee");
            System.out.println("2: Estimate Servings");
            mode = App.scanner.nextInt();
            if (mode >= 1 && mode <= 2) {
                break;
            } else {
                System.out.println("That is not a valid mode");
            }
        }
        executeAction(mode);
    }

    private static void executeAction(int mode) {
        switch (mode) {
            case 1:
                makeCoffee();
                break;
            case 2:
                estimateServings();
                break;
            default:
                break;
        }
    }

    private final static Coffee coffee = new BasicCoffee();

    private static void makeCoffee() {
        System.out.println("Write how many cups of coffee you will need: ");
        int numCoffee = App.scanner.nextInt();
        System.out.println(String.format("For %d cups of coffee you will need:", numCoffee));
        System.out.println(String.format("%d ml of water", numCoffee * coffee.water));
        System.out.println(String.format("%d ml of milk", numCoffee * coffee.milk));
        System.out.println(String.format("%d g of coffee beans", coffee.coffeeBeans));
    }

    private static void estimateServings() {
        System.out.println("Write how many ml of water the coffee machine has: ");
        int amtWater = App.scanner.nextInt();
        System.out.println("Write how many ml of milk the coffee machine has: ");
        int amtMilk = App.scanner.nextInt();
        System.out.println("Write how many grams of coffee beans the coffee machine has: ");
        int amtBeans = App.scanner.nextInt();
        System.out.println("Write how many cups of coffee you will need: ");
        int numCups = App.scanner.nextInt();
        int possibleCups = Collections.min(Arrays.asList((int) (amtWater / coffee.water), (int) (amtMilk / coffee.milk),
                (int) (amtBeans / coffee.coffeeBeans)));
        if (possibleCups >= numCups) {
            int surplus = possibleCups - numCups;
            System.out.println(String.format("Yes, I can make that amount of coffee%s",
                    surplus == 0 ? "" : String.format("(and even %d more than that)", surplus)));
        } else {
            System.out.println(String.format("No, I can make only %d cup(s) of coffee", possibleCups));
        }
    }
}