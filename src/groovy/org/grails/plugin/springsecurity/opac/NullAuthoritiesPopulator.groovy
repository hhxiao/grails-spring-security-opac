package org.grails.plugin.springsecurity.opac
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils

final class NullAuthoritiesPopulator implements AuthoritiesPopulator {
    @Override
    Collection<GrantedAuthority> getGrantedAuthorities(String username) {
        return AuthorityUtils.NO_AUTHORITIES
    }
}
