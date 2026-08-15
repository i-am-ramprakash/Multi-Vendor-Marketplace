package com.marketplace.vendor.application.usecase;

import com.marketplace.vendor.application.dto.VendorSuspensionRequest;
import com.marketplace.vendor.application.dto.VendorResponse;
import com.marketplace.vendor.domain.entity.Vendor;
import com.marketplace.vendor.domain.event.VendorSuspendedEvent;
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
public class SuspendVendorUseCase {

    private final VendorRepository vendorRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public VendorResponse execute(VendorSuspensionRequest request) {
        Vendor vendor = vendorRepository.findById(request.getVendorId())
            .orElseThrow(() -> new VendorNotFoundException(request.getVendorId()));

        if (!vendor.getStatus().canTransitionTo(VendorStatus.SUSPENDED)) {
            throw new InvalidVendorStateException(vendor.getStatus().name(), "suspend");
        }

        vendor.suspend(request.getSuspendedBy(), request.getSuspensionReason());

        Vendor savedVendor = vendorRepository.save(vendor);

        eventPublisher.publishEvent(new VendorSuspendedEvent(
            savedVendor.getId(),
            savedVendor.getUserId(),
            savedVendor.getStoreName(),
            request.getSuspendedBy(),
            request.getSuspensionReason()
        ));

        return VendorResponse.from(savedVendor);
    }
}