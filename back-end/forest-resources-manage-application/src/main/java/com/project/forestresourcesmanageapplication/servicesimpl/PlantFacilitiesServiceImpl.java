package com.project.forestresourcesmanageapplication.servicesimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.forestresourcesmanageapplication.dtos.CoordinatesDTO;
import com.project.forestresourcesmanageapplication.dtos.PfPsRelationshipDTO;
import com.project.forestresourcesmanageapplication.dtos.PlantFacilitiesDTO;
import com.project.forestresourcesmanageapplication.dtos.PlantSeedDTO;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataAlreadyExistsException;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataNotFoundException;
import com.project.forestresourcesmanageapplication.exceptionhandling.InvalidDataException;
import com.project.forestresourcesmanageapplication.models.Administration;
import com.project.forestresourcesmanageapplication.models.PfPsRelationship;
import com.project.forestresourcesmanageapplication.models.PlantFacilities;
import com.project.forestresourcesmanageapplication.models.PlantSeed;
import com.project.forestresourcesmanageapplication.repositories.PfPsRelationshipRepository;
import com.project.forestresourcesmanageapplication.repositories.PlantFacilitiesRepository;
import com.project.forestresourcesmanageapplication.repositories.PlantSeedRepository;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantity;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInMoth;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInQuarter;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInYear;
import com.project.forestresourcesmanageapplication.services.AdminstrationService;
import com.project.forestresourcesmanageapplication.services.PlantFacilitiesService;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class PlantFacilitiesServiceImpl implements PlantFacilitiesService {
    private final PlantFacilitiesRepository plantFacilitiesRepository;
    private final PlantSeedRepository plantSeedRepository;
    private final PfPsRelationshipRepository pfPsRelationshipRepository;
    private final AdminstrationService adminstrationService;

    // -------------------------CƠ SỞ SẢN XUẤT GIỐNG CÂY TRỒNG------------------
    @Override
    public List<PlantFacilities> getAllPlantFacilities() {
        return this.plantFacilitiesRepository.findAll();
    }

    @Override
    @Transactional
    public PlantFacilities updatePlantFacilities(String code, PlantFacilitiesDTO plantFacilitiesDTO) {
        PlantFacilities plantFacilitiesExisting = this.getPlantFacilitiesByCode(code);

        // kiểm tra tên
        if (!plantFacilitiesExisting.getName().equals(plantFacilitiesDTO.getName())) {
            Optional<PlantFacilities> tmp = this.plantFacilitiesRepository
                    .findByName(plantFacilitiesDTO.getName());
            if (tmp.isPresent()) {
                throw new DataAlreadyExistsException(
                        "Đã có cơ sở sản xuất giống cây trồng với tên = " + plantFacilitiesDTO.getName());
            }
            plantFacilitiesExisting.setName(plantFacilitiesDTO.getName());
        }

        // kiểm tra đơn vị hành chính
        if (!plantFacilitiesExisting.getAdministration().getCode()
                .equals(plantFacilitiesDTO.getAdministrationCode())) {
            try {
                Administration administration = this.adminstrationService
                        .retrieveAdministrationByCode(plantFacilitiesDTO.getAdministrationCode());
                plantFacilitiesExisting.setAdministration(administration);
            } catch (Exception exception) {
                throw new DataNotFoundException("Không tìm thấy cơ sở hành chính với code = "
                        + plantFacilitiesDTO.getAdministrationCode());
            }
        }

        plantFacilitiesExisting.setCapacity(plantFacilitiesDTO.getCapacity());
        plantFacilitiesExisting.setDate(plantFacilitiesDTO.getDate());

        return plantFacilitiesRepository.save(plantFacilitiesExisting);
    }

    @Override
    public PlantFacilities getPlantFacilitiesByCode(String code) {
        return plantFacilitiesRepository.findById(code)
                .orElseThrow(
                        () -> new DataNotFoundException(
                                "Không tìm thấy cơ sở sản xuất giống cây trồng với code = " + code));
    }

    @Override
    @Transactional
    public PlantFacilities addPlantFacilities(PlantFacilitiesDTO plantFacilitiesDTO, String code) {
        PlantFacilities plantFacilities = new PlantFacilities();

        // kiểm tra code
        Optional<PlantFacilities> tmp1 = this.plantFacilitiesRepository.findById(code);
        if (tmp1.isPresent()) {
            throw new DataAlreadyExistsException("Đã có cơ sở sản xuất giống cây trồng với code = " + code);
        }
        // kiểm tra tên
        Optional<PlantFacilities> tmp2 = this.plantFacilitiesRepository
                .findByName(plantFacilitiesDTO.getName());
        if (tmp2.isPresent()) {
            throw new DataAlreadyExistsException(
                    "Đã có cơ sở sản xuất giống cây trồng với tên = " + plantFacilitiesDTO.getName());
        }
        // kiểm tra đơn vị hành chính
        try {
            Administration administration = this.adminstrationService
                    .retrieveAdministrationByCode(plantFacilitiesDTO.getAdministrationCode());
            plantFacilities.setAdministration(administration);
        } catch (Exception exception) {
            throw new DataNotFoundException(
                    "Không tìm thấy cơ sở hành chính với code = " + plantFacilitiesDTO.getAdministrationCode());
        }

        plantFacilities.setCode(code);
        plantFacilities.setName(plantFacilitiesDTO.getName());
        plantFacilities.setCapacity(plantFacilitiesDTO.getCapacity());
        plantFacilities.setDate(plantFacilitiesDTO.getDate());

        return plantFacilitiesRepository.save(plantFacilities);
    }

    @Override
    @Transactional
    public void deletePlantFacilitiesByCode(String code) {
        PlantFacilities plantFacilities = this.getPlantFacilitiesByCode(code);
        this.plantFacilitiesRepository.deleteById(plantFacilities.getCode());
    }

    // --------------------Tọa độ trên bản đồ-----------------------------
    public List<CoordinatesDTO> retrieveAllCoordinates() {
        List<PlantFacilities> plantFacilities = this.getAllPlantFacilities();
        List<CoordinatesDTO> coordinatesDTOs = plantFacilities.stream().map((facility) -> {
            CoordinatesDTO coordinatesDTO = CoordinatesDTO.builder()
                    .code(facility.getCode())
                    .lat(facility.getLat())
                    .lng(facility.getLng())
                    .build();
            return coordinatesDTO;
        }).toList();
        return coordinatesDTOs;
    }

    public CoordinatesDTO retrieveCoordinates(String code) {
        PlantFacilities plantFacilities = this.getPlantFacilitiesByCode(code);
        CoordinatesDTO coordinatesDTO = new CoordinatesDTO(plantFacilities.getCode(),
                plantFacilities.getLat(), plantFacilities.getLng());
        return coordinatesDTO;
    }

    public CoordinatesDTO updateCoordinates(CoordinatesDTO coordinatesDTO) {
        PlantFacilities plantFacilities = this
                .getPlantFacilitiesByCode(coordinatesDTO.getCode());
        plantFacilities.setLat(coordinatesDTO.getLat());
        plantFacilities.setLng(coordinatesDTO.getLng());
        this.plantFacilitiesRepository.save(plantFacilities);
        return coordinatesDTO;
    }

    public void deleteCoordinates(String code) {
        PlantFacilities plantFacilities = this
                .getPlantFacilitiesByCode(code);
        plantFacilities.setLat("");
        plantFacilities.setLng("");
        this.plantFacilitiesRepository.save(plantFacilities);
    }

    // -------------------------GIỐNG CÂY TRỒNG------------------
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
    public List<PlantSeed> getAllPlantSeed() {
        return this.plantSeedRepository.findAll();
    }

    @Override
    @Transactional
    public PlantSeed updatePlantSeed(PlantSeedDTO plantSeedDTO, MultipartFile imageFile) {
        PlantSeed plantSeed = this.getPlantSeedByName(plantSeedDTO.getName());

        // Kiểm tra image đã thay đổi chưa, nếu đã thay đổi -> gọi hàm để lưu file
        if (!plantSeed.getImage().equals(plantSeedDTO.getImage())) {
            String image = this.saveImage(imageFile);
            plantSeed.setImage(image);
        }

        plantSeed.setType(plantSeedDTO.getType());
        plantSeed.setSoilType(plantSeedDTO.getSoilType());
        plantSeed.setPlantSeason(plantSeedDTO.getPlantSeason());
        plantSeed.setMainPest(plantSeedDTO.getMainPest());
        plantSeed.setHarvestingPeriod(plantSeedDTO.getHarvestingPeriod());

        return plantSeedRepository.save(plantSeed);
    }

    @Override
    public PlantSeed getPlantSeedByName(String name) {
        return plantSeedRepository.findById(name)
                .orElseThrow(
                        () -> new DataNotFoundException("Không tìm thấy giống cây trồng với tên = " + name));
    }

    @Override
    @Transactional
    public PlantSeed addPlantSeed(PlantSeedDTO plantSeedDTO, MultipartFile imageFile) {
        // kiểm tra tên
        Optional<PlantSeed> tmp = this.plantSeedRepository.findById(plantSeedDTO.getName());
        if (tmp.isPresent()) {
            throw new DataAlreadyExistsException("Đã tồn tại giống cây trồng với tên = " + plantSeedDTO.getName());
        }
        String image = this.saveImage(imageFile);

        PlantSeed plantSeed = PlantSeed.builder()
                .name(plantSeedDTO.getName())
                .mainPest(plantSeedDTO.getMainPest())
                .image(image)
                .type(plantSeedDTO.getType())
                .soilType(plantSeedDTO.getSoilType())
                .plantSeason(plantSeedDTO.getPlantSeason())
                .harvestingPeriod(plantSeedDTO.getHarvestingPeriod())
                .build();

        return plantSeedRepository.save(plantSeed);
    }

    @Override
    @Transactional
    public void deletePlantSeedByName(String name) {
        PlantSeed plantSeed = this.getPlantSeedByName(name);
        this.plantSeedRepository.deleteById(plantSeed.getName());
    }

    @Override
    public List<PlantSeed> getAllPlantSeedByFacilitiesCode(String code) {
        List<PlantSeed> plantSeeds = this.pfPsRelationshipRepository
                .selectAllPlantSeedByFacilitiesCode(code)
                .orElseThrow(() -> new DataNotFoundException("cơ sở sản xuất này không có giống cây trồng nào"));
        return plantSeeds;
    }

    // -------------------------QUAN HỆ CSSX VÀ GIỐNG CÂY TRỒNG------------------
    @Override
    public List<PfPsRelationship> getAllPfPsRelationship() {
        return this.pfPsRelationshipRepository.findAll();
    }

    @Override
    @Transactional
    public PfPsRelationship updatePfPsRelationship(int id, PfPsRelationshipDTO pfPsRelationshipDTO) {
        PfPsRelationship pfPsRelationship = this.getPfPsRelationshipById(id);
        // kiểm tra tên CSSX và tên giống cây trồng
        PlantFacilities plantFacilities = this
                .getPlantFacilitiesByCode(pfPsRelationshipDTO.getCodePF());
        PlantSeed plantSeed = this.getPlantSeedByName(pfPsRelationshipDTO.getNamePS());

        pfPsRelationship.setPlantFacilities(plantFacilities);
        pfPsRelationship.setPlantSeed(plantSeed);
        pfPsRelationship.setQuantity(pfPsRelationshipDTO.getQuantity());
        pfPsRelationship.setDate(pfPsRelationshipDTO.getDate());

        return pfPsRelationshipRepository.save(pfPsRelationship);
    }

    @Override
    public PfPsRelationship getPfPsRelationshipById(int id) {
        return this.pfPsRelationshipRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tồn tại bảng quan hệ Pf-Ps với id = " + id));
    }

    @Override
    @Transactional
    public PfPsRelationship addPfPsRelationship(PfPsRelationshipDTO pfPsRelationshipDTO) {
        PfPsRelationship pfPsRelationship = new PfPsRelationship();
        // kiểm tra tên CSSX và tên giống cây trồng
        PlantFacilities plantFacilities = this
                .getPlantFacilitiesByCode(pfPsRelationshipDTO.getCodePF());
        PlantSeed plantSeed = this.getPlantSeedByName(pfPsRelationshipDTO.getNamePS());

        pfPsRelationship.setPlantFacilities(plantFacilities);
        pfPsRelationship.setPlantSeed(plantSeed);
        pfPsRelationship.setQuantity(pfPsRelationshipDTO.getQuantity());
        pfPsRelationship.setDate(pfPsRelationshipDTO.getDate());

        return pfPsRelationshipRepository.save(pfPsRelationship);
    }

    @Override
    @Transactional
    public void deletePfPsRelationshipById(int id) {
        PfPsRelationship pfPsRelationship = this.getPfPsRelationshipById(id);
        this.pfPsRelationshipRepository.deleteById(pfPsRelationship.getId());
    }

    // --------------------------THỐNG KÊ--------------------------------
    // Hàm hỗ trợ
    // -------------START--------------
    private LocalDate caculateDate(LocalDate date) {
        int yearData = date.getYear();
        int monthData = date.getMonthValue();
        int day = date.lengthOfMonth();
        return LocalDate.of(yearData, monthData, day);
    }

    private String changeToQuarter(LocalDate date) {
        int month = date.getMonthValue();
        int year = date.getYear();

        return (month >= 1 && month <= 3) ? "Q1-" + year
                : (month >= 4 && month <= 6) ? "Q2-" + year : (month >= 7 && month <= 9) ? "Q3-" + year : "Q4-" + year;
    }

    private LocalDate getLastDayOfQuarter(LocalDate date) {
        LocalDate firstDayOfQuarter = date.with(date.getMonth().firstMonthOfQuarter())
                .with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfQuarter = firstDayOfQuarter.plusMonths(2)
                .with(TemporalAdjusters.lastDayOfMonth());
        return lastDayOfQuarter;
    }

    private LocalDate getFirstDayOfQuarter(LocalDate date) {
        LocalDate firstDayOfQuarter = date.with(date.getMonth().firstMonthOfQuarter())
                .with(TemporalAdjusters.firstDayOfMonth());
        return firstDayOfQuarter;
    }

    private long getMonthQuantityOfFacilities(String name, LocalDate date) {
        LocalDate date2 = this.caculateDate(date);
        Date dateExisting = Date.valueOf(date2);
        try {
            long quantity = this.pfPsRelationshipRepository.getMonthQuantityOfFacilitiesNew(name, dateExisting);
            return quantity;
        } catch (Exception e) {
            return 0;
        }
    }

    private int getTotalBeforeFuntion(String name, LocalDate date, int i) {
        LocalDate date2 = this.caculateDate(date);
        Date dateExisting = Date.valueOf(date2);
        try {
            long quantity = this.pfPsRelationshipRepository.getMonthQuantityOfFacilitiesNew(name, dateExisting);
            return i + 1;
        } catch (Exception e) {
            return i;
        }

    }

    private List<FacilitiesQuantity> getQuarterFacilitiesWithStartDate(LocalDate startDate) {
        LocalDate dateTmp = startDate;
        List<FacilitiesQuantity> list = new ArrayList<>();
        LocalDate lastDayOfQuarter = this.getLastDayOfQuarter(startDate);
        List<String> listPF = this.plantFacilitiesRepository
                .findAllFacilitiesNameBeforeTime(Date.valueOf(lastDayOfQuarter));
        for (String pf : listPF) {
            long sum = 0;
            int i = 0;
            for (int j = 1; j <= 3; j++) {
                sum += this.getMonthQuantityOfFacilities(pf, startDate);
                i = this.getTotalBeforeFuntion(pf, startDate, i);

                startDate = startDate.plusMonths(1);
            }
            if (i == 0) {
                throw new DataNotFoundException("Dữ liệu trong " + this.changeToQuarter(startDate) + " của "
                        + pf + " không tồn tại");
            }
            long quantity = sum / i;
            FacilitiesQuantity facilitiesQuantity = new FacilitiesQuantity(pf, quantity);

            list.add(facilitiesQuantity);
            startDate = dateTmp;
        }
        return list;
    }

    private List<FacilitiesQuantity> getYearQuantityFacilitiesWithYear(int year) {
        List<FacilitiesQuantity> list = new ArrayList<>();
        String endDate = year + "-12-30";
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate dateTmp = startDate;
        List<String> listPF = this.plantFacilitiesRepository
                .findAllFacilitiesNameBeforeTime(Date.valueOf(endDate));
        for (String pf : listPF) {
            long sum = 0;
            int i = 0;
            for (int j = 1; j <= 12; j++) {
                sum += this.getMonthQuantityOfFacilities(pf, startDate);
                i = this.getTotalBeforeFuntion(pf, startDate, i);
                startDate = startDate.plusMonths(1);
            }
            if (i == 0) {
                throw new DataNotFoundException(
                        "Dữ liệu trong năm " + year + " của " + pf + " không tồn tại");
            }
            long quantity = sum / i;
            FacilitiesQuantity facilitiesQuantity = new FacilitiesQuantity(pf, quantity);
            list.add(facilitiesQuantity);
            startDate = dateTmp;
        }
        return list;
    }

    // ----------------END-----------------

    @Override
    public List<FacilitiesQuantity> getQuantityOfFacilitiesBeforeTime(LocalDate date) {
        List<FacilitiesQuantity> facilitiesQuantities = this.pfPsRelationshipRepository
                .selectAllQuantityOfFacilities(Date.valueOf(date));
        if (facilitiesQuantities.isEmpty()) {
            throw new DataNotFoundException(
                    "Dữ liệu trong tháng " + date.getMonthValue() + " năm " + date.getYear() + " chưa tồn tại");
        }
        return facilitiesQuantities;
    }

    // thống kê theo tháng
    @Override
    public List<FacilitiesQuantityInMoth> getMonthQuantityFacilitiesWithTime(LocalDate beginDate, LocalDate endDate) {
        List<FacilitiesQuantityInMoth> allData = new ArrayList<>();
        beginDate = this.caculateDate(beginDate);
        endDate = this.caculateDate(endDate);
        if (endDate.isAfter(LocalDate.now())) {
            endDate = LocalDate.now();
            while (beginDate.isBefore(endDate.plusDays(1))) {
                List<FacilitiesQuantity> data = this.getQuantityOfFacilitiesBeforeTime(beginDate);
                FacilitiesQuantityInMoth tmp = FacilitiesQuantityInMoth.builder().date(beginDate).data(data).build();
                allData.add(tmp);
                beginDate = beginDate.plusMonths(1);
                beginDate = this.caculateDate(beginDate);
            }
            List<FacilitiesQuantity> data = this.getQuantityOfFacilitiesBeforeTime(endDate);
            FacilitiesQuantityInMoth tmp = FacilitiesQuantityInMoth.builder().date(endDate).data(data).build();
            allData.add(tmp);
        } else {
            while (beginDate.isBefore(endDate.plusDays(1))) {
                List<FacilitiesQuantity> data = this.getQuantityOfFacilitiesBeforeTime(beginDate);
                FacilitiesQuantityInMoth tmp = FacilitiesQuantityInMoth.builder().date(beginDate).data(data).build();
                allData.add(tmp);
                beginDate = beginDate.plusMonths(1);
                beginDate = this.caculateDate(beginDate);
            }
        }
        return allData;
    }

    // thống kê theo quý
    @Override
    public List<FacilitiesQuantityInQuarter> getQuarterQuantityOfFacilitiesWithTime(LocalDate startDate,
            LocalDate endDate) {
        if (this.changeToQuarter(startDate).equals(this.changeToQuarter(endDate))
                || startDate.isAfter(endDate)) {
            throw new DataAccessResourceFailureException(
                    "Ngày bắt đầu và ngày kết thúc không hợp lệ .Vui lòng nhập lại");
        }
        List<FacilitiesQuantityInQuarter> list = new ArrayList<>();
        while (true) {
            FacilitiesQuantityInQuarter tmp = new FacilitiesQuantityInQuarter();
            List<FacilitiesQuantity> data = this.getQuarterFacilitiesWithStartDate(startDate);
            tmp.setQuarter(this.changeToQuarter(startDate));
            tmp.setData(data);
            list.add(tmp);
            LocalDate firstDayNextQuarter = this.getLastDayOfQuarter(startDate).plusDays(1);
            startDate = firstDayNextQuarter;
            if (startDate.isAfter(endDate))
                break;
        }
        return list;
    }

    // thống kê theo năm
    @Override
    public List<FacilitiesQuantityInYear> getYearQuantityOfFacilitiesWithTime(int startYear, int endYear) {
        List<FacilitiesQuantityInYear> list = new ArrayList<>();
        if (startYear > endYear) {
            throw new DataAccessResourceFailureException("Năm bắt đầu và năm kết thúc không hợp lệ .Vui lòng nhập lại");
        }
        if (startYear == endYear) {
            List<FacilitiesQuantity> data = this.getYearQuantityFacilitiesWithYear(endYear);
            FacilitiesQuantityInYear tmp = new FacilitiesQuantityInYear(endYear, data);
            list.add(tmp);
            return list;
        }
        while (true) {
            FacilitiesQuantityInYear tmp = new FacilitiesQuantityInYear();
            List<FacilitiesQuantity> data = this.getYearQuantityFacilitiesWithYear(startYear);
            tmp.setYear(startYear);
            tmp.setData(data);
            list.add(tmp);
            int nextYear = startYear + 1;
            if (nextYear == endYear) {
                List<FacilitiesQuantity> data2 = this.getYearQuantityFacilitiesWithYear(endYear);
                FacilitiesQuantityInYear tmp2 = new FacilitiesQuantityInYear(endYear, data2);
                list.add(tmp2);
                break;
            }
            startYear = nextYear;
        }
        return list;
    }

}
