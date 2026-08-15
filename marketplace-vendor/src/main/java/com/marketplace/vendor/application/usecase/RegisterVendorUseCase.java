package com.marketplace.vendor.application.usecase;

import com.marketplace.vendor.application.dto.VendorRegistrationRequest;
import com.marketplace.vendor.application.dto.VendorResponse;
import com.marketplace.vendor.domain.entity.Vendor;
import com.marketplace.vendor.domain.event.VendorRegisteredEvent;
import com.marketplace.vendor.domain.exception.VendorAlreadyExistsException;
import com.marketplace.vendor.domain.repository.VendorRepository;
import com.marketplace.vendor.domain.valueobject.StoreSlug;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RegisterVendorUseCase {

    private final VendorRepository vendorRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public VendorResponse execute(VendorRegistrationRequest request) {
        if (vendorRepository.existsByUserId(request.getUserId())) {
            throw new VendorAlreadyExistsException(request.getUserId());
        }

        StoreSlug storeSlug = StoreSlug.of(request.getStoreSlug());
        if (vendorRepository.existsByStoreSlug(storeSlug)) {
            throw new VendorAlreadyExistsException("store slug", request.getStoreSlug());
        }

        Vendor vendor = new Vendor(
            request.getUserId(),
            request.getStoreName(),
            storeSlug,
            request.getContactEmail(),
            new BigDecimal("10.00") // Default commission rate
        );

        vendor.updateProfile(
            request.getStoreName(),
            request.getStoreDescription(),
            request.getStoreLogoUrl(),
            request.getStoreBannerUrl(),
            request.getContactEmail(),
            request.getContactPhone(),
            request.getAddressLine1(),
            request.getAddressLine2(),
            request.getCity(),
            request.getState(),
            request.getCountry(),
            request.getPostalCode()
        );

        if (request.getTaxId() != null) {
            vendor.setTaxId(request.getTaxId());
        }
        if (request.getBankAccountHolder() != null) {
            vendor.setBankAccountHolder(request.getBankAccountHolder());
        }
        if (request.getBankAccountNumber() != null) {
            vendor.setBankAccountNumber(request.getBankAccountNumber());
        }
        if (request.getBankName() != null) {
            vendor.setBankName(request.getBankName());
        }
        if (request.getBankRoutingNumber() != null) {
            vendor.setBankRoutingNumber(request.getBankRoutingNumber());
        }

        Vendor savedVendor = vendorRepository.save(vendor);

        eventPublisher.publishEvent(new VendorRegisteredEvent(
            savedVendor.getId(),
            savedVendor.getUserId(),
            savedVendor.getStoreName(),
            savedVendor.getStoreSlug().getValue(),
            savedVendor.getContactEmail()
        ));

        return VendorResponse.from(savedVendor);
    }
}