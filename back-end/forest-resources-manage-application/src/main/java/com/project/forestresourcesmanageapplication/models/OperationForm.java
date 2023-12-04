package com.project.forestresourcesmanageapplication.models;

import org.hibernate.validator.constraints.CodePointLength;

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
    @Column(name="name")
    private String name;

    @Column(name="decription")
    private String decription;
}
