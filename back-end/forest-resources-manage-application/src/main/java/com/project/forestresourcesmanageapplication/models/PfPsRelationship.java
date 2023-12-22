package com.project.forestresourcesmanageapplication.models;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

//---------------------QUAN HỆ CƠ SỞ - GIỐNG CÂY TRỒNG
@Entity
@Table(name="pf_ps_relationship")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class PfPsRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="plant_facilities_code")
    private PlantFacilities plantFacilities;

    @ManyToOne
    @JoinColumn(name="plant_seed_name")
    private PlantSeed plantSeed;

    @Column(name="quantity")
    private long quantity;

    @Column(name="date")
    private Date date;
}
