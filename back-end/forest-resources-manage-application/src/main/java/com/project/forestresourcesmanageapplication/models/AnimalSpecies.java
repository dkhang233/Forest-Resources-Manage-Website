package com.project.forestresourcesmanageapplication.models;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

//-----------------------LOÀI ĐỘNG VẬT

@Entity
@Table(name="animal_species")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class AnimalSpecies {
    @Id
    @Column(name="name")
    private String name;
    
    @Column(name="animal_type",nullable = false,length = 100)
    private String animalType;

    @Column(name="image", length = 150)
    private String image;

    @Column(name="main_food")
    private String mainFood;

    //bệnh chính
    @Column(name="main_disease")
    private String mainDisease;

    //tuổi thọ
    @Column(name="longevity")
    private int longevity;

    @ManyToOne(fetch = FetchType.EAGER,
                cascade = {
                CascadeType.MERGE,
                CascadeType.DETACH,
                CascadeType.PERSIST,
                CascadeType.REFRESH
                }
    )
    @JoinColumn(name="fluctuation_id")
    private Fluctuation fluctuation;

}
