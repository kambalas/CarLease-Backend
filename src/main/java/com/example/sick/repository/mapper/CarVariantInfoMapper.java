package com.example.sick.repository.mapper;

import com.example.sick.api.model.response.CarVariantInfoResponse;
import com.example.sick.domain.EngineDataAPIResponse;

import java.util.List;
import java.util.stream.Collectors;

public class CarVariantInfoMapper {
    public CarVariantInfoResponse mapFrom(EngineDataAPIResponse engineDataResponse) {

        List<Integer> years = engineDataResponse.data().stream()
                .map(engineData -> engineData.make_model_trim().year())
                .distinct()
                .collect(Collectors.toList());

        List<String> fuelTypes = engineDataResponse.data().stream()
                .map(EngineDataAPIResponse.EngineData::engine_type)
                .distinct()
                .collect(Collectors.toList());

        List<Integer> enginePowers = engineDataResponse.data().stream()
                .map(EngineDataAPIResponse.EngineData::horsepower_hp)
                .distinct()
                .collect(Collectors.toList());

        List<String> engineSizes = engineDataResponse.data().stream()
                .map(EngineDataAPIResponse.EngineData::size)
                .distinct()
                .collect(Collectors.toList());

        return new CarVariantInfoResponse(
                years,
                fuelTypes,
                enginePowers,
                engineSizes);
    }
}
