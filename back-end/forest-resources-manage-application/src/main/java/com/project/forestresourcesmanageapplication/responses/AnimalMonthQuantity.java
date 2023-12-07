package com.project.forestresourcesmanageapplication.responses;

import java.util.List;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnimalMonthQuantity {
    private String facilitiesName;
    private MonthQuantity monthQuantity;
}
