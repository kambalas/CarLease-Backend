package com.example.sick.service;

import com.example.sick.api.model.response.GeneralFormsResponse;

import java.util.List;

public interface GeneralFormService {
    List<GeneralFormsResponse> selectAllApplications();
}
