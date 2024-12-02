package com.example.bckndApi.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "measures")
public class Measure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userID")
    @JsonIgnore
    private User user;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Readings readings;

    @OneToMany(mappedBy = "measure")
    private List<Comment> comments;

    private String description;
    private State state;
    private long time;
    private MeasureType measureType;

    @Temporal(TemporalType.TIMESTAMP)  // Esto asegura que la fecha se guarda como un timestamp en la base de datos
    private Date dateMeasure;

    public Measure() {
        this.dateMeasure = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Readings getReadings() {
        return readings;
    }

    public void setReadings(Readings readings) {
        this.readings = readings;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }

    public void setDateMeasure(Date dateMeasure) {this.dateMeasure = dateMeasure;}
    public Date getDateMeasure() {return dateMeasure;}
}
