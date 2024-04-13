package com.chaos.handler.authenticationToken;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-04-13 22:34
 **/
@Getter
public class EmailPasswordAuthenticationToken extends AbstractAuthenticationToken {
    private Object principal;

    private CharSequence credentials;

    private String email;


    public EmailPasswordAuthenticationToken(String email, String password) {
        super(null);
        this.credentials = password;
        this.email = email;
        this.setAuthenticated(false);
    }

    public EmailPasswordAuthenticationToken(Object principal, CharSequence credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
