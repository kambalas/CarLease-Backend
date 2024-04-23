package com.example.sick.repository;
import com.example.sick.domain.ApplicationListDAORequest;
import com.example.sick.domain.ApplicationListDAOResponse;

import java.util.List;

public interface ApplicationListRepositoryInterface {

    List<ApplicationListDAOResponse> sortAndFilterByStatus(ApplicationListDAORequest applicationListRequest);
    List<ApplicationListDAOResponse> sortApplicationsByTimestamp(ApplicationListDAORequest applicationListRequest);
    List<ApplicationListDAOResponse> sortAndFilterBySearchQuery(ApplicationListDAORequest applicationListRequest);
    List<ApplicationListDAOResponse> sortAndFilterByStatusAndSearchQuery(ApplicationListDAORequest applicationListRequest);
}
