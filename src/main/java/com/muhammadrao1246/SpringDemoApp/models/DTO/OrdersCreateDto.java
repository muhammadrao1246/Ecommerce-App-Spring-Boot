package com.muhammadrao1246.SpringDemoApp.models.DTO;

import com.muhammadrao1246.SpringDemoApp.models.Order;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Order}
 */
@Value
public class OrdersCreateDto implements Serializable {
    Set<OrderProductDto> products;
}