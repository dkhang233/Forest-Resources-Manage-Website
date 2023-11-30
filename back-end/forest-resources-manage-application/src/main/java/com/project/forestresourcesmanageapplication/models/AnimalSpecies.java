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
import jakarta.persistence.OneToMany;
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

    @Column(name="main_food")
    private String mainFood;

    //bệnh chính
    @Column(name="main_disease")
    private String mainDisease;

    //tuổi thọ
    @Column(name="longevity")
    private int longevity;

    //thông tin chi tiết
    @Column(name="detail")
    private String detail;

    @ManyToMany(fetch = FetchType.EAGER,
                cascade = {
                CascadeType.MERGE,
                CascadeType.DETACH,
                CascadeType.PERSIST,
                CascadeType.REFRESH
                }
    )
    @JoinTable(
        name="as_f_relationship",
        joinColumns = @JoinColumn(name="animal_species_name"),
        inverseJoinColumns = @JoinColumn(name="fluctuation_id")
    )
    private List<Fluctuation> fluctuations;

}
