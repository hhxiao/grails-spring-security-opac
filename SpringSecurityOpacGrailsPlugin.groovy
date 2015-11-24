import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.grails.plugin.springsecurity.opac.NullAuthoritiesPopulator
import org.grails.plugin.springsecurity.opac.OpacAuthenticationProvider
import org.grails.plugin.springsecurity.opac.OpacAuthenticator
import org.grails.plugin.springsecurity.opac.PersistentAuthoritiesPopulator
import org.grails.plugin.springsecurity.opac.UserRegistrar

class SpringSecurityOpacGrailsPlugin {
	// the plugin version
	String version = '1.0.0'
	String grailsVersion = '2.0.0 > *'
	Map dependsOn = ['springSecurityCore': '1.0 > *']

	List pluginExcludes = [
			'docs/**',
			'src/docs/**'
	]

	def author = "Hai-Hua Xiao"
	def authorEmail = "hhxiao@gmail.com"
	def title = "Opac authentication support for the Spring Security plugin"
	def description = "Opac authentication support for the Spring Security plugin"

	// URL to the plugin's documentation
	def documentation = "http://grails.org/plugin/spring-security-opac"

	def doWithSpring = {
		def conf = SpringSecurityUtils.securityConfig
		if (!conf || !conf.active) {
			return
		}

		println 'Configuring Spring Security Opac ...'

		SpringSecurityUtils.loadSecondaryConfig 'DefaultOpacSecurityConfig'
		conf = SpringSecurityUtils.securityConfig

		SpringSecurityUtils.getProviderNames().add('opacAuthProvider') // after all other providers

		if(conf.opac.authorities.defaultRoles) {
			opacAuthoritiesPopulator(PersistentAuthoritiesPopulator) {
				userDetailsService = ref('userDetailsService')
				retrieveDatabaseRoles = conf.opac.authorities.retrieveDatabaseRoles // false
				defaultRoles = conf.opac.authorities.defaultRoles.toArray()
			}
		} else {
			opacAuthoritiesPopulator(NullAuthoritiesPopulator)
		}

		opacAuthenticator(OpacAuthenticator) {
			url = conf.opac.service.url
			library = conf.opac.service.library
		}

		opacUserRegistrar(UserRegistrar, application) {
			persistenceInterceptor = ref('persistenceInterceptor')
			springSecurityService = ref('springSecurityService')
		}

		opacAuthProvider(OpacAuthenticationProvider, opacAuthenticator, opacAuthoritiesPopulator) {
			userRegistrar = ref('opacUserRegistrar')
			userCache = ref('userCache')
			hideUserNotFoundExceptions = conf.opac.auth.hideUserNotFoundExceptions // true
			preAuthenticationChecks = ref('preAuthenticationChecks')
			postAuthenticationChecks = ref('postAuthenticationChecks')
		}
	}

	private String[] toStringArray(value) {
		value == null ? null : value as String[]
	}

	private Class<?> classForName(String name) {
		Class.forName name, true, Thread.currentThread().contextClassLoader
	}
}
