package com.example.sick.domain;

import java.util.List;
import java.util.Map;

public record EngineDataAPIResponse(
        Map<String, Object> collection,
        List<EngineData> data
) {
    public record EngineData(
            int id,
            int make_model_trim_id,
            String engine_type,
            String fuel_type,
            String cylinders,
            String size,
            int horsepower_hp,
            int horsepower_rpm,
            int torque_ft_lbs,
            int torque_rpm,
            int valves,
            String valve_timing,
            String cam_type,
            String drive_type,
            String transmission,
            MakeModelTrim make_model_trim
    ) {
    }

    public record MakeModelTrim(
            int id,
            int make_model_id,
            int year,
            String name,
            String description,
            int msrp,
            int invoice,
            String created,
            String modified,
            MakeModel make_model
    ) {
    }

    public record MakeModel(
            int id,
            int make_id,
            String name,
            Make make
    ) {
    }

    public record Make(
            int id,
            String name
    ) {
    }
}