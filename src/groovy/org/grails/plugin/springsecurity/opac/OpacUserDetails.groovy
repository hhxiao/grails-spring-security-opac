package org.grails.plugin.springsecurity.opac

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
/**
 * Created by IntelliJ IDEA.
 * User: xiaohai
 * Date: 2010-11-6
 * Time: 18:05:04
 * To change this template use File | Settings | File Templates.
 */
class OpacUserDetails implements UserDetails {
    private Collection<GrantedAuthority> authorities = Collections.emptyList()

    private String username
    private String password
    boolean enabled
    boolean credentialsNonExpired = true
    boolean accountNonLocked = true
    boolean accountNonExpired = true

    private Object id

    OpacUserDetails(String username, String password, Collection<GrantedAuthority> authorities) {
        this(username, password, authorities, null)
    }

    OpacUserDetails(String username, String password, Collection<GrantedAuthority> authorities, Object id) {
        this.username = username
        this.password = password
        this.authorities = authorities
        this.enabled = true
        this.id = id
    }

    @Override
    Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    String getPassword() {
        return password; 
    }

    @Override
    String getUsername() {
        return username
    }

    @Override
    String toString() {
        username
    }
}
