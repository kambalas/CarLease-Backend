package com.example.sick.repository.mapper;

import com.example.sick.api.model.response.CarModelInfoResponse;
import com.example.sick.domain.CarEngineDataAPIResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CarModelInfoMapper {
    public CarModelInfoResponse mapFrom(CarEngineDataAPIResponse engineDataResponse) {

        Set<String> names = new HashSet<>();
        List<CarModelInfoResponse.Variant> variants = engineDataResponse.data().stream()
                .map(engineData -> new CarModelInfoResponse.Variant(
                        engineData.make_model_trim().id(),
                        engineData.make_model_trim().name()))
                .filter(variant -> names.add(variant.name()))
                .collect(Collectors.toList());

        List<Integer> years = engineDataResponse.data().stream()
                .map(engineData -> engineData.make_model_trim().year())
                .distinct()
                .collect(Collectors.toList());

        List<String> fuelTypes = engineDataResponse.data().stream()
                .map(CarEngineDataAPIResponse.EngineData::engine_type)
                .distinct()
                .collect(Collectors.toList());

        List<Integer> enginePowers = engineDataResponse.data().stream()
                .map(CarEngineDataAPIResponse.EngineData::horsepower_hp)
                .distinct()
                .collect(Collectors.toList());

        List<Double> engineSizes = engineDataResponse.data().stream()
                .map(engineData -> Double.parseDouble(engineData.size()))
                .distinct()
                .collect(Collectors.toList());

        return new CarModelInfoResponse(variants,
                                        years,
                                        fuelTypes,
                                        enginePowers,
                                        engineSizes);
    }
}