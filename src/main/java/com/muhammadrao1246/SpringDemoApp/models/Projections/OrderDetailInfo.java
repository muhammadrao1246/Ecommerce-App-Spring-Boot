package com.muhammadrao1246.SpringDemoApp.models.Projections;

import com.muhammadrao1246.SpringDemoApp.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.generator.EventType;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;


/**
 * Projection for {@link com.muhammadrao1246.SpringDemoApp.models.Order}
 */
public interface OrderDetailInfo {

    UUID getId();

    Long getOrderNumber();
    Integer getTotalAmount();

    String getStatus();

    Instant getCreatedAt();
    Instant getConfirmedAt();
    Instant getShippedAt();
    Instant getDeliveredAt();

    UserInfo getUser();
    interface UserInfo{
        UUID getId();
    }

    Set<OrderProductInfo> getProducts();

}
