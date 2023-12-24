package com.project.forestresourcesmanageapplication.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import java.time.LocalDate;
import java.util.HashMap;
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

import com.project.forestresourcesmanageapplication.dtos.AnimalSpeciesDTO;
import com.project.forestresourcesmanageapplication.dtos.AnimalStorageFacilitiesDTO;
import com.project.forestresourcesmanageapplication.dtos.AsfAsRelationshipDTO;
import com.project.forestresourcesmanageapplication.dtos.CoordinatesDTO;
import com.project.forestresourcesmanageapplication.models.AnimalSpecies;
import com.project.forestresourcesmanageapplication.models.AnimalStorageFacilities;
import com.project.forestresourcesmanageapplication.models.AsfAsRelationship;
import com.project.forestresourcesmanageapplication.models.Fluctuation;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInMoth;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInQuarter;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInYear;
import com.project.forestresourcesmanageapplication.responses.FacilityQuantity;
import com.project.forestresourcesmanageapplication.services.AnimalStorageFacilitiesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/animal-storage-facilities")
@RequiredArgsConstructor
public class AnimalController {
    private final AnimalStorageFacilitiesService animalStorageFacilitiesService;

    // ------------------------CƠ SỞ LƯU TRỮ ĐỘNG VẬT-----------------------------
    @GetMapping("")
    public ResponseEntity<List<AnimalStorageFacilities>> getAllAnimalStorageFacilities() {
        List<AnimalStorageFacilities> listAnimalStorageFacilities = this.animalStorageFacilitiesService
                .getAllAnimalStorageFacilities();
        return ResponseEntity.ok(listAnimalStorageFacilities);
    }

    @GetMapping("/{code}")
    public ResponseEntity<AnimalStorageFacilities> getAnimalStorageFacilitiesByCode(@PathVariable String code) {
        AnimalStorageFacilities animalStorageFacilities = this.animalStorageFacilitiesService
                .getAnimalStorageFacilitiesByCode(code);
        return ResponseEntity.ok(animalStorageFacilities);
    }

    @PostMapping("/{code}")
    public ResponseEntity<AnimalStorageFacilities> addAnimalStorageFacilities(@PathVariable String code,
            @RequestBody AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO) {
        AnimalStorageFacilities animalStorageFacilities = this.animalStorageFacilitiesService
                .addAnimalStorageFacilities(animalStorageFacilitiesDTO, code);
        return ResponseEntity.status(HttpStatus.CREATED).body(animalStorageFacilities);
    }

    @PutMapping("/{code}")
    public ResponseEntity<AnimalStorageFacilities> updateAnimalStorageFacilities(@PathVariable String code,
            @RequestBody AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO) {
        AnimalStorageFacilities animalStorageFacilities = this.animalStorageFacilitiesService
                .updateAnimalStorageFacilities(code, animalStorageFacilitiesDTO);
        return ResponseEntity.ok(animalStorageFacilities);
    }

    @DeleteMapping("{code}")
    public ResponseEntity<Void> deleteAnimalStorageFacilities(@PathVariable String code) {
        this.animalStorageFacilitiesService.deleteAnimalStorageFacilitiesByCode(code);
        return ResponseEntity.ok().build();
    }

    // --------------------------Tọa độ trên bản đồ---------------------
    @GetMapping("/coordinates")
    public ResponseEntity<?> getAllCoordinates() {
        List<CoordinatesDTO> coordinatesDTOs = this.animalStorageFacilitiesService.retrieveAllCoordinates();
        return ResponseEntity.ok(coordinatesDTOs);
    }

    @PutMapping("/coordinates")
    public ResponseEntity<?> updateCoordinates(@RequestBody CoordinatesDTO coordinatesDTO) {
        coordinatesDTO = this.animalStorageFacilitiesService.updateCoordinates(coordinatesDTO);
        return ResponseEntity.ok(coordinatesDTO);
    }

    @DeleteMapping("/coordinates/{code}")
    public ResponseEntity<?> deleteCoordinates(@PathVariable(name = "code") String code) {
        this.animalStorageFacilitiesService.deleteCoordinates(code);
        return ResponseEntity.ok().build();
    }

    // ---------------------------LOÀI ĐỘNG VẬT--------------------------

    @GetMapping("/species")
    public ResponseEntity<List<AnimalSpecies>> getAllAnimalSpecies() {
        List<AnimalSpecies> listAnimalSpecies = this.animalStorageFacilitiesService.getAllAnimalSpecies();
        return ResponseEntity.ok(listAnimalSpecies);
    }

