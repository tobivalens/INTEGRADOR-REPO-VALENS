package com.example.bckndApi.data;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;


@Entity
@Table(name = "measures")

public class Measure {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

   @Type(JsonBinaryType.class)
   @Column( columnDefinition = "jsonb")
    private Readings readings;

   private State state;
   private String comentary;
   private TypeMeasure typeMeasure;
   private String lastMedicneHour;

    public String getLastMedicneHour() {
        return lastMedicneHour;
    }

    public void setLastMedicneHour(String lastMedicneHour) {
        this.lastMedicneHour = lastMedicneHour;
    }



    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getComentary() {
        return comentary;
    }

    public void setComentary(String comentary) {
        this.comentary = comentary;
    }

    public TypeMeasure getTypeMeasure() {
        return typeMeasure;
    }

    public void setTypeMeasure(TypeMeasure typeMeasure) {
        this.typeMeasure = typeMeasure;
    }

    public Readings getReadings() {
        return readings;
    }

    public void setReadings(Readings readings) {
        this.readings = readings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
