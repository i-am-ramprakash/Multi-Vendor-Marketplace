package com.marketplace.vendor.application.usecase;

import com.marketplace.vendor.application.dto.VendorResponse;
import com.marketplace.vendor.domain.entity.Vendor;
import com.marketplace.vendor.domain.event.VendorReactivatedEvent;
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
public class ReactivateVendorUseCase {

    private final VendorRepository vendorRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public VendorResponse execute(Long vendorId, Long reactivatedBy) {
        Vendor vendor = vendorRepository.findById(vendorId)
            .orElseThrow(() -> new VendorNotFoundException(vendorId));

        if (!vendor.getStatus().canTransitionTo(VendorStatus.APPROVED)) {
            throw new InvalidVendorStateException(vendor.getStatus().name(), "reactivate");
        }

        vendor.reactivate(reactivatedBy);

        Vendor savedVendor = vendorRepository.save(vendor);

        eventPublisher.publishEvent(new VendorReactivatedEvent(
            savedVendor.getId(),
            savedVendor.getUserId(),
            savedVendor.getStoreName(),
            reactivatedBy
        ));

        return VendorResponse.from(savedVendor);
    }
}