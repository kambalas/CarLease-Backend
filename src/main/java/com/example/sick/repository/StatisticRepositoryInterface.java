package com.example.sick.repository;

import com.example.sick.domain.AcceptedApplicationDAOResponse;
import com.example.sick.domain.ApplicationDailyCountDAOResponse;
import com.example.sick.domain.ApplicationMonthlyCountDAOResponse;
import com.example.sick.domain.HighRiskDAOResponse;
import com.example.sick.domain.StatusCountDAOResponse;

import java.util.List;

public interface StatisticRepositoryInterface {
    StatusCountDAOResponse countStatus();
    List<ApplicationDailyCountDAOResponse> countApplicationsCurrentMonthDaily();
    HighRiskDAOResponse countHighRiskApplications();
    AcceptedApplicationDAOResponse countAcceptedApplicationsTotalSum();
    ApplicationMonthlyCountDAOResponse getApplicationMonthlyCount();

}
