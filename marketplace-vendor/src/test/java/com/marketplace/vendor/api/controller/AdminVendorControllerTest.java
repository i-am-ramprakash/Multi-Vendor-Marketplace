package com.marketplace.vendor.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.vendor.application.dto.*;
import com.marketplace.vendor.application.service.VendorService;
import com.marketplace.vendor.domain.valueobject.VendorStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminVendorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VendorService vendorService;

    @InjectMocks
    private AdminVendorController adminVendorController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private VendorResponse vendorResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminVendorController).build();

        vendorResponse = VendorResponse.builder()
            .id(1L)
            .userId(1L)
            .storeName("Fashion Paradise")
            .storeSlug("fashion-paradise")
            .contactEmail("vendor@example.com")
            .status(VendorStatus.PENDING)
            .createdAt(Instant.now())
            .build();
    }

    @Test
    void getVendors_WithNoFilter_ShouldReturn200() throws Exception {
        VendorListResponse listResponse = VendorListResponse.builder()
            .vendors(List.of(vendorResponse))
            .totalElements(1L)
            .totalPages(1)
            .currentPage(0)
            .pageSize(10)
            .hasNext(false)
            .hasPrevious(false)
            .build();

        when(vendorService.getVendorsByStatus(isNull(), eq(0), eq(10))).thenReturn(listResponse);

        mockMvc.perform(get("/v1/admin/vendors"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(jsonPath("$.vendors[0].storeName").value("Fashion Paradise"));

        verify(vendorService).getVendorsByStatus(isNull(), eq(0), eq(10));
    }

    @Test
    void getVendors_WithStatusFilter_ShouldReturn200() throws Exception {
        VendorListResponse listResponse = VendorListResponse.builder()
            .vendors(List.of(vendorResponse))
            .totalElements(1L)
            .totalPages(1)
            .currentPage(0)
            .pageSize(10)
            .hasNext(false)
            .hasPrevious(false)
            .build();

        when(vendorService.getVendorsByStatus(eq(VendorStatus.PENDING), eq(0), eq(10))).thenReturn(listResponse);

        mockMvc.perform(get("/v1/admin/vendors")
                .param("status", "PENDING"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.vendors[0].status").value("PENDING"));

        verify(vendorService).getVendorsByStatus(eq(VendorStatus.PENDING), eq(0), eq(10));
    }

    @Test
    void getVendor_WithValidId_ShouldReturn200() throws Exception {
        when(vendorService.getVendorProfile(1L)).thenReturn(vendorResponse);

        mockMvc.perform(get("/v1/admin/vendors/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.storeName").value("Fashion Paradise"));

        verify(vendorService).getVendorProfile(1L);
    }

    @Test
    void approveVendor_WithValidId_ShouldReturn200() throws Exception {
        VendorResponse approvedResponse = VendorResponse.builder()
            .id(1L)
            .status(VendorStatus.APPROVED)
            .build();

        when(vendorService.approveVendor(any(VendorApprovalRequest.class))).thenReturn(approvedResponse);

        mockMvc.perform(put("/v1/admin/vendors/1/approve")
                .param("approvedBy", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APPROVED"));

        verify(vendorService).approveVendor(any(VendorApprovalRequest.class));
    }

    @Test
    void approveVendor_WithCustomCommissionRate_ShouldReturn200() throws Exception {
        VendorResponse approvedResponse = VendorResponse.builder()
            .id(1L)
            .status(VendorStatus.APPROVED)
            .commissionRate(new BigDecimal("5.00"))
            .build();

        when(vendorService.approveVendor(any(VendorApprovalRequest.class))).thenReturn(approvedResponse);

        mockMvc.perform(put("/v1/admin/vendors/1/approve")
                .param("approvedBy", "1")
                .param("customCommissionRate", "5.00"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.commissionRate").value(5.00));

        verify(vendorService).approveVendor(any(VendorApprovalRequest.class));
    }

    @Test
    void rejectVendor_WithValidIdAndReason_ShouldReturn200() throws Exception {
        VendorResponse rejectedResponse = VendorResponse.builder()
            .id(1L)
            .status(VendorStatus.REJECTED)
            .build();

        when(vendorService.rejectVendor(any(VendorRejectionRequest.class))).thenReturn(rejectedResponse);

        mockMvc.perform(put("/v1/admin/vendors/1/reject")
                .param("rejectedBy", "1")
                .param("rejectionReason", "Store description does not meet our guidelines"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("REJECTED"));

        verify(vendorService).rejectVendor(any(VendorRejectionRequest.class));
    }

    @Test
    void suspendVendor_WithValidIdAndReason_ShouldReturn200() throws Exception {
        VendorResponse suspendedResponse = VendorResponse.builder()
            .id(1L)
            .status(VendorStatus.SUSPENDED)
            .build();

        when(vendorService.suspendVendor(any(VendorSuspensionRequest.class))).thenReturn(suspendedResponse);

        mockMvc.perform(put("/v1/admin/vendors/1/suspend")
                .param("suspendedBy", "1")
                .param("suspensionReason", "Violation of terms of service"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SUSPENDED"));

        verify(vendorService).suspendVendor(any(VendorSuspensionRequest.class));
    }

    @Test
    void reactivateVendor_WithValidId_ShouldReturn200() throws Exception {
        VendorResponse reactivatedResponse = VendorResponse.builder()
            .id(1L)
            .status(VendorStatus.APPROVED)
            .build();

        when(vendorService.reactivateVendor(1L, 1L)).thenReturn(reactivatedResponse);

        mockMvc.perform(put("/v1/admin/vendors/1/reactivate")
                .param("reactivatedBy", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APPROVED"));

        verify(vendorService).reactivateVendor(1L, 1L);
    }
}