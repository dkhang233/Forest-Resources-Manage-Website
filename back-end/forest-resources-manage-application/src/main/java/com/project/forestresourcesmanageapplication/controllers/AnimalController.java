package com.project.forestresourcesmanageapplication.controllers;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.forestresourcesmanageapplication.dtos.AnimalSpeciesDTO;
import com.project.forestresourcesmanageapplication.dtos.AnimalStorageFacilitiesDTO;
import com.project.forestresourcesmanageapplication.models.AnimalSpecies;
import com.project.forestresourcesmanageapplication.models.AnimalStorageFacilities;
import com.project.forestresourcesmanageapplication.models.AsfAsRelationship;
import com.project.forestresourcesmanageapplication.models.Fluctuation;
import com.project.forestresourcesmanageapplication.responses.AnimalQuantity;
import com.project.forestresourcesmanageapplication.responses.MonthQuantity;
import com.project.forestresourcesmanageapplication.responses.QuarterQuantity;
import com.project.forestresourcesmanageapplication.responses.YearQuantity;
import com.project.forestresourcesmanageapplication.services.AnimalStorageFacilitiesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/animal-storage-facilities")
@RequiredArgsConstructor
public class AnimalController {
    private final AnimalStorageFacilitiesService animalStorageFacilitiesService;

    //------------------------CƠ SỞ LƯU TRỮ ĐỘNG VẬT-----------------------------
    @GetMapping("")
    public ResponseEntity<List<AnimalStorageFacilities>> getAllAnimalStorageFacilities(){
        List<AnimalStorageFacilities> listAnimalStorageFacilities = this.animalStorageFacilitiesService.getAllAnimalStorageFacilities();
        return ResponseEntity.ok(listAnimalStorageFacilities);
    }

    @GetMapping("/{code}")
    public ResponseEntity<AnimalStorageFacilities> getAnimalStorageFacilitiesByCode(@PathVariable String code){
        AnimalStorageFacilities animalStorageFacilities = this.animalStorageFacilitiesService.getAnimalStorageFacilitiesByCode(code);
        return ResponseEntity.ok(animalStorageFacilities);
    }

    @PostMapping("/{code}")
    public ResponseEntity<AnimalStorageFacilities> addAnimalStorageFacilities(@PathVariable String code, @RequestBody AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO){
        AnimalStorageFacilities animalStorageFacilities = this.animalStorageFacilitiesService.addAnimalStorageFacilities(animalStorageFacilitiesDTO,code);
        return ResponseEntity.status(HttpStatus.CREATED).body(animalStorageFacilities);
    }

    @PutMapping("/{code}")
    public ResponseEntity<AnimalStorageFacilities> updateAnimalStorageFacilities(@PathVariable String code, @RequestBody AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO){
        AnimalStorageFacilities animalStorageFacilities = this.animalStorageFacilitiesService.updateAnimalStorageFacilities(code, animalStorageFacilitiesDTO);
        return ResponseEntity.ok(animalStorageFacilities);
    }

    @DeleteMapping("{code}")
    public ResponseEntity<Void> deleteAnimalStorageFacilities(@PathVariable String code){
        this.animalStorageFacilitiesService.deleteAnimalStorageFacilitiesByCode(code);
        return ResponseEntity.ok().build();
    }

//---------------------------LOÀI ĐỘNG VẬT--------------------------
    
    @GetMapping("/species")
    public ResponseEntity<List<AnimalSpecies>> getAllAnimalSpecies(){
        List<AnimalSpecies> listAnimalSpecies = this.animalStorageFacilitiesService.getAllAnimalSpecies();
        return ResponseEntity.ok(listAnimalSpecies);
    }

    @GetMapping("/species/{name}")
    public ResponseEntity<AnimalSpecies> getAnimalSpeciesByName(@PathVariable String name){
        AnimalSpecies animalSpecies = this.animalStorageFacilitiesService.getAnimalSpeciesByName(name);
        return ResponseEntity.ok(animalSpecies);
    }

    @PostMapping("/species/{name}")
    public ResponseEntity<AnimalSpecies> addAnimalSpecies(@PathVariable String name, @RequestBody AnimalSpeciesDTO animalSpeciesDTO){
        AnimalSpecies animalSpecies = this.animalStorageFacilitiesService.addAnimalSpecies(animalSpeciesDTO,name);
        return ResponseEntity.status(HttpStatus.CREATED).body(animalSpecies);
    }

    @PutMapping("/species/{name}")
    public ResponseEntity<AnimalSpecies> updateAnimalSpecies(@PathVariable String name, @RequestBody AnimalSpeciesDTO animalSpeciesDTO){
        AnimalSpecies animalSpecies = this.animalStorageFacilitiesService.updateAnimalSpecies(name, animalSpeciesDTO);
        return ResponseEntity.ok(animalSpecies);
    }

    @DeleteMapping("/species/{name}")
    public ResponseEntity<Void> deleteAnimalSpecies(@PathVariable String name){
        this.animalStorageFacilitiesService.deleteAnimalSpeciesByName(name);
        return ResponseEntity.ok().build();
    }

