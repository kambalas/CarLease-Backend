package com.example.sick.repository.inteface;

import com.example.sick.api.model.response.GeneralFormsResponse;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface GeneralFormsInterface {

    public List<GeneralFormsResponse> getAllGeneralForms();

    public Optional<GeneralFormsResponse> getGeneralFormById(BigInteger id);

}
