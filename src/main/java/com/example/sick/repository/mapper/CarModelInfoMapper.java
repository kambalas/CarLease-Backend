package com.example.sick.repository.mapper;

import com.example.sick.api.model.response.CarModelInfoResponse;
import com.example.sick.domain.EngineDataAPIResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CarModelInfoMapper {
    public CarModelInfoResponse mapFrom(EngineDataAPIResponse engineDataResponse) {

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

        return new CarModelInfoResponse(
                variants,
                years,
                fuelTypes,
                enginePowers,
                engineSizes);
    }
}