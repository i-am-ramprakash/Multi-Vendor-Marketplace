package com.marketplace.vendor.application.usecase;

import com.marketplace.vendor.application.dto.VendorListResponse;
import com.marketplace.vendor.application.dto.VendorResponse;
import com.marketplace.vendor.domain.entity.Vendor;
import com.marketplace.vendor.domain.repository.VendorRepository;
import com.marketplace.vendor.domain.valueobject.VendorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetVendorsByStatusUseCase {

    private final VendorRepository vendorRepository;

    @Transactional(readOnly = true)
    public VendorListResponse execute(VendorStatus status, int page, int size) {
        List<Vendor> vendors;

        if (status != null) {
            vendors = vendorRepository.findByStatusOrderByCreatedAtDesc(status);
        } else {
            vendors = vendorRepository.findAllByOrderByCreatedAtDesc();
        }

        // Simple pagination implementation
        int totalElements = vendors.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int start = page * size;
        int end = Math.min(start + size, totalElements);

        List<VendorResponse> vendorResponses = vendors.subList(start, end).stream()
            .map(VendorResponse::from)
            .toList();

        return VendorListResponse.builder()
            .vendors(vendorResponses)
            .totalElements((long) totalElements)
            .totalPages(totalPages)
            .currentPage(page)
            .pageSize(size)
            .hasNext(page < totalPages - 1)
            .hasPrevious(page > 0)
            .build();
    }
}