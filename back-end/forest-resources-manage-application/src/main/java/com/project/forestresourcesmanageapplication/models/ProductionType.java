package com.project.forestresourcesmanageapplication.models;

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

//-------------------------------LOẠI HÌNH SẢN XUẤT
@Entity
@Table(name="production_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class ProductionType {
    @Id
    @Column(name="wood_type",length = 100)
    private String woodType;

    @Column(name="image", length = 150)
    private String image;

    @Column(name="capacity")
    private long capacity;
}
