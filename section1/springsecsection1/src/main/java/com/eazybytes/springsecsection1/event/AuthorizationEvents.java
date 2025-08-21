package com.eazybytes.springsecsection1.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationEvents {

    @EventListener
    public void onFailure(AuthorizationDeniedEvent failureEvent) {
        log.error("failed to authz user {} bc of : {}", failureEvent.getAuthentication().get().getName(), failureEvent.getAuthorizationDecision().toString());
    }
}
