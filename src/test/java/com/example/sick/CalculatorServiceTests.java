package com.example.sick;

// import com.example.sick.api.model.request.CalculatorRequest;
// import com.example.sick.api.model.response.CalculatorResponse;
// import com.example.sick.domain.CalculatorDAOResponse;
// import com.example.sick.repository.CalculatorRepository;
// import com.example.sick.service.CalculatorService;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.math.BigDecimal;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;

// @ExtendWith(MockitoExtension.class)
 class CalculatorServiceTests {

  // @Mock
  // private CalculatorRepository calculatorRepository;

  // @InjectMocks
  // private CalculatorService calculatorService;

  // @Test
  // void testCalculateMonthlyPayment_NormalCase() {
  //   CalculatorRequest request = new CalculatorRequest(BigDecimal.valueOf(100000), 36, BigDecimal.valueOf(20000), 20, false);
  //   when(calculatorRepository.selectAllInterestRate()).thenReturn(new CalculatorDAOResponse(5.0, 4.8));

  //   CalculatorResponse response = calculatorService.calculateMonthlyPayment(request);

  //   BigDecimal expectedPayment = new BigDecimal("1687.50");
  //   assertEquals(0, expectedPayment.compareTo(response.monthlyPayment()));
  // }
  // @Test
  // void testCalculateMonthlyPayment_EcoFriendlyCase() {
  //   CalculatorRequest request = new CalculatorRequest(BigDecimal.valueOf(100000), 36, BigDecimal.valueOf(20000), 20, true);
  //   when(calculatorRepository.selectAllInterestRate()).thenReturn(new CalculatorDAOResponse(5.0, 4.8));

  //   CalculatorResponse response = calculatorService.calculateMonthlyPayment(request);

  //   BigDecimal expectedPayment = new BigDecimal("1686.67");
  //   assertEquals(0, expectedPayment.compareTo(response.monthlyPayment()));
  // }
  // @Test
  // void testCalculateMonthlyPayment_ZeroDownPayment() {
  //   CalculatorRequest request = new CalculatorRequest(BigDecimal.valueOf(100000), 36, BigDecimal.ZERO, 20, false);
  //   when(calculatorRepository.selectAllInterestRate()).thenReturn(new CalculatorDAOResponse(5.0, 4.8));

  //   CalculatorResponse response = calculatorService.calculateMonthlyPayment(request);

  //   BigDecimal expectedPayment = new BigDecimal("2250.00");
  //   assertEquals(0, expectedPayment.compareTo(response.monthlyPayment()));
  // }
}
