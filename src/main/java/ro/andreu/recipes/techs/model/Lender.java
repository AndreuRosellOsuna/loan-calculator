package ro.andreu.recipes.techs.model;

import com.opencsv.bean.CsvBindByName;

public class Lender implements Comparable<Lender> {

    @CsvBindByName(column = "Lender")
    private String name;

    @CsvBindByName(column = "Rate")
    private Float rate;

    @CsvBindByName(column = "Available")
    private Float available;

    public Lender() {
    }

    public Lender(String name, Float rate, Float available) {
        this.name = name;
        this.rate = rate;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Float getAvailable() {
        return available;
    }

    public void setAvailable(Float available) {
        this.available = available;
    }

    @Override
    public int compareTo(Lender o) {
        return this.getRate().compareTo(o.getRate());
    }
}
