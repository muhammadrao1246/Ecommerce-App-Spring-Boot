package com.muhammadrao1246.SpringDemoApp.models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for {@link com.muhammadrao1246.SpringDemoApp.models.Brand}
 */
@Value
public class BrandDto implements Serializable {
    @NotNull
    @NotEmpty
    @NotBlank
    @Length(max = 100, min = 1, message = "Brand name must be between 1 and 100 characters")
    String name;
}