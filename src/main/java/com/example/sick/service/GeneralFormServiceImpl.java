package com.example.sick.service;

import com.example.sick.api.model.exception.ApplicationNotFoundException;
import com.example.sick.api.model.request.GeneralFormsRequest;
import com.example.sick.domain.LeaseAndRatesDAORequest;
import com.example.sick.domain.PersonalInformationDAORequest;
import com.example.sick.api.model.response.GeneralFormsResponse;
import com.example.sick.domain.LeaseAndRatesDAOResponse;
import com.example.sick.api.model.response.LeaseResponse;
import com.example.sick.domain.PersonalInformationDAOResponse;
import com.example.sick.api.model.response.PersonalInformationResponse;
import com.example.sick.api.model.response.RatesResponse;
import com.example.sick.repository.LeaseAndRatesRepository;
import com.example.sick.repository.PersonalInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class GeneralFormServiceImpl implements GeneralFormService {

    ArrayList<Integer> PERIOD_VALUE = new ArrayList<>(Arrays.asList(3, 4, 5, 6, 12, 24, 36, 48, 60, 72));
    ArrayList<Integer> RESIDUAL_VALUE_PERCENTAGES = new ArrayList<>(Arrays.asList(0, 5, 10, 15, 20, 25, 30));
    String MAIL_SUBJECT = "TLizingas Loan";
    String MAIL_BODY = """
            Hey there!,
                            
            Thank you for using the TLizingas loan calculator!
            We've successfully received your application.
                            
            TLizingas staff will get in touch with you shortly!
                            
            Have a great day!
            TLizingas Team
            """;


    LeaseAndRatesRepository leaseAndRatesRepository;
    PersonalInformationRepository personalInformationRepository;
    EmailService emailService;

    public GeneralFormServiceImpl(LeaseAndRatesRepository leaseAndRatesRepository, PersonalInformationRepository personalInformationRepository, EmailService emailService) {
        this.leaseAndRatesRepository = leaseAndRatesRepository;
        this.personalInformationRepository = personalInformationRepository;
        this.emailService = emailService;
    }


    public List<GeneralFormsResponse> selectAllApplications() {

        List<LeaseAndRatesDAOResponse> leaseAndRatesDAOResponses = leaseAndRatesRepository.getAllLeaseAndRates();
        List<PersonalInformationDAOResponse> personalInformationDAOResponses = personalInformationRepository.getAllPersonalInformation();

        return leaseAndRatesDAOResponses.stream()
                .map(leaseAndRatesDAOResponse -> new GeneralFormsResponse(
                        convertDAOResponseIntoRatesResponse(leaseAndRatesDAOResponse),
                        convertDAOResponseIntoPersonalInformationResponse(personalInformationDAOResponses
                                .get(leaseAndRatesDAOResponses.indexOf(leaseAndRatesDAOResponse))),
                        convertDAOResponseIntoLeaseResponse(leaseAndRatesDAOResponse))).toList();

    }

    public GeneralFormsResponse getApplicationById(long id) throws ApplicationNotFoundException {

        Optional<LeaseAndRatesDAOResponse> leaseAndRatesDAOResponse = leaseAndRatesRepository.getLeaseAndRateById(id);
        Optional<PersonalInformationDAOResponse> personalInformationDAOResponse = personalInformationRepository.getPersonalInformationById(id);
        if (leaseAndRatesDAOResponse.isPresent() && personalInformationDAOResponse.isPresent()) {
            LeaseResponse leaseResponse = convertDAOResponseIntoLeaseResponse(leaseAndRatesDAOResponse.orElse(null));
            RatesResponse ratesResponse = convertDAOResponseIntoRatesResponse(leaseAndRatesDAOResponse.orElse(null));
            PersonalInformationResponse personalInformationResponse = convertDAOResponseIntoPersonalInformationResponse(personalInformationDAOResponse.orElse(null));
            return new GeneralFormsResponse(ratesResponse, personalInformationResponse, leaseResponse);
        }
        throw new ApplicationNotFoundException(id);

    }

    public void createApplication(GeneralFormsRequest generalFormsRequest) {


        PersonalInformationDAORequest personalInformationDAORequest = convertGeneralFormsRequestIntoPersonalInformationRequest(generalFormsRequest);
        LeaseAndRatesDAORequest leaseAndRatesDAORequest = convertGeneralFormsRequestIntoLeaseAndRatesRequest(generalFormsRequest);
        try {
            validatePersonalInformation(personalInformationDAORequest);
            validateLeaseAndRatesResponse(leaseAndRatesDAORequest);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        long pid = personalInformationRepository.createPersonalInformation(personalInformationDAORequest);
        if (pid != 0) {
            leaseAndRatesRepository.createLeaseAndRate(leaseAndRatesDAORequest, pid);
            try {
                emailService.sendMail(personalInformationDAORequest.email(), MAIL_SUBJECT, MAIL_BODY);
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to send email: " + e.getMessage());
            }
        }
    }

    private PersonalInformationDAORequest convertGeneralFormsRequestIntoPersonalInformationRequest(GeneralFormsRequest generalFormsRequest) {
        return new PersonalInformationDAORequest(
                generalFormsRequest.personalInformationRequest().firstName(),
                generalFormsRequest.personalInformationRequest().lastName(),
                generalFormsRequest.personalInformationRequest().email(),
                generalFormsRequest.personalInformationRequest().phoneNumber(),
                generalFormsRequest.personalInformationRequest().pid(),
                generalFormsRequest.personalInformationRequest().dateOfBirth(),
                generalFormsRequest.personalInformationRequest().maritalStatus(),
                generalFormsRequest.personalInformationRequest().numberOfChildren(),
                generalFormsRequest.personalInformationRequest().citizenship(),
                generalFormsRequest.personalInformationRequest().monthlyIncome()
        );
    }

    private LeaseAndRatesDAORequest convertGeneralFormsRequestIntoLeaseAndRatesRequest(GeneralFormsRequest generalFormsRequest) {
        return new LeaseAndRatesDAORequest(
                generalFormsRequest.leaseRequest().make(),
                generalFormsRequest.leaseRequest().model(),
                generalFormsRequest.leaseRequest().modelVariant(),
                generalFormsRequest.leaseRequest().year(),
                generalFormsRequest.leaseRequest().fuelType(),
                generalFormsRequest.leaseRequest().enginePower(),
                generalFormsRequest.leaseRequest().engineSize(),
                generalFormsRequest.leaseRequest().url(),
                generalFormsRequest.leaseRequest().offer(),
                generalFormsRequest.leaseRequest().terms(),
                generalFormsRequest.leaseRequest().confirmation(),
                generalFormsRequest.ratesRequest().carValue(),
                generalFormsRequest.ratesRequest().period(),
                generalFormsRequest.ratesRequest().downPayment(),
                generalFormsRequest.ratesRequest().residualValuePercentage(),
                generalFormsRequest.ratesRequest().isEcoFriendly(),
                generalFormsRequest.ratesRequest().monthlyPayment()
        );
    }

    private PersonalInformationResponse convertDAOResponseIntoPersonalInformationResponse(PersonalInformationDAOResponse personalInformationDAOResponse) {

        return new PersonalInformationResponse(
                personalInformationDAOResponse.id(),
                personalInformationDAOResponse.firstName(),
                personalInformationDAOResponse.lastName(),
                personalInformationDAOResponse.email(),
                personalInformationDAOResponse.phoneNumber(),
                personalInformationDAOResponse.pid(),
                personalInformationDAOResponse.dateOfBirth(),
                personalInformationDAOResponse.maritalStatus(),
                personalInformationDAOResponse.numberOfChildren(),
                personalInformationDAOResponse.citizenship(),
                personalInformationDAOResponse.monthlyIncome());
    }

    private LeaseResponse convertDAOResponseIntoLeaseResponse(LeaseAndRatesDAOResponse leaseAndRatesDAOResponse) {

        return new LeaseResponse(
                leaseAndRatesDAOResponse.id(),
                leaseAndRatesDAOResponse.make(),
                leaseAndRatesDAOResponse.model(),
                leaseAndRatesDAOResponse.modelVariant(),
                leaseAndRatesDAOResponse.year(),
                leaseAndRatesDAOResponse.fuelType(),
                leaseAndRatesDAOResponse.enginePower(),
                leaseAndRatesDAOResponse.engineSize(),
                leaseAndRatesDAOResponse.url(),
                leaseAndRatesDAOResponse.offer(),
                leaseAndRatesDAOResponse.terms(),
                leaseAndRatesDAOResponse.confirmation()
        );

    }

    private RatesResponse convertDAOResponseIntoRatesResponse(LeaseAndRatesDAOResponse leaseAndRatesDAOResponse) {
        return new RatesResponse(
                leaseAndRatesDAOResponse.id(),
                leaseAndRatesDAOResponse.carValue(),
                leaseAndRatesDAOResponse.period(),
                leaseAndRatesDAOResponse.downPayment(),
                leaseAndRatesDAOResponse.residualValuePercentage(),
                leaseAndRatesDAOResponse.isEcoFriendly(),
                leaseAndRatesDAOResponse.monthlyPayment()

        );
    }

    private void validatePersonalInformation(PersonalInformationDAORequest personalInformationDAORequest) {

        if (personalInformationDAORequest.firstName() == null || personalInformationDAORequest.firstName().isEmpty() || !personalInformationDAORequest.firstName().toLowerCase().matches("[a-z\\-]+")) {
            throw new IllegalArgumentException("Invalid first name.");
        }

        if (personalInformationDAORequest.lastName() == null || personalInformationDAORequest.lastName().isEmpty() || !personalInformationDAORequest.lastName().toLowerCase().matches("[a-z\\-]+")) {
            throw new IllegalArgumentException("Invalid last name.");
        }

        if (personalInformationDAORequest.email() == null || personalInformationDAORequest.email().isEmpty() || !personalInformationDAORequest.email().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email.");
        }

        if (personalInformationDAORequest.phoneNumber() == null || personalInformationDAORequest.phoneNumber().isEmpty() || !personalInformationDAORequest.phoneNumber().matches("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")) {
            throw new IllegalArgumentException("Invalid phone number.");
        }

        if (personalInformationDAORequest.pid() < 0) {
            throw new IllegalArgumentException("PID must be non-negative.");
        }

        if (personalInformationDAORequest.dateOfBirth() == null || personalInformationDAORequest.dateOfBirth().after(new Date())) {
            throw new IllegalArgumentException("Invalid date of birth.");
        }

        List<String> validStatuses = List.of("Single", "Married", "Partnership");
        if (!validStatuses.contains(personalInformationDAORequest.maritalStatus())) {
            throw new IllegalArgumentException("Invalid marital status.");
        }


        if (personalInformationDAORequest.numberOfChildren() < 0) {
            throw new IllegalArgumentException("Number of children cannot be negative.");
        }


        List<String> europeanUnionCountries = List.of(
                "Austria", "Belgium", "Bulgaria", "Croatia", "Cyprus", "Czech Republic",
                "Denmark", "Estonia", "Finland", "France", "Germany", "Greece", "Hungary",
                "Ireland", "Italy", "Latvia", "Lithuania", "Luxembourg", "Malta", "Netherlands",
                "Poland", "Portugal", "Romania", "Slovakia", "Slovenia", "Spain", "Sweden"
        );

        if (!europeanUnionCountries.contains(personalInformationDAORequest.citizenship())) {
            throw new IllegalArgumentException("Invalid citizenship.");
        }


        if (personalInformationDAORequest.monthlyIncome() == null || personalInformationDAORequest.monthlyIncome().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly income must be non-negative.");
        }
    }

    public void validateLeaseAndRatesResponse(LeaseAndRatesDAORequest request) {

        if (request.make() == null || request.make().isEmpty()) {
            throw new IllegalArgumentException("Make cannot be null or empty.");
        }

        if (request.model() == null || request.model().isEmpty()) {
            throw new IllegalArgumentException("Model cannot be null or empty.");
        }

        if (request.modelVariant() == null || request.modelVariant().isEmpty()) {
            throw new IllegalArgumentException("Model variant cannot be null or empty.");
        }

        if (request.year() == null || request.year().isEmpty() || !request.year().matches("\\d{4}")) {
            throw new IllegalArgumentException("Invalid year format. Year must be 4 digits.");
        }

        if (request.fuelType() == null || request.fuelType().isEmpty()) {
            throw new IllegalArgumentException("Fuel type cannot be null or empty.");
        }

        if (request.enginePower() == null || request.enginePower() < 0) {
            throw new IllegalArgumentException("Engine power must be non-negative.");
        }

        if (request.engineSize() == null || request.engineSize() < 0) {
            throw new IllegalArgumentException("Engine size must be non-negative.");
        }

        if (!request.url().isEmpty() && !request.url().matches("^(http|https)://.*")) {
            throw new IllegalArgumentException("Invalid URL.");
        }

        if (!request.terms() || !request.confirmation()) {
            throw new IllegalArgumentException("Terms and confirmation must not be null.");
        }

        if (request.carValue() == null || request.carValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Car value must be greater than zero.");
        }

        if (!PERIOD_VALUE.contains((request.period()))) {
            throw new IllegalArgumentException("Lease period incorrect.");
        }

        if (request.downPayment() == null || request.downPayment().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Down payment must be non-negative.");
        }

        if (!RESIDUAL_VALUE_PERCENTAGES.contains(request.residualValuePercentage())) {
            throw new IllegalArgumentException("Residual value is incorrect");
        }

        if (request.monthlyPayment() == null || request.monthlyPayment().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monthly payment must be greater than zero.");
        }

        if (request.isEcoFriendly() == null) {
            throw new IllegalArgumentException("Eco-friendly status must not be null.");
        }
    }

}