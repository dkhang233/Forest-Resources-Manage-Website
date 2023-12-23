package com.project.forestresourcesmanageapplication.repositories;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.forestresourcesmanageapplication.models.ProductionType;
import com.project.forestresourcesmanageapplication.models.WfPtRelationship;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantity;
import com.project.forestresourcesmanageapplication.responses.FacilityQuantity;

@Repository
public interface WfPtRelationshipRepository extends JpaRepository<WfPtRelationship, Integer> {
        @Query("SELECT w.productionType FROM WfPtRelationship w WHERE w.woodFacilities.code = :code")
        Optional<List<ProductionType>> selectAllProductionTypeByFacilitiesCode(String code);

        @Query("SELECT NEW com.project.forestresourcesmanageapplication.responses.FacilitiesQuantity(w.woodFacilities.name c , SUM(w.quantity)) "
                        + " FROM WfPtRelationship w WHERE w.date <= :date"
                        + " GROUP BY w.woodFacilities"
                        + " ORDER BY c")
        List<FacilitiesQuantity> selectAllQuantityOfFacilities(@Param("date") Date date);

        @Query("SELECT SUM(w.quantity) FROM WfPtRelationship w"
                        + " WHERE w.woodFacilities.name = :name AND w.date <= :date"
                        + " GROUP BY w.woodFacilities")
        long getMonthQuantityOfFacilitiesNew(@Param("name") String name, @Param("date") Date date);

        @Query("SELECT NEW com.project.forestresourcesmanageapplication.responses.FacilityQuantity(a.woodFacilities.code , a.productionType.woodType , SUM(a.quantity) ) "
                        + " FROM WfPtRelationship a WHERE a.date <= :date"
                        + " GROUP BY a.woodFacilities, a.productionType"
                        + " ORDER BY a.woodFacilities.code , a.productionType.woodType DESC")
        List<FacilityQuantity> selectAllQuantityOfAllWoodType(@Param("date") Date date);
}
