package com.bq.corbel.iam.auth.google.api.userinfo.impl;

import org.springframework.web.client.RestTemplate;

import com.bq.corbel.iam.auth.google.api.Endpoint;
import com.bq.corbel.iam.auth.google.api.impl.AbstractGoogleApiOperations;
import com.bq.corbel.iam.auth.google.api.userinfo.GoogleUserInfo;
import com.bq.corbel.iam.auth.google.api.userinfo.UserInfoOperations;

public class UserInfoTemplate extends AbstractGoogleApiOperations implements UserInfoOperations {

    public UserInfoTemplate(RestTemplate restTemplate, boolean authorized) {
        super(restTemplate, authorized);
    }

    @Override
    public GoogleUserInfo getUserInfo() {
        requireAuthorization();
        return restTemplate.getForObject(Endpoint.USER_INFO, GoogleUserInfo.class);
    }

}
