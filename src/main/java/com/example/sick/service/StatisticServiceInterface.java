package com.example.sick.service;

import com.example.sick.api.model.response.ApplicationDailyCountResponse;
import com.example.sick.api.model.response.HighRiskResponse;
import com.example.sick.api.model.response.StatusCountResponse;

import java.util.List;

public interface StatisticServiceInterface {
    StatusCountResponse getApplicationStatusCount();
    List<ApplicationDailyCountResponse> getApplicationCurrentMonthCount();
    HighRiskResponse getHighRiskApplicationCount();
}
