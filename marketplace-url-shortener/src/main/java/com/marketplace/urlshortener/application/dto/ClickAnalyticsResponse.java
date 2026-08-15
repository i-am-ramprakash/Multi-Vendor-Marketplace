package com.marketplace.urlshortener.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClickAnalyticsResponse {

    private String shortCode;
    private long totalClicks;
    private List<DailyClicks> dailyClicks;
    private List<DeviceClicks> deviceClicks;
    private List<BrowserClicks> browserClicks;
    private List<CountryClicks> countryClicks;
    private List<RefererClicks> refererClicks;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyClicks {
        private String date;
        private long clicks;
        private long uniqueClicks;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceClicks {
        private String device;
        private long count;
        private double percentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BrowserClicks {
        private String browser;
        private long count;
        private double percentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountryClicks {
        private String country;
        private long count;
        private double percentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefererClicks {
        private String referer;
        private long count;
        private double percentage;
    }
}