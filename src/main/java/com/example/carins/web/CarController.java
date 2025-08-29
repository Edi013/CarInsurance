package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.service.CarService;
import com.example.carins.service.CarServiceImpl;
import com.example.carins.service.InsurancePolicyService;
import com.example.carins.service.InsurancePolicyServiceImpl;
import com.example.carins.web.dto.CarDto;
import com.example.carins.web.dto.InsurancePolicyDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class CarController {

    private final CarService carService;
    private final InsurancePolicyService insuranceService;


    public CarController(@Qualifier("carServiceImpl") CarService carService, @Qualifier("insurancePolicyServiceImpl") InsurancePolicyService insuranceService) {
        this.carService = carService;
        this.insuranceService = insuranceService;
    }

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return carService.listCars().stream().map(this::toDto).toList();
    }

    @GetMapping("/cars/{carId}/insurance-valid")
    public ResponseEntity<?> isInsuranceValid(
            @PathVariable Long carId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        boolean valid = carService.isInsuranceValid(carId, date);
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, date.toString(), valid));
    }

    @PostMapping("/cars/{carId}/claims")
    public ResponseEntity<?> createInsurance(
            @PathVariable Long carId,
            @RequestBody @Valid CreateInsuranceRequest request) {
        InsurancePolicyDto result = insuranceService.createInsurance(carId, request.description, request.claimDate, request.amount);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(result);
    }

    @GetMapping("/cars/{carId}/history")
    public ResponseEntity<?> getHistoryByCar(@PathVariable Long carId){
        List<InsurancePolicyDto> history = carService.getHistory(carId);
        return ResponseEntity.ok(history);
    }

    private CarDto toDto(Car car) {
        var owner = car.getOwner();
        return new CarDto(car.getId(), car.getVin(), car.getMake(), car.getModel(), car.getYearOfManufacture(),
                owner != null ? owner.getId() : null,
                owner != null ? owner.getName() : null,
                owner != null ? owner.getEmail() : null);
    }

    public record CreateInsuranceRequest(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate claimDate,
            @Size(min = 3) String description,
            @Min(1) @Max(120) int amount
    ) {}

    public record InsuranceValidityResponse(Long carId, String date, boolean valid) {}
}

