package com.example.carins;

import com.example.carins.exception.CarNotFound;
import com.example.carins.service.CarService;
import com.example.carins.service.InsurancePolicyService;
import com.example.carins.web.CarController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;

 import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean(name = "carServiceImpl")
    private CarService carService;

    @MockitoBean(name = "insurancePolicyServiceImpl")
    private InsurancePolicyService insuranceService;

    private final LocalDate validDate = LocalDate.of(2025, 8, 29);
    private final String invalidDate = "29-08-2025";

    @Test
    public void havingInvalidCarIdFormat_whenClientChecksInsuranceValidity_shouldReturn400() throws Exception {
        //Arrange
        String invalidCarId = "invalid-car-id";

        //Act
        mockMvc.perform(get("/api/cars/{carId}/insurance-valid", invalidCarId)
                        .param("date", validDate.toString())
                        .accept(MediaType.APPLICATION_JSON))
        //Assert
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Invalid path parameter: carId must be Long"));
    }

    @Test
    public void havingValidCarIdFormat_whenClientChecksInsuranceValidityAndNonExistingCar_shouldReturn404() throws Exception {
        //Arrage
        Long carId = 123L;
        when(carService.isInsuranceValid(carId, validDate))
                .thenThrow(new CarNotFound(carId));

        //Act
        mockMvc.perform(get("/api/cars/{carId}/insurance-valid", carId)
                        .param("date", validDate.toString())
                        .accept(MediaType.APPLICATION_JSON))
        //Assert
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value(String.format("Car with id %d does not exist", carId)));
    }

    @Test
    public void havingInvalidDate_whenClientChecksInsuranceValidity_shouldReturn404() throws Exception {
        //Arrage
        Long carId = 123L;

        //Act
        mockMvc.perform(get("/api/cars/{carId}/insurance-valid", carId)
                        .param("date", invalidDate)
                        .accept(MediaType.APPLICATION_JSON))
        //Assert
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("The provided date is invalid or not in yyyy-MM-dd format."));
    }

    @Test
    public void havingValidCarIdAndDate_whenCheckInsurance_thenReturn200() throws Exception {
        //Arrage
        Long carId = 1L;
        when(carService.isInsuranceValid(carId, validDate)).thenReturn(true);

        //Act
        mockMvc.perform(get("/api/cars/{carId}/insurance-valid", carId)
                        .param("date", validDate.toString())
                        .accept(MediaType.APPLICATION_JSON))
        //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId").value(carId))
                .andExpect(jsonPath("$.date").value(validDate.toString()))
                .andExpect(jsonPath("$.valid").value(true));
    }
}
