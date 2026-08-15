package com.marketplace.vendor.application.usecase;

import com.marketplace.vendor.application.dto.VendorResponse;
import com.marketplace.vendor.domain.entity.Vendor;
import com.marketplace.vendor.domain.exception.VendorNotFoundException;
import com.marketplace.vendor.domain.repository.VendorRepository;
import com.marketplace.vendor.domain.valueobject.StoreSlug;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetVendorProfileUseCase {

    private final VendorRepository vendorRepository;

    @Transactional(readOnly = true)
    public VendorResponse execute(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
            .orElseThrow(() -> new VendorNotFoundException(vendorId));

        return VendorResponse.from(vendor);
    }

    @Transactional(readOnly = true)
    public VendorResponse executeByUserId(Long userId) {
        Vendor vendor = vendorRepository.findByUserId(userId)
            .orElseThrow(() -> new VendorNotFoundException("userId: " + userId));

        return VendorResponse.from(vendor);
    }

    @Transactional(readOnly = true)
    public VendorResponse executeByStoreSlug(String storeSlug) {
        StoreSlug slug = StoreSlug.of(storeSlug);
        Vendor vendor = vendorRepository.findByStoreSlug(slug)
            .orElseThrow(() -> new VendorNotFoundException("storeSlug: " + storeSlug));

        return VendorResponse.from(vendor);
    }
}