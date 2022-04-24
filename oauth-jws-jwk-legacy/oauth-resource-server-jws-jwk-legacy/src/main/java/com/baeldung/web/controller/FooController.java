package com.baeldung.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baeldung.web.dto.Foo;

@RestController
@RequestMapping("/foos")
public class FooController {

    @Autowired
    TokenStore tokenStore;

    @PreAuthorize("#oauth2.hasScope('read')")
    @GetMapping("/{id}")
    public Foo retrieveFoo(@PathVariable("id") Long id, OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails authenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        OAuth2AccessToken token = tokenStore.readAccessToken(authenticationDetails.getTokenValue());
        return new Foo(id, (String) token.getAdditionalInformation().get("jti"));
    }

}
