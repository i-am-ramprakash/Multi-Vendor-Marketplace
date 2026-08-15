package com.marketplace.vendor.application.service;

import com.marketplace.vendor.application.dto.*;
import com.marketplace.vendor.application.usecase.*;
import com.marketplace.vendor.domain.valueobject.VendorStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendorServiceTest {

    @Mock
    private RegisterVendorUseCase registerVendorUseCase;

    @Mock
    private ApproveVendorUseCase approveVendorUseCase;

    @Mock
    private RejectVendorUseCase rejectVendorUseCase;

    @Mock
    private SuspendVendorUseCase suspendVendorUseCase;

    @Mock
    private ReactivateVendorUseCase reactivateVendorUseCase;

    @Mock
    private GetVendorProfileUseCase getVendorProfileUseCase;

    @Mock
    private UpdateVendorProfileUseCase updateVendorProfileUseCase;

    @Mock
    private GetVendorDashboardUseCase getVendorDashboardUseCase;

    @Mock
    private GetVendorAnalyticsUseCase getVendorAnalyticsUseCase;

    @Mock
    private GetVendorsByStatusUseCase getVendorsByStatusUseCase;

    private VendorServiceImpl vendorService;

    @BeforeEach
    void setUp() {
        vendorService = new VendorServiceImpl(
            registerVendorUseCase,
            approveVendorUseCase,
            rejectVendorUseCase,
            suspendVendorUseCase,
            reactivateVendorUseCase,
            getVendorProfileUseCase,
            updateVendorProfileUseCase,
            getVendorDashboardUseCase,
            getVendorAnalyticsUseCase,
            getVendorsByStatusUseCase
        );
    }

    @Test
    void registerVendor_ShouldDelegateToUseCase() {
        VendorRegistrationRequest request = VendorRegistrationRequest.builder()
            .userId(1L)
            .storeName("Fashion Paradise")
            .storeSlug("fashion-paradise")
            .contactEmail("vendor@example.com")
            .build();

        VendorResponse expectedResponse = VendorResponse.builder()
            .id(1L)
            .userId(1L)
            .storeName("Fashion Paradise")
            .storeSlug("fashion-paradise")
            .contactEmail("vendor@example.com")
            .status(VendorStatus.PENDING)
            .build();

        when(registerVendorUseCase.execute(request)).thenReturn(expectedResponse);

        VendorResponse response = vendorService.registerVendor(request);

        assertThat(response).isEqualTo(expectedResponse);
        verify(registerVendorUseCase).execute(request);
    }

    @Test
    void approveVendor_ShouldDelegateToUseCase() {
        VendorApprovalRequest request = VendorApprovalRequest.builder()
            .vendorId(1L)
            .approvedBy(1L)
            .customCommissionRate(new BigDecimal("5.00"))
            .build();

        VendorResponse expectedResponse = VendorResponse.builder()
            .id(1L)
            .status(VendorStatus.APPROVED)
            .build();

        when(approveVendorUseCase.execute(request)).thenReturn(expectedResponse);

        VendorResponse response = vendorService.approveVendor(request);

        assertThat(response).isEqualTo(expectedResponse);
        verify(approveVendorUseCase).execute(request);
    }

    @Test
    void rejectVendor_ShouldDelegateToUseCase() {
        VendorRejectionRequest request = VendorRejectionRequest.builder()
            .vendorId(1L)
            .rejectedBy(1L)
            .rejectionReason("Store description does not meet our guidelines")
            .build();

        VendorResponse expectedResponse = VendorResponse.builder()
            .id(1L)
            .status(VendorStatus.REJECTED)
            .build();

        when(rejectVendorUseCase.execute(request)).thenReturn(expectedResponse);

        VendorResponse response = vendorService.rejectVendor(request);

        assertThat(response).isEqualTo(expectedResponse);
        verify(rejectVendorUseCase).execute(request);
    }

    @Test
    void suspendVendor_ShouldDelegateToUseCase() {
        VendorSuspensionRequest request = VendorSuspensionRequest.builder()
            .vendorId(1L)
            .suspendedBy(1L)
            .suspensionReason("Violation of terms of service")
            .build();

        VendorResponse expectedResponse = VendorResponse.builder()
            .id(1L)
            .status(VendorStatus.SUSPENDED)
            .build();

        when(suspendVendorUseCase.execute(request)).thenReturn(expectedResponse);

        VendorResponse response = vendorService.suspendVendor(request);

        assertThat(response).isEqualTo(expectedResponse);
        verify(suspendVendorUseCase).execute(request);
    }

    @Test
    void reactivateVendor_ShouldDelegateToUseCase() {
        VendorResponse expectedResponse = VendorResponse.builder()
            .id(1L)
            .status(VendorStatus.APPROVED)
            .build();

        when(reactivateVendorUseCase.execute(1L, 1L)).thenReturn(expectedResponse);

        VendorResponse response = vendorService.reactivateVendor(1L, 1L);

        assertThat(response).isEqualTo(expectedResponse);
        verify(reactivateVendorUseCase).execute(1L, 1L);
    }

    @Test
    void getVendorProfile_ShouldDelegateToUseCase() {
        VendorResponse expectedResponse = VendorResponse.builder()
            .id(1L)
            .storeName("Fashion Paradise")
            .build();

        when(getVendorProfileUseCase.execute(1L)).thenReturn(expectedResponse);

        VendorResponse response = vendorService.getVendorProfile(1L);

        assertThat(response).isEqualTo(expectedResponse);
        verify(getVendorProfileUseCase).execute(1L);
    }

    @Test
    void getVendorProfileByUserId_ShouldDelegateToUseCase() {
        VendorResponse expectedResponse = VendorResponse.builder()
            .id(1L)
            .userId(1L)
            .build();

        when(getVendorProfileUseCase.executeByUserId(1L)).thenReturn(expectedResponse);

        VendorResponse response = vendorService.getVendorProfileByUserId(1L);

        assertThat(response).isEqualTo(expectedResponse);
        verify(getVendorProfileUseCase).executeByUserId(1L);
    }

    @Test
    void getVendorProfileByStoreSlug_ShouldDelegateToUseCase() {
        VendorResponse expectedResponse = VendorResponse.builder()
            .id(1L)
            .storeSlug("fashion-paradise")
            .build();

        when(getVendorProfileUseCase.executeByStoreSlug("fashion-paradise")).thenReturn(expectedResponse);

        VendorResponse response = vendorService.getVendorProfileByStoreSlug("fashion-paradise");

        assertThat(response).isEqualTo(expectedResponse);
        verify(getVendorProfileUseCase).executeByStoreSlug("fashion-paradise");
    }

    @Test
    void updateVendorProfile_ShouldDelegateToUseCase() {
        UpdateVendorProfileRequest request = UpdateVendorProfileRequest.builder()
            .storeName("Updated Store Name")
            .build();

        VendorResponse expectedResponse = VendorResponse.builder()
            .id(1L)
            .storeName("Updated Store Name")
            .build();

        when(updateVendorProfileUseCase.execute(1L, request)).thenReturn(expectedResponse);

        VendorResponse response = vendorService.updateVendorProfile(1L, request);

        assertThat(response).isEqualTo(expectedResponse);
        verify(updateVendorProfileUseCase).execute(1L, request);
    }

    @Test
    void getVendorDashboard_ShouldDelegateToUseCase() {
        VendorDashboardResponse expectedResponse = VendorDashboardResponse.builder()
            .vendor(VendorResponse.builder().id(1L).build())
            .build();

        when(getVendorDashboardUseCase.execute(1L)).thenReturn(expectedResponse);

        VendorDashboardResponse response = vendorService.getVendorDashboard(1L);

        assertThat(response).isEqualTo(expectedResponse);
        verify(getVendorDashboardUseCase).execute(1L);
    }

    @Test
    void getVendorAnalytics_ShouldDelegateToUseCase() {
        VendorAnalyticsResponse expectedResponse = VendorAnalyticsResponse.builder()
            .vendorId(1L)
            .period("month")
            .build();

        when(getVendorAnalyticsUseCase.execute(1L, "month")).thenReturn(expectedResponse);

        VendorAnalyticsResponse response = vendorService.getVendorAnalytics(1L, "month");

        assertThat(response).isEqualTo(expectedResponse);
        verify(getVendorAnalyticsUseCase).execute(1L, "month");
    }

    @Test
    void getVendorsByStatus_ShouldDelegateToUseCase() {
        VendorListResponse expectedResponse = VendorListResponse.builder()
            .vendors(java.util.List.of())
            .totalElements(0L)
            .build();

        when(getVendorsByStatusUseCase.execute(VendorStatus.PENDING, 0, 10)).thenReturn(expectedResponse);

        VendorListResponse response = vendorService.getVendorsByStatus(VendorStatus.PENDING, 0, 10);

        assertThat(response).isEqualTo(expectedResponse);
        verify(getVendorsByStatusUseCase).execute(VendorStatus.PENDING, 0, 10);
    }
}