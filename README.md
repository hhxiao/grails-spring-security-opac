[![Build Status](https://travis-ci.org/hhxiao/grails-spring-security-opac.svg)](https://travis-ci.org/hhxiao/grails-spring-security-opac)

grails-spring-security-opac
===========================

Opac authentication plugin for Grails, extension to [Grails Spring Security Core plugin](http://www.grails.org/plugin/spring-security-core)


Configuration
---------------------------


Server configuration
~~~~~~~~~~~
grails.plugins.springsecurity.opac.service.url = "SERVICE_URL"
grails.plugins.springsecurity.opac.username = "USERNAME"
grails.plugins.springsecurity.opac.password = "PASSWORD"
~~~~~~~~~~~

Define field mapping with user domain class and role mapping
~~~~~~~~~~~
grails.plugins.springsecurity.opac.user.autoCreate = true
grails.plugins.springsecurity.opac.user.fieldMappings = [
    regUsername: '{username}',
    regPassword: '{password}',
    regId: 'z303-id',
    displayName: 'z303-name',
    realName: 'z303-name',
    email: 'z304-email-address'
]
grails.plugins.springsecurity.opac.authorities.defaultRoles = ['ROLE_USER']
~~~~~~~~~~~
