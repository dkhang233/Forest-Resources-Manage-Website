package com.project.forestresourcesmanageapplication.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.forestresourcesmanageapplication.dtos.CoordinatesDTO;
import com.project.forestresourcesmanageapplication.dtos.OperationFormDTO;
import com.project.forestresourcesmanageapplication.dtos.PfPsRelationshipDTO;
import com.project.forestresourcesmanageapplication.dtos.PlantFacilitiesDTO;
import com.project.forestresourcesmanageapplication.dtos.PlantSeedDTO;
import com.project.forestresourcesmanageapplication.dtos.ProductionTypeDTO;
import com.project.forestresourcesmanageapplication.dtos.WfPtRelationshipDTO;
import com.project.forestresourcesmanageapplication.dtos.WoodFacilitiesDTO;
import com.project.forestresourcesmanageapplication.models.OperationForm;
import com.project.forestresourcesmanageapplication.models.PfPsRelationship;
import com.project.forestresourcesmanageapplication.models.PlantFacilities;
import com.project.forestresourcesmanageapplication.models.PlantSeed;
import com.project.forestresourcesmanageapplication.models.ProductionType;
import com.project.forestresourcesmanageapplication.models.WfPtRelationship;
import com.project.forestresourcesmanageapplication.models.WoodFacilities;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantity;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInMoth;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInQuarter;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInYear;

public interface PlantFacilitiesService {
        // PlantFacilities
        public List<PlantFacilities> getAllPlantFacilities();

        public PlantFacilities updatePlantFacilities(String code,
                        PlantFacilitiesDTO plantFacilitiesDTO);

        public PlantFacilities getPlantFacilitiesByCode(String code);

        public PlantFacilities addPlantFacilities(PlantFacilitiesDTO plantFacilitiesDTO,
                        String code);

        public void deletePlantFacilitiesByCode(String code);

        // Tọa độ trên bản đồ
        public List<CoordinatesDTO> retrieveAllCoordinates();

        public CoordinatesDTO retrieveCoordinates(String code);

        public CoordinatesDTO updateCoordinates(CoordinatesDTO coordinatesDTO);

        public void deleteCoordinates(String code);

        // PlantSeed
        public List<PlantSeed> getAllPlantSeed();

        public PlantSeed updatePlantSeed(PlantSeedDTO plantSeedDTO, MultipartFile imageFile);

        public PlantSeed getPlantSeedByName(String name);

        public PlantSeed addPlantSeed(PlantSeedDTO plantSeedDTO, MultipartFile imageFile);

        public void deletePlantSeedByName(String name);

        public List<PlantSeed> getAllPlantSeedByFacilitiesCode(String code);

        // PfPsRelationship
        public List<PfPsRelationship> getAllPfPsRelationship();

        public PfPsRelationship updatePfPsRelationship(int id, PfPsRelationshipDTO pfPsRelationshipDTO);

        public PfPsRelationship getPfPsRelationshipById(int id);

        public PfPsRelationship addPfPsRelationship(PfPsRelationshipDTO pfPsRelationshipDTO);

        public void deletePfPsRelationshipById(int id);

        // thống kê
        // thống kê
        public List<FacilitiesQuantity> getQuantityOfFacilitiesBeforeTime(LocalDate date);

        public List<FacilitiesQuantityInMoth> getMonthQuantityFacilitiesWithTime(LocalDate beginDate,
                        LocalDate endDate);

        public List<FacilitiesQuantityInQuarter> getQuarterQuantityOfFacilitiesWithTime(LocalDate startDate,
                        LocalDate endDate);

        public List<FacilitiesQuantityInYear> getYearQuantityOfFacilitiesWithTime(int startYear, int endYear);

}
