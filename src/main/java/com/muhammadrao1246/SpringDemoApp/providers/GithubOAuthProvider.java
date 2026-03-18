package com.muhammadrao1246.SpringDemoApp.providers;

import com.muhammadrao1246.SpringDemoApp.models.types.AppAuthProviders;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class GithubOAuthProvider implements OAuthProvider{
    private final OAuth2User oAuth2User;
    public final AppAuthProviders provider = AppAuthProviders.GITHUB;

    public String getId(){
        return oAuth2User.getAttribute("id")+"";
    }

    public String getUsername(){
        // fetching username compatible to google token here
        return oAuth2User.getAttribute("login"); // a unique id or we can construct later on using email
    }

    public String getEmail(){
        // fetching email from OAuthUser principal
        return oAuth2User.getAttribute("email");
    }

    public String getName(){
        return oAuth2User.getAttribute("name");
    }
}
