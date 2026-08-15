package com.marketplace.vendor.application.service;

import com.marketplace.vendor.application.dto.*;
import com.marketplace.vendor.domain.valueobject.VendorStatus;

public interface VendorService {

    VendorResponse registerVendor(VendorRegistrationRequest request);

    VendorResponse approveVendor(VendorApprovalRequest request);

    VendorResponse rejectVendor(VendorRejectionRequest request);

    VendorResponse suspendVendor(VendorSuspensionRequest request);

    VendorResponse reactivateVendor(Long vendorId, Long reactivatedBy);

    VendorResponse getVendorProfile(Long vendorId);

    VendorResponse getVendorProfileByUserId(Long userId);

    VendorResponse getVendorProfileByStoreSlug(String storeSlug);

    VendorResponse updateVendorProfile(Long vendorId, UpdateVendorProfileRequest request);

    VendorDashboardResponse getVendorDashboard(Long vendorId);

    VendorAnalyticsResponse getVendorAnalytics(Long vendorId, String period);

    VendorListResponse getVendorsByStatus(VendorStatus status, int page, int size);
}