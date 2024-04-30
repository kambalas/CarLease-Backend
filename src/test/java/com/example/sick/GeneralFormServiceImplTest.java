package com.example.sick;


import com.example.sick.api.model.exception.ApplicationNotFoundException;
import com.example.sick.api.model.request.ApplicationListRequest;
import com.example.sick.api.model.request.GeneralFormsRequest;
import com.example.sick.api.model.request.LeaseRequest;
import com.example.sick.api.model.request.PersonalInformationRequest;
import com.example.sick.api.model.request.RatesRequest;
import com.example.sick.api.model.request.StatusRequest;
import com.example.sick.api.model.response.ApplicationListResponse;
import com.example.sick.api.model.response.GeneralAllFormsResponse;
import com.example.sick.api.model.response.GeneralFormsResponse;
import com.example.sick.api.model.response.LeaseResponse;
import com.example.sick.api.model.response.PersonalInformationResponse;
import com.example.sick.api.model.response.RatesResponse;
import com.example.sick.domain.ApplicationListDAORequest;
import com.example.sick.domain.ApplicationListDAOResponse;
import com.example.sick.domain.LeaseAndRatesDAOResponse;
import com.example.sick.domain.PersonalInformationDAOResponse;
import com.example.sick.domain.StatusDAOResponse;
import com.example.sick.repository.ApplicationListRepository;
import com.example.sick.repository.LeaseAndRatesRepository;
import com.example.sick.repository.PersonalInformationRepository;
import com.example.sick.repository.StatusRepository;
import com.example.sick.service.EmailService;
import com.example.sick.service.GeneralFormServiceImpl;
import com.example.sick.utils.ApplicationStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneralFormServiceImplTest {

    private static final LocalDateTime mockDateTime = LocalDateTime.now();

    @Mock
    LeaseAndRatesRepository leaseAndRatesRepository;
    @Mock
    PersonalInformationRepository personalInformationRepository;
    @Mock
    StatusRepository statusRepository;
    @Mock
    ApplicationListRepository applicationListRepository;
    @Mock
    EmailService emailService;
    @InjectMocks
    GeneralFormServiceImpl generalFormService;

    @Test
    public void testSelectAllApplicationsByPage_ValidPage() {

        List<LeaseAndRatesDAOResponse> leaseAndRatesDAOResponses = List.of(
                new LeaseAndRatesDAOResponse(
                        1L,
                        "bmw",
                        "m3",
                        "competition",
                        "2021",
                        "diesel",
                        401.0,
                        5.4,
                        "www.google.com",
                        null,
                        true,
                        true,
                        BigDecimal.valueOf(10000),
                        24,
                        BigDecimal.valueOf(4000),
                        5,
                        true,
                        BigDecimal.valueOf(1000))
        );
        List<PersonalInformationDAOResponse> personalInformationDAOResponses = List.of(
                new PersonalInformationDAOResponse(
                        1L,
                        "John",
                        "Doe",
                        "egerdvila@gmail.com",
                        "+37060000000",
                        "123456789",
                        mockDateTime,
                        "single",
                        0,
                        "Lithuanian",
                        BigDecimal.valueOf(1000)
                ));
        List<StatusDAOResponse> statusDaoResponses = List.of(
                new StatusDAOResponse(
                        1L,
                        ApplicationStatus.REJECTED,
                        true,
                        mockDateTime,
                        mockDateTime,
                        true)
        );

        when(leaseAndRatesRepository.getAllLeaseAndRatesByPage(1)).thenReturn(leaseAndRatesDAOResponses);
        when(personalInformationRepository.getAllPersonalInformationByPage(1)).thenReturn(personalInformationDAOResponses);
        when(statusRepository.getAllStatusByPage(1)).thenReturn(statusDaoResponses);

        List<GeneralAllFormsResponse> allApplications = generalFormService.selectAllApplicationsByPage(1);

        assertNotNull(allApplications);
        assertFalse(allApplications.isEmpty());
        assertEquals(1, allApplications.size());

        GeneralAllFormsResponse generalAllFormsResponse = allApplications.getFirst();

        assertNotNull(generalAllFormsResponse);
        assertEquals(1, generalAllFormsResponse.personalInformationResponse().id());
        assertEquals("John", generalAllFormsResponse.personalInformationResponse().firstName());
        assertEquals("Doe", generalAllFormsResponse.personalInformationResponse().lastName());
        assertEquals("egerdvila@gmail.com", generalAllFormsResponse.personalInformationResponse().email());
        assertEquals("+37060000000", generalAllFormsResponse.personalInformationResponse().phoneNumber());
        assertEquals("123456789", generalAllFormsResponse.personalInformationResponse().pid());
        assertEquals(mockDateTime, generalAllFormsResponse.personalInformationResponse().dateOfBirth());
        assertEquals("single", generalAllFormsResponse.personalInformationResponse().maritalStatus());
        assertEquals(0, generalAllFormsResponse.personalInformationResponse().numberOfChildren());
        assertEquals("Lithuanian", generalAllFormsResponse.personalInformationResponse().citizenship());
        assertEquals(BigDecimal.valueOf(1000), generalAllFormsResponse.personalInformationResponse().monthlyIncome());

        assertEquals(1, generalAllFormsResponse.leaseResponse().id());
        assertEquals("bmw", generalAllFormsResponse.leaseResponse().make());
        assertEquals("m3", generalAllFormsResponse.leaseResponse().model());
        assertEquals("competition", generalAllFormsResponse.leaseResponse().modelVariant());
        assertEquals("2021", generalAllFormsResponse.leaseResponse().year());
        assertEquals("diesel", generalAllFormsResponse.leaseResponse().fuelType());
        assertEquals(401.0, generalAllFormsResponse.leaseResponse().enginePower());
        assertEquals(5.4, generalAllFormsResponse.leaseResponse().engineSize());
        assertEquals("www.google.com", generalAllFormsResponse.leaseResponse().url());
        assertNull(generalAllFormsResponse.leaseResponse().offer());

        assertEquals(1, generalAllFormsResponse.statusResponse().id());
        assertEquals(ApplicationStatus.REJECTED, generalAllFormsResponse.statusResponse().APPLICATIONSTATUS());
        assertTrue(generalAllFormsResponse.statusResponse().isOpened());
        assertEquals(mockDateTime, generalAllFormsResponse.statusResponse().updatedAt());
        assertEquals(mockDateTime, generalAllFormsResponse.statusResponse().createdAt());
        assertTrue(generalAllFormsResponse.statusResponse().isHighRisk());

        assertEquals(1, generalAllFormsResponse.ratesResponse().id());
        assertEquals(BigDecimal.valueOf(10000), generalAllFormsResponse.ratesResponse().carValue());
        assertEquals(24, generalAllFormsResponse.ratesResponse().period());
        assertEquals(BigDecimal.valueOf(4000), generalAllFormsResponse.ratesResponse().downPayment());
        assertEquals(5, generalAllFormsResponse.ratesResponse().residualValuePercentage());
        assertTrue(generalAllFormsResponse.ratesResponse().isEcoFriendly());
        assertEquals(BigDecimal.valueOf(1000), generalAllFormsResponse.ratesResponse().monthlyPayment());
    }

    @Test
    public void testGetApplicationById_ValidId() throws ApplicationNotFoundException {

        LeaseAndRatesDAOResponse leaseAndRatesDAOResponses =
                new LeaseAndRatesDAOResponse(
                        1L,
                        "bmw",
                        "m3",
                        "competition",
                        "2021",
                        "diesel",
                        401.0,
                        5.4,
                        "www.google.com",
                        null,
                        true,
                        true,
                        BigDecimal.valueOf(10000),
                        24,
                        BigDecimal.valueOf(4000),
                        5,
                        true,
                        BigDecimal.valueOf(1000)
                );
        PersonalInformationDAOResponse expected_personalInformationDAOResponses =
                new PersonalInformationDAOResponse(
                        1L,
                        "John",
                        "Doe",
                        "egerdvila@gmail.com",
                        "+37060000000",
                        "123456789",
                        mockDateTime,
                        "single",
                        0,
                        "Lithuanian",
                        BigDecimal.valueOf(1000)
                );

        when(leaseAndRatesRepository.getLeaseAndRateById(1)).thenReturn(Optional.of(leaseAndRatesDAOResponses));
        when(personalInformationRepository.getPersonalInformationById(1)).thenReturn(Optional.of(expected_personalInformationDAOResponses));

        GeneralFormsResponse application = generalFormService.getApplicationById(1);
        assertNotNull(application);

        assertEquals(1, application.personalInformationResponse().id());
        assertEquals("John", application.personalInformationResponse().firstName());
        assertEquals("Doe", application.personalInformationResponse().lastName());
        assertEquals("egerdvila@gmail.com", application.personalInformationResponse().email());
        assertEquals("+37060000000", application.personalInformationResponse().phoneNumber());
        assertEquals("123456789", application.personalInformationResponse().pid());
        assertEquals(mockDateTime, application.personalInformationResponse().dateOfBirth());
        assertEquals("single", application.personalInformationResponse().maritalStatus());
        assertEquals(0, application.personalInformationResponse().numberOfChildren());
        assertEquals("Lithuanian", application.personalInformationResponse().citizenship());
        assertEquals(BigDecimal.valueOf(1000), application.personalInformationResponse().monthlyIncome());

        assertNotNull(application.leaseResponse());
        assertEquals(1, application.leaseResponse().id());
        assertEquals("bmw", application.leaseResponse().make());
        assertEquals("m3", application.leaseResponse().model());
        assertEquals("competition", application.leaseResponse().modelVariant());
        assertEquals("2021", application.leaseResponse().year());
        assertEquals("diesel", application.leaseResponse().fuelType());
        assertEquals(401.0, application.leaseResponse().enginePower(), 0.0);
        assertEquals(5.4, application.leaseResponse().engineSize(), 0.0);
        assertEquals("www.google.com", application.leaseResponse().url());
        assertNull(application.leaseResponse().offer());

        assertNotNull(application.ratesResponse());
        assertEquals(1, application.ratesResponse().id());
        assertEquals(BigDecimal.valueOf(10000), application.ratesResponse().carValue());
        assertEquals(24, application.ratesResponse().period());
        assertEquals(BigDecimal.valueOf(4000), application.ratesResponse().downPayment());
        assertEquals(5, application.ratesResponse().residualValuePercentage());
        assertTrue(application.ratesResponse().isEcoFriendly());
        assertEquals(BigDecimal.valueOf(1000), application.ratesResponse().monthlyPayment());

    }


    @Test
    public void testGetApplicationById_InvalidId() {
        when(leaseAndRatesRepository.getLeaseAndRateById(999)).thenReturn(Optional.empty());
        when(personalInformationRepository.getPersonalInformationById(999)).thenReturn(Optional.empty());

        ApplicationNotFoundException applicationNotFoundException =
                Assertions.assertThrows(ApplicationNotFoundException.class, () -> generalFormService.getApplicationById(999));

        assertEquals("Cannot find a task with id: 999", applicationNotFoundException.getMessage());
    }

    @Test
    public void testCreateApplication_Valid() throws Exception {
        GeneralFormsRequest mockGeneralFormsRequest = new GeneralFormsRequest(
                new RatesRequest(
                        BigDecimal.valueOf(10000),
                        24,
                        BigDecimal.valueOf(4000),
                        5,
                        true,
                        BigDecimal.valueOf(1000)),
                new PersonalInformationRequest(
                        "John",
                        "Doe",
                        "egerdvila@gmail.com",
                        "+37060000000",
                        "123456789",
                        LocalDateTime.of(1999, 1, 1, 1, 1),
                        "Single",
                        0,
                        "Lithuania",
                        BigDecimal.valueOf(1000))
                ,
                new LeaseRequest(
                        "bmw",
                        "m3",
                        "competition",
                        "2021",
                        "diesel",
                        401.0,
                        5.4,
                        "https://www.google.com",
                        null,
                        true,
                        true
                ),
                new StatusRequest(1L, "NEW")
        );
        long expectedId = 1L;
        when(personalInformationRepository.createPersonalInformation(any())).thenReturn(expectedId);

        generalFormService.createApplication(mockGeneralFormsRequest);

        verify(personalInformationRepository).createPersonalInformation(any());
        verify(leaseAndRatesRepository).createLeaseAndRate(any(), eq(expectedId));
        verify(statusRepository).createStatus(eq(expectedId), anyBoolean());
        verify(emailService).sendMail(
                mockGeneralFormsRequest.personalInformationRequest().email(),
                "Car lease application #1",
                """
                        Hi John Doe,

                        Just a quick email to let you know that we've received your car lease application and it's in good hands. We're excited to have you on board!

                        **Your car details**
                        - Make: bmw
                        - Model: m3
                        - Year: 2021
                        - Car value: 10000 €

                        Our team is currently working hard to process all the information and we'll be sure to keep you updated every step of the way.

                        If you have any questions or need assistance, feel free to reach out to our friendly customer support team during our working hours from 8 to 17.

                        Thank you for choosing Tarzan leasing. We appreciate your trust and can't wait to help make your dream car a reality!

                        Have a great day!

                        Best regards,
                        T-Leasing
                        """);

    }

    @Test
    public void testCreateApplication_Invalid() {
        GeneralFormsRequest mockGeneralFormsRequest = new GeneralFormsRequest(
                new RatesRequest(
                        BigDecimal.valueOf(10000),
                        24,
                        BigDecimal.valueOf(4000),
                        5,
                        true,
                        BigDecimal.valueOf(1000)),
                new PersonalInformationRequest(
                        "John",
                        "Doe",
                        "egerdvila@gmail.com",
                        "+37060000000",
                        "123456789",
                        LocalDateTime.of(1999, 1, 1, 1, 1),
                        "Single",
                        0,
                        "ithuania",
                        BigDecimal.valueOf(1000))
                ,
                new LeaseRequest(
                        "bmw",
                        "m3",
                        "competition",
                        "2021",
                        "diesel",
                        401.0,
                        5.4,
                        "https://www.google.com",
                        null,
                        true,
                        true
                ),
                new StatusRequest(1L, "NEW")
        );

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> generalFormService.createApplication(mockGeneralFormsRequest));

        assertEquals("Invalid citizenship.", exception.getMessage());

    }

    @Test
    public void testCreateApplication_InvalidSendEmail() throws Exception {
        GeneralFormsRequest mockGeneralFormsRequest = new GeneralFormsRequest(
                new RatesRequest(
                        BigDecimal.valueOf(10000),
                        24,
                        BigDecimal.valueOf(4000),
                        5,
                        true,
                        BigDecimal.valueOf(1000)),
                new PersonalInformationRequest(
                        "John",
                        "Doe",
                        "egerdvila@gmail.com",
                        "+37060000000",
                        "123456789",
                        LocalDateTime.of(1999, 1, 1, 1, 1),
                        "Single",
                        0,
                        "Lithuania",
                        BigDecimal.valueOf(1000))
                ,
                new LeaseRequest(
                        "bmw",
                        "m3",
                        "competition",
                        "2021",
                        "diesel",
                        401.0,
                        5.4,
                        "https://www.google.com",
                        null,
                        true,
                        true
                ),
                new StatusRequest(1L, "NEW")
        );

        long expectedId = 1L;
        when(personalInformationRepository.createPersonalInformation(any())).thenReturn(expectedId);
        doThrow(new Exception("Email server down")).when(emailService).sendMail(anyString(), anyString(), anyString());
        Exception exception = assertThrows(IllegalArgumentException.class, ()
                -> generalFormService.createApplication(mockGeneralFormsRequest));

        assertEquals("Failed to send email: Email server down", exception.getMessage());
        verify(leaseAndRatesRepository).createLeaseAndRate(any(), eq(expectedId));
        verify(statusRepository).createStatus(eq(expectedId), anyBoolean());

    }

    @Test
    public void testSortApplications_Valid_StatusAndPageAndSearchQuery() {
        List<ApplicationListDAOResponse> mockApplicationListDAOResponse = List.of(new ApplicationListDAOResponse(
                        1L,
                        "John",
                        "Doe",
                        false,
                        ApplicationStatus.NEW,
                        mockDateTime,
                        false
                )
        );
        when(applicationListRepository.sortAndFilterByStatusAndSearchQuery(new ApplicationListDAORequest(1L, List.of("NEW"), "John")))
                .thenReturn(mockApplicationListDAOResponse);
        List<ApplicationListResponse> applicationListResponses = generalFormService.sortApplications(new ApplicationListRequest(1L, List.of(ApplicationStatus.NEW), "John"));
        assertNotNull(applicationListResponses);
        assertFalse(applicationListResponses.isEmpty());
        assertEquals(1, applicationListResponses.size());

        ApplicationListResponse applicationListResponse = applicationListResponses.getFirst();

        assertEquals(1, applicationListResponse.id());
        assertEquals("John", applicationListResponse.firstName());
        assertEquals("Doe", applicationListResponse.lastName());
        assertFalse(applicationListResponse.isOpened());
        assertEquals(ApplicationStatus.NEW, applicationListResponse.status());
        assertEquals(mockDateTime, applicationListResponse.createdAt());
        assertFalse(applicationListResponse.isHighRisk());

    }

    @Test
    public void testSortApplications_Valid_StatusAndPage() {
        List<ApplicationListDAOResponse> mockApplicationListDAOResponse = List.of(new ApplicationListDAOResponse(
                        1L,
                        "John",
                        "Doe",
                        false,
                        ApplicationStatus.NEW,
                        mockDateTime,
                        false
                ),
                new ApplicationListDAOResponse(
                        2L,
                        "Jane",
                        "Doe",
                        false,
                        ApplicationStatus.REJECTED,
                        mockDateTime,
                        false
                )
        );
        when(applicationListRepository.sortAndFilterByStatus(new ApplicationListDAORequest(1L, List.of("NEW", "REJECTED"), null)))
                .thenReturn(mockApplicationListDAOResponse);
        List<ApplicationListResponse> applicationListResponses = generalFormService.sortApplications(new ApplicationListRequest(1L, List.of(ApplicationStatus.NEW, ApplicationStatus.REJECTED), null));
        assertNotNull(applicationListResponses);
        assertFalse(applicationListResponses.isEmpty());
        assertEquals(2, applicationListResponses.size());

        ApplicationListResponse applicationListResponse = applicationListResponses.getFirst();

        assertEquals(1, applicationListResponse.id());
        assertEquals("John", applicationListResponse.firstName());
        assertEquals("Doe", applicationListResponse.lastName());
        assertFalse(applicationListResponse.isOpened());
        assertEquals(ApplicationStatus.NEW, applicationListResponse.status());
        assertEquals(mockDateTime, applicationListResponse.createdAt());
        assertFalse(applicationListResponse.isHighRisk());

    }

    @Test
    public void testSortApplications_Valid_SearchAndPage() {
        List<ApplicationListDAOResponse> mockApplicationListDAOResponse = List.of(new ApplicationListDAOResponse(
                        1L,
                        "John",
                        "Doe",
                        false,
                        ApplicationStatus.NEW,
                        mockDateTime,
                        false
                ),
                new ApplicationListDAOResponse(
                        2L,
                        "Jane",
                        "Doe",
                        false,
                        ApplicationStatus.REJECTED,
                        mockDateTime,
                        false
                )
        );
        when(applicationListRepository.sortAndFilterBySearchQuery(new ApplicationListDAORequest(1L, null, "J")))
                .thenReturn(mockApplicationListDAOResponse);
        List<ApplicationListResponse> applicationListResponses = generalFormService.sortApplications(new ApplicationListRequest(1L, null, "J"));
        assertNotNull(applicationListResponses);
        assertFalse(applicationListResponses.isEmpty());
        assertEquals(2, applicationListResponses.size());

        ApplicationListResponse applicationListResponse = applicationListResponses.getFirst();

        assertEquals(1, applicationListResponse.id());
        assertEquals("John", applicationListResponse.firstName());
        assertEquals("Doe", applicationListResponse.lastName());
        assertFalse(applicationListResponse.isOpened());
        assertEquals(ApplicationStatus.NEW, applicationListResponse.status());
        assertEquals(mockDateTime, applicationListResponse.createdAt());
        assertFalse(applicationListResponse.isHighRisk());

    }

    @Test
    public void testSortApplications_Valid_Page() {
        List<ApplicationListDAOResponse> mockApplicationListDAOResponse = List.of(new ApplicationListDAOResponse(
                        1L,
                        "John",
                        "Doe",
                        false,
                        ApplicationStatus.NEW,
                        mockDateTime,
                        false
                ),
                new ApplicationListDAOResponse(
                        2L,
                        "Jane",
                        "Doe",
                        false,
                        ApplicationStatus.REJECTED,
                        mockDateTime,
                        false
                )
        );
        when(applicationListRepository.sortApplicationsByTimestamp(new ApplicationListDAORequest(1L, null, null)))
                .thenReturn(mockApplicationListDAOResponse);
        List<ApplicationListResponse> applicationListResponses = generalFormService.sortApplications(new ApplicationListRequest(1L, null, null));
        assertNotNull(applicationListResponses);
        assertFalse(applicationListResponses.isEmpty());
        assertEquals(2, applicationListResponses.size());

        ApplicationListResponse applicationListResponse = applicationListResponses.getFirst();

        assertEquals(1, applicationListResponse.id());
        assertEquals("John", applicationListResponse.firstName());
        assertEquals("Doe", applicationListResponse.lastName());
        assertFalse(applicationListResponse.isOpened());
        assertEquals(ApplicationStatus.NEW, applicationListResponse.status());
        assertEquals(mockDateTime, applicationListResponse.createdAt());
        assertFalse(applicationListResponse.isHighRisk());

    }

    @Test
    public void testGetPersonalInformationById_Found() throws ApplicationNotFoundException{
        PersonalInformationDAOResponse personalInformationDAOResponse = new PersonalInformationDAOResponse(
                1L,
                "John",
                "Doe",
                "egerdvila@gmail.com",
                "+37060000000",
                "123456789",
                mockDateTime,
                "single",
                0,
                "Lithuanian",
                BigDecimal.valueOf(1000)
        );
        when(personalInformationRepository.getPersonalInformationById(1)).thenReturn(Optional.of(personalInformationDAOResponse));
        PersonalInformationResponse response = generalFormService.getPersonalInformationById(1);

        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals("egerdvila@gmail.com", response.email());
        assertEquals("+37060000000", response.phoneNumber());
        assertEquals("123456789", response.pid());
        assertEquals(mockDateTime, response.dateOfBirth());
        assertEquals("single", response.maritalStatus());
        assertEquals(0, response.numberOfChildren());
        assertEquals("Lithuanian", response.citizenship());
        assertEquals(BigDecimal.valueOf(1000), response.monthlyIncome());

    }

    @Test
    public void testGetPersonalInformationById_NotFound(){
        when(personalInformationRepository.getPersonalInformationById(1)).thenReturn(Optional.empty());
        ApplicationNotFoundException applicationNotFoundException =
                assertThrows(ApplicationNotFoundException.class, () -> generalFormService.getPersonalInformationById(1));
        assertEquals("Cannot find a task with id: 1", applicationNotFoundException.getMessage());

    }

    @Test
    public void testGetLeaseInformationById_Found() throws ApplicationNotFoundException{
        LeaseAndRatesDAOResponse leaseAndRatesDAOResponse = new LeaseAndRatesDAOResponse(
                1L,
                "bmw",
                "m3",
                "competition",
                "2021",
                "diesel",
                401.0,
                5.4,
                "www.google.com",
                null,
                true,
                true,
                BigDecimal.valueOf(10000),
                24,
                BigDecimal.valueOf(4000),
                5,
                true,
                BigDecimal.valueOf(1000)
        );
        when(leaseAndRatesRepository.getLeaseAndRateById(1)).thenReturn(Optional.of(leaseAndRatesDAOResponse));
        LeaseResponse response = generalFormService.getLeaseInformationById(1);

        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals("bmw", response.make());
        assertEquals("m3", response.model());
        assertEquals("competition", response.modelVariant());
        assertEquals("2021", response.year());
        assertEquals("diesel", response.fuelType());
        assertEquals(401.0, response.enginePower());
        assertEquals(5.4, response.engineSize());
        assertEquals("www.google.com", response.url());
        assertNull(response.offer());
    }

    @Test
    public void testGetLeaseInformationById_NotFound(){
        when(leaseAndRatesRepository.getLeaseAndRateById(1)).thenReturn(Optional.empty());
        ApplicationNotFoundException applicationNotFoundException =
                assertThrows(ApplicationNotFoundException.class, () -> generalFormService.getLeaseInformationById(1));
        assertEquals("Cannot find a task with id: 1", applicationNotFoundException.getMessage());

    }

    @Test
    public void testGetRatesInformationById_Found() throws ApplicationNotFoundException{
        LeaseAndRatesDAOResponse leaseAndRatesDAOResponse = new LeaseAndRatesDAOResponse(
                1L,
                "bmw",
                "m3",
                "competition",
                "2021",
                "diesel",
                401.0,
                5.4,
                "www.google.com",
                null,
                true,
                true,
                BigDecimal.valueOf(10000),
                24,
                BigDecimal.valueOf(4000),
                5,
                true,
                BigDecimal.valueOf(1000)
        );
        when(leaseAndRatesRepository.getLeaseAndRateById(1)).thenReturn(Optional.of(leaseAndRatesDAOResponse));

        RatesResponse response = generalFormService.getRatesInformationById(1);

        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals(BigDecimal.valueOf(10000), response.carValue());
        assertEquals(24, response.period());
        assertEquals(BigDecimal.valueOf(4000), response.downPayment());
        assertEquals(5, response.residualValuePercentage());
        assertTrue(response.isEcoFriendly());
        assertEquals(BigDecimal.valueOf(1000), response.monthlyPayment());

    }

    @Test
    public void testGetRatesInformationById_NotFound(){
        when(leaseAndRatesRepository.getLeaseAndRateById(1)).thenReturn(Optional.empty());
        ApplicationNotFoundException applicationNotFoundException =
                assertThrows(ApplicationNotFoundException.class, () -> generalFormService.getRatesInformationById(1));
        assertEquals("Cannot find a task with id: 1", applicationNotFoundException.getMessage());
    }

}
