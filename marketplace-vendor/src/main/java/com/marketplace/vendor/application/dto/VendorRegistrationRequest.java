package com.marketplace.vendor.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Vendor registration request")
public class VendorRegistrationRequest {

    @NotBlank(message = "User ID is required")
    @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotBlank(message = "Store name is required")
    @Size(min = 1, max = 200, message = "Store name must be between 1 and 200 characters")
    @Schema(example = "Fashion Paradise", requiredMode = Schema.RequiredMode.REQUIRED)
    private String storeName;

    @NotBlank(message = "Store slug is required")
    @Size(min = 3, max = 200, message = "Store slug must be between 3 and 200 characters")
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Store slug can only contain lowercase letters, numbers, and hyphens")
    @Schema(example = "fashion-paradise", requiredMode = Schema.RequiredMode.REQUIRED)
    private String storeSlug;

    @Size(max = 1000, message = "Store description cannot exceed 1000 characters")
    @Schema(example = "We offer the latest fashion trends...")
    private String storeDescription;

    @Size(max = 500, message = "Store logo URL cannot exceed 500 characters")
    @Schema(example = "https://example.com/logo.png")
    private String storeLogoUrl;

    @Size(max = 500, message = "Store banner URL cannot exceed 500 characters")
    @Schema(example = "https://example.com/banner.png")
    private String storeBannerUrl;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid contact email format")
    @Schema(example = "vendor@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactEmail;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Schema(example = "+1234567890")
    private String contactPhone;

    @Size(max = 255, message = "Address line 1 cannot exceed 255 characters")
    @Schema(example = "123 Fashion Street")
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 cannot exceed 255 characters")
    @Schema(example = "Suite 100")
    private String addressLine2;

    @Size(max = 100, message = "City cannot exceed 100 characters")
    @Schema(example = "New York")
    private String city;

    @Size(max = 100, message = "State cannot exceed 100 characters")
    @Schema(example = "NY")
    private String state;

    @Size(max = 100, message = "Country cannot exceed 100 characters")
    @Schema(example = "USA")
    private String country;

    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    @Schema(example = "10001")
    private String postalCode;

    @Size(max = 100, message = "Tax ID cannot exceed 100 characters")
    @Schema(example = "12-3456789")
    private String taxId;

    @Size(max = 200, message = "Bank account holder cannot exceed 200 characters")
    @Schema(example = "Fashion Paradise LLC")
    private String bankAccountHolder;

    @Size(max = 50, message = "Bank account number cannot exceed 50 characters")
    @Schema(example = "123456789")
    private String bankAccountNumber;

    @Size(max = 100, message = "Bank name cannot exceed 100 characters")
    @Schema(example = "Chase Bank")
    private String bankName;

    @Size(max = 50, message = "Bank routing number cannot exceed 50 characters")
    @Schema(example = "021000021")
    private String bankRoutingNumber;
}