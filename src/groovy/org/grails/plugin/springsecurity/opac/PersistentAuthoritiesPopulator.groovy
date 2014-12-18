package org.grails.plugin.springsecurity.opac

import org.springframework.beans.factory.InitializingBean
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.util.Assert
/**
 * An AuthoritiesPopulator loads the roles from database mapping
 */
class PersistentAuthoritiesPopulator extends DefaultAuthoritiesPopulator implements AuthoritiesPopulator, InitializingBean {
    UserDetailsService userDetailsService
    boolean retrieveDatabaseRoles

	@Override
	protected Set<GrantedAuthority> getAdditionalRoles(final String username) {
		if (retrieveDatabaseRoles) {
            try {
                UserDetails dbDetails = userDetailsService.loadUserByUsername(username)
                if (dbDetails.getAuthorities() != null) {
                    return new HashSet<GrantedAuthority>(dbDetails.getAuthorities())
                }
            } catch(Exception ignore) {}
		}
		return null
	}

    /**
     * {@inheritDoc}
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    void afterPropertiesSet() {
        Assert.notNull(userDetailsService, "userDetailsService must be specified")
    }
}
