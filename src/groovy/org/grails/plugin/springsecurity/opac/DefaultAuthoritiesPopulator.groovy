package org.grails.plugin.springsecurity.opac

import groovy.util.slurpersupport.GPathResult
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.GrantedAuthorityImpl
/**
 * The default AuthoritiesPopulator has fixed set of authorities
 */
class DefaultAuthoritiesPopulator implements AuthoritiesPopulator {
    String[] defaultRoles

    /**
     * This method should be overridden if required to obtain any additional
     * roles for the given user (on top of those obtained from the standard
     * search implemented by this class).
     *
     * @param userData the context representing the user who's roles are required
     * @return the extra roles which will be merged with those returned by the group search
     */
    protected Set<GrantedAuthority> getAdditionalRoles(String username) {
        return Collections.emptySet()
    }

    @Override
    Collection<GrantedAuthority> getGrantedAuthorities(String username) {
        Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>()
        for(String role : defaultRoles) {
            authorities.add(new GrantedAuthorityImpl(role))
        }

        Set<GrantedAuthority> extraRoles = getAdditionalRoles(username)

        if (extraRoles != null) {
            authorities.addAll(extraRoles)
        }
        return authorities
    }
}
