package com.example.bckndApi.data;

import java.util.List;


public class Readings {

    private List<Coordinate> accelerometer;
    private List<Coordinate> gyroscope;

    public List<Coordinate> getAccelerometer() {
        return accelerometer;
    }

    public void setAccelerometer(List<Coordinate> accelerometer) {
        this.accelerometer = accelerometer;
    }

    public List<Coordinate> getGyroscope() {
        return gyroscope;
    }

    public void setGyroscope(List<Coordinate> gyroscope) {
        this.gyroscope = gyroscope;
    }
}
