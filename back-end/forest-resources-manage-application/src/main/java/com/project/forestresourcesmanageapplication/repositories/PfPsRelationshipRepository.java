package com.project.forestresourcesmanageapplication.repositories;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.forestresourcesmanageapplication.models.PfPsRelationship;
import com.project.forestresourcesmanageapplication.models.PlantSeed;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantity; 

public interface PfPsRelationshipRepository extends JpaRepository<PfPsRelationship,Integer>{
    @Query("SELECT p.plantSeed FROM PfPsRelationship p WHERE p.plantFacilities.code = :code")
    Optional<List<PlantSeed>> selectAllPlantSeedByFacilitiesCode(String code);

    @Query("SELECT NEW com.project.forestresourcesmanageapplication.responses.FacilitiesQuantity(p.plantFacilities.name c , SUM(p.quantity)) "
            + " FROM PfPsRelationship p WHERE p.date <= :date"
            + " GROUP BY p.plantFacilities"
            + " ORDER BY c")
    List<FacilitiesQuantity> selectAllQuantityOfFacilities(@Param("date") Date date);

    @Query("SELECT SUM(p.quantity) FROM PfPsRelationship p"
            + " WHERE p.plantFacilities.name = :name AND p.date <= :date"
            + " GROUP BY p.plantFacilities")
    long getMonthQuantityOfFacilitiesNew(@Param("name") String name, @Param("date") Date date);
}
