package com.marketplace.admin.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User analytics response")
public class UserAnalyticsResponse {

    @Schema(description = "Total registered users")
    private Long totalUsers;

    @Schema(description = "Active users (logged in within 30 days)")
    private Long activeUsers;

    @Schema(description = "New users this month")
    private Long newUsersThisMonth;

    @Schema(description = "New users last month")
    private Long newUsersLastMonth;

    @Schema(description = "User growth rate percentage")
    private Double growthRate;

    @Schema(description = "User registration trend")
    private List<UserTrendItem> registrationTrend;

    @Schema(description = "Users by role breakdown")
    private List<RoleBreakdownItem> roleBreakdown;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "User trend item")
    public static class UserTrendItem {
        private String period;
        private Long count;
        private Double growthRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Role breakdown item")
    public static class RoleBreakdownItem {
        private String role;
        private Long count;
        private Double percentage;
    }
}