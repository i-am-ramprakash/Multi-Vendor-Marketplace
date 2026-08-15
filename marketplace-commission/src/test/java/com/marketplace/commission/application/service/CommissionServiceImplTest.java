package com.marketplace.commission.application.service;

import com.marketplace.commission.application.dto.*;
import com.marketplace.commission.application.usecase.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommissionServiceImplTest {

    @Mock
    private CreateCommissionRuleUseCase createCommissionRuleUseCase;

    @Mock
    private CalculateCommissionUseCase calculateCommissionUseCase;

    @Mock
    private GetVendorEarningsUseCase getVendorEarningsUseCase;

    @Mock
    private CreateSettlementUseCase createSettlementUseCase;

    @Mock
    private ProcessSettlementUseCase processSettlementUseCase;

    @Mock
    private CompleteSettlementUseCase completeSettlementUseCase;

    @Mock
    private GetMonthlyRevenueUseCase getMonthlyRevenueUseCase;

    private CommissionServiceImpl commissionService;

    @BeforeEach
    void setUp() {
        commissionService = new CommissionServiceImpl(
            createCommissionRuleUseCase,
            calculateCommissionUseCase,
            getVendorEarningsUseCase,
            createSettlementUseCase,
            processSettlementUseCase,
            completeSettlementUseCase,
            getMonthlyRevenueUseCase
        );
    }

    @Test
    void createCommissionRule_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        CreateCommissionRuleRequest request = CreateCommissionRuleRequest.builder()
            .name("Default Rule")
            .type("PERCENTAGE")
            .rate(new BigDecimal("10.00"))
            .build();

        CommissionRuleResponse expectedResponse = CommissionRuleResponse.builder()
            .id(1L)
            .name("Default Rule")
            .type("PERCENTAGE")
            .rate(new BigDecimal("10.00"))
            .build();

        when(createCommissionRuleUseCase.execute(request)).thenReturn(expectedResponse);

        // When
        CommissionRuleResponse response = commissionService.createCommissionRule(request);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(createCommissionRuleUseCase).execute(request);
    }

    @Test
    void calculateCommission_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        CommissionRecordResponse expectedResponse = CommissionRecordResponse.builder()
            .id(1L)
            .orderId(100L)
            .vendorId(10L)
            .orderAmount(new BigDecimal("100.00"))
            .commissionAmount(new BigDecimal("10.00"))
            .vendorPayout(new BigDecimal("90.00"))
            .commissionRate(new BigDecimal("10.00"))
            .currency("USD")
            .build();

        when(calculateCommissionUseCase.execute(100L, 200L, 10L, 5L, new BigDecimal("100.00"), "USD"))
            .thenReturn(expectedResponse);

        // When
        CommissionRecordResponse response = commissionService.calculateCommission(
            100L, 200L, 10L, 5L, new BigDecimal("100.00"), "USD");

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(calculateCommissionUseCase).execute(100L, 200L, 10L, 5L, new BigDecimal("100.00"), "USD");
    }

    @Test
    void getVendorEarnings_WithValidVendor_ShouldDelegateToUseCase() {
        // Given
        VendorEarningsResponse expectedResponse = VendorEarningsResponse.builder()
            .vendorId(10L)
            .totalSales(new BigDecimal("1000.00"))
            .totalCommission(new BigDecimal("100.00"))
            .totalNetEarnings(new BigDecimal("900.00"))
            .build();

        when(getVendorEarningsUseCase.execute(10L)).thenReturn(expectedResponse);

        // When
        VendorEarningsResponse response = commissionService.getVendorEarnings(10L);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getVendorEarningsUseCase).execute(10L);
    }

    @Test
    void getVendorRecords_WithValidVendor_ShouldDelegateToUseCase() {
        // Given
        List<CommissionRecordResponse> expectedResponse = List.of(
            CommissionRecordResponse.builder().id(1L).orderId(100L).build(),
            CommissionRecordResponse.builder().id(2L).orderId(101L).build()
        );

        when(getVendorEarningsUseCase.getVendorRecords(10L, 0, 10)).thenReturn(expectedResponse);

        // When
        List<CommissionRecordResponse> response = commissionService.getVendorRecords(10L, 0, 10);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getVendorEarningsUseCase).getVendorRecords(10L, 0, 10);
    }

    @Test
    void getVendorUnsettledRecords_WithValidVendor_ShouldDelegateToUseCase() {
        // Given
        List<CommissionRecordResponse> expectedResponse = List.of(
            CommissionRecordResponse.builder().id(1L).orderId(100L).isSettled(false).build()
        );

        when(getVendorEarningsUseCase.getVendorUnsettledRecords(10L)).thenReturn(expectedResponse);

        // When
        List<CommissionRecordResponse> response = commissionService.getVendorUnsettledRecords(10L);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getVendorEarningsUseCase).getVendorUnsettledRecords(10L);
    }

    @Test
    void createSettlement_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        SettlementResponse expectedResponse = SettlementResponse.builder()
            .id(1L)
            .settlementNumber("STL-123456")
            .vendorId(10L)
            .status("PENDING")
            .build();

        Instant start = LocalDate.now().minusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();

        when(createSettlementUseCase.execute(10L, start, end)).thenReturn(expectedResponse);

        // When
        SettlementResponse response = commissionService.createSettlement(10L, start, end);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(createSettlementUseCase).execute(10L, start, end);
    }

    @Test
    void processSettlement_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        SettlementResponse expectedResponse = SettlementResponse.builder()
            .id(1L)
            .settlementNumber("STL-123456")
            .status("PROCESSING")
            .build();

        when(processSettlementUseCase.execute(1L, 10L)).thenReturn(expectedResponse);

        // When
        SettlementResponse response = commissionService.processSettlement(1L, 10L);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(processSettlementUseCase).execute(1L, 10L);
    }

    @Test
    void completeSettlement_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        SettlementResponse expectedResponse = SettlementResponse.builder()
            .id(1L)
            .settlementNumber("STL-123456")
            .status("COMPLETED")
            .paymentReference("PAY-789")
            .build();

        when(completeSettlementUseCase.execute(1L, "PAY-789", 10L)).thenReturn(expectedResponse);

        // When
        SettlementResponse response = commissionService.completeSettlement(1L, "PAY-789", 10L);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(completeSettlementUseCase).execute(1L, "PAY-789", 10L);
    }

    @Test
    void getMonthlyRevenue_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        MonthlyRevenueResponse expectedResponse = MonthlyRevenueResponse.builder()
            .year(2026)
            .month(6)
            .totalSales(new BigDecimal("10000.00"))
            .totalCommission(new BigDecimal("1000.00"))
            .build();

        when(getMonthlyRevenueUseCase.execute(2026, 6)).thenReturn(expectedResponse);

        // When
        MonthlyRevenueResponse response = commissionService.getMonthlyRevenue(2026, 6);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getMonthlyRevenueUseCase).execute(2026, 6);
    }
}