package com.marketplace.vendor.application.service;

import com.marketplace.vendor.application.dto.*;
import com.marketplace.vendor.application.usecase.*;
import com.marketplace.vendor.domain.valueobject.VendorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final RegisterVendorUseCase registerVendorUseCase;
    private final ApproveVendorUseCase approveVendorUseCase;
    private final RejectVendorUseCase rejectVendorUseCase;
    private final SuspendVendorUseCase suspendVendorUseCase;
    private final ReactivateVendorUseCase reactivateVendorUseCase;
    private final GetVendorProfileUseCase getVendorProfileUseCase;
    private final UpdateVendorProfileUseCase updateVendorProfileUseCase;
    private final GetVendorDashboardUseCase getVendorDashboardUseCase;
    private final GetVendorAnalyticsUseCase getVendorAnalyticsUseCase;
    private final GetVendorsByStatusUseCase getVendorsByStatusUseCase;

    @Override
    @Transactional
    public VendorResponse registerVendor(VendorRegistrationRequest request) {
        return registerVendorUseCase.execute(request);
    }

    @Override
    @Transactional
    public VendorResponse approveVendor(VendorApprovalRequest request) {
        return approveVendorUseCase.execute(request);
    }

    @Override
    @Transactional
    public VendorResponse rejectVendor(VendorRejectionRequest request) {
        return rejectVendorUseCase.execute(request);
    }

    @Override
    @Transactional
    public VendorResponse suspendVendor(VendorSuspensionRequest request) {
        return suspendVendorUseCase.execute(request);
    }

    @Override
    @Transactional
    public VendorResponse reactivateVendor(Long vendorId, Long reactivatedBy) {
        return reactivateVendorUseCase.execute(vendorId, reactivatedBy);
    }

    @Override
    @Transactional(readOnly = true)
    public VendorResponse getVendorProfile(Long vendorId) {
        return getVendorProfileUseCase.execute(vendorId);
    }

    @Override
    @Transactional(readOnly = true)
    public VendorResponse getVendorProfileByUserId(Long userId) {
        return getVendorProfileUseCase.executeByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public VendorResponse getVendorProfileByStoreSlug(String storeSlug) {
        return getVendorProfileUseCase.executeByStoreSlug(storeSlug);
    }

    @Override
    @Transactional
    public VendorResponse updateVendorProfile(Long vendorId, UpdateVendorProfileRequest request) {
        return updateVendorProfileUseCase.execute(vendorId, request);
    }

    @Override
    @Transactional(readOnly = true)
    public VendorDashboardResponse getVendorDashboard(Long vendorId) {
        return getVendorDashboardUseCase.execute(vendorId);
    }

    @Override
    @Transactional(readOnly = true)
    public VendorAnalyticsResponse getVendorAnalytics(Long vendorId, String period) {
        return getVendorAnalyticsUseCase.execute(vendorId, period);
    }

    @Override
    @Transactional(readOnly = true)
    public VendorListResponse getVendorsByStatus(VendorStatus status, int page, int size) {
        return getVendorsByStatusUseCase.execute(status, page, size);
    }
}