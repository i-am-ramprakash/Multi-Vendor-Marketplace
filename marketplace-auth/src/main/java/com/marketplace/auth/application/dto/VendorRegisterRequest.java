package com.marketplace.auth.application.dto;

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
public class VendorRegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    @Schema(example = "vendor@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    @Schema(example = "SecurePass123!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    @Schema(example = "Jane", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    @Schema(example = "Smith", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format. Use E.164 format (e.g., +1234567890)")
    @Schema(example = "+1234567890")
    private String phone;

    @NotBlank(message = "Store name is required")
    @Size(min = 1, max = 200, message = "Store name must be between 1 and 200 characters")
    @Schema(example = "Jane's Fashion Store", requiredMode = Schema.RequiredMode.REQUIRED)
    private String storeName;

    @NotBlank(message = "Store slug is required")
    @Size(min = 3, max = 200, message = "Store slug must be between 3 and 200 characters")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Store slug can only contain lowercase letters, numbers, and hyphens")
    @Schema(example = "janes-fashion-store", requiredMode = Schema.RequiredMode.REQUIRED)
    private String storeSlug;

    @Size(max = 1000, message = "Store description cannot exceed 1000 characters")
    @Schema(example = "Welcome to Jane's Fashion Store! We offer the latest trends...")
    private String storeDescription;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid contact email format")
    @Schema(example = "contact@janesfashion.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactEmail;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Schema(example = "+1987654321")
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
}