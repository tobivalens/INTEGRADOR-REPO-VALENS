package com.example.bckndApi.data;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String email;
    private String lastname;
    private String cedula;
    private String password;
    private int age;
    private Date birthday;
    private Status status;

    private Diagnostic diagnostic;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Measure> measures;

    public User(String name, String email, String lastname, String cedula, String password, int age, Date birthday, Diagnostic diagnostic) {
        this.name = name;
        this.email = email;
        this.lastname = lastname;
        this.cedula = cedula;
        this.password = password;
        this.age = age;
        this.birthday = birthday;
        this.status = Status.INGRESADO;
        this.diagnostic = diagnostic;
        this.measures = new ArrayList<>();
    }

    public User() {
        this.measures = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }

    public Diagnostic getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(Diagnostic diagnostic) {
        this.diagnostic = diagnostic;
    }
}