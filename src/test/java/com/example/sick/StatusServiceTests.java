package com.example.sick;

import com.example.sick.api.model.exception.StatusNotFoundException;
import com.example.sick.api.model.request.StatusRequest;
import com.example.sick.api.model.response.StatusResponse;
import com.example.sick.domain.StatusDAORequest;
import com.example.sick.domain.StatusDAOResponse;
import com.example.sick.service.StatusServiceImpl;
import com.example.sick.utils.ApplicationStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.example.sick.repository.StatusRepository;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatusServiceTests {
  @Mock
  private StatusRepository statusRepository;

  @InjectMocks
  private StatusServiceImpl statusServiceImpl;

  @Test
  public void testUpdateStatusById_WithRejectedStatus() {
    StatusRequest statusRequest = new StatusRequest(1L, "REJECTED");

    statusServiceImpl.updateStatusById(statusRequest);

    verify(statusRepository).updateStatusById(any(StatusDAORequest.class));
    verify(statusRepository).updateStatusRead(1L, true);
  }

  @Test
  public void testUpdateStatusById_WithNonRejectedStatus() {
    StatusRequest statusRequest = new StatusRequest(1L, "ACCEPTED");

    statusServiceImpl.updateStatusById(statusRequest);

    verify(statusRepository).updateStatusById(new StatusDAORequest(1L, ApplicationStatus.ACCEPTED));
    verify(statusRepository).updateStatusRead(1L, false);
  }

  @Test
  public void testGetStatusById_StatusFound() throws StatusNotFoundException {
    LocalDateTime testDate = LocalDateTime.now();
    StatusDAOResponse mockedResponse = new StatusDAOResponse(
            1,
            ApplicationStatus.NEW,
            false,
            testDate,
            testDate,
            false);

    when(statusRepository.getStatusById(1)).thenReturn(Optional.of(mockedResponse));

    StatusResponse result = statusServiceImpl.getStatusById(1);

    Assertions.assertEquals(1, result.id());
    Assertions.assertEquals(ApplicationStatus.NEW, result.APPLICATIONSTATUS());
    Assertions.assertEquals(false, result.isOpened());
    Assertions.assertEquals(testDate, result.createdAt());
    Assertions.assertEquals(testDate, result.updatedAt());
    Assertions.assertEquals(false, result.isHighRisk());

  }

  @Test
  public void testGetStatusById_StatusNotFound() {
    when(statusRepository.getStatusById(1)).thenReturn(Optional.empty());

    StatusNotFoundException exception = Assertions.assertThrows(StatusNotFoundException.class,
            () -> statusServiceImpl.getStatusById(1));

    Assertions.assertEquals("Cannot find a task with id: 1", exception.getMessage());
  }

  @Test
  public void testUpdateStatusIsRead() {
    statusServiceImpl.updateStatusIsRead(1);

    verify(statusRepository).updateStatusRead(1, true);
  }
}
