package com.marketplace.urlshortener.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlStatsResponse {

    private long totalUrls;
    private long activeUrls;
    private long expiredUrls;
    private long totalClicks;
    private long uniqueVisitors;
    private long todayClicks;
    private long thisWeekClicks;
    private long thisMonthClicks;
    private long urlsCreatedToday;
    private long urlsCreatedThisWeek;
    private long urlsCreatedThisMonth;
}