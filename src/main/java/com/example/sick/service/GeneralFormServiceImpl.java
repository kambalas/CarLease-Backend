package com.example.sick.service;

import com.example.sick.api.model.exception.ApplicationNotFoundException;
import com.example.sick.api.model.request.ApplicationListRequest;
import com.example.sick.api.model.request.GeneralFormsRequest;
import com.example.sick.api.model.response.ApplicationListResponse;
import com.example.sick.api.model.response.GeneralAllFormsResponse;
import com.example.sick.api.model.response.StatusResponse;
import com.example.sick.domain.ApplicationListDAORequest;
import com.example.sick.domain.ApplicationListDAOResponse;
import com.example.sick.domain.LeaseAndRatesDAORequest;
import com.example.sick.domain.PersonalInformationDAORequest;
import com.example.sick.api.model.response.GeneralFormsResponse;
import com.example.sick.domain.LeaseAndRatesDAOResponse;
import com.example.sick.api.model.response.LeaseResponse;
import com.example.sick.domain.PersonalInformationDAOResponse;
import com.example.sick.api.model.response.PersonalInformationResponse;
import com.example.sick.api.model.response.RatesResponse;
import com.example.sick.domain.StatusDAOResponse;
import com.example.sick.repository.ApplicationListRepository;
import com.example.sick.repository.LeaseAndRatesRepository;
import com.example.sick.repository.PersonalInformationRepository;
import com.example.sick.repository.StatusRepository;
import com.example.sick.utils.ApplicationStatus;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


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


    private final LeaseAndRatesRepository leaseAndRatesRepository;
    private final PersonalInformationRepository personalInformationRepository;
    private final StatusRepository statusRepository;
    private final ApplicationListRepository applicationListRepository;
    private final EmailService emailService;


    public GeneralFormServiceImpl(LeaseAndRatesRepository leaseAndRatesRepository, PersonalInformationRepository personalInformationRepository, EmailService emailService, StatusRepository statusRepository, ApplicationListRepository applicationListRepository) {
        this.leaseAndRatesRepository = leaseAndRatesRepository;
        this.personalInformationRepository = personalInformationRepository;
        this.emailService = emailService;
        this.statusRepository = statusRepository;
        this.applicationListRepository = applicationListRepository;
    }

    @Override
    public List<GeneralAllFormsResponse> selectAllApplicationsByPage(long id) {

        List<LeaseAndRatesDAOResponse> leaseAndRatesDAOResponses = leaseAndRatesRepository.getAllLeaseAndRatesByPage(id);
        List<PersonalInformationDAOResponse> personalInformationDAOResponses = personalInformationRepository.getAllPersonalInformationByPage(id);
        List<StatusDAOResponse> statusDaoResponses = statusRepository.getAllStatusByPage(id);
        return leaseAndRatesDAOResponses.stream()
                .map(leaseAndRatesDAOResponse -> new GeneralAllFormsResponse(
                        convertDAOResponseIntoRatesResponse(leaseAndRatesDAOResponse),
                        convertDAOResponseIntoPersonalInformationResponse(personalInformationDAOResponses
                                .get(leaseAndRatesDAOResponses.indexOf(leaseAndRatesDAOResponse))),
                        convertDAOResponseIntoLeaseResponse(leaseAndRatesDAOResponse),
                        convertDAOResponseIntoStatusResponse(statusDaoResponses
                                .get(leaseAndRatesDAOResponses.indexOf(leaseAndRatesDAOResponse))))).toList();

    }


    @Override
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

    @Override
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
            statusRepository.createStatus(pid);
            try {
                emailService.sendMail(personalInformationDAORequest.email(), MAIL_SUBJECT, MAIL_BODY);
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to send email: " + e.getMessage());
            }
        }
    }

    @Override
    public List<ApplicationListResponse> sortApplications(ApplicationListRequest applicationListRequest) {
        ApplicationListDAORequest applicationListDAORequest = convertRequestIntoDAORequest(applicationListRequest);
        if (applicationListRequest.STATUS() == null && applicationListRequest.searchQuery() == null) {
            return applicationListRepository.sortApplicationsByTimestamp(applicationListDAORequest).stream()
                    .map(this::convertApplicationListDAOResponseIntoResponse).toList();
        }
        if (applicationListRequest.STATUS() != null && applicationListRequest.searchQuery() != null) {
            return applicationListRepository.sortAndFilterByStatusAndSearchQuery(applicationListDAORequest).stream()
                    .map(this::convertApplicationListDAOResponseIntoResponse).toList();
        }
        if (applicationListRequest.STATUS() != null && applicationListRequest.searchQuery() == null) {
            return applicationListRepository.sortAndFilterByStatus(applicationListDAORequest).stream()
                    .map(this::convertApplicationListDAOResponseIntoResponse).toList();
        } else {
            return applicationListRepository.sortAndFilterBySearchQuery(applicationListDAORequest).stream()
                    .map(this::convertApplicationListDAOResponseIntoResponse).toList();
        }
    }


    public PersonalInformationResponse getPersonalInformationById(long id) throws ApplicationNotFoundException {
        Optional<PersonalInformationDAOResponse> personalInformationDAOResponse = personalInformationRepository.getPersonalInformationById(id);
        if (personalInformationDAOResponse.isPresent()) {
            return convertDAOResponseIntoPersonalInformationResponse(personalInformationDAOResponse.orElse(null));
        }
        throw new ApplicationNotFoundException(id);
    }

    public LeaseResponse getLeaseInformationById(long id) throws ApplicationNotFoundException {
        Optional<LeaseAndRatesDAOResponse> leaseAndRatesDAOResponse = leaseAndRatesRepository.getLeaseAndRateById(id);
        if (leaseAndRatesDAOResponse.isPresent()) {
            return convertDAOResponseIntoLeaseResponse(leaseAndRatesDAOResponse.orElse(null));
        }
        throw new ApplicationNotFoundException(id);
    }

    public RatesResponse getRatesInformationById(long id) throws ApplicationNotFoundException {
        Optional<LeaseAndRatesDAOResponse> leaseAndRatesDAOResponse = leaseAndRatesRepository.getLeaseAndRateById(id);
        if (leaseAndRatesDAOResponse.isPresent()) {
            return convertDAOResponseIntoRatesResponse(leaseAndRatesDAOResponse.orElse(null));
        }
        throw new ApplicationNotFoundException(id);
    }

    public ApplicationListResponse convertApplicationListDAOResponseIntoResponse(ApplicationListDAOResponse applicationListDAOResponse) {
        return new ApplicationListResponse(
                applicationListDAOResponse.id(),
                applicationListDAOResponse.firstName(),
                applicationListDAOResponse.lastName(),
                applicationListDAOResponse.isOpened(),
                applicationListDAOResponse.status(),
                applicationListDAOResponse.updatedAt()
        );
    }

    private ApplicationListDAORequest convertRequestIntoDAORequest(ApplicationListRequest applicationListRequest) {
        if (applicationListRequest.searchQuery() != null && applicationListRequest.STATUS() != null) {
            return new ApplicationListDAORequest(
                    applicationListRequest.page(),
                    applicationListRequest.STATUS().stream().map(ApplicationStatus::toString).toList(),
                    applicationListRequest.searchQuery());
        } else if (applicationListRequest.STATUS() != null) {
            return new ApplicationListDAORequest(
                    applicationListRequest.page(),
                    applicationListRequest.STATUS().stream().map(ApplicationStatus::toString).toList(),
                    null);
        } else {
            return new ApplicationListDAORequest(
                    applicationListRequest.page(),
                    null,
                    applicationListRequest.searchQuery());
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

    private StatusResponse convertDAOResponseIntoStatusResponse(StatusDAOResponse statusDAOResponse) {
        return new StatusResponse(
                statusDAOResponse.id(),
                statusDAOResponse.APPLICATIONSTATUS(),
                statusDAOResponse.isOpened(),
                statusDAOResponse.updatedAt(),
                statusDAOResponse.createdAt()
        );
    }

    private void validatePersonalInformation(PersonalInformationDAORequest personalInformationDAORequest) {

        if (personalInformationDAORequest.firstName() == null || personalInformationDAORequest.firstName().isEmpty() || !personalInformationDAORequest.firstName().toLowerCase().matches("^[A-Za-zÀ-ÖØ-öø-ſƀ-ƺǍ-ǥǦ-ǳǴ-ǵǶ-ȟȠ-ȯȱ-ȳȴ-ɏ-]\\D*$")) {
            throw new IllegalArgumentException("Invalid first name.");
        }

        if (personalInformationDAORequest.lastName() == null || personalInformationDAORequest.lastName().isEmpty() || !personalInformationDAORequest.lastName().toLowerCase().matches("^[A-Za-zÀ-ÖØ-öø-ſƀ-ƺǍ-ǥǦ-ǳǴ-ǵǶ-ȟȠ-ȯȱ-ȳȴ-ɏ-]\\D*$")) {
            throw new IllegalArgumentException("Invalid last name.");
        }

        if (personalInformationDAORequest.email() == null || personalInformationDAORequest.email().isEmpty() || !personalInformationDAORequest.email().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email.");
        }

        if (personalInformationDAORequest.phoneNumber() == null || personalInformationDAORequest.phoneNumber().isEmpty() || !personalInformationDAORequest.phoneNumber().matches("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")) {
            throw new IllegalArgumentException("Invalid phone number.");
        }

        if (personalInformationDAORequest.pid() == null || personalInformationDAORequest.pid().isEmpty()) {
            throw new IllegalArgumentException("PID must not be empty.");
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
