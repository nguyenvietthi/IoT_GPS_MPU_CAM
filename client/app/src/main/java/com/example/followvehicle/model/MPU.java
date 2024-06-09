package com.example.followvehicle.model;

public class MPU {
    private double accx;
    private double accy;
    private double accz;
    private double gyrox;
    private double gyroy;
    private double gyroz;
    private double temperature;

    public MPU(double accx, double accy, double accz, double gyrox, double gyroy, double gyroz, double temperature){
        this.accx = accx;
        this.accy = accy;
        this.accz = accz;
        this.gyrox = gyrox;
        this.gyroy = gyroy;
        this.gyroz = gyroz;
        this.temperature = temperature;
    }

    public double getAccx() {
        return accx;
    }

    public double getAccy() {
        return accy;
    }

    public double getAccz() {
        return accz;
    }

    public double getGyrox() {
        return gyrox;
    }

    public double getGyroy() {
        return gyroy;
    }

    public double getGyroz() {
        return gyroz;
    }

    public double getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return "MPU{" +
                "accx=" + accx +
                ", accy=" + accy +
                ", accz=" + accz +
                ", gyrox=" + gyrox +
                ", gyroy=" + gyroy +
                ", gyroz=" + gyroz +
                ", temperature=" + temperature +
                '}';
    }
}
