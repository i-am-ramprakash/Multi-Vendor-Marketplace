package com.marketplace.vendor.application.usecase;

import com.marketplace.vendor.application.dto.VendorApprovalRequest;
import com.marketplace.vendor.application.dto.VendorResponse;
import com.marketplace.vendor.domain.entity.Vendor;
import com.marketplace.vendor.domain.event.VendorApprovedEvent;
import com.marketplace.vendor.domain.exception.InvalidVendorStateException;
import com.marketplace.vendor.domain.exception.VendorNotFoundException;
import com.marketplace.vendor.domain.repository.VendorRepository;
import com.marketplace.vendor.domain.valueobject.VendorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ApproveVendorUseCase {

    private final VendorRepository vendorRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public VendorResponse execute(VendorApprovalRequest request) {
        Vendor vendor = vendorRepository.findById(request.getVendorId())
            .orElseThrow(() -> new VendorNotFoundException(request.getVendorId()));

        if (!vendor.getStatus().canTransitionTo(VendorStatus.APPROVED)) {
            throw new InvalidVendorStateException(vendor.getStatus().name(), "approve");
        }

        if (request.getCustomCommissionRate() != null) {
            vendor.updateCommissionRate(request.getCustomCommissionRate());
        }

        vendor.approve(request.getApprovedBy());

        Vendor savedVendor = vendorRepository.save(vendor);

        eventPublisher.publishEvent(new VendorApprovedEvent(
            savedVendor.getId(),
            savedVendor.getUserId(),
            savedVendor.getStoreName(),
            request.getApprovedBy()
        ));

        return VendorResponse.from(savedVendor);
    }
}