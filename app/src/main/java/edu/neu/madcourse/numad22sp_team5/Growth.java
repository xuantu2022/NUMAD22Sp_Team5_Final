package edu.neu.madcourse.numad22sp_team5;

public class Growth {
    public String date;
    public int height;
    public int weight;
    public int headCirc;

    public Growth() {

    }

    public Growth(String date, int height, int weight, int headCirc) {
        this.date = date;
        this.height = height;
        this.weight = weight;
        this.headCirc = headCirc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeadCirc() {
        return headCirc;
    }

    public void setHeadCirc(int headCirc) {
        this.headCirc = headCirc;
    }
}
