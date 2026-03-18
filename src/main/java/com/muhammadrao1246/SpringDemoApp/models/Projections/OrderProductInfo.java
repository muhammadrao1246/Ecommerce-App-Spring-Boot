package com.muhammadrao1246.SpringDemoApp.models.Projections;

import java.math.BigDecimal;

/**
 * Projection for {@link com.muhammadrao1246.SpringDemoApp.models.OrderProduct}
 */
public interface OrderProductInfo {
    Integer getQuantity();

    BigDecimal getPriceAtThatTime();

    ProductInfo getProduct();
}