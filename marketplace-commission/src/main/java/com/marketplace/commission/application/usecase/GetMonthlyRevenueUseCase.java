package com.marketplace.commission.application.usecase;

import com.marketplace.commission.application.dto.MonthlyRevenueResponse;
import com.marketplace.commission.domain.repository.CommissionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetMonthlyRevenueUseCase {

    private final CommissionRecordRepository commissionRecordRepository;

    @Transactional(readOnly = true)
    public MonthlyRevenueResponse execute(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = endDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant();

        // Get aggregated data
        BigDecimal totalSales = commissionRecordRepository.sumOrderAmountByCreatedAtBetween(startInstant, endInstant);
        BigDecimal totalCommission = commissionRecordRepository.sumCommissionAmountByCreatedAtBetween(startInstant, endInstant);

        if (totalSales == null) totalSales = BigDecimal.ZERO;
        if (totalCommission == null) totalCommission = BigDecimal.ZERO;

        BigDecimal totalNetPayout = totalSales.subtract(totalCommission);

        // Get daily revenues
        List<MonthlyRevenueResponse.DailyRevenue> dailyRevenues = new ArrayList<>();
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate dayStart = yearMonth.atDay(day);
            LocalDate dayEnd = dayStart.plusDays(1);

            Instant dayStartInstant = dayStart.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant dayEndInstant = dayEnd.atStartOfDay(ZoneId.systemDefault()).toInstant();

            BigDecimal daySales = commissionRecordRepository.sumOrderAmountByCreatedAtBetween(dayStartInstant, dayEndInstant);
            BigDecimal dayCommission = commissionRecordRepository.sumCommissionAmountByCreatedAtBetween(dayStartInstant, dayEndInstant);

            if (daySales == null) daySales = BigDecimal.ZERO;
            if (dayCommission == null) dayCommission = BigDecimal.ZERO;

            dailyRevenues.add(MonthlyRevenueResponse.DailyRevenue.builder()
                .day(day)
                .sales(daySales)
                .commission(dayCommission)
                .orders(0) // Would need additional query
                .build());
        }

        return MonthlyRevenueResponse.builder()
            .year(year)
            .month(month)
            .totalSales(totalSales)
            .totalCommission(totalCommission)
            .totalNetPayout(totalNetPayout)
            .totalOrders(0) // Would need additional query
            .totalVendors(0) // Would need additional query
            .averageCommissionRate(totalSales.compareTo(BigDecimal.ZERO) > 0 ?
                totalCommission.multiply(new BigDecimal("100")).divide(totalSales, 2, RoundingMode.HALF_UP) :
                BigDecimal.ZERO)
            .dailyRevenues(dailyRevenues)
            .build();
    }
}