package com.project.forestresourcesmanageapplication.servicesimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cglib.core.Local;
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
import com.project.forestresourcesmanageapplication.responses.AnimalsQuantity;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantity;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInMoth;
import com.project.forestresourcesmanageapplication.responses.MonthQuantity;
import com.project.forestresourcesmanageapplication.responses.QuarterQuantity;
import com.project.forestresourcesmanageapplication.responses.YearQuantity;
import com.project.forestresourcesmanageapplication.services.AdminstrationService;
import com.project.forestresourcesmanageapplication.services.AnimalStorageFacilitiesService;

import jakarta.transaction.Transactional;
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

    // @Override
    // public Long getQuantityAnimalOfFacilitiesCode(String code, String name){
    // List<AsfAsRelationship> asRelationships =
    // this.asfAsRelationshipRepository.selectAsfAsRelationshipByAnimalAndCode(code,name)
    // .orElseThrow(() -> new DataNotFoundException("không tồn tại loài động vật này
    // trong cơ sở lưu trữ" ));
    // long sum = 0;
    // for(AsfAsRelationship a : asRelationships){
    // sum+=a.getQuantity();
    // }
    // return sum;
    // }

    // @Override
    // public List<AnimalQuantity> getQuantityOfAllAnimalByFacilitiesCode(String
    // code){
    // List<AnimalQuantity> animalQuantities = new ArrayList<>();
    // List<AnimalSpecies> listAnimalSpecies =
    // this.asfAsRelationshipRepository.selectAllAnimalSpeciesByFacilitiesCode(code)
    // .orElseThrow( () -> new DataNotFoundException("cơ sở lưu trữ này không có
    // loài động vật nào"));
    // for(AnimalSpecies a : listAnimalSpecies){
    // long quantity = this.getQuantityAnimalOfFacilitiesCode(code, a.getName());
    // AnimalQuantity tmp = new AnimalQuantity(a.getName(),quantity);
    // animalQuantities.add(tmp);
    // }
    // return animalQuantities;
    // }

    // hàm hỗ trợ cho hàm bên dưới
    public Long getQuantityAnimalWithMonth(String code, String name, int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int day = yearMonth.lengthOfMonth();
        LocalDate localDate = LocalDate.of(year, month, day);
        Date date = Date.valueOf(localDate);
        List<AsfAsRelationship> asRelationships = this.asfAsRelationshipRepository
                .selectAsfAsRelationshipBeforeTime(code, name, date)
                .orElseThrow(() -> new DataNotFoundException("không tồn tại loài động vật này trong cơ sở lưu trữ"));
        long sum = 0;
        for (AsfAsRelationship a : asRelationships) {
            sum += a.getQuantity();
        }
        return sum;
    }

    @Override
    public List<MonthQuantity> getQuantityAnimalWithMonthOfYear(String code, String name, int year) {
        List<MonthQuantity> monthQuantities = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            long quantity = this.getQuantityAnimalWithMonth(code, name, i, year);
            MonthQuantity tmp = new MonthQuantity(i, quantity);
            monthQuantities.add(tmp);
        }
        return monthQuantities;
    }

    @Override
    public List<QuarterQuantity> getQuantityAnimalWithQuarterOfYear(String code, String name, int year) {
        List<QuarterQuantity> quarterQuantities = new ArrayList<>();
        int q = 1;
        for (int i = 3; i <= 12; i += 3) {
            long quantity = this.getQuantityAnimalWithMonth(code, name, i, year);
            QuarterQuantity tmp = new QuarterQuantity(q, quantity);
            quarterQuantities.add(tmp);
            q++;
        }
        return quarterQuantities;
    }

    @Override
    public List<YearQuantity> getQuantityAnimalWithYear(String code, String name) {
        List<YearQuantity> yearQuantities = new ArrayList<>();
        for (int i = 2013; i <= 2017; i++) {
            long quantity = this.getQuantityAnimalWithMonth(code, name, 12, i);
            YearQuantity tmp = new YearQuantity(i, quantity);
            yearQuantities.add(tmp);
        }
        return yearQuantities;
    }

    @Override
    public List<FacilitiesQuantity> getQuantityOfFacilitiesBeforeTime(LocalDate date) {
        List<FacilitiesQuantity> facilitiesQuantities = this.asfAsRelationshipRepository
                .selectAllQuantityOfFacilities( Date.valueOf(date));
        if(facilitiesQuantities.isEmpty()){
            throw new DataNotFoundException("Dữ liệu trong tháng "+date.getMonthValue()+" năm "+date.getYear()+" chưa tồn tại");
        }
        return facilitiesQuantities;
    }

  @Override
    public List<FacilitiesQuantityInMoth> getQuantityOfFacilitiesWithTime(LocalDate endDate) {
        int count = 1;
        List<FacilitiesQuantityInMoth> allData = new ArrayList<>();
        endDate = this.caculateDate(endDate);
        while (count <= 9) { // Thống kê 12 tháng trước endDate
            List<FacilitiesQuantity> data = this.getQuantityOfFacilitiesBeforeTime(endDate);
            FacilitiesQuantityInMoth tmp = FacilitiesQuantityInMoth.builder().date(endDate).data(data).build();
            allData.add(tmp);
            endDate = endDate.minusMonths(1);
            endDate = this.caculateDate(endDate);
            count ++ ;
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
