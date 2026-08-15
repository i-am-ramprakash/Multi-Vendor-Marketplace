package com.marketplace.vendor.application.usecase;

import com.marketplace.vendor.application.dto.VendorRejectionRequest;
import com.marketplace.vendor.application.dto.VendorResponse;
import com.marketplace.vendor.domain.entity.Vendor;
import com.marketplace.vendor.domain.event.VendorRejectedEvent;
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
public class RejectVendorUseCase {

    private final VendorRepository vendorRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public VendorResponse execute(VendorRejectionRequest request) {
        Vendor vendor = vendorRepository.findById(request.getVendorId())
            .orElseThrow(() -> new VendorNotFoundException(request.getVendorId()));

        if (!vendor.getStatus().canTransitionTo(VendorStatus.REJECTED)) {
            throw new InvalidVendorStateException(vendor.getStatus().name(), "reject");
        }

        vendor.reject(request.getRejectedBy(), request.getRejectionReason());

        Vendor savedVendor = vendorRepository.save(vendor);

        eventPublisher.publishEvent(new VendorRejectedEvent(
            savedVendor.getId(),
            savedVendor.getUserId(),
            savedVendor.getStoreName(),
            request.getRejectedBy(),
            request.getRejectionReason()
        ));

        return VendorResponse.from(savedVendor);
    }
}