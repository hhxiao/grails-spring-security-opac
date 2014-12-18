package org.grails.plugin.springsecurity.opac

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
/**
 * Created by haihxiao on 2014/12/18.
 */
class OpacAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements AuthenticationProvider {
    private static final Log logger = LogFactory.getLog(OpacAuthenticationProvider.class)

    OpacAuthenticator opacAuthenticator
    AuthoritiesPopulator authoritiesPopulator
    UserRegistrar userRegistrar
    boolean autoCreate

    public OpacAuthenticationProvider(OpacAuthenticator opacAuthenticator, AuthoritiesPopulator authoritiesPopulator) {
        this.opacAuthenticator = opacAuthenticator
        this.authoritiesPopulator = authoritiesPopulator
        this.autoCreate = SpringSecurityUtils.securityConfig.opac.user.autoCreate
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        if(userDetails) {
            try {
                OpacUserDetails opacUserDetails = (OpacUserDetails)userDetails
                opacUserDetails.userData = opacAuthenticator.authenticate(opacUserDetails.username, opacUserDetails.password)
            } catch (OpacException e) {
                logger.warn("Failed to login ${userDetails.username}", e)
                throw new AuthenticationServiceException(e.getMessage(), e);
            }
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String password = (String)authentication.getCredentials()
        Collection<GrantedAuthority> extraAuthorities = authoritiesPopulator.getGrantedAuthorities(username)
        OpacUserDetails user = new OpacUserDetails(username, password, extraAuthorities)
        return user
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        if(autoCreate && !userRegistrar.isRegistered(user)) {
            userRegistrar.registerUser((OpacUserDetails)user)
        }
        return super.createSuccessAuthentication(principal, authentication, user)
    }
}
