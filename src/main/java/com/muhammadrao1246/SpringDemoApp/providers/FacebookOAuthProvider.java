package com.muhammadrao1246.SpringDemoApp.providers;

import com.muhammadrao1246.SpringDemoApp.models.types.AppAuthProviders;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class FacebookOAuthProvider implements OAuthProvider{
    private final OAuth2User oAuth2User;
    public final AppAuthProviders provider = AppAuthProviders.FACEBOOK;

    public String getId(){
        return oAuth2User.getAttribute("id");
    }

    public String getEmail(){
        // fetching email from OAuthUser principal
        return oAuth2User.getAttribute("email");
    }

    public String getName(){
        return oAuth2User.getAttribute("name");
    }
}
