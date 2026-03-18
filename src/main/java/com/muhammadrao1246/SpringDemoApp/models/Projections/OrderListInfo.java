package com.muhammadrao1246.SpringDemoApp.models.Projections;

import java.time.Instant;
import java.util.UUID;

/**
 * Projection for {@link com.muhammadrao1246.SpringDemoApp.models.Order}
 */
public interface OrderListInfo {
    UUID getId();

    Long getOrderNumber();

    Integer getTotalAmount();

    String getStatus();

    Instant getCreatedAt();

    Instant getConfirmedAt();

    Instant getShippedAt();

    Instant getDeliveredAt();
}