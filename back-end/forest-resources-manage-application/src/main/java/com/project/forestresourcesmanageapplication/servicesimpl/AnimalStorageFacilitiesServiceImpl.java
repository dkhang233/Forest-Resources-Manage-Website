package com.project.forestresourcesmanageapplication.servicesimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cglib.core.Local;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.forestresourcesmanageapplication.dtos.AnimalSpeciesDTO;
import com.project.forestresourcesmanageapplication.dtos.AnimalStorageFacilitiesDTO;
import com.project.forestresourcesmanageapplication.dtos.AsfAsRelationshipDTO;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataAlreadyExistsException;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataNotFoundException;
import com.project.forestresourcesmanageapplication.exceptionhandling.InvalidDataException;
import com.project.forestresourcesmanageapplication.models.Administration;
import com.project.forestresourcesmanageapplication.models.AnimalSpecies;
import com.project.forestresourcesmanageapplication.models.AnimalStorageFacilities;
import com.project.forestresourcesmanageapplication.models.AsfAsRelationship;
import com.project.forestresourcesmanageapplication.models.Fluctuation;
import com.project.forestresourcesmanageapplication.repositories.AnimalSpeciesRepository;
import com.project.forestresourcesmanageapplication.repositories.AnimalStorageFacilitiesRepository;
import com.project.forestresourcesmanageapplication.repositories.AsfAsRelationshipRepository;
import com.project.forestresourcesmanageapplication.repositories.FluctuationRepository;
import com.project.forestresourcesmanageapplication.responses.AnimalMonthQuantity;
import com.project.forestresourcesmanageapplication.responses.AnimalQuarterQuantity;
import com.project.forestresourcesmanageapplication.responses.AnimalYearQuantity;
import com.project.forestresourcesmanageapplication.responses.AnimalsQuantity;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantity;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInMoth;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInQuarter;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInYear;
import com.project.forestresourcesmanageapplication.responses.MonthQuantity;
import com.project.forestresourcesmanageapplication.responses.QuarterQuantity;
import com.project.forestresourcesmanageapplication.responses.YearQuantity;
import com.project.forestresourcesmanageapplication.services.AdminstrationService;
import com.project.forestresourcesmanageapplication.services.AnimalStorageFacilitiesService;

import jakarta.transaction.Transactional;
import jakarta.validation.OverridesAttribute;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalStorageFacilitiesServiceImpl implements AnimalStorageFacilitiesService {
    private final AnimalStorageFacilitiesRepository animalStorageFacilitiesRepository;
    private final AnimalSpeciesRepository animalSpeciesRepository;
    private final AsfAsRelationshipRepository asfAsRelationshipRepository;
    private final FluctuationRepository fluctuationRepository;
    private final AdminstrationService adminstrationService;

    // -----------------------CƠ SỞ LƯU TRỮ ĐỘNG VẬT---------------------------
    @Override
    public List<AnimalStorageFacilities> getAllAnimalStorageFacilities() {
        return animalStorageFacilitiesRepository.findAll();
    }

    @Override
    @Transactional
    public AnimalStorageFacilities updateAnimalStorageFacilities(String code,
            AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO) {
        AnimalStorageFacilities animalStorageFacilitiesExisting = this.getAnimalStorageFacilitiesByCode(code);

        // kiểm tra tên
        if (!animalStorageFacilitiesExisting.getName().equals(animalStorageFacilitiesDTO.getName())) {
            Optional<AnimalStorageFacilities> tmp = this.animalStorageFacilitiesRepository
                    .findByName(animalStorageFacilitiesDTO.getName());
            if (tmp.isPresent()) {
                throw new DataAlreadyExistsException(
                        "Đã có cơ sở lưu trữ động vật với tên = " + animalStorageFacilitiesDTO.getName());
            }
            animalStorageFacilitiesExisting.setName(animalStorageFacilitiesDTO.getName());
        }

        // kiểm tra đơn vị hành chính
        if (!animalStorageFacilitiesExisting.getAdministration().getCode()
                .equals(animalStorageFacilitiesDTO.getAdminstrationCode())) {
            try {
                Administration administration = this.adminstrationService
                        .retrieveAdministrationByCode(animalStorageFacilitiesDTO.getAdminstrationCode());
                animalStorageFacilitiesExisting.setAdministration(administration);
            } catch (Exception exception) {
                throw new DataNotFoundException("Không tìm thấy cơ sở hành chính với code = "
                        + animalStorageFacilitiesDTO.getAdminstrationCode());
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
                .orElseThrow(
                        () -> new DataNotFoundException("Không tìm thấy cơ sở lưu trữ động vật với code = " + code));
    }

    @Override
    public AnimalStorageFacilities addAnimalStorageFacilities(AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO,
            String code) {
        AnimalStorageFacilities animalStorageFacilities = new AnimalStorageFacilities();
        // kiểm tra code
        Optional<AnimalStorageFacilities> tmp1 = this.animalStorageFacilitiesRepository.findById(code);
        if (tmp1.isPresent()) {
            throw new DataAlreadyExistsException("Đã có cơ sở lưu trữ động vật với code = " + code);
        }
        // kiểm tra tên
        Optional<AnimalStorageFacilities> tmp2 = this.animalStorageFacilitiesRepository
                .findByName(animalStorageFacilitiesDTO.getName());
        if (tmp2.isPresent()) {
            throw new DataAlreadyExistsException(
                    "Đã có cơ sở lưu trữ động vật với tên = " + animalStorageFacilitiesDTO.getName());
        }
        // kiểm tra đơn vị hành chính
        try {
            Administration administration = this.adminstrationService
                    .retrieveAdministrationByCode(animalStorageFacilitiesDTO.getAdminstrationCode());
            animalStorageFacilities.setAdministration(administration);
        } catch (Exception exception) {
            throw new DataNotFoundException(
                    "Không tìm thấy cơ sở hành chính với code = " + animalStorageFacilitiesDTO.getAdminstrationCode());
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

    // -----------------------LOÀI ĐỘNG VẬT----------------------------
    // lƯu file ảnh avatar và trả về đường dẫn đến ảnh
    public String saveImage(MultipartFile avatarFile) {
        if (avatarFile == null) {
            return "";
        }

        // Kiểm tra kích thước file
        if (avatarFile.getSize() > 10 * 1024 * 1024) { // kích thước file phải <= 10 MB
            throw new InvalidDataException("Kích thước ảnh đại diện phải nhỏ hơn 10MB");
        }

        // Kiểm tra định dạng file
        String contentType = avatarFile.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) { // Phải là file ảnh
            throw new InvalidDataException("Ảnh đại diện phải là ảnh");
        }

        // Trích xuất và làm sạch tên file gốc từ hệ thống file của client
        String fileName = StringUtils.cleanPath(avatarFile.getOriginalFilename());

        // Tạo ra một tên file duy nhất
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

        // Tạo đường dẫn để lưu file
        Path uploadDir = Path.of("uploads");

        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path uploadPath = Path.of(uploadDir.toString(), uniqueFileName);
            Files.copy(avatarFile.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return uniqueFileName;
    }

    @Override
    public List<AnimalSpecies> getAllAnimalSpecies() {
        return this.animalSpeciesRepository.findAll();
    }

    @Override
    public AnimalSpecies getAnimalSpeciesByName(String name) {
        return this.animalSpeciesRepository.findById(name)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy loài động vật với tên = " + name));
    }

    @Override
    public AnimalSpecies updateAnimalSpecies(AnimalSpeciesDTO animalSpeciesDTO, MultipartFile imageFile) {
        AnimalSpecies animalSpecies = this.getAnimalSpeciesByName(animalSpeciesDTO.getName());

        // Kiểm tra image đã thay đổi chưa, nếu đã thay đổi -> gọi hàm để lưu file
        if (!animalSpecies.getImage().equals(animalSpeciesDTO.getImage())) {
            String image = this.saveImage(imageFile);
            animalSpecies.setImage(image);
        }
        Fluctuation fluctuation = this.getFluctuationById(animalSpeciesDTO.getFluctuationId());

        animalSpecies.setAnimalType(animalSpeciesDTO.getAnimalType())
                .setMainFood(animalSpeciesDTO.getMainFood())
                .setMainDisease(animalSpeciesDTO.getMainDisease())
                .setLongevity(animalSpeciesDTO.getLongevity())
                .setFluctuation(fluctuation);

        return animalSpeciesRepository.save(animalSpecies);
    }

    @Override
    public AnimalSpecies addAnimalSpecies(AnimalSpeciesDTO animalSpeciesDTO, MultipartFile imageFile) {

        // kiểm tra tên
        Optional<AnimalSpecies> tmp = this.animalSpeciesRepository.findById(animalSpeciesDTO.getName());
        if (tmp.isPresent()) {
            throw new DataAlreadyExistsException("Đã tồn tại loài động vật với tên = " + animalSpeciesDTO.getName());
        }
        String image = this.saveImage(imageFile);
        Fluctuation fluctuation = this.getFluctuationById(animalSpeciesDTO.getFluctuationId());

        AnimalSpecies animalSpecies = AnimalSpecies.builder()
                .name(animalSpeciesDTO.getName())
                .animalType(animalSpeciesDTO.getAnimalType())
                .image(image)
                .mainFood(animalSpeciesDTO.getMainFood())
                .mainDisease(animalSpeciesDTO.getMainDisease())
                .longevity(animalSpeciesDTO.getLongevity())
                .fluctuation(fluctuation)
                .build();

        return animalSpeciesRepository.save(animalSpecies);
    }

    @Override
    public void deleteAnimalSpeciesByName(String name) {
        AnimalSpecies animalSpecies = this.getAnimalSpeciesByName(name);
        this.animalSpeciesRepository.deleteById(animalSpecies.getName());
    }

    // lấy tất cả loài động vật trong 1 cơ sở
    @Override
    public List<AnimalSpecies> getAllAnimalSpeciesByFacilitiesCode(String code) {
        List<AnimalSpecies> listAnimalSpecies = this.asfAsRelationshipRepository
                .selectAllAnimalSpeciesByFacilitiesCode(code)
                .orElseThrow(() -> new DataNotFoundException("cơ sở lưu trữ này không có loài động vật nào"));
        return listAnimalSpecies;
    }

    // --------------------------LOẠI BIẾN ĐỘNG-------------------------
    @Override
    public List<Fluctuation> getAllFluctuations() {
        return this.fluctuationRepository.findAll();
    }

    @Override
    public Fluctuation getFluctuationById(int id) {
        return this.fluctuationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy loại biến động với id = " + id));
    }

    // ---------------------QUAN HỆ CSLTDV VÀ LOÀI ĐỘNG
    // VẬT---------------------------
    @Override
    public List<AsfAsRelationship> getAllAsfAsRelationships() {
        return this.asfAsRelationshipRepository.findAll();
    }

    @Override
    public AsfAsRelationship updateAsfAsRelationship(int id, AsfAsRelationshipDTO asfAsRelationshipDTO) {
        AsfAsRelationship asfAsRelationship = this.getAsfAsRelationshipById(id);
        // kiểm tra tên CSLTDV và tên loài động vật
        AnimalStorageFacilities animalStorageFacilities = this
                .getAnimalStorageFacilitiesByCode(asfAsRelationshipDTO.getCodeASF());
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
                .orElseThrow(() -> new DataNotFoundException("Không tồn tại bảng quan hệ Asf-As với id = " + id));
    }

    @Override
    public AsfAsRelationship addAsfAsRelationship(AsfAsRelationshipDTO asfAsRelationshipDTO) {
        AsfAsRelationship asfAsRelationship = new AsfAsRelationship();
        // kiểm tra tên CSLTDV và tên loài động vật
        AnimalStorageFacilities animalStorageFacilities = this
                .getAnimalStorageFacilitiesByCode(asfAsRelationshipDTO.getCodeASF());
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

    // --------------------------THỐNG KÊ--------------------------------
    @Override
    public List<AsfAsRelationship> getAsfAsRelationshipWithTime(Date startDate, Date endDate) {
        List<AsfAsRelationship> asRelationships = this.asfAsRelationshipRepository
                .selectAsfAsRelationshipWithTime(startDate, endDate)
                .orElseThrow(() -> new DataNotFoundException("Không tồn tại dữ liệu trong khoảng thời gian "));
        return asRelationships;
    }

    @Override
    public List<AsfAsRelationship> getAll() {
        return this.getAll();
    }

    @Override
    public List<AsfAsRelationship> getAsfAsRelationshipInYear(int year) {
        String str1 = year + "-01-01";
        String str2 = year + "-12-31";
        Date startDate = Date.valueOf(str1);
        Date endDate = Date.valueOf(str2);
        List<AsfAsRelationship> asRelationships = this.asfAsRelationshipRepository
                .selectAsfAsRelationshipWithTime(startDate, endDate)
                .orElseThrow(() -> new DataNotFoundException("Không tồn tại dữ liệu trong năm " + year));
        return asRelationships;
    }

    @Override
    public List<AsfAsRelationship> getAsfAsRelationshipByFacilitiesInYear(String code, int year) {
        String str1 = year + "-01-01";
        String str2 = year + "-12-31";
        Date startDate = Date.valueOf(str1);
        Date endDate = Date.valueOf(str2);
        List<AsfAsRelationship> asRelationships = this.asfAsRelationshipRepository
                .selectAsfAsRelationshipByFacilitiesInYear(code, startDate, endDate)
                .orElseThrow(() -> new DataNotFoundException(
                        "Không tồn tại dữ liệu của cơ sở với code " + code + " trong năm " + year));
        return asRelationships;
    }

    // hàm hỗ trợ cho hàm bên dưới
    public Long getMonthQuantityOfFacilities(String name, int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int day = yearMonth.lengthOfMonth();
        LocalDate localDate = LocalDate.of(year, month, day);
        Date date = Date.valueOf(localDate);
        return this.asfAsRelationshipRepository.getMonthQuantityOfFacilities(name, date)
        .orElseThrow(() -> new DataNotFoundException("Không tồn tại dữ liệu của "+name+" trong tháng "+month+" năm "+year));
    }

    public long getMonthQuantityOfFacilities(String name, LocalDate date){
        LocalDate date2 = this.caculateDate(date);
        Date dateExisting = Date.valueOf(date2);
        try {
            long quantity = this.asfAsRelationshipRepository.getMonthQuantityOfFacilitiesNew(name, dateExisting);
            return quantity;
        }catch(Exception e){
            return 0;
        }
    }    

    public int getTotalBeforeFuntion(String name, LocalDate date , int i){
        LocalDate date2 = this.caculateDate(date);
        Date dateExisting = Date.valueOf(date2);
        try {
            long quantity = this.asfAsRelationshipRepository.getMonthQuantityOfFacilitiesNew(name, dateExisting);
            return i+1;
        }catch(Exception e){
            return i;
        }
    }

    // thống kê số lượng theo tháng trong tất cả cơ sở
    @Override

    public List<AnimalMonthQuantity> getMonthQuantityOfFacilities(int year) {
        List<AnimalMonthQuantity> animalMonthQuantities = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            YearMonth yearMonth = YearMonth.of(year, i);
            int day = yearMonth.lengthOfMonth();
            LocalDate localDate = LocalDate.of(year, i, day);
            Date date = Date.valueOf(localDate);
            List<AnimalMonthQuantity> animalMonthQuantities2 = this.asfAsRelationshipRepository
                    .selectMonthQuantityOfFacilities(i, date);
            animalMonthQuantities.addAll(animalMonthQuantities2);
        }
        if (animalMonthQuantities.isEmpty()) {
            throw new DataNotFoundException("Dữ liệu trong năm "
                    + year + " chưa tồn tại");
        }
        return animalMonthQuantities;
    }

    // thống kê số lượng theo quý trong tất cả cơ sở
    @Override
    public List<AnimalQuarterQuantity> getQuarterQuantityOfFacilities(int year) {
        List<AnimalQuarterQuantity> listAQQ = new ArrayList<>();
        String str = year + "-12-30";
        Date date = Date.valueOf(str);
        List<AnimalStorageFacilities> listASF = this.animalStorageFacilitiesRepository.findAllBeforeTime(date);
        for (AnimalStorageFacilities asf : listASF) {
            int quarter = 1;
            for (int i = 1; i <= 12; i += 3) {
                long quantity1 = this.getMonthQuantityOfFacilities(asf.getName(), i, year);
                long quantity2 = this.getMonthQuantityOfFacilities(asf.getName(), i + 1, year);
                long quantity3 = this.getMonthQuantityOfFacilities(asf.getName(), i + 2, year);
                long quantity = (quantity1 + quantity2 + quantity3) / 3;
                AnimalQuarterQuantity tmp = new AnimalQuarterQuantity();
                tmp.setFacilitiesName(asf.getName());
                QuarterQuantity quarterQuantity = new QuarterQuantity(quarter, quantity);
                tmp.setQuarterQuantity(quarterQuantity);
                listAQQ.add(tmp);
                quarter++;
            }

        }
        return listAQQ;
    }


    //Hàm hỗ trợ cho thống kê theo quý
//--------------------START--------------------
    public String changeToQuarter(LocalDate date){
        int month = date.getMonthValue();
        int year = date.getYear();
        return (month >= 1 && month <= 3)     ? "Q1-"+year :
                (month >= 4 && month <= 6)    ? "Q2-"+year :
                (month >= 7 && month <= 9)    ? "Q3-"+year :
                                                "Q4-"+year;
    }
    
    public LocalDate getLastDayOfQuarter(LocalDate date) {
        LocalDate firstDayOfQuarter = date.with(date.getMonth().firstMonthOfQuarter())
                                    .with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfQuarter = firstDayOfQuarter.plusMonths(2)
                                    .with(TemporalAdjusters.lastDayOfMonth());
        return lastDayOfQuarter;
    }

    public LocalDate getFirstDayOfQuarter(LocalDate date) {
        LocalDate firstDayOfQuarter = date.with(date.getMonth().firstMonthOfQuarter())
                                    .with(TemporalAdjusters.firstDayOfMonth());
        return firstDayOfQuarter;
    }

    public List<FacilitiesQuantity> getQuarterFacilitiesWithStartDate(LocalDate startDate){
        List<FacilitiesQuantity> list = new ArrayList<>();
        LocalDate lastDayOfQuarter = this.getLastDayOfQuarter(startDate);
        List<AnimalStorageFacilities> listASF = this.animalStorageFacilitiesRepository.findAllBeforeTime(Date.valueOf(lastDayOfQuarter));
        for(AnimalStorageFacilities asf : listASF){
            long sum=0;
            int i=0;
            // while (startDate.getMonthValue()<=lastDayOfQuarter.getMonthValue()){
            //     sum+=this.getMonthQuantityOfFacilities(asf.getName(), startDate.getMonthValue(), startDate.getYear());
            //     i++;
            //     startDate = startDate.plusMonths(1);
            for(int j=1;j<=3;j++){
                sum+=this.getMonthQuantityOfFacilities(asf.getName(), startDate);
                i=this.getTotalBeforeFuntion(asf.getName(), startDate, i);
                startDate = startDate.plusMonths(1);
            }
            if(i==0){
                throw new DataNotFoundException("Dữ liệu trong "+this.changeToQuarter(startDate)+" của "+asf.getName()+" không tồn tại");
            }
            long quantity = sum/i;
            FacilitiesQuantity facilitiesQuantity = new FacilitiesQuantity(asf.getCode(),quantity);
            list.add(facilitiesQuantity);
        }
        return list;
    }

    public List<FacilitiesQuantity> getQuarterFacilitiesWithEndDate(LocalDate endDate){
        int start = this.getFirstDayOfQuarter(endDate).getMonthValue();
        int end = endDate.getMonthValue();
        if(start==end){
            throw new DataNotFoundException("Ngày kết thúc không hợp lệ . Vui lòng nhập lại");
        }
        String quarter = this.changeToQuarter(endDate);
        List<FacilitiesQuantity> list = new ArrayList<>();
        List<AnimalStorageFacilities> listASF = this.animalStorageFacilitiesRepository.findAllBeforeTime(Date.valueOf(endDate));
        for(AnimalStorageFacilities asf : listASF){
            long sum=0;
            int i=0;
            for(int j=start;j<=end;j++){
                sum+=this.getMonthQuantityOfFacilities(asf.getName(),endDate);
                i=this.getTotalBeforeFuntion(asf.getName(),endDate, i);
                endDate = endDate.minusMonths(1);
                // endDate = endDate.minusDays(31);
                if(!this.changeToQuarter(endDate).equals(quarter)){
                    break;
                }
            }
            if(sum==0){
                throw new DataNotFoundException("Dữ liệu trong "+this.changeToQuarter(endDate)+" của "+asf.getName()+" không tồn tại");
            }
            long quantity = sum/i;
            FacilitiesQuantity facilitiesQuantity = new FacilitiesQuantity(asf.getCode(),quantity);
            list.add(facilitiesQuantity);
        }
        return list;
    }
//---------------------------END--------------------------

    //thống kê số lượng theo quý trong tất cả cơ sở (NEW)
    @Override
    public List<FacilitiesQuantityInQuarter> getQuarterQuantityOfFacilitiesWithTime(LocalDate startDate, LocalDate endDate){
        if(this.changeToQuarter(startDate).equals(this.changeToQuarter(endDate))
            ||startDate.isAfter(endDate)
            ){
                throw new DataAccessResourceFailureException("Ngày bắt đầu và ngày kết thúc không hợp lệ .Vui lòng nhập lại");
            }
        List<FacilitiesQuantityInQuarter> list = new ArrayList<>();
        while(true){
            FacilitiesQuantityInQuarter tmp = new FacilitiesQuantityInQuarter();
            List<FacilitiesQuantity> data = this.getQuarterFacilitiesWithStartDate(startDate);
            tmp.setQuarter(this.changeToQuarter(startDate));
            tmp.setData(data);
            list.add(tmp);
            LocalDate firstDayNextQuarter = this.getLastDayOfQuarter(startDate).plusDays(1);
            if(this.changeToQuarter(firstDayNextQuarter).equals(this.changeToQuarter(endDate))){
                List<FacilitiesQuantity> data2 = this.getQuarterFacilitiesWithEndDate(endDate);
                FacilitiesQuantityInQuarter tmp2 = new FacilitiesQuantityInQuarter();
                tmp2.setQuarter(this.changeToQuarter(endDate));
                tmp2.setData(data2);
                list.add(tmp2);
                break;
            }
            startDate = firstDayNextQuarter;
        }    
        return list;
    }

    //Hàm hỗ trợ thống kê theo năm
    //--------------------------START---------------------------
    public List<FacilitiesQuantity> getYearQuantityFacilitiesWithYear (int year){
        List<FacilitiesQuantity> list = new ArrayList<>();
        String endDate = year+"-12-30";
        LocalDate startDate = LocalDate.of(year, 1, 1);
        // LocalDate endDate = LocalDate.of(year, 12, 30);
        List<AnimalStorageFacilities> listASF = this.animalStorageFacilitiesRepository.findAllBeforeTime(Date.valueOf(endDate));
        for(AnimalStorageFacilities asf : listASF){
            long sum=0;
            int i=0;
            for(int j=1;j<=12;j++){
                sum+=this.getMonthQuantityOfFacilities(asf.getName(), startDate);
                i=this.getTotalBeforeFuntion(asf.getName(), startDate, i);
                startDate = startDate.plusMonths(1); }
            if(i==0){
                throw new DataNotFoundException("Dữ liệu trong năm "+year+" của "+asf.getName()+" không tồn tại");
            }
            long quantity = sum/i;
            FacilitiesQuantity facilitiesQuantity = new FacilitiesQuantity(asf.getCode(),quantity);
            list.add(facilitiesQuantity);
        }
        return list;
    }
//-------------------------END---------------------------

    //thống kê số lượng theo năm trong tất cả cơ sở (NEW)
    @Override
    public List<FacilitiesQuantityInYear> getYearQuantityOfFacilitiesWithTime(int startYear, int endYear){
        List<FacilitiesQuantityInYear> list = new ArrayList<>();
        if(startYear > endYear){
            throw new DataAccessResourceFailureException("Năm bắt đầu và năm kết thúc không hợp lệ .Vui lòng nhập lại");
        }
        if(startYear == endYear){
            List<FacilitiesQuantity> data = this.getYearQuantityFacilitiesWithYear(endYear);
            FacilitiesQuantityInYear tmp = new FacilitiesQuantityInYear(endYear,data);
            list.add(tmp);
            return list;
        }
        while(true){
            FacilitiesQuantityInYear tmp = new FacilitiesQuantityInYear();
            List<FacilitiesQuantity> data = this.getYearQuantityFacilitiesWithYear(startYear);
            tmp.setYear(startYear);
            tmp.setData(data);
            list.add(tmp);
            int nextYear = startYear+1;
            if(nextYear==endYear){
                List<FacilitiesQuantity> data2 = this.getYearQuantityFacilitiesWithYear(endYear);
                FacilitiesQuantityInYear tmp2 = new FacilitiesQuantityInYear(endYear,data2);
                list.add(tmp2);
                break;
            }
            startYear=nextYear;
        }    
        return list;
    }

    // thống kê số lượng theo năm trong tất cả cơ sở (4 năm trước thời điểm xét)
    public List<AnimalYearQuantity> getYearQuantityOfFacilities(int year) {
        List<AnimalYearQuantity> listAYQ = new ArrayList<>();
        String str = year + "-12-30";
        Date date = Date.valueOf(str);
        List<AnimalStorageFacilities> listASF = this.animalStorageFacilitiesRepository.findAllBeforeTime(date);
        for (AnimalStorageFacilities asf : listASF) {
            for (int y = year; y >= year - 3; y--) {
                long sum = 0;
                for (int i = 1; i <= 12; i++) {
                    sum += this.getMonthQuantityOfFacilities(asf.getName(), i, y);
                }
                long quantity = sum / 12;
                AnimalYearQuantity tmp = new AnimalYearQuantity();
                tmp.setFacilitiesName(asf.getName());
                YearQuantity yearQuantity = new YearQuantity(y, quantity);
                tmp.setYearQuantity(yearQuantity);
                listAYQ.add(tmp);
            }
        }
        return listAYQ;
    }

    @Override
    public List<FacilitiesQuantity> getQuantityOfFacilitiesBeforeTime(LocalDate date) {
        List<FacilitiesQuantity> facilitiesQuantities = this.asfAsRelationshipRepository
                .selectAllQuantityOfFacilities(Date.valueOf(date));
        if (facilitiesQuantities.isEmpty()) {
            throw new DataNotFoundException(
                    "Dữ liệu trong tháng " + date.getMonthValue() + " năm " + date.getYear() + " chưa tồn tại");
        }
        return facilitiesQuantities;
    }

    @Override
    public List<FacilitiesQuantityInMoth> getQuantityOfFacilitiesWithTime(LocalDate beginDate, LocalDate endDate) {
        List<FacilitiesQuantityInMoth> allData = new ArrayList<>();
        endDate = this.caculateDate(endDate);
        while (endDate.isAfter(beginDate)) { // Thống kê 12 tháng trước endDate
            if (endDate.isAfter(LocalDate.now())) {
                throw new DataNotFoundException("Dữ liệu trong tháng " + endDate.getMonthValue() + " năm "
                        + endDate.getYear() + " chưa tồn tại");
            }
            List<FacilitiesQuantity> data = this.getQuantityOfFacilitiesBeforeTime(endDate);
            FacilitiesQuantityInMoth tmp = FacilitiesQuantityInMoth.builder().date(endDate).data(data).build();
            allData.add(tmp);
            endDate = endDate.minusMonths(1);
            endDate = this.caculateDate(endDate);
        }
        return allData;
    }

    private LocalDate caculateDate(LocalDate date) {
        int yearData = date.getYear();
        int monthData = date.getMonthValue();
        int day = date.lengthOfMonth();
        return LocalDate.of(yearData, monthData, day);
    }

    @Override
    public List<AnimalsQuantity> getQuantityOfAllAnimalBeforeTime(Date date) {
        List<AnimalsQuantity> animalsQuantities = this.asfAsRelationshipRepository.selectAllQuantityOfAllAnimal(date);
        return animalsQuantities;
    }

}
