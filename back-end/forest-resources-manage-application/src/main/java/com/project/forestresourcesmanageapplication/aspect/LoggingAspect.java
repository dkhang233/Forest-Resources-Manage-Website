package com.project.forestresourcesmanageapplication.aspect;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.forestresourcesmanageapplication.dtos.LoginDTO;
import com.project.forestresourcesmanageapplication.models.AccessHistory;
import com.project.forestresourcesmanageapplication.repositories.AccessHistoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    
    private final AccessHistoryRepository accessHistoryRepository;
    private String userName;

    @Pointcut("execution (* com.project.*.controllers.UserController.postMethodName(..))")
    public void login(){}

    //Plant Facilities Pointcut
    @Pointcut("execution (* com.project.*.services.PlantFacilitiesService.addPlantFacilities(..))")
    public void addPlantFacilitiesPointCut(){}

     @Pointcut("execution (* com.project.*.services.PlantFacilitiesService.updatePlantFacilities(..))")
    public void updatePlantFacilitiesPointCut(){}

    @Pointcut("execution (* com.project.*.services.PlantFacilitiesService.deletePlantFacilitiesByCode(..))")
    public void deletePlantFacilitiesPointCut(){}

    @Pointcut("execution (* com.project.*.services.PlantFacilitiesService.addPlantSeed(..))")
    public void addPlantSeedPointCut(){}

    @Pointcut("execution (* com.project.*.services.PlantFacilitiesService.updatePlantSeed(..))")
    public void updatePlantSeedPointCut(){}

    @Pointcut("execution (* com.project.*.services.PlantFacilitiesService.deletePlantSeedByName(..))")
    public void deletePlantSeedPointCut(){}

    //Animal Facilities Pointcut
    @Pointcut("execution (* com.project.*.services.AnimalStorageFacilitiesService.addAnimalStorageFacilities(..))")
    public void addAnimalStorageFacilitiesPointCut(){}

    @Pointcut("execution (* com.project.*.services.AnimalStorageFacilitiesService.updateAnimalStorageFacilities(..))")
    public void updateAnimalStorageFacilitiesPointCut(){}

    @Pointcut("execution (* com.project.*.services.AnimalStorageFacilitiesService.deleteAnimalStorageFacilities(..))")
    public void deleteAnimalStorageFacilitiesPointCut(){}

    @Pointcut("execution (* com.project.*.services.AnimalStorageFacilitiesService.addAnimalSpecies(..))")
    public void addAnimalSpeciesPointCut(){}
    
    @Pointcut("execution (* com.project.*.services.AnimalStorageFacilitiesService.updateAnimalSpecies(..))")
    public void updateAnimalSpeciesPointCut(){}

    @Pointcut("execution (* com.project.*.services.AnimalStorageFacilitiesService.deleteAnimalSpeciesByName(..))")
    public void deleteAnimalSpeciesByNamePointCut(){}

    //Wood Facilities Pointcut
    @Pointcut("execution (* com.project.*.services.WoodFacilitiesService.addWoodFacilities(..))")
    public void addWoodFacilitiesPointCut(){}

     @Pointcut("execution (* com.project.*.services.WoodFacilitiesService.updateWoodFacilities(..))")
    public void updateWoodFacilitiesPointCut(){}

    @Pointcut("execution (* com.project.*.services.WoodFacilitiesService.deleteWoodFacilitiesByCode(..))")
    public void deleteWoodFacilitiesPointCut(){}

    @Pointcut("execution (* com.project.*.services.WoodFacilitiesService.addProductionType(..))")
    public void addProductionTypePointCut(){}

    @Pointcut("execution (* com.project.*.services.WoodFacilitiesService.updateProductionType(..))")
    public void updateProductionTypePointCut(){}

    @Pointcut("execution (* com.project.*.services.WoodFacilitiesService.deleteProductionTypeByWoodName(..))")
    public void deleteProductionTypePointCut(){}

    @Pointcut("execution (* com.project.*.services.WoodFacilitiesService.addOperationForm(..))")
    public void addOperationFormPointCut(){}

    @Pointcut("execution (* com.project.*.services.WoodFacilitiesService.updateOperationForm(..))")
    public void updateOperationFormPointCut(){}

    @Pointcut("execution (* com.project.*.services.WoodFacilitiesService.deleteOperationFormByName(..))")
    public void deleteOperationFormPointCut(){}

    public Object getAccess(ProceedingJoinPoint joinPoint, String methodName) throws Throwable{
        AccessHistory accessHistory = new AccessHistory();

        ObjectMapper mapper = new ObjectMapper();
        
        // String methodName = joinPoint.getSignature().getName();
        long startTime = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
        Date resultdate = new Date(startTime);
        Object[] array = joinPoint.getArgs();

        Object object = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // accessHistory.setUsername(auth.getName());
        accessHistory.setUsername(this.userName);
        accessHistory.setArgument(mapper.writeValueAsString(array));
        accessHistory.setTime(sdf.format(resultdate));
        accessHistory.setPerformance(executionTime);
        accessHistory.setMethod(methodName);
        

        this.accessHistoryRepository.save(accessHistory);
        return object;
    }
    //get username
    @After("execution (* com.project.*.controllers.UserController.postMethodName(..)) && args(user)")
    public String getUserName(JoinPoint joinPoint, LoginDTO user){
        this.userName = user.getUsername();
        return user.getUsername();
    }

    //Plant Facilities
    @Around("addPlantFacilitiesPointCut()")
    public void addPlantFacilities(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Thêm cssx cây trồng");
    }

    @Around("updatePlantFacilitiesPointCut()")
    public void updatePlantFacilities(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Cập nhật cssx cây trồng");
    }

    @Around("deletePlantFacilitiesPointCut()")
    public void deletePlantFacilities(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Xóa cssx cây trồng");
    }

    @Around("addAnimalSpeciesPointCut()")
    public void addPlantSeed(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Thêm giống cây trồng");
    }

    @Around("updatePlantSeedPointCut()")
    public void updatePlantSeed(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Cập nhật giống cây trồng");
    }

    @Around("deletePlantSeedPointCut()")
    public void deletePlantSeed(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Xóa giống cây trồng");
    }

    //Animal Facilities 
    @Around("addAnimalStorageFacilitiesPointCut()")
    public void addAnimalStorageFacilities(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Thêm cslt động vật ");
    }

    @Around("updateAnimalStorageFacilitiesPointCut()")
    public void updateAnimalStorageFacilities(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Cập nhật cslt động vật ");
    }

    @Around("deleteAnimalStorageFacilitiesPointCut()")
    public void deleteAnimalStorageFacilities(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Xóa cslt động vật ");
    }

    @Around("addAnimalSpeciesPointCut()")
    public void addAnimalSpecies(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Thêm động vật ");
    }

    @Around("updateAnimalSpeciesPointCut()")
    public void updateAnimalSpecies(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Cập nhật động vật ");
    }

    @Around("deleteAnimalSpeciesByNamePointCut()")
    public void deleteAnimalSpeciesByName(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Xóa động vật ");
    }

    //Wood Facilities
    @Around("addWoodFacilitiesPointCut()")
    public void addWoodFacilities(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Thêm cssx gỗ");
    }

    @Around("updateWoodFacilitiesPointCut()")
    public void updateWoodFacilities(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Cập nhật cssx gỗ");
    }

    @Around("deleteWoodFacilitiesPointCut()")
    public void deleteWoodFacilities(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Xóa cssx gỗ");
    }

    @Around("addProductionTypePointCut()")
    public void addProductionType(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Thêm loại hình sản xuất");
    }

    @Around("updateProductionTypePointCut()")
    public void updateProductionType(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Cập nhật loại hình sản xuất");
    }

    @Around("deleteProductionTypePointCut()")
    public void deleteProductionType(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Xóa loại hình sản xuất");
    }

    @Around("addOperationFormPointCut()")
    public void addOperationForm(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Thêm hình thức hoạt động");
    }

    @Around("updateOperationFormPointCut()")
    public void updateOperationFormPointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Cập nhật hình thức hoạt động");
    }

    @Around("deleteOperationFormPointCut()")
    public void deleteOperationForm(ProceedingJoinPoint joinPoint) throws Throwable{
        this.getAccess(joinPoint, "Xóa hình thức hoạt động");
    }

}
