package org.grails.plugin.springsecurity.opac

import groovy.util.slurpersupport.GPathResult
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
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
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String password = (String)authentication.getCredentials()
        try {
            GPathResult userData = opacAuthenticator.authenticate(username, password)
            Collection<GrantedAuthority> authorities = authoritiesPopulator.getGrantedAuthorities(username)

            if(autoCreate && !userRegistrar.isRegistered(username)) {
                return userRegistrar.registerUser(username, password, authorities, userData)
            }
            return new OpacUserDetails(username, password, authorities, userData)
        } catch (OpacException e) {
            logger.warn("Failed to login ${username}", e)
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }
}
