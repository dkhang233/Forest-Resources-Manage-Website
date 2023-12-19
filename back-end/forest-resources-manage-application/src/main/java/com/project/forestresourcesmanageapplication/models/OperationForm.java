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


//-----------------------------HÌNH THỨC HOẠT ĐỘNG
@Entity
@Table(name="operation_form")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class OperationForm {
    @Id
    @Column(name="name",length = 100)
    private String name;

    //mô tả
    @Column(name="description",length = 256)
    private String description;
}
