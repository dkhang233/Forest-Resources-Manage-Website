package com.project.forestresourcesmanageapplication.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.forestresourcesmanageapplication.dtos.CoordinatesDTO;
import com.project.forestresourcesmanageapplication.dtos.PlantFacilitiesDTO;
import com.project.forestresourcesmanageapplication.dtos.PlantSeedDTO;
import com.project.forestresourcesmanageapplication.models.PlantFacilities;
import com.project.forestresourcesmanageapplication.models.PlantSeed;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInMoth;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInQuarter;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInYear;
import com.project.forestresourcesmanageapplication.services.PlantFacilitiesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/plant-facilities")
@RequiredArgsConstructor
public class PlantController {
    private final PlantFacilitiesService plantFacilitiesService;

    // ------------------------CƠ SỞ SẢN XUẤT-----------------------------
    @GetMapping("")
    public ResponseEntity<List<PlantFacilities>> getAllPlantFacilities() {
        List<PlantFacilities> list = this.plantFacilitiesService
                .getAllPlantFacilities();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{code}")
    public ResponseEntity<PlantFacilities> getPlantFacilitiesByCode(@PathVariable String code) {
        PlantFacilities plantFacilities = this.plantFacilitiesService
                .getPlantFacilitiesByCode(code);
        return ResponseEntity.ok(plantFacilities);
    }

    @PostMapping("/{code}")
    public ResponseEntity<PlantFacilities> addPlantFacilities(@PathVariable String code,
            @RequestBody PlantFacilitiesDTO plantFacilitiesDTO) {
        PlantFacilities plantFacilities = this.plantFacilitiesService
                .addPlantFacilities(plantFacilitiesDTO, code);
        return ResponseEntity.status(HttpStatus.CREATED).body(plantFacilities);
    }

    @PutMapping("/{code}")
    public ResponseEntity<PlantFacilities> updatePlantFacilities(@PathVariable String code,
            @RequestBody PlantFacilitiesDTO plantFacilitiesDTO) {
        PlantFacilities plantFacilities = this.plantFacilitiesService
                .updatePlantFacilities(code, plantFacilitiesDTO);
        return ResponseEntity.ok(plantFacilities);
    }

    @DeleteMapping("{code}")
    public ResponseEntity<Void> deletePlantFacilities(@PathVariable String code) {
        this.plantFacilitiesService.deletePlantFacilitiesByCode(code);
        return ResponseEntity.ok().build();
    }

    // --------------------------Tọa độ trên bản đồ---------------------
    @GetMapping("/coordinates")
    public ResponseEntity<?> getAllCoordinates() {
        List<CoordinatesDTO> coordinatesDTOs = this.plantFacilitiesService.retrieveAllCoordinates();
        return ResponseEntity.ok(coordinatesDTOs);
    }

    @PutMapping("/coordinates")
    public ResponseEntity<?> updateCoordinates(@RequestBody CoordinatesDTO coordinatesDTO) {
        coordinatesDTO = this.plantFacilitiesService.updateCoordinates(coordinatesDTO);
        return ResponseEntity.ok(coordinatesDTO);
    }

    @DeleteMapping("/coordinates/{code}")
    public ResponseEntity<?> deleteCoordinates(@PathVariable(name = "code") String code) {
        this.plantFacilitiesService.deleteCoordinates(code);
        return ResponseEntity.ok().build();
    }

    // ---------------------------GIỐNG CÂY TRỒNG--------------------------

    @GetMapping("/plant_seed")
    public ResponseEntity<List<PlantSeed>> getPlantSeed() {
        List<PlantSeed> list = this.plantFacilitiesService.getAllPlantSeed();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/plant_seed/{name}")
    public ResponseEntity<PlantSeed> getProductionTypeByWoodName(@PathVariable String name) {
        PlantSeed plantSeed = this.plantFacilitiesService.getPlantSeedByName(name);
        return ResponseEntity.ok(plantSeed);
    }

    @GetMapping(value = "/plant_seed/images/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable String fileName) throws IOException {
        Path uploadPath = Path.of("uploads", fileName);
        InputStream in = Files.newInputStream(uploadPath);
        return IOUtils.toByteArray(in);
    }

    @PostMapping("/plant_seed/{name}")
    public ResponseEntity<PlantSeed> addPlantSeed(@PathVariable String name,
            @RequestPart(name = "body") PlantSeedDTO plantSeedDTO,
            @RequestParam(name = "file-image", required = false) MultipartFile imageFile) {
        PlantSeed plantSeed = this.plantFacilitiesService.addPlantSeed(plantSeedDTO, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(plantSeed);
    }

    @PutMapping("/plant_seed/{name}")
    public ResponseEntity<PlantSeed> updatePlantSeed(@PathVariable String name,
            @RequestPart(name = "body") PlantSeedDTO plantSeedDTO,
            @RequestParam(name = "file-image", required = false) MultipartFile imageFile) {
        PlantSeed plantSeed = this.plantFacilitiesService.updatePlantSeed(plantSeedDTO,
                imageFile);
        return ResponseEntity.ok(plantSeed);
    }

    @DeleteMapping("/plant_seed/{name}")
    public ResponseEntity<Void> deletePlantSeed(@PathVariable String name) {
        this.plantFacilitiesService.deletePlantSeedByName(name);
        return ResponseEntity.ok().build();
    }

    // --------------------------THỐNG KÊ DỮ LIỆU VỀ SỐ LƯỢNG----------------------
    // ------Thống kê số lượng theo tháng trong tất cả cơ sở-------
    @GetMapping("/plant_seed/facilities/month/{beginMonth}/{endMonth}")
    public ResponseEntity<List<FacilitiesQuantityInMoth>> getMonthQuantityOfFacilitiesWithTime(
            @PathVariable(value = "beginMonth") LocalDate beginMonth,
            @PathVariable(value = "endMonth") LocalDate endMonth) {
        List<FacilitiesQuantityInMoth> facilitiesQuantities = this.plantFacilitiesService
                .getMonthQuantityFacilitiesWithTime(beginMonth, endMonth);
        return ResponseEntity.ok(facilitiesQuantities);
    }

    // ------Thống kê số lượng theo quý trong tất cả cơ sở-------
    @GetMapping("/plant_seed/facilities/quarter/{startDate}/{endDate}")
    public ResponseEntity<List<FacilitiesQuantityInQuarter>> getQuarterQuantityOfFacilitiesWithTime(
            @PathVariable(value = "startDate") LocalDate startDate,
            @PathVariable(value = "endDate") LocalDate endDate) {
        List<FacilitiesQuantityInQuarter> facilitiesQuantities = this.plantFacilitiesService
                .getQuarterQuantityOfFacilitiesWithTime(startDate, endDate);
        return ResponseEntity.ok(facilitiesQuantities);
    }

    // -------Thống kê số lượng theo năm trong tất cả cơ sở----------
    @GetMapping("/plant_seed/facilities/year/{startYear}/{endYear}")
    public ResponseEntity<List<FacilitiesQuantityInYear>> getYearQuantityOfFacilitiesWithTime(
            @PathVariable(value = "startYear") int startYear,
            @PathVariable(value = "endYear") int endYear) {
        List<FacilitiesQuantityInYear> facilitiesQuantityInYears = this.plantFacilitiesService
                .getYearQuantityOfFacilitiesWithTime(startYear, endYear);
        return ResponseEntity.ok(facilitiesQuantityInYears);
    }

}
