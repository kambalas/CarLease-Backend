package com.example.sick.api.controller;

import com.example.sick.api.model.exception.ApplicationNotFoundException;
import com.example.sick.api.model.response.LeaseResponse;
import com.example.sick.domain.LeaseAndRatesDAOResponse;
import com.example.sick.repository.LeaseAndRatesRepository;
import com.example.sick.service.GeneralFormService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class LeaseController {
    private final LeaseAndRatesRepository leaseAndRatesRepository;

    GeneralFormService generalFormsService;

    public LeaseController(LeaseAndRatesRepository leaseAndRatesRepository, GeneralFormService generalFormsService) {
      this.leaseAndRatesRepository = leaseAndRatesRepository;
      this.generalFormsService = generalFormsService;
    }

    @GetMapping("/lease/{id}")
    public LeaseResponse getLeaseById(@PathVariable long id) throws ApplicationNotFoundException {
        return generalFormsService.getLeaseInformationById(id);
    }
    @GetMapping("/get-pdf/{id}")
    public ResponseEntity<byte[]> getPdf(@PathVariable Long id) {
        Optional<LeaseAndRatesDAOResponse> leaseOptional = leaseAndRatesRepository.getLeaseAndRateById(id);
            LeaseAndRatesDAOResponse lease = leaseOptional.get();

        byte[] decodedBytes;
        if (lease.offer() != null && !lease.offer().isEmpty()) {
            decodedBytes = Base64.getDecoder().decode(lease.offer());
        } else {
            throw new IllegalStateException("No PDF data available for lease ID: " + id);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + lease.offer() + ".pdf\"")
                .body(decodedBytes);
    }
}
