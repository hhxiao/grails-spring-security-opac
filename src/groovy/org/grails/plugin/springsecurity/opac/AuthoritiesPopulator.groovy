package org.grails.plugin.springsecurity.opac

import org.springframework.security.core.GrantedAuthority
/**
 * Obtains a list of granted authorities for a  user.
 * <p>
 * Used by the <tt>OpacAuthenticationProvider</tt> once a user has been
 * authenticated to create the final user details object.
 * </p>
 */
interface AuthoritiesPopulator {
    /**
     * Get the list of authorities for the user.
     *
     * @return the granted authorities for the given user.
     *
     */
    Collection<GrantedAuthority> getGrantedAuthorities(String username)
}
