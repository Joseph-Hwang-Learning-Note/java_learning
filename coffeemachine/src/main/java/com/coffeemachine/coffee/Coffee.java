package com.coffeemachine.coffee;

public abstract class Coffee {
    public int water;
    public int milk;
    public int coffeeBeans;
    public int price;

    public Coffee(int water, int milk, int coffeeBeans, int price) {
        super();
        this.water = water;
        this.milk = milk;
        this.coffeeBeans = coffeeBeans;
        this.price = price;
    }
}
