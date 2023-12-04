package com.project.forestresourcesmanageapplication.servicesimpl;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.forestresourcesmanageapplication.dtos.AnimalSpeciesDTO;
import com.project.forestresourcesmanageapplication.dtos.AnimalStorageFacilitiesDTO;
import com.project.forestresourcesmanageapplication.dtos.AsfAsRelationshipDTO;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataAlreadyExistsException;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataNotFoundException;
import com.project.forestresourcesmanageapplication.models.Administration;
import com.project.forestresourcesmanageapplication.models.AnimalSpecies;
import com.project.forestresourcesmanageapplication.models.AnimalStorageFacilities;
import com.project.forestresourcesmanageapplication.models.AsfAsRelationship;
import com.project.forestresourcesmanageapplication.models.Fluctuation;
import com.project.forestresourcesmanageapplication.repositories.AnimalSpeciesRepository;
import com.project.forestresourcesmanageapplication.repositories.AnimalStorageFacilitiesRepository;
import com.project.forestresourcesmanageapplication.repositories.AsfAsRelationshipRepository;
import com.project.forestresourcesmanageapplication.repositories.FluctuationRepository;
import com.project.forestresourcesmanageapplication.services.AdminstrationService;
import com.project.forestresourcesmanageapplication.services.AnimalStorageFacilitiesService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalStorageFacilitiesServiceImpl implements AnimalStorageFacilitiesService{
    private final AnimalStorageFacilitiesRepository animalStorageFacilitiesRepository;
    private final AnimalSpeciesRepository animalSpeciesRepository;
    private final AsfAsRelationshipRepository asfAsRelationshipRepository;
    private final FluctuationRepository fluctuationRepository;
    private final AdminstrationService adminstrationService;

    //-----------------------CƠ SỞ LƯU TRỮ ĐỘNG VẬT---------------------------
    @Override
    public List<AnimalStorageFacilities> getAllAnimalStorageFacilities() {
        return animalStorageFacilitiesRepository.findAll();
    }

    @Override
    @Transactional
    public AnimalStorageFacilities updateAnimalStorageFacilities(String code,AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO) {
        AnimalStorageFacilities animalStorageFacilitiesExisting = this.getAnimalStorageFacilitiesByCode(code);

        //kiểm tra tên
        if(!animalStorageFacilitiesExisting.getName().equals(animalStorageFacilitiesDTO.getName())){
            Optional<AnimalStorageFacilities> tmp = this.animalStorageFacilitiesRepository.findByName(animalStorageFacilitiesDTO.getName());
            if(tmp.isPresent()){
                throw new DataAlreadyExistsException("Đã có cơ sở lưu trữ động vật với tên = "+animalStorageFacilitiesDTO.getName());
            }
            animalStorageFacilitiesExisting.setName(animalStorageFacilitiesDTO.getName());
        }
        
        //kiểm tra đơn vị hành chính
        if(!animalStorageFacilitiesExisting.getAdministration().getCode().equals(animalStorageFacilitiesDTO.getAdminstrationCode())){
            try {
                 Administration administration = this.adminstrationService.retrieveAdministrationByCode(animalStorageFacilitiesDTO.getAdminstrationCode());
                 animalStorageFacilitiesExisting.setAdministration(administration);
            }catch(Exception exception){
                throw new DataNotFoundException("Không tìm thấy cơ sở hành chính với code = "+ animalStorageFacilitiesDTO.getAdminstrationCode());
            }
        }

        animalStorageFacilitiesExisting.setCapacity(animalStorageFacilitiesDTO.getCapacity());
        animalStorageFacilitiesExisting.setDate(animalStorageFacilitiesDTO.getDate());
        animalStorageFacilitiesExisting.setDetail(animalStorageFacilitiesDTO.getDetail());

        return animalStorageFacilitiesRepository.save(animalStorageFacilitiesExisting);
    }

    @Override
    public AnimalStorageFacilities getAnimalStorageFacilitiesByCode(String code) {
        return animalStorageFacilitiesRepository.findById(code)
        .orElseThrow( () -> new DataNotFoundException("Không tìm thấy cơ sở lưu trữ động vật với code = "+code));
    }

    @Override
    public AnimalStorageFacilities addAnimalStorageFacilities(AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO,String code) {
        AnimalStorageFacilities animalStorageFacilities = new AnimalStorageFacilities();
        //kiểm tra code 
        Optional<AnimalStorageFacilities> tmp1 = this.animalStorageFacilitiesRepository.findById(code);
            if(tmp1.isPresent()){
                throw new DataAlreadyExistsException("Đã có cơ sở lưu trữ động vật với code = "+code);
            }
        //kiểm tra tên
        Optional<AnimalStorageFacilities> tmp2 = this.animalStorageFacilitiesRepository.findByName(animalStorageFacilitiesDTO.getName());
            if(tmp2.isPresent()){
                throw new DataAlreadyExistsException("Đã có cơ sở lưu trữ động vật với tên = "+animalStorageFacilitiesDTO.getName());
            }
        //kiểm tra đơn vị hành chính
        try {
                 Administration administration = this.adminstrationService.retrieveAdministrationByCode(animalStorageFacilitiesDTO.getAdminstrationCode());
                 animalStorageFacilities.setAdministration(administration);
            }catch(Exception exception){
                throw new DataNotFoundException("Không tìm thấy cơ sở hành chính với code = "+ animalStorageFacilitiesDTO.getAdminstrationCode());
            }
        animalStorageFacilities.setCode(code);
        animalStorageFacilities.setName(animalStorageFacilitiesDTO.getName());
        animalStorageFacilities.setCapacity(animalStorageFacilitiesDTO.getCapacity());
        animalStorageFacilities.setDate(animalStorageFacilitiesDTO.getDate());
        animalStorageFacilities.setDetail(animalStorageFacilitiesDTO.getDetail());

        return animalStorageFacilitiesRepository.save(animalStorageFacilities);
    }

    @Override
    public void deleteAnimalStorageFacilitiesByCode(String code) {
        AnimalStorageFacilities animalStorageFacilities = this.getAnimalStorageFacilitiesByCode(code);
        this.animalStorageFacilitiesRepository.deleteById(animalStorageFacilities.getCode());
    }


    //-----------------------LOÀI ĐỘNG VẬT----------------------------
    @Override
    public List<AnimalSpecies> getAllAnimalSpecies() {
        return this.animalSpeciesRepository.findAll();
    }

    @Override
    public AnimalSpecies getAnimalSpeciesByName(String name) {
        return this.animalSpeciesRepository.findById(name)
        .orElseThrow( () -> new DataNotFoundException("Không tìm thấy loài động vật với tên = "+name));
    }

    @Override
    public AnimalSpecies updateAnimalSpecies(String name, AnimalSpeciesDTO animalSpeciesDTO) {
        AnimalSpecies animalSpecies = this.getAnimalSpeciesByName(name);

        animalSpecies.setAnimalType(animalSpeciesDTO.getAnimalType());
        animalSpecies.setMainFood(animalSpeciesDTO.getMainFood());
        animalSpecies.setMainDisease(animalSpeciesDTO.getMainDisease());
        animalSpecies.setLongevity(animalSpeciesDTO.getLongevity());

        Fluctuation fluctuation = this.getFluctuationById(animalSpeciesDTO.getFluctuationId());
        animalSpecies.setFluctuation(fluctuation);
        return animalSpeciesRepository.save(animalSpecies);
    }

    @Override
    public AnimalSpecies addAnimalSpecies(AnimalSpeciesDTO animalSpeciesDTO, String name) {
        AnimalSpecies animalSpecies = new AnimalSpecies();

        //kiểm tra tên
        Optional<AnimalSpecies> tmp = this.animalSpeciesRepository.findById(name);
        if(tmp.isPresent()){
            throw new DataAlreadyExistsException("Đã tồn tại loài động vật với tên = "+ name);
        }
        animalSpecies.setName(name);
        animalSpecies.setAnimalType(animalSpeciesDTO.getAnimalType());
        animalSpecies.setMainFood(animalSpeciesDTO.getMainFood());
        animalSpecies.setMainDisease(animalSpeciesDTO.getMainDisease());
        animalSpecies.setLongevity(animalSpeciesDTO.getLongevity());

        Fluctuation fluctuation = this.getFluctuationById(animalSpeciesDTO.getFluctuationId());
        animalSpecies.setFluctuation(fluctuation);
        return animalSpeciesRepository.save(animalSpecies);
    }

    @Override
    public void deleteAnimalSpeciesByName(String name) {
        AnimalSpecies animalSpecies = this.getAnimalSpeciesByName(name);
        this.animalSpeciesRepository.deleteById(animalSpecies.getName());
    }


    //--------------------------LOẠI BIẾN ĐỘNG-------------------------
    @Override
    public List<Fluctuation> getAllFluctuations() {
        return this.fluctuationRepository.findAll();
    }


    @Override
    public Fluctuation getFluctuationById(int id) {
        return this.fluctuationRepository.findById(id)
        .orElseThrow( () -> new DataNotFoundException("Không tìm thấy loại biến động với id = "+id));
    }


    //---------------------QUAN HỆ CSLTDV VÀ LOÀI ĐỘNG VẬT---------------------------
    @Override
    public List<AsfAsRelationship> getAllAsfAsRelationships() {
        return this.asfAsRelationshipRepository.findAll();
    }

    @Override
    public AsfAsRelationship updateAsfAsRelationship(int id, AsfAsRelationshipDTO asfAsRelationshipDTO ) {
        AsfAsRelationship asfAsRelationship = this.getAsfAsRelationshipById(id);
        //kiểm tra tên CSLTDV và tên loài động vật
        AnimalStorageFacilities animalStorageFacilities = this.getAnimalStorageFacilitiesByCode(asfAsRelationshipDTO.getCodeASF());
        AnimalSpecies animalSpecies = this.getAnimalSpeciesByName(asfAsRelationshipDTO.getNameAS());

        asfAsRelationship.setAnimalStorageFacilities(animalStorageFacilities);
        asfAsRelationship.setAnimalSpecies(animalSpecies);
        asfAsRelationship.setQuantity(asfAsRelationshipDTO.getQuantity());
        asfAsRelationship.setDate(asfAsRelationshipDTO.getDate());

        return asfAsRelationshipRepository.save(asfAsRelationship);
    }

    @Override
    public AsfAsRelationship getAsfAsRelationshipById(int id) {
        return this.asfAsRelationshipRepository.findById(id)
        .orElseThrow( () -> new DataNotFoundException("Không tồn tại bảng quan hệ Asf-As với id = "+id));
    }

    @Override
    public AsfAsRelationship addAsfAsRelationship(AsfAsRelationshipDTO asfAsRelationshipDTO) {
        AsfAsRelationship asfAsRelationship = new AsfAsRelationship();
        //kiểm tra tên CSLTDV và tên loài động vật
        AnimalStorageFacilities animalStorageFacilities = this.getAnimalStorageFacilitiesByCode(asfAsRelationshipDTO.getCodeASF());
        AnimalSpecies animalSpecies = this.getAnimalSpeciesByName(asfAsRelationshipDTO.getNameAS());

        asfAsRelationship.setAnimalStorageFacilities(animalStorageFacilities);
        asfAsRelationship.setAnimalSpecies(animalSpecies);
        asfAsRelationship.setQuantity(asfAsRelationshipDTO.getQuantity());
        asfAsRelationship.setDate(asfAsRelationshipDTO.getDate());

        return asfAsRelationshipRepository.save(asfAsRelationship);
    }

    @Override
    public void deleteAsfAsRelationshipById(int id) {
        AsfAsRelationship asfAsRelationship = this.getAsfAsRelationshipById(id);
        this.asfAsRelationshipRepository.deleteById(asfAsRelationship.getId());
    }


    //--------------------------THỐNG KÊ--------------------------------
    // @Override
    // public List<AsfAsRelationship> getAsfAsRelationshipWithYear(Date date) {
    //     return this.getAsfAsRelationshipWithYear(date);
    // }
}
