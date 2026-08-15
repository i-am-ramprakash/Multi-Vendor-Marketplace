package com.marketplace.vendor.application.usecase;

import com.marketplace.vendor.application.dto.VendorRegistrationRequest;
import com.marketplace.vendor.application.dto.VendorResponse;
import com.marketplace.vendor.domain.entity.Vendor;
import com.marketplace.vendor.domain.event.VendorRegisteredEvent;
import com.marketplace.vendor.domain.exception.VendorAlreadyExistsException;
import com.marketplace.vendor.domain.repository.VendorRepository;
import com.marketplace.vendor.domain.valueobject.StoreSlug;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterVendorUseCaseTest {

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private RegisterVendorUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegisterVendorUseCase(vendorRepository, eventPublisher);
    }

    @Test
    void execute_WithValidRequest_ShouldRegisterVendorAndReturnResponse() {
        // Given
        VendorRegistrationRequest request = VendorRegistrationRequest.builder()
            .userId(1L)
            .storeName("Fashion Paradise")
            .storeSlug("fashion-paradise")
            .contactEmail("vendor@example.com")
            .storeDescription("We offer the latest fashion trends")
            .contactPhone("+1234567890")
            .addressLine1("123 Fashion Street")
            .city("New York")
            .state("NY")
            .country("USA")
            .postalCode("10001")
            .build();

        when(vendorRepository.existsByUserId(1L)).thenReturn(false);
        when(vendorRepository.existsByStoreSlug(any(StoreSlug.class))).thenReturn(false);
        when(vendorRepository.save(any(Vendor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        VendorResponse response = useCase.execute(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getStoreName()).isEqualTo("Fashion Paradise");
        assertThat(response.getStoreSlug()).isEqualTo("fashion-paradise");
        assertThat(response.getContactEmail()).isEqualTo("vendor@example.com");
        assertThat(response.getStatus()).isEqualTo(com.marketplace.vendor.domain.valueobject.VendorStatus.PENDING);

        verify(vendorRepository).save(any(Vendor.class));
        verify(eventPublisher).publishEvent(any(VendorRegisteredEvent.class));
    }

    @Test
    void execute_WithExistingUserId_ShouldThrowVendorAlreadyExistsException() {
        // Given
        VendorRegistrationRequest request = VendorRegistrationRequest.builder()
            .userId(1L)
            .storeName("Fashion Paradise")
            .storeSlug("fashion-paradise")
            .contactEmail("vendor@example.com")
            .build();

        when(vendorRepository.existsByUserId(1L)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> useCase.execute(request))
            .isInstanceOf(VendorAlreadyExistsException.class)
            .hasMessageContaining("1");

        verify(vendorRepository, never()).save(any(Vendor.class));
    }

    @Test
    void execute_WithExistingStoreSlug_ShouldThrowVendorAlreadyExistsException() {
        // Given
        VendorRegistrationRequest request = VendorRegistrationRequest.builder()
            .userId(1L)
            .storeName("Fashion Paradise")
            .storeSlug("fashion-paradise")
            .contactEmail("vendor@example.com")
            .build();

        when(vendorRepository.existsByUserId(1L)).thenReturn(false);
        when(vendorRepository.existsByStoreSlug(any(StoreSlug.class))).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> useCase.execute(request))
            .isInstanceOf(VendorAlreadyExistsException.class)
            .hasMessageContaining("store slug");

        verify(vendorRepository, never()).save(any(Vendor.class));
    }

    @Test
    void execute_WithMinimalFields_ShouldRegisterVendorWithDefaults() {
        // Given
        VendorRegistrationRequest request = VendorRegistrationRequest.builder()
            .userId(1L)
            .storeName("Fashion Paradise")
            .storeSlug("fashion-paradise")
            .contactEmail("vendor@example.com")
            .build();

        when(vendorRepository.existsByUserId(1L)).thenReturn(false);
        when(vendorRepository.existsByStoreSlug(any(StoreSlug.class))).thenReturn(false);
        when(vendorRepository.save(any(Vendor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        VendorResponse response = useCase.execute(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStoreName()).isEqualTo("Fashion Paradise");
        assertThat(response.getContactEmail()).isEqualTo("vendor@example.com");
        assertThat(response.getCommissionRate()).isEqualByComparingTo(new BigDecimal("10.00"));
    }
}