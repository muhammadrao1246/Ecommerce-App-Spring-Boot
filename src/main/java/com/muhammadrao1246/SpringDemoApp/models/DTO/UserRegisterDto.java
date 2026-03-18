package com.muhammadrao1246.SpringDemoApp.models.DTO;

import com.muhammadrao1246.SpringDemoApp.models.types.RoleTypes;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * DTO for {@link com.muhammadrao1246.SpringDemoApp.models.User}
 */
@Value
public class UserRegisterDto implements Serializable {
    @NotNull
    @NotEmpty
    @NotBlank
    @Length(max = 100, min = 1, message = "Name must be between 1 and 100 characters")
    String name;

    @NotNull
    @NotEmpty
    @NotBlank
    @Length(max = 100, min = 1, message = "Username must be between 1 and 50 characters")
    String username; // can be email or username

    @NotNull
    @Email
    String email;

    @NotNull
    @Length(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit")
    String password;
    @NotNull
    String confirmPassword;

    @Size(min = 1, message = "Role list should not be empty")
    @NotNull
    Set<RoleTypes> roles;
}