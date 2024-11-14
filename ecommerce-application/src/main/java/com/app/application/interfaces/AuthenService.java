package com.app.application.interfaces;

import com.app.application.dto.BaseResponse;
import com.app.application.dto.auth.AuthRegisReq;
import com.app.application.dto.auth.AuthReq;
import com.app.application.dto.auth.AuthResponse;
import com.app.application.dto.auth.ProviderRequest;


public interface AuthenService {
    BaseResponse signUp(AuthRegisReq body);
    AuthResponse signIn(AuthReq body);
    AuthResponse refreshToken(String refreshToken);
    Boolean findByProviderAndProviderId(String provider,String providerId);
    AuthResponse providerSignUp(ProviderRequest provider);
    AuthResponse provider(ProviderRequest provider);
}
