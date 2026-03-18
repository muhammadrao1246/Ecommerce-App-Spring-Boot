package com.muhammadrao1246.SpringDemoApp.models.Projections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.muhammadrao1246.SpringDemoApp.models.Brand;
import com.muhammadrao1246.SpringDemoApp.models.Category;
import com.muhammadrao1246.SpringDemoApp.models.Product;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Projection for {@link Product}
 */
@JsonPropertyOrder({"id", "name", "description", "price", "quantity", "createdAt", "brand", "category"})
public interface ProductInfo {
    UUID getId();

    String getName();

    String getDescription();

    BigDecimal getPrice();

    BigDecimal getQuantity();

    Instant getCreatedAt();

    // Dynamic attribute creation after loading
//    @Value(value = "#{target.id != null && target.imageName != null ? '/products/'+target.id+'/image' : null}")
//    String getImageUrl();

//    @JsonIgnore
//    String getImageName();

    BrandInfo getBrand();

    CategoryInfo getCategory();

    /**
     * Projection for {@link Brand}
     */
    interface BrandInfo {
        Integer getId();

        String getName();
    }

    /**
     * Projection for {@link Category}
     */
    interface CategoryInfo {
        Integer getId();

        String getName();
    }
}