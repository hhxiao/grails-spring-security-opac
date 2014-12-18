package org.grails.plugin.springsecurity.opac

import groovy.util.slurpersupport.GPathResult
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * Created by haihxiao on 2014/12/18.
 */
class OpacAuthenticator {
    private static final Log logger = LogFactory.getLog(OpacAuthenticator.class)

    String url = 'http://aleph-01.clcn.net.cn:8991'
    String library = 'stl50'

    GPathResult authenticate(username, password) {
        String url = "${url}/X?op=bor-auth&bor-id=${username}&verification=${password}&library=${library}"
        String result = new URL(url).getText('UTF-8')
        def xmlSlurper = new XmlSlurper()
        def xml = xmlSlurper.parseText(result)
        String error = xml.error
        if(error) {
            logger.warn("Can not login ${username} - ${error}")
            throw new OpacException(error)
        }
        return xml
    }
}
