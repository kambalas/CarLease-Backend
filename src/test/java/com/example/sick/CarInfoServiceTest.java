package com.example.sick;

import com.example.sick.api.model.response.CarMakeResponse;
import com.example.sick.api.model.response.CarModelInfoResponse;
import com.example.sick.api.model.response.CarModelResponse;
import com.example.sick.api.model.response.CarVariantInfoResponse;
import com.example.sick.domain.CarMakeAPIResponse;
import com.example.sick.domain.CarModelAPIResponse;
import com.example.sick.domain.EngineDataAPIResponse;
import com.example.sick.repository.CarInfoRepository;
import com.example.sick.service.CarInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarInfoServiceTest {

    @InjectMocks
    private CarInfoService carInfoService;

    @Mock
    private CarInfoRepository carInfoRepository;

    @Test
    public void testGetCarMakesReturnsListOfMakes() throws JsonProcessingException {
        Map<String, Object> collection = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> carMake1 = new HashMap<>();

        carMake1.put("id", 1);
        carMake1.put("name", "Acura");
        data.add(carMake1);

        Map<String, Object> carMake2 = new HashMap<>();
        carMake2.put("id", 24);
        carMake2.put("name", "Alfa Romeo");
        data.add(carMake2);

        CarMakeAPIResponse mockResponse = new CarMakeAPIResponse(collection, data);

        when(carInfoRepository.getCarMakes()).thenReturn(mockResponse);

        CarMakeResponse response = carInfoService.getCarMakes();

        List<String> expectedMakes = Arrays.asList("Acura", "Alfa Romeo");
        assertEquals(expectedMakes, response.carMakes());
    }

    @Test
    public void testGetModelsForMakeReturnsListOfModels() throws JsonProcessingException {
        CarModelAPIResponse mockResponse = getMockModelAPIResponse();

        when(carInfoRepository.getCarModels("Toyota")).thenReturn(mockResponse);
        CarModelResponse response = carInfoService.getModelsForMake("Toyota");

        List<CarModelResponse.CarModel> expectedModels = Arrays.asList(
                new CarModelResponse.CarModel(360, "4Runner"),
                new CarModelResponse.CarModel(458, "86")
        );
        assertEquals(expectedModels, response.carModels());
    }

    @Test
    public void testGetModelInfoReturnsModelVariantsAndInfo() throws JsonProcessingException {
        EngineDataAPIResponse mockResponse = getMockModelInfoAPIResponse();

        when(carInfoRepository.getModelEngineData(360)).thenReturn(mockResponse);
        CarModelInfoResponse response = carInfoService.getModelInfo(360);

        List<CarModelInfoResponse.Variant> expectedVariants = List.of(
                new CarModelInfoResponse.Variant(11845, "Limited"));
        List<Integer> expectedYears = List.of(2019);
        List<String> expectedFuelTypes = List.of("gas");
        List<Integer> expectedEnginePowers = List.of(270);
        List<String> expectedEngineSizes = List.of("4.0");

        assertEquals(expectedVariants, response.variants());
        assertEquals(expectedYears, response.years());
        assertEquals(expectedFuelTypes, response.fuelTypes());
        assertEquals(expectedEnginePowers, response.enginePowers());
        assertEquals(expectedEngineSizes, response.engineSizes());
    }

    @Test
    public void testGetVariantInfoReturnsVariantInfo() throws JsonProcessingException {
        EngineDataAPIResponse mockResponse = getMockVariantInfoAPIResponse();

        when(carInfoRepository.getVariantEngineData(11845)).thenReturn(mockResponse);
        CarVariantInfoResponse response = carInfoService.getVariantInfo(11845);

        List<Integer> expectedYears = List.of(2019);
        List<String> expectedFuelTypes = List.of("gas");
        List<Integer> expectedEnginePowers = List.of(270);
        List<String> expectedEngineSizes = List.of("4.0");

        assertEquals(expectedYears, response.years());
        assertEquals(expectedFuelTypes, response.fuelTypes());
        assertEquals(expectedEnginePowers, response.enginePowers());
        assertEquals(expectedEngineSizes, response.engineSizes());
    }

    private static CarModelAPIResponse getMockModelAPIResponse() {
        Map<String, Object> collection = new HashMap<>();
        List<CarModelAPIResponse.APIResponseModelData> data = new ArrayList<>();

        CarModelAPIResponse.APIResponseModelData carModel1 =
                new CarModelAPIResponse.APIResponseModelData(360, 22, "4Runner");
        data.add(carModel1);
        CarModelAPIResponse.APIResponseModelData carModel2
                = new CarModelAPIResponse.APIResponseModelData(458, 22, "86");
        data.add(carModel2);

        return new CarModelAPIResponse(collection, data);
    }

    private static EngineDataAPIResponse getMockModelInfoAPIResponse() {
        Map<String, Object> collection = new HashMap<>();
        List<EngineDataAPIResponse.EngineData> data = new ArrayList<>();

        EngineDataAPIResponse.MakeModelTrim makeModelTrim = new EngineDataAPIResponse.MakeModelTrim(
                11845, 360, 2019, "Limited", "Limited 4dr SUV (4.0L 6cyl 5A)", 43625, 40134,
                "2023-06-29T21:01:10-04:00", "2023-06-29T21:01:10-04:00",
                new EngineDataAPIResponse.MakeModel(360, 22, "4Runner",
                        new EngineDataAPIResponse.Make(22, "Toyota")));

        EngineDataAPIResponse.EngineData engineData = new EngineDataAPIResponse.EngineData(
                11845, 11845, "gas", "regular unleaded", "V6", "4.0", 270, 5600, 278, 4400, 24,
                "Variable", "Double overhead cam (DOHC)", "rear wheel drive", "5-speed shiftable automatic", makeModelTrim);

        data.add(engineData);

        return new EngineDataAPIResponse(collection, data);
    }

    private static EngineDataAPIResponse getMockVariantInfoAPIResponse() {
        Map<String, Object> collection = new HashMap<>();
        List<EngineDataAPIResponse.EngineData> data = new ArrayList<>();

        EngineDataAPIResponse.MakeModelTrim makeModelTrim = new EngineDataAPIResponse.MakeModelTrim(
                11845, 360, 2019, "Limited", "Limited 4dr SUV (4.0L 6cyl 5A)", 43625, 40134,
                "2023-06-29T21:01:10-04:00", "2023-06-29T21:01:10-04:00",
                new EngineDataAPIResponse.MakeModel(360, 22, "4Runner",
                        new EngineDataAPIResponse.Make(22, "Toyota")));

        EngineDataAPIResponse.EngineData engineData = new EngineDataAPIResponse.EngineData(
                11845, 11845, "gas", "regular unleaded", "V6", "4.0", 270, 5600, 278, 4400, 24,
                "Variable", "Double overhead cam (DOHC)", "rear wheel drive", "5-speed shiftable automatic", makeModelTrim);

        data.add(engineData);

        return new EngineDataAPIResponse(collection, data);
    }
}
