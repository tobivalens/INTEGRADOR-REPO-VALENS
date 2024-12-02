package com.example.bckndApi.data;

public class PatientData {

    private String cedula;
    private String state;
    private String lastMedicine;
    private String mode;

    public PatientData(){

    }

    public PatientData(String cedula, String state, String lastMedicine, String mode) {
        this.cedula = cedula;
        this.state = state;
        this.lastMedicine = lastMedicine;
        this.mode = mode;
    }

    // Getters y Setters
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLastMedicine() {
        return lastMedicine;
    }

    public void setLastMedicine(String lastMedicine) {
        this.lastMedicine = lastMedicine;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
