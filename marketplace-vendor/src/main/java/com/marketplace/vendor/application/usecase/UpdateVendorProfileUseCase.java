package com.marketplace.vendor.application.usecase;

import com.marketplace.vendor.application.dto.UpdateVendorProfileRequest;
import com.marketplace.vendor.application.dto.VendorResponse;
import com.marketplace.vendor.domain.entity.Vendor;
import com.marketplace.vendor.domain.exception.VendorNotFoundException;
import com.marketplace.vendor.domain.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateVendorProfileUseCase {

    private final VendorRepository vendorRepository;

    @Transactional
    public VendorResponse execute(Long vendorId, UpdateVendorProfileRequest request) {
        Vendor vendor = vendorRepository.findById(vendorId)
            .orElseThrow(() -> new VendorNotFoundException(vendorId));

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

        return VendorResponse.from(savedVendor);
    }
}