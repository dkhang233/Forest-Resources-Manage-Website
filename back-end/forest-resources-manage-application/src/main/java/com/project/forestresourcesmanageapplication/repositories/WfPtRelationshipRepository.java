package com.project.forestresourcesmanageapplication.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.forestresourcesmanageapplication.models.ProductionType;
import com.project.forestresourcesmanageapplication.models.WfPtRelationship;

@Repository
public interface WfPtRelationshipRepository extends JpaRepository<WfPtRelationship,Integer>{
    @Query("SELECT w.productionType FROM WfPtRelationship w WHERE w.woodFacilities.code = :code")
    Optional<List<ProductionType>> selectAllProductionTypeByFacilitiesCode(String code);
}
