package com.muhammadrao1246.SpringDemoApp.models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for {@link com.muhammadrao1246.SpringDemoApp.models.User}
 */
@Value
public class UserLoginDto implements Serializable {
    @NotNull
    @NotEmpty
    @NotBlank
    @Length(max = 100, min = 1, message = "Username or email must be between 1 and 100 characters")
    String username; // can be email or username

    @NotNull
    String password;
}