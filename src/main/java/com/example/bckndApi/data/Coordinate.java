package com.example.bckndApi.data;


public class Coordinate {

    double x;
    double y;
    double z;
    long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}