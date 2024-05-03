package com.example.sick.service;

import com.example.sick.api.model.exception.ApplicationNotFoundException;
import com.example.sick.api.model.request.ApplicationListRequest;
import com.example.sick.api.model.request.GeneralFormsRequest;
import com.example.sick.api.model.response.ApplicationListResponse;
import com.example.sick.api.model.response.GeneralAllFormsResponse;
import com.example.sick.api.model.response.GeneralFormsResponse;
import com.example.sick.api.model.response.LeaseResponse;
import com.example.sick.api.model.response.PersonalInformationResponse;
import com.example.sick.api.model.response.RatesResponse;
import com.example.sick.api.model.response.StatusResponse;
import com.example.sick.domain.ApplicationListDAORequest;
import com.example.sick.domain.ApplicationListDAOResponse;
import com.example.sick.domain.LeaseAndRatesDAORequest;
import com.example.sick.domain.LeaseAndRatesDAOResponse;
import com.example.sick.domain.PersonalInformationDAORequest;
import com.example.sick.domain.PersonalInformationDAOResponse;
import com.example.sick.domain.StatusDAOResponse;
import com.example.sick.repository.ApplicationListRepository;
import com.example.sick.repository.LeaseAndRatesRepository;
import com.example.sick.repository.PersonalInformationRepository;
import com.example.sick.repository.StatusRepository;
import com.example.sick.utils.ApplicationStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class GeneralFormServiceImpl implements GeneralFormService {

    private static final ArrayList<Integer> PERIOD_VALUE = new ArrayList<>(Arrays.asList(3, 4, 5, 6, 12, 18, 24, 36, 48, 60, 72));
    private static final ArrayList<Integer> RESIDUAL_VALUE_PERCENTAGES = new ArrayList<>(Arrays.asList(0, 5, 10, 15, 20, 25, 30));
    private static final String MAIL_SUBJECT = "Car lease application #%s";


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
    @Transactional
    public void createApplication(GeneralFormsRequest generalFormsRequest) {


        PersonalInformationDAORequest personalInformationDAORequest = convertGeneralFormsRequestIntoPersonalInformationRequest(generalFormsRequest);
        LeaseAndRatesDAORequest leaseAndRatesDAORequest = convertGeneralFormsRequestIntoLeaseAndRatesRequest(generalFormsRequest);
        try {
            validatePersonalInformation(personalInformationDAORequest);
            validateLeaseAndRatesResponse(leaseAndRatesDAORequest);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        boolean isHighRisk = validateIfHighRisk(personalInformationDAORequest, leaseAndRatesDAORequest);
        long pid = personalInformationRepository.createPersonalInformation(personalInformationDAORequest);
        if (pid != 0) {
            leaseAndRatesRepository.createLeaseAndRate(leaseAndRatesDAORequest, pid);
            statusRepository.createStatus(pid, isHighRisk);
            try {
                emailService.sendMail(personalInformationDAORequest.email(), MAIL_SUBJECT.formatted(pid), setMailTemplate(generalFormsRequest));
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to send email: " + e.getMessage());
            }
        }
    }

    @Override
    public List<ApplicationListResponse> sortApplications(ApplicationListRequest applicationListRequest) {
        ApplicationListDAORequest applicationListDAORequest = convertRequestIntoDAORequest(applicationListRequest);

        if (
                (applicationListRequest.STATUS() != null && !applicationListRequest.STATUS().isEmpty())
                && (applicationListRequest.searchQuery() != null && !applicationListRequest.searchQuery().isEmpty())
        ) {
            return applicationListRepository.sortAndFilterByStatusAndSearchQuery(applicationListDAORequest).stream()
                    .map(this::convertApplicationListDAOResponseIntoResponse).toList();

        } else if (applicationListRequest.STATUS() != null && !applicationListRequest.STATUS().isEmpty()) {
            return applicationListRepository.sortAndFilterByStatus(applicationListDAORequest).stream()
                    .map(this::convertApplicationListDAOResponseIntoResponse).toList();
        } else if (applicationListRequest.searchQuery() != null && !applicationListRequest.searchQuery().isEmpty()) {
            return applicationListRepository.sortAndFilterBySearchQuery(applicationListDAORequest).stream()
                    .map(this::convertApplicationListDAOResponseIntoResponse).toList();
        } else {
            return applicationListRepository.sortApplicationsByTimestamp(applicationListDAORequest).stream()
                    .map(this::convertApplicationListDAOResponseIntoResponse).toList();
        }
    }

    private String setMailTemplate(GeneralFormsRequest request) {
        if((request.personalInformationRequest().languagePref().equals("lithuania"))){
            return """
                    Sveiki %s %s,
                    
                    Norime pranešti, kad gavome jūsų automobilio lizingo paraišką. Labai džiaugiamės, jog pasirinkote mus!
                    
                    *Jūsų automobilio duomenys*
                    - Gamintojas: %s
                    - Modelis: %s
                    - Metai: %s
                    - Automobilio vertė: %s €
                    
                    Mūsų komanda aktyviai tvarko visas gautas paraiškas ir informuos Jus dėl lizingo paraiškos būsenos greitu metu.
                    
                    Jei turite kokių nors klausimų ar reikia pagalbos, kreipkitės į mūsų draugišką klientų aptarnavimo komandą darbo valandomis: nuo 8 iki 17 valandos.
                    
                    Dėkojame, kad pasirinkote Tarzano lizingą. Vertiname jūsų pasitikėjimą ir laukiame galimybės padėti įgyvendinti vieną iš jūsų svajonių!
                    
                    Geros dienos!
                    
                    Pagarbiai,
                    Tarzano Lizingas
                """.formatted(request.personalInformationRequest().firstName(),
                    request.personalInformationRequest().lastName(),
                    request.leaseRequest().make(),
                    request.leaseRequest().model(),
                    request.leaseRequest().year(),
                    request.ratesRequest().carValue());
        }
        else {
            return """
                    Hi %s %s,

                    Just a quick email to let you know that we've received your car lease application and it's in good hands. We're excited to have you on board!

                    **Your car details**
                    - Make: %s
                    - Model: %s
                    - Year: %s
                    - Car value: %s €

                    Our team is currently working hard to process all the information and we'll be sure to keep you updated every step of the way.

                    If you have any questions or need assistance, feel free to reach out to our friendly customer support team during our working hours from 8 to 17.

                    Thank you for choosing Tarzan leasing. We appreciate your trust and can't wait to help make your dream car a reality!

                    Have a great day!

                    Best regards,
                    T-Leasing
                    """.formatted(request.personalInformationRequest().firstName(),
                    request.personalInformationRequest().lastName(),
                    request.leaseRequest().make(),
                    request.leaseRequest().model(),
                    request.leaseRequest().year(),
                    request.ratesRequest().carValue());
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
                applicationListDAOResponse.createdAt(),
                applicationListDAOResponse.isHighRisk()
        );
    }

    private ApplicationListDAORequest convertRequestIntoDAORequest(ApplicationListRequest applicationListRequest) {
        if (null != applicationListRequest.searchQuery() && null != applicationListRequest.STATUS()) {
            return new ApplicationListDAORequest(
                    applicationListRequest.page(),
                    applicationListRequest.STATUS().stream().map(ApplicationStatus::toString).toList(),
                    applicationListRequest.searchQuery());
        } else if (null != applicationListRequest.STATUS()) {
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
                generalFormsRequest.personalInformationRequest().monthlyIncome(),
                generalFormsRequest.personalInformationRequest().languagePref()
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
                personalInformationDAOResponse.monthlyIncome(),
                personalInformationDAOResponse.languagePref());
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
                statusDAOResponse.createdAt(),
                statusDAOResponse.isHighRisk()
        );
    }

    private void validatePersonalInformation(PersonalInformationDAORequest personalInformationDAORequest) {

        if (null == personalInformationDAORequest.firstName() || personalInformationDAORequest.firstName().isEmpty() || !personalInformationDAORequest.firstName().toLowerCase().matches("^[A-Za-zÀ-ÖØ-öø-ſƀ-ƺǍ-ǥǦ-ǳǴ-ǵǶ-ȟȠ-ȯȱ-ȳȴ-ɏ-]\\D*$")) {
            throw new IllegalArgumentException("Invalid first name.");
        }

        if (null == personalInformationDAORequest.lastName() || personalInformationDAORequest.lastName().isEmpty() || !personalInformationDAORequest.lastName().toLowerCase().matches("^[A-Za-zÀ-ÖØ-öø-ſƀ-ƺǍ-ǥǦ-ǳǴ-ǵǶ-ȟȠ-ȯȱ-ȳȴ-ɏ-]\\D*$")) {
            throw new IllegalArgumentException("Invalid last name.");
        }

        if (null == personalInformationDAORequest.email() || personalInformationDAORequest.email().isEmpty() || !personalInformationDAORequest.email().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email.");
        }

        if (null == personalInformationDAORequest.phoneNumber() || personalInformationDAORequest.phoneNumber().isEmpty() || !personalInformationDAORequest.phoneNumber().matches("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")) {
            throw new IllegalArgumentException("Invalid phone number.");
        }

        if (null == personalInformationDAORequest.pid() || personalInformationDAORequest.pid().isEmpty()) {
            throw new IllegalArgumentException("PID must not be empty.");
        }

        if (null == personalInformationDAORequest.dateOfBirth() || personalInformationDAORequest.dateOfBirth().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid date of birth.");
        }

        List<String> validStatuses = List.of("Single", "Married", "Partnership");
        if (!validStatuses.contains(personalInformationDAORequest.maritalStatus())) {
            throw new IllegalArgumentException("Invalid marital status.");
        }


        if (personalInformationDAORequest.numberOfChildren() < 0) {
            throw new IllegalArgumentException("Number of children cannot be negative.");
        }


        List<String> availableSelectionOptions = List.of(
                "Austria", "Belgium", "Bulgaria", "Croatia", "Cyprus", "Czech Republic",
                "Denmark", "Estonia", "Finland", "France", "Germany", "Greece", "Hungary",
                "Ireland", "Italy", "Latvia", "Lithuania", "Luxembourg", "Malta", "Netherlands",
                "Poland", "Portugal", "Romania", "Slovakia", "Slovenia", "Spain", "Sweden", "Other"
        );

        if (!availableSelectionOptions.contains(personalInformationDAORequest.citizenship())) {
            throw new IllegalArgumentException("Invalid citizenship.");
        }


        if (null == personalInformationDAORequest.monthlyIncome() || personalInformationDAORequest.monthlyIncome().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly income must be non-negative.");
        }
    }

    public void validateLeaseAndRatesResponse(LeaseAndRatesDAORequest request) {

        if (null == request.make() || request.make().isEmpty()) {
            throw new IllegalArgumentException("Make cannot be null or empty.");
        }

        if (null == request.model() || request.model().isEmpty()) {
            throw new IllegalArgumentException("Model cannot be null or empty.");
        }

        if (null == request.year() || request.year().isEmpty() || !request.year().matches("\\d{4}")) {
            throw new IllegalArgumentException("Invalid year format. Year must be 4 digits.");
        }

        if (null == request.fuelType() || request.fuelType().isEmpty()) {
            throw new IllegalArgumentException("Fuel type cannot be null or empty.");
        }

        if (null == request.enginePower() || request.enginePower() < 0) {
            throw new IllegalArgumentException("Engine power must be non-negative.");
        }

        if (null == request.engineSize() || request.engineSize() < 0) {
            throw new IllegalArgumentException("Engine size must be non-negative.");
        }

        if (!request.url().isEmpty() && !request.url().matches("^(http|https)://.*")) {
            throw new IllegalArgumentException("Invalid URL.");
        }
        int MAX_BASE64_SIZE = 3 * 1024 * 1024 * 4 / 3;
        if (null != request.offer() && request.offer().length() > MAX_BASE64_SIZE) {
            throw new IllegalArgumentException("File too large. Maximum size allowed is 3MB.");
        }

        if (!request.terms() || !request.confirmation()) {
            throw new IllegalArgumentException("Terms and confirmation must not be null.");
        }

        if (null == request.carValue() || request.carValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Car value must be greater than zero.");
        }

        if (!PERIOD_VALUE.contains((request.period()))) {
            throw new IllegalArgumentException("Lease period incorrect.");
        }

        if (null == request.downPayment() || request.downPayment().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Down payment must be non-negative.");
        }

        if (!RESIDUAL_VALUE_PERCENTAGES.contains(request.residualValuePercentage())) {
            throw new IllegalArgumentException("Residual value is incorrect");
        }

        if (null == request.monthlyPayment() || request.monthlyPayment().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monthly payment must be greater than zero.");
        }

        if (null == request.isEcoFriendly()) {
            throw new IllegalArgumentException("Eco-friendly status must not be null.");
        }
    }

    private boolean validateIfHighRisk(PersonalInformationDAORequest personalInformationRequest, LeaseAndRatesDAORequest leaseAndRatesRequest) {
        List<String> europeanUnionCountries = List.of(
                "Austria", "Belgium", "Bulgaria", "Croatia", "Cyprus", "Czech Republic",
                "Denmark", "Estonia", "Finland", "France", "Germany", "Greece", "Hungary",
                "Ireland", "Italy", "Latvia", "Lithuania", "Luxembourg", "Malta", "Netherlands",
                "Poland", "Portugal", "Romania", "Slovakia", "Slovenia", "Spain", "Sweden"
        );

        if (!europeanUnionCountries.contains(personalInformationRequest.citizenship())) {
            return true;
        }

        if (isYoungerThan21(personalInformationRequest.dateOfBirth()) && leaseAndRatesRequest.carValue().compareTo(BigDecimal.valueOf(50000)) > 0) {
            return true;
        }

        if (isYoungerThan21(personalInformationRequest.dateOfBirth()) && leaseAndRatesRequest.engineSize() >= 2.5) {
            return true;
        }

        if (isYoungerThan21(personalInformationRequest.dateOfBirth()) && leaseAndRatesRequest.enginePower() >= 300) {
            return !Objects.equals(leaseAndRatesRequest.model(), "Tesla");
        }

        return !isValidIncome(personalInformationRequest.monthlyIncome(), leaseAndRatesRequest.monthlyPayment(), personalInformationRequest.numberOfChildren());
    }

    private boolean isYoungerThan21(LocalDateTime birthDate) {
        Period period = Period.between(birthDate.toLocalDate(), LocalDate.now());
        return period.getYears() < 21;
    }

    private boolean isValidIncome(BigDecimal monthlyIncome, BigDecimal monthlyPayment, int children) {
        return monthlyIncome.subtract(monthlyPayment).compareTo(BigDecimal.valueOf(600L * (1 + children))) >= 0;
    }
}
