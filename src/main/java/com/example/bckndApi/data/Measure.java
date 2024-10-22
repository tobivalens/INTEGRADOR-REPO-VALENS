package com.example.bckndApi.data;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;


@Entity
@Table(name = "measures")

public class Measure {


    private Long idSearch;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

   @Type(JsonBinaryType.class)
   @Column( columnDefinition = "jsonb")
    private Readings readings;


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
