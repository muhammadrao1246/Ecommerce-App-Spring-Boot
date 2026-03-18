package com.muhammadrao1246.SpringDemoApp.models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * DTO for {@link com.muhammadrao1246.SpringDemoApp.models.Product}
 */
@Value
public class ProductSaveDto {
    @NotNull
    @NotEmpty
    @NotBlank
    @Length
    String name;
    @Length(max = 300)
    String description;
    @NotNull
    @PositiveOrZero
    BigDecimal price;
    @NotNull
    @PositiveOrZero
    BigDecimal quantity;
    @NotNull
    Integer brandId;
    @NotNull
    Integer categoryId;


    MultipartFile image;
}