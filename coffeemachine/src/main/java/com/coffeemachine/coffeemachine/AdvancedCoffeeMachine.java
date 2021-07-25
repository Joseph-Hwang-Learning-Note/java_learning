package com.coffeemachine.coffeemachine;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.coffeemachine.App;
import com.coffeemachine.coffee.Cappuccino;
import com.coffeemachine.coffee.Coffee;
import com.coffeemachine.coffee.Espresso;
import com.coffeemachine.coffee.Latte;

import org.json.JSONObject;

class CoffeeMachineStatus {
    public int money;
    public int coffeeBeans;
    public int milk;
    public int water;
    public int disposableCups;

    public CoffeeMachineStatus(int money, int coffeeBeans, int milk, int water, int disposableCups) {
        super();
        this.money = money;
        this.coffeeBeans = coffeeBeans;
        this.milk = milk;
        this.water = water;
        this.disposableCups = disposableCups;
    }
}

public class AdvancedCoffeeMachine implements CoffeeMachine {

    private final static String BUY = "buy";
    private final static String FILL = "fill";
    private final static String TAKE = "take";
    private final static String REMAINING = "remaining";
    private final static String EXIT = "exit";

    private static String statusFileName = "ingredients.txt";
    private final static File statusFile = new File(App.storageDir + "/" + statusFileName);

    @Override
    public void operate() throws IOException {
        String action;
        while (true) {
            System.out.println("Write action (buy, fill, take, remaining, exit): ");
            action = App.scanner.next();
            if (Arrays.asList(BUY, FILL, TAKE, REMAINING, EXIT).contains(action)) {
                if (action.equals(EXIT)) {
                    break;
                }
                executeAction(action);
            } else {
                System.out.println("That is not a valid action");
            }
        }
    }

    private static void executeAction(String action) throws IOException {
        CoffeeMachineStatus currentStatus = getStatus();
        switch (action) {
            case BUY:
                currentStatus = buy(currentStatus);
                break;
            case FILL:
                currentStatus = fill(currentStatus);
                break;
            case TAKE:
                currentStatus = take(currentStatus);
                break;
            case REMAINING:
                showStatus(currentStatus);
                currentStatus = null;
                break;
            default:
                currentStatus = null;
                break;
        }
        if (currentStatus != null) {
            writeStatus(currentStatus);
        }
    }

    private final static String ESPRESSO = "1";
    private final static String LATTE = "2";
    private final static String CAPPUCINO = "3";

    private static CoffeeMachineStatus buy(CoffeeMachineStatus currentStatus) throws IOException {
        Coffee coffee = null;
        String action;
        while (true) {
            System.out
                    .println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ");
            action = App.scanner.next();
            if (Arrays.asList("1", "2", "3", "back").contains(action)) {
                break;
            } else {
                System.out.println("That is not a valid choice");
            }
        }
        switch (action) {
            case ESPRESSO:
                coffee = new Espresso();
                break;
            case LATTE:
                coffee = new Latte();
                break;
            case CAPPUCINO:
                coffee = new Cappuccino();
                break;
            default:
                break;
        }
        if (coffee == null) {
            return null;
        }
        int newAmtWater = currentStatus.water - coffee.water;
        int newAmtBeans = currentStatus.coffeeBeans - coffee.coffeeBeans;
        int newAmtMilk = currentStatus.milk - coffee.milk;
        int newAmtMoney = currentStatus.money + coffee.price;
        int newAmtCups = currentStatus.disposableCups - 1;
        Map<String, Integer> newAmtHashMap = new HashMap<>();
        newAmtHashMap.put("beans", newAmtBeans);
        newAmtHashMap.put("water", newAmtWater);
        newAmtHashMap.put("milk", newAmtMilk);
        newAmtHashMap.put("cups", newAmtCups);
        List<String> keyList = new ArrayList<>(newAmtHashMap.keySet());
        Collection<Integer> valueList = new ArrayList<>(newAmtHashMap.values());
        if (Collections.min(valueList) < 0) {
            StringBuilder prompt = new StringBuilder("Sorry, not enough ");
            keyList.forEach((key) -> {
                if (newAmtHashMap.get(key) < 0) {
                    prompt.append(key + ", ");
                }
            });
            System.out.println(prompt.substring(0, prompt.length() - 2));
            return null;
        } else {
            System.out.println("I have enough resources, making you a coffee!");
            currentStatus.coffeeBeans = newAmtBeans;
            currentStatus.water = newAmtWater;
            currentStatus.milk = newAmtMilk;
            currentStatus.disposableCups = newAmtCups;
            currentStatus.money = newAmtMoney;
        }
        return currentStatus;
    }

    private static CoffeeMachineStatus fill(CoffeeMachineStatus currentStatus) throws IOException {
        System.out.println("Write how many ml of water you want to add: ");
        currentStatus.water = currentStatus.water + App.scanner.nextInt();
        System.out.println("Write how many ml of milk you want to add: ");
        currentStatus.milk = currentStatus.milk + App.scanner.nextInt();
        System.out.println("Write how many grams of coffee beans you want to add: ");
        currentStatus.coffeeBeans = currentStatus.coffeeBeans + App.scanner.nextInt();
        System.out.println("Write how many disposable cups of coffee you want to add: ");
        currentStatus.disposableCups = currentStatus.disposableCups + App.scanner.nextInt();
        return currentStatus;
    }

    private static CoffeeMachineStatus take(CoffeeMachineStatus currentStatus) {
        System.out.println(String.format("I gave you %d", currentStatus.money));
        currentStatus.money = 0;
        return currentStatus;
    }

    private static void showStatus(CoffeeMachineStatus currentStatus) {
        System.out.println("The coffee machine has:");
        System.out.println(String.format("%d ml of water", currentStatus.water));
        System.out.println(String.format("%d ml of milk", currentStatus.milk));
        System.out.println(String.format("%d g of coffee beans", currentStatus.coffeeBeans));
        System.out.println(String.format("%d disposable cups", currentStatus.disposableCups));
        System.out.println(String.format("$%d of money", currentStatus.money));
    }

    private static CoffeeMachineStatus getStatus() throws IOException {
        FileReader fileReader = new FileReader(statusFile);
        String jsonString = "";
        int EOF = -1;
        int c;
        while ((c = fileReader.read()) != EOF) {
            jsonString += (char) c;
        }
        // int i = jsonString.indexOf('{');
        // int p = jsonString.lastIndexOf('}');
        // jsonString = jsonString.substring(i).substring(i - 1, p +
        // 1).replaceAll("\\\\", "").trim();
        JSONObject status = new JSONObject(jsonString);
        fileReader.close();
        return new CoffeeMachineStatus(status.getInt("money"), status.getInt("coffeeBeans"), status.getInt("milk"),
                status.getInt("water"), status.getInt("disposableCups"));
    }

    private static void writeStatus(CoffeeMachineStatus newStatus) throws IOException {
        FileWriter fileWriter = new FileWriter(statusFile, false);
        JSONObject status = new JSONObject();
        status.put("water", newStatus.water);
        status.put("coffeeBeans", newStatus.coffeeBeans);
        status.put("milk", newStatus.milk);
        status.put("money", newStatus.money);
        status.put("disposableCups", newStatus.disposableCups);
        fileWriter.write(status.toString());
        fileWriter.close();
    }
}
