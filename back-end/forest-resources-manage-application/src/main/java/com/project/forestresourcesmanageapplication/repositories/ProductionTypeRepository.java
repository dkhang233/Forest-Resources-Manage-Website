package com.project.forestresourcesmanageapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.forestresourcesmanageapplication.models.ProductionType;

@Repository
public interface ProductionTypeRepository extends JpaRepository<ProductionType,String>{
    
}