    @GetMapping("/species/{name}")
    public ResponseEntity<AnimalSpecies> getAnimalSpeciesByName(@PathVariable String name) {
        AnimalSpecies animalSpecies = this.animalStorageFacilitiesService.getAnimalSpeciesByName(name);
        return ResponseEntity.ok(animalSpecies);
    }

    @GetMapping(value = "/species/images/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable String fileName) throws IOException {
        Path uploadPath = Path.of("uploads", fileName);
        InputStream in = Files.newInputStream(uploadPath);
        return IOUtils.toByteArray(in);
    }

    @PostMapping("/species/{name}")
    public ResponseEntity<AnimalSpecies> addAnimalSpecies(@PathVariable String name,
            @RequestPart(name = "body") AnimalSpeciesDTO animalSpeciesDTO,
            @RequestParam(name = "file-image", required = false) MultipartFile imageFile) {
        AnimalSpecies animalSpecies = this.animalStorageFacilitiesService.addAnimalSpecies(animalSpeciesDTO, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(animalSpecies);
    }

    @PutMapping("/species/{name}")
    public ResponseEntity<AnimalSpecies> updateAnimalSpecies(@PathVariable String name,
            @RequestPart(name = "body") AnimalSpeciesDTO animalSpeciesDTO,
            @RequestParam(name = "file-image", required = false) MultipartFile imageFile) {
        AnimalSpecies animalSpecies = this.animalStorageFacilitiesService.updateAnimalSpecies(animalSpeciesDTO,
                imageFile);
        return ResponseEntity.ok(animalSpecies);
    }

    @DeleteMapping("/species/{name}")
    public ResponseEntity<Void> deleteAnimalSpecies(@PathVariable String name) {
        this.animalStorageFacilitiesService.deleteAnimalSpeciesByName(name);
        return ResponseEntity.ok().build();
    }

    // //lấy tất cả loài động vật trong 1 cơ sở lưu trữ
    // @GetMapping("/species/facilities/{facilitiesCode}")
    // public ResponseEntity<List<AnimalSpecies>>
    // getAnimalSpeciesByFacilitiesCode(@PathVariable(value = "facilitiesCode")
    // String code){
    // List<AnimalSpecies> listAnimalSpecies =
    // this.animalStorageFacilitiesService.getAllAnimalSpeciesByFacilitiesCode(code);
    // return ResponseEntity.ok(listAnimalSpecies);
    // }
    // lấy số lượng của 1 con vật trong 1 cơ sở lưu trữ
    // @GetMapping("/species/facilities/{facilitiesCode}/{name}")
    // public Long getQuantityAnimalByCode(@PathVariable(value = "facilitiesCode")
    // String code,@PathVariable(value = "name") String name){
    // return
    // this.animalStorageFacilitiesService.getQuantityAnimalOfFacilitiesCode(code,name);
    // }

    // ---------------------------LOẠI BIẾN ĐỘNG--------------------------

    @GetMapping("/fluctuation")
    public ResponseEntity<List<Fluctuation>> getAllFluctuation() {
        List<Fluctuation> fluctuations = this.animalStorageFacilitiesService.getAllFluctuations();
        return ResponseEntity.ok(fluctuations);
    }

    @GetMapping("/fluctuation/{id}")
    public ResponseEntity<Fluctuation> getFluctuationById(@PathVariable int id) {
        Fluctuation fluctuation = this.animalStorageFacilitiesService.getFluctuationById(id);
        return ResponseEntity.ok(fluctuation);
    }

    // --------------------------THỐNG KÊ DỮ LIỆU VỀ SỐ LƯỢNG----------------------

    // thống kê số lượng của tất cả các loài động vật trong 1 cơ sở
    // @GetMapping("/species/facilities/{facilitiesCode}")
    // public ResponseEntity<List<AnimalQuantity>>
    // getAllQuantityAnimalByCode(@PathVariable(value = "facilitiesCode") String
    // code){
    // List<AnimalQuantity> animalQuantities =
    // this.animalStorageFacilitiesService.getQuantityOfAllAnimalByFacilitiesCode(code);
    // return ResponseEntity.ok(animalQuantities);
    // }

    // // thống kê số lượng theo tháng của 1 con vật trong 1 cơ sở
    // @GetMapping("/species/facilities/month/{facilitiesCode}/{animalName}/{year}")
    // public ResponseEntity<List<MonthQuantity>> getQuantityAnimalWithMonth(
    // @PathVariable(value = "facilitiesCode") String code, @PathVariable(value =
    // "animalName") String name,
    // @PathVariable(value = "year") int year) {
    // List<MonthQuantity> monthQuantities =
    // this.animalStorageFacilitiesService.getQuantityAnimalWithMonthOfYear(code,
    // name, year);
    // return ResponseEntity.ok(monthQuantities);
    // }

    // ------Thống kê số lượng theo tháng trong tất cả cơ sở-------
    @GetMapping("/species/facilities/month/{beginMonth}/{endMonth}")
    public ResponseEntity<List<FacilitiesQuantityInMoth>> getQuantityOfFacilitiesWithTime(
            @PathVariable(value = "beginMonth") LocalDate beginMonth,
            @PathVariable(value = "endMonth") LocalDate endMonth) {
        List<FacilitiesQuantityInMoth> facilitiesQuantities = this.animalStorageFacilitiesService
                .getQuantityOfFacilitiesWithTime(beginMonth, endMonth);
        return ResponseEntity.ok(facilitiesQuantities);
    }

    // -------Thống kê số lượng theo quý trong tất cả cơ sở----------
    @GetMapping("/species/facilities/quarter/{startDate}/{endDate}")
    public ResponseEntity<List<FacilitiesQuantityInQuarter>> getQuarterQuantityAnimalWithTime(
            @PathVariable(value = "startDate") LocalDate startDate,
            @PathVariable(value = "endDate") LocalDate endDate) {
        List<FacilitiesQuantityInQuarter> facilitiesQuantityInQuarters = this.animalStorageFacilitiesService
                .getQuarterQuantityOfFacilitiesWithTime(startDate, endDate);
        return ResponseEntity.ok(facilitiesQuantityInQuarters);
    }

    // -------Thống kê số lượng theo nắm trong tất cả cơ sở----------
    @GetMapping("/species/facilities/year/{startYear}/{endYear}")
    public ResponseEntity<List<FacilitiesQuantityInYear>> getYearQuantityAnimalWithYear(
            @PathVariable(value = "startYear") int startYear,
            @PathVariable(value = "endYear") int endYear) {
        List<FacilitiesQuantityInYear> facilitiesQuantityInYears = this.animalStorageFacilitiesService
                .getYearQuantityOfFacilitiesWithTime(startYear, endYear);
        return ResponseEntity.ok(facilitiesQuantityInYears);
    }

    // @GetMapping("/species/facilities/quarter/{year}")
    // public ResponseEntity<List<AnimalQuarterQuantity>>
    // getQuarterQuantityAnimalWithYear(
    // @PathVariable(value = "year") int year) {
    // List<AnimalQuarterQuantity> animalQuarterQuantities =
    // this.animalStorageFacilitiesService
    // .getQuarterQuantityOfFacilities(year);
    // return ResponseEntity.ok(animalQuarterQuantities);
    // }

    // @GetMapping("/species/facilities/month/{year}")
    // public ResponseEntity<List<AnimalMonthQuantity>>
    // getMonthQuantityAnimalWithYear(
    // @PathVariable(value = "year") int year) {
    // List<AnimalMonthQuantity> animalMonthQuantities =
    // this.animalStorageFacilitiesService
    // .getMonthQuantityOfFacilities(year);
    // return ResponseEntity.ok(animalMonthQuantities);
    // }

    // -----------------Thống kê số lượng từng loại động vật theo tháng tại các cơ
    // sở--------------
    @GetMapping("/species/facility-quantity/month/{begin}/{end}")
    public ResponseEntity<?> retrieveQuantiyOfFacilityByMonth(@PathVariable(name = "begin") LocalDate begin,
            @PathVariable(name = "end") LocalDate end) {
        return ResponseEntity.ok("");
    }

    // -----------------Thống kê số lượng từng loại động vật theo quý tại các cơ
    // sở--------------
    @GetMapping("/species/facility-quantity/quarter/{begin}/{end}")
    public ResponseEntity<?> retrieveQuantiyOfFacilityByQuarter(@PathVariable(name = "begin") LocalDate begin,
            @PathVariable(name = "end") LocalDate end) {
        return ResponseEntity.ok("");
    }

    // -----------------Thống kê số lượng từng loại động vật theo năm tại các cơ
    // sở--------------
    @GetMapping("/species/facility-quantity/year/{begin}/{end}")
    public ResponseEntity<?> retrieveQuantiyOfFacilityByYear(@PathVariable(name = "begin") LocalDate begin,
            @PathVariable(name = "end") LocalDate end) {
        return ResponseEntity.ok("");
    }

    // ----------lấy số lượng của các loại động vật trong các cơ sở động vật tại
    // thời điểm
    // hiện tại-------------------
    @GetMapping("/species/facility-quantity/now")
    public ResponseEntity<HashMap<String, List<FacilityQuantity>>> getQuantityOfAllAnimalNow() {
        HashMap<String, List<FacilityQuantity>> animalsQuantities = this.animalStorageFacilitiesService
                .getQuantityOfAllAnimalBeforeTime(LocalDate.now());
        return ResponseEntity.ok(animalsQuantities);
    }

    @PostMapping("/species/facility-quantity/add")
    public ResponseEntity<?> addQuantityOfAnimal(@RequestBody AsfAsRelationshipDTO asfAsRelationshipDTO) {
        AsfAsRelationship asfAsRelationship = this.animalStorageFacilitiesService
                .addAsfAsRelationship(asfAsRelationshipDTO);
        return ResponseEntity.ok(asfAsRelationship);
    }

    // lấy tổng số lượng của các cơ sở động vật trước 1 khoảng thời gian nào đó
    // @GetMapping("/species/facility-quantity/{date}")
    // public ResponseEntity<List<FacilitiesQuantity>> getQuantityOfFacilities(
    // @PathVariable(value = "date") LocalDate date) {
    // List<FacilitiesQuantity> facilitiesQuantities =
    // this.animalStorageFacilitiesService
    // .getQuantityOfFacilitiesBeforeTime(date);
    // return ResponseEntity.ok(facilitiesQuantities);
    // }

    // lấy số lượng của các loại động vật trong các cơ sở động vật trước 1 khoảng
    // thời gian nào đó

    // lấy tổng số lượng của các cơ sở động vật tại thời điểm hiện tại
    // @GetMapping("/species/facility-quantity")
    // public ResponseEntity<List<FacilitiesQuantity>> getQuantityOfFacilitiesNow()
    // {
    // List<FacilitiesQuantity> facilitiesQuantities =
    // this.animalStorageFacilitiesService
    // .getQuantityOfFacilitiesBeforeTime(LocalDate.now());
    // return ResponseEntity.ok(facilitiesQuantities);
    // }

    // lấy số lượng của các loại động vật trong các cơ sở động vật trước 1 khoảng
    // thời gian nào đó

    // @GetMapping("/species/animals-quantity/{date}")
    // public ResponseEntity<List<AnimalsQuantity>>
    // getQuantityOfAllAnimal(@PathVariable(value = "date") Date date) {
    // List<AnimalsQuantity> animalsQuantities = this.animalStorageFacilitiesService
    // .getQuantityOfAllAnimalBeforeTime(date);
    // return ResponseEntity.ok(animalsQuantities);
    // }

    // lấy tất cả dữ liệu trong 1 khoảng thời gian

    // @GetMapping("/statistical/{startDate}/{endDate}")
    // public ResponseEntity<List<AsfAsRelationship>> getAsfAsRelationship(
    // @PathVariable(value = "startDate") Date startDate, @PathVariable(value =
    // "endDate") Date endDate) {
    // List<AsfAsRelationship> asRelationships = this.animalStorageFacilitiesService
    // .getAsfAsRelationshipWithTime(startDate, endDate);
    // return ResponseEntity.ok(asRelationships);
    // }

    // lấy tất cả dữ liệu trong 1 năm
    // @GetMapping("/statistical/{year}")
    // public ResponseEntity<List<AsfAsRelationship>>
    // getAsfAsRelationshipInYear(@PathVariable int year) {
    // List<AsfAsRelationship> asRelationships =
    // this.animalStorageFacilitiesService.getAsfAsRelationshipInYear(year);
    // return ResponseEntity.ok(asRelationships);
    // }

    // lấy dữ liệu của 1 cơ sở lưu trữ trong 1 năm
    // @GetMapping("/statistical/facilities/{facilitiesCode}/{year}")
    // public ResponseEntity<List<AsfAsRelationship>>
    // getAsfAsRelationshipByFacilitiesIdInYear(
    // @PathVariable(value = "facilitiesCode") String code, @PathVariable(value =
    // "year") int year) {
    // List<AsfAsRelationship> asRelationships = this.animalStorageFacilitiesService
    // .getAsfAsRelationshipByFacilitiesInYear(code, year);
    // return ResponseEntity.ok(asRelationships);
    // }
}
