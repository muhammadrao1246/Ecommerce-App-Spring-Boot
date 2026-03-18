package com.muhammadrao1246.SpringDemoApp.providers;

import com.muhammadrao1246.SpringDemoApp.models.types.AppAuthProviders;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class GoogleOAuthProvider implements OAuthProvider{
    private final OAuth2User oAuth2User;
    public final AppAuthProviders provider = AppAuthProviders.GOOGLE;

    public String getId(){
        return oAuth2User.getAttribute("sub");
    }

    public String getUsername(){
        // fetching username compatible to google token here
        String username = oAuth2User.getAttribute("sub"); // a unique id or we can construct later on using email

        // fetching email from OAuthUser principal
        String email = getEmail();

        // if email not empty then create username from there
        if(email != null && !email.isEmpty()){
            username = email.split("@")[0];
        }

        return username;
    }

    public String getEmail(){
        // fetching email from OAuthUser principal
        return oAuth2User.getAttribute("email");
    }

    public String getName(){
        return oAuth2User.getAttribute("name");
    }
}