    // //lấy tất cả loài động vật trong 1 cơ sở lưu trữ
    // @GetMapping("/species/facilities/{facilitiesCode}")
    // public ResponseEntity<List<AnimalSpecies>> getAnimalSpeciesByFacilitiesCode(@PathVariable(value = "facilitiesCode") String code){
    //     List<AnimalSpecies> listAnimalSpecies = this.animalStorageFacilitiesService.getAllAnimalSpeciesByFacilitiesCode(code);
    //     return ResponseEntity.ok(listAnimalSpecies);
    // }
    //lấy số lượng của 1 con vật trong 1 cơ sở lưu trữ
    //  @GetMapping("/species/facilities/{facilitiesCode}/{name}")
    // public Long getQuantityAnimalByCode(@PathVariable(value = "facilitiesCode") String code,@PathVariable(value = "name") String name){
    //     return this.animalStorageFacilitiesService.getQuantityAnimalOfFacilitiesCode(code,name);
    // }


    

//---------------------------LOẠI BIẾN ĐỘNG--------------------------

    @GetMapping("/fluctuation")
    public ResponseEntity<List<Fluctuation>> getAllFluctuation(){
        List<Fluctuation> fluctuations = this.animalStorageFacilitiesService.getAllFluctuations();
        return ResponseEntity.ok(fluctuations);
    }

    @GetMapping("/fluctuation/{id}")
    public ResponseEntity<Fluctuation> getFluctuationById(@PathVariable int id){
        Fluctuation fluctuation = this.animalStorageFacilitiesService.getFluctuationById(id);
        return ResponseEntity.ok(fluctuation);
    }
    
//--------------------------THỐNG KÊ DỮ LIỆU VỀ SỐ LƯỢNG----------------------
    //thống kê số lượng của tất cả các loài động vật trong 1 cơ sở
    @GetMapping("/species/facilities/{facilitiesCode}")
    public ResponseEntity<List<AnimalQuantity>> getAllQuantityAnimalByCode(@PathVariable(value = "facilitiesCode") String code){
        List<AnimalQuantity> animalQuantities = this.animalStorageFacilitiesService.getQuantityOfAllAnimalByFacilitiesCode(code);
        return ResponseEntity.ok(animalQuantities);
    }

    //thống kê số lượng theo tháng của 1 con vật trong 1 cơ sở
    @GetMapping("/species/facilities/month/{facilitiesCode}/{animalName}/{year}")
    public ResponseEntity<List<MonthQuantity>> getQuantityAnimalWithMonth(@PathVariable(value = "facilitiesCode") String code, @PathVariable(value = "animalName") String name, @PathVariable(value = "year") int year){
        List<MonthQuantity> monthQuantities = this.animalStorageFacilitiesService.getQuantityAnimalWithMonthOfYear(code,name,year);
        return ResponseEntity.ok(monthQuantities);
    }

    //thống kê số lượng theo quý của 1 con vật trong 1 cơ sở
    @GetMapping("/species/facilities/quarter/{facilitiesCode}/{animalName}/{year}")
    public ResponseEntity<List<QuarterQuantity>> getQuantityAnimalWithQuarter(@PathVariable(value = "facilitiesCode") String code, @PathVariable(value = "animalName") String name, @PathVariable(value = "year") int year){
        List<QuarterQuantity> quarterQuantities = this.animalStorageFacilitiesService.getQuantityAnimalWithQuarterOfYear(code,name,year);
        return ResponseEntity.ok(quarterQuantities);
    }

    //thống kê số lượng theo năm của 1 con vật trong 1 cơ sở (2013->2017)
    @GetMapping("/species/facilities/year/{facilitiesCode}/{animalName}")
    public ResponseEntity<List<YearQuantity>> getQuantityAnimalWithYear(@PathVariable(value = "facilitiesCode") String code, @PathVariable(value = "animalName") String name){
        List<YearQuantity> yearQuantities = this.animalStorageFacilitiesService.getQuantityAnimalWithYear(code,name);
        return ResponseEntity.ok(yearQuantities);
    }

    //lấy tất cả dữ liệu trong 1 khoảng thời gian
    @GetMapping("/statistical/{startDate}/{endDate}")
    public ResponseEntity<List<AsfAsRelationship>> getAsfAsRelationship(@PathVariable(value ="startDate") Date startDate,@PathVariable(value = "endDate") Date endDate){
        List<AsfAsRelationship> asRelationships = this.animalStorageFacilitiesService.getAsfAsRelationshipWithTime(startDate,endDate);
        return ResponseEntity.ok(asRelationships);
    }

    //lấy tất cả dữ liệu trong 1 năm
    @GetMapping("/statistical/{year}")
    public ResponseEntity<List<AsfAsRelationship>> getAsfAsRelationshipInYear(@PathVariable int year){
        List<AsfAsRelationship> asRelationships = this.animalStorageFacilitiesService.getAsfAsRelationshipInYear(year);
        return ResponseEntity.ok(asRelationships);
    }

    //lấy dữ liệu của 1 cơ sở lưu trữ trong 1 năm
    @GetMapping("/statistical/{facilitiesCode}/{year}")
    public ResponseEntity<List<AsfAsRelationship>> getAsfAsRelationshipByFacilitiesIdInYear(@PathVariable(value = "facilitiesCode") String code,@PathVariable(value = "year") int year){
        List<AsfAsRelationship> asRelationships = this.animalStorageFacilitiesService.getAsfAsRelationshipByFacilitiesInYear(code,year);
        return ResponseEntity.ok(asRelationships);
    }

}
