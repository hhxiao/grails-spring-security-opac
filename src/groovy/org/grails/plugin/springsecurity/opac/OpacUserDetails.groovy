package org.grails.plugin.springsecurity.opac

import groovy.util.slurpersupport.GPathResult
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
/**
 * Created by IntelliJ IDEA.
 * User: xiaohai
 * Date: 2010-11-6
 * Time: 18:05:04
 * To change this template use File | Settings | File Templates.
 */
public class OpacUserDetails implements UserDetails {
    private Collection<GrantedAuthority> extraAuthorities = Collections.emptyList()

    private String username
    private String password
    boolean enabled
    boolean credentialsNonExpired = true
    boolean accountNonLocked = true
    boolean accountNonExpired = true

    Object id
    GPathResult userData

    public OpacUserDetails(String username, String password, Collection<GrantedAuthority> extraAuthorities) {
        this.username = username
        this.password = password
        this.extraAuthorities = extraAuthorities
        this.enabled = true
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return extraAuthorities;
    }

    @Override
    public String getPassword() {
        return password; 
    }

    @Override
    public String getUsername() {
        return username
    }
}
