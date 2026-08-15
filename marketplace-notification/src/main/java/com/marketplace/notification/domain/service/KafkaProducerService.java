package com.marketplace.notification.domain.service;

import com.marketplace.notification.domain.entity.Notification;

public interface KafkaProducerService {

    void sendNotification(Notification notification);

    void sendNotificationWithKey(String key, Notification notification);

    void sendToTopic(String topic, String key, String payload);
}