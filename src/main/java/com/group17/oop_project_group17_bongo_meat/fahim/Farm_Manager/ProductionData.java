package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

import java.io.Serializable;

public class ProductionData implements Serializable {

    private int meat;
    private int milk;
    private int egg;

    public ProductionData(int meat, int milk, int egg) {
        this.meat = meat;
        this.milk = milk;
        this.egg = egg;
    }

    public int getMeat() { return meat; }
    public int getMilk() { return milk; }
    public int getEgg() { return egg; }
}
