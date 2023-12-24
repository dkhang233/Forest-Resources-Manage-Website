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

//-----------------------CƠ SỞ LƯU TRỮ ĐỘNG VẬT

@Entity
@Table(name="animal_storage_facilities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class AnimalStorageFacilities {
    @Id
    @Column(name="code")
    private String code;

    @Column(name="name",nullable = false,length = 100)
    private String name;

    @Column(name="date")
    private Date date;
    
    //sức chứa
    @Column(name="capacity",nullable = false,length = 100)
    private long capacity;

    @ManyToOne
    @JoinColumn(name="adminstration_code")
    private Administration administration;

    //thông tin chi tiết
    @Column(name="detail")
    private String detail;

    @Column(name = "lat")
    private String lat;

    @Column(name = "lng")
    private String lng;

}
