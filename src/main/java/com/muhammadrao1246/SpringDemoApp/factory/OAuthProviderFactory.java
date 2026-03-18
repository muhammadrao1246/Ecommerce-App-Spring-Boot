package com.muhammadrao1246.SpringDemoApp.factory;

import com.muhammadrao1246.SpringDemoApp.models.types.AppAuthProviders;
import com.muhammadrao1246.SpringDemoApp.providers.FacebookOAuthProvider;
import com.muhammadrao1246.SpringDemoApp.providers.GithubOAuthProvider;
import com.muhammadrao1246.SpringDemoApp.providers.GoogleOAuthProvider;
import com.muhammadrao1246.SpringDemoApp.providers.OAuthProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;


public class OAuthProviderFactory {
    private final AppAuthProviders provider;

    public OAuthProviderFactory(String provider){
        this.provider = OAuthProviderFactory.getAuthProviderType(provider);
    }

    public OAuthProviderFactory(AppAuthProviders provider){
        this.provider = provider;
    }

    public OAuthProvider getProvider(OAuth2User oAuth2User){
        return switch (this.provider){
            case GOOGLE -> new GoogleOAuthProvider(oAuth2User);
            case GITHUB -> new GithubOAuthProvider(oAuth2User);
            case FACEBOOK -> new FacebookOAuthProvider(oAuth2User);
//            case TWITTER -> null;
//            case DISCORD -> null;
            case EMAIL -> null;
            default -> throw new IllegalArgumentException("Provider not supported");
        };
    }

    // getting which auth provider is used
    public static AppAuthProviders getAuthProviderType(String registrationId){
        if(registrationId.isEmpty()) throw new IllegalArgumentException("Registration id not supplied");
        try{
            return AppAuthProviders.valueOf(registrationId.toUpperCase());
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Provider not supported");
        }
    }
}
