package com.example.sick.api.model.response;

import java.util.List;

public record CarModelResponse(
        List<CarModel> carModels
) {
    public record CarModel(
            int id,
            String name
    ) {
    }
}
