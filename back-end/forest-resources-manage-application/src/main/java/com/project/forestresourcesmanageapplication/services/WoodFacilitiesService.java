package com.project.forestresourcesmanageapplication.services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.forestresourcesmanageapplication.dtos.CoordinatesDTO;
import com.project.forestresourcesmanageapplication.dtos.OperationFormDTO;
import com.project.forestresourcesmanageapplication.dtos.ProductionTypeDTO;
import com.project.forestresourcesmanageapplication.dtos.WfPtRelationshipDTO;
import com.project.forestresourcesmanageapplication.dtos.WoodFacilitiesDTO;
import com.project.forestresourcesmanageapplication.models.OperationForm;
import com.project.forestresourcesmanageapplication.models.ProductionType;
import com.project.forestresourcesmanageapplication.models.WfPtRelationship;
import com.project.forestresourcesmanageapplication.models.WoodFacilities;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantity;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInMoth;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInQuarter;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInYear;
import com.project.forestresourcesmanageapplication.responses.FacilityQuantity;

public interface WoodFacilitiesService {
        // WoodFacilities
        public List<WoodFacilities> getAllWoodFacilities();

        public WoodFacilities updateWoodFacilities(String code,
                        WoodFacilitiesDTO woodFacilitiesDTO);

        public WoodFacilities getWoodFacilitiesByCode(String code);

        public WoodFacilities addWoodFacilities(WoodFacilitiesDTO woodFacilitiesDTO,
                        String code);

        public void deleteWoodFacilitiesByCode(String code);

        // Tọa độ trên bản đồ
        public List<CoordinatesDTO> retrieveAllCoordinates();

        public CoordinatesDTO retrieveCoordinates(String code);

        public CoordinatesDTO updateCoordinates(CoordinatesDTO coordinatesDTO);

        public void deleteCoordinates(String code);

        // ProductionType - Loại hình sản xuất
        public List<ProductionType> getAllProductionType();

        public ProductionType updateProductionType(ProductionTypeDTO productionTypeDTO, MultipartFile imageFile);

        public ProductionType getProductionTypeByWoodName(String woodType);

        public ProductionType addProductionType(ProductionTypeDTO productionTypeDTO, MultipartFile imageFile);

        public void deleteProductionTypeByWoodName(String name);

        public List<ProductionType> getAllProductionTypeByFacilitiesCode(String code);

        // OperationForm
        public List<OperationForm> getAllOperationForm();

        public OperationForm updateOperationForm(String name,
                        OperationFormDTO operationFormDTO);

        public OperationForm getOperationFormByName(String name);

        public OperationForm addOperationForm(OperationFormDTO operationFormDTO,
                        String name);

        public void deleteOperationFormByName(String name);

        // WfPtRelationship
        public List<WfPtRelationship> getAllWfPtRelationship();

        public WfPtRelationship updateWfPtRelationship(int id, WfPtRelationshipDTO wfPtRelationshipDTO);

        public WfPtRelationship getWfPtRelationshipById(int id);

        public WfPtRelationship addWfPtRelationship(WfPtRelationshipDTO wfPtRelationshipDTO);

        public void deleteWfPtRelationshipById(int id);

        // Thống kê
        public List<FacilitiesQuantity> getQuantityOfFacilitiesBeforeTime(LocalDate date);

        public List<FacilitiesQuantityInMoth> getMonthQuantityFacilitiesWithTime(LocalDate beginDate,
                        LocalDate endDate);

        public List<FacilitiesQuantityInQuarter> getQuarterQuantityOfFacilitiesWithTime(LocalDate startDate,
                        LocalDate endDate);

        public List<FacilitiesQuantityInYear> getYearQuantityOfFacilitiesWithTime(int startYear, int endYear);

        public HashMap<String, List<FacilityQuantity>> getAllQuantityOfAnimal(Date date);
        // test

}
