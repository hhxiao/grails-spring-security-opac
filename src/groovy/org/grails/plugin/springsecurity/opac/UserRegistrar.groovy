package org.grails.plugin.springsecurity.opac

import groovy.util.slurpersupport.GPathResult
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import java.util.concurrent.ConcurrentHashMap
/**
 * Created by haihxiao on 2014/12/18.
 */
class UserRegistrar {
    private static final Log logger = LogFactory.getLog(UserRegistrar.class)
    public static final String USERNAME = '{username}'
    public static final String PASSWORD = '{password}'

    private Map<String, Object> rolesMap = new ConcurrentHashMap<>()

    def persistenceInterceptor
    def springSecurityService

    private def domainClass
    private def authorityJoinClass
    private def authorityClass

    private String usernamePropertyName
    private String passwordPropertyName
    private String enabledPropertyName
    private String nameField
    private Map<String, String> fieldMappings

    UserRegistrar(def grailsApplication) {
        def conf = SpringSecurityUtils.securityConfig
        domainClass = grailsApplication.getClassForName(conf.userLookup.userDomainClassName)
        authorityJoinClass = grailsApplication.getClassForName(conf.userLookup.authorityJoinClassName)
        authorityClass = grailsApplication.getClassForName(conf.authority.className)

        usernamePropertyName = conf.userLookup.usernamePropertyName
        passwordPropertyName = conf.userLookup.passwordPropertyName
        enabledPropertyName = conf.userLookup.enabledPropertyName
        nameField = conf.authority.nameField

        fieldMappings = SpringSecurityUtils.securityConfig.opac.user.fieldMappings
    }

    boolean isRegistered(UserDetails userDetails) {
        null != (domainClass."findBy${usernamePropertyName.capitalize()}"(userDetails.username))
    }

    def registerUser(OpacUserDetails userDetails) {
        try {
            if (persistenceInterceptor) {
                logger.debug("opening persistence context for UserRegistrar")
                persistenceInterceptor.init()
            }
            else {
                logger.debug("no persistence interceptor for UserRegistrar")
            }

            def domain = domainClass.newInstance()
            domain[(usernamePropertyName)] = userDetails.username
            domain[(passwordPropertyName)] = springSecurityService.encodePassword(userDetails.password)
            domain[(enabledPropertyName)] = true
            populate(domain, userDetails)
            domain.save(flush: true)

            for(GrantedAuthority authority : userDetails.authorities) {
                rolesMap
                def role = rolesMap.get(authority.authority)
                if(!role) {
                    role = authorityClass."findBy${nameField.capitalize()}"(authority.authority)
                    if(!role) throw new IllegalArgumentException("Role not found: ${authority.authority}")
                    rolesMap.put(authority.authority, role)
                }
                authorityJoinClass.create(domain, role, true)
            }
            logger.info("${userDetails.username} registered successfully")
            userDetails.id = domain.id
            return domain
        } finally {
            if (persistenceInterceptor) {
                logger.debug("destroying persistence context for UserRegistrar")
                persistenceInterceptor.flush()
                persistenceInterceptor.destroy()
            }
        }
    }

    private static Map<String, Object> toMap(GPathResult node) {
        Map<String, Object> bindings = new HashMap<String, Object>()
        node.children().each { child ->
            child.children().each { dec ->
                bindings.put(dec.name(), dec.text())
            }
        }
        return bindings
    }

    private void populate(def domain, OpacUserDetails userDetails) {
        Map<String, Object> bindings = toMap(userDetails.userData)
        bindings.put(USERNAME, userDetails.username)
        bindings.put(PASSWORD, userDetails.password)

        fieldMappings.each { key, value ->
            domain[(key)] = bindings.get(value)
        }
    }
}
