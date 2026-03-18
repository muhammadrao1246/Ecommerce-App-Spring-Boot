package com.muhammadrao1246.SpringDemoApp.models.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.muhammadrao1246.SpringDemoApp.models.OrderProduct}
 */
@Value
public class OrderProductDto implements Serializable {
    UUID productId;
    @NotNull
    @Positive
    Integer quantity;
}