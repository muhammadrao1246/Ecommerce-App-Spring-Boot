package com.muhammadrao1246.SpringDemoApp.models.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJwtTokenDto {
    String access_token;
    String refresh_token;
}
