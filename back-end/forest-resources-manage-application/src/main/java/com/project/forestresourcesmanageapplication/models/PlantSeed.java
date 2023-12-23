package com.project.forestresourcesmanageapplication.models;

import org.hibernate.annotations.CollectionId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

//-----------------------------GIỐNG CÂY TRỒNG
@Entity
@Table(name="plant_seed")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
public class PlantSeed {
    @Id
    @Column(name="name",length = 100)
    private String name;

    @Column(name="type",length = 100)
    private String type;

    @Column(name="image", length = 150)
    private String image;

    //loại đất
    @Column(name="soil_type",length = 100)
    private String soilType;

    //sâu bệnh chính
    @Column(name="main_pest",length = 100)
    private String mainPest;

    //thời gian thu hoạch
    @Column(name="harvesting_period")
    private String harvestingPeriod;

    //mùa vụ
    @Column(name="plant_season",length = 100)
    private String plantSeason;
}
