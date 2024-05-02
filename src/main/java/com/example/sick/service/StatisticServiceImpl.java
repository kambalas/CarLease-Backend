package com.example.sick.service;

import com.example.sick.api.model.response.AcceptedApplicationResponse;
import com.example.sick.api.model.response.ApplicationDailyCountResponse;
import com.example.sick.api.model.response.ApplicationMonthlyCountResponse;
import com.example.sick.api.model.response.HighRiskResponse;
import com.example.sick.api.model.response.StatusCountResponse;
import com.example.sick.domain.AcceptedApplicationDAOResponse;
import com.example.sick.domain.ApplicationDailyCountDAOResponse;
import com.example.sick.domain.ApplicationMonthlyCountDAOResponse;
import com.example.sick.domain.HighRiskDAOResponse;
import com.example.sick.domain.StatusCountDAOResponse;
import com.example.sick.repository.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticServiceImpl {

    private final StatisticRepository statisticsRepository;

    @Autowired
    public StatisticServiceImpl(StatisticRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public StatusCountResponse getApplicationStatusCount() {
        return this.statisticCountDAOResponseToResponse(statisticsRepository.countStatus());
    }

    public List<ApplicationDailyCountResponse> getApplicationCurrentMonthDailyCount() {
        return statisticsRepository.countApplicationsCurrentMonthDaily().stream()
                .map(this::applicationDailyCountDAOResponseToResponse)
                .toList();
    }

    public HighRiskResponse getHighRiskApplicationCount() {
        return this.highRiskDAOResponseToResponse(statisticsRepository.countHighRiskApplications());
    }

    public AcceptedApplicationResponse getAcceptedApplicationsTotalSum() {
        return this.acceptedApplicationDAOResponseToResponse(statisticsRepository.countAcceptedApplicationsTotalSum());
    }

    public ApplicationMonthlyCountResponse getApplicationCurrentMonth() {
        return this.applicationMonthlyCountDAOResponseToResponse(statisticsRepository.getApplicationMonthlyCount());
    }

    private StatusCountResponse statisticCountDAOResponseToResponse(StatusCountDAOResponse statisticCountDAOResponse) {
        return new StatusCountResponse(
                statisticCountDAOResponse.newCount(),
                statisticCountDAOResponse.acceptedCount(),
                statisticCountDAOResponse.rejectedCount(),
                statisticCountDAOResponse.pendingCount()
        );
    }

    private ApplicationDailyCountResponse applicationDailyCountDAOResponseToResponse(ApplicationDailyCountDAOResponse applicationDailyCountDAOResponse) {
        return new ApplicationDailyCountResponse(
                applicationDailyCountDAOResponse.day(),
                applicationDailyCountDAOResponse.applicationCount()
        );
    }

    private HighRiskResponse highRiskDAOResponseToResponse(HighRiskDAOResponse highRiskDAOResponse) {
        return new HighRiskResponse(
                highRiskDAOResponse.currentMonthCount(),
                highRiskDAOResponse.lastMonthCount()
        );
    }

    private AcceptedApplicationResponse acceptedApplicationDAOResponseToResponse(AcceptedApplicationDAOResponse acceptedApplicationDAOResponse){
        return new AcceptedApplicationResponse(
                acceptedApplicationDAOResponse.thisYearSum(),
                acceptedApplicationDAOResponse.lastYearSum()
        );
    }

    private ApplicationMonthlyCountResponse applicationMonthlyCountDAOResponseToResponse(ApplicationMonthlyCountDAOResponse applicationMonthlyCountDAOResponse) {
        return new ApplicationMonthlyCountResponse(
                applicationMonthlyCountDAOResponse.thisMonthCount(),
                applicationMonthlyCountDAOResponse.previousMonthCount()
        );
    }


}
