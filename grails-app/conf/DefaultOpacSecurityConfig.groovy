
/**
 * Created by IntelliJ IDEA.
 * User: xiaohai
 * Date: 2010-11-5
 * Time: 17:53:55
 * To change this template use File | Settings | File Templates.
 */
security {
	opac {
		service {
			url = 'http://aleph-01.clcn.net.cn:8991'
			username = 'www-test'
			password = 'test'
			library = 'stl50'
		}

		auth {
			hideUserNotFoundExceptions = false
		}

		user {
			autoCreate = true
			fieldMappings = [:]
		}

		authorities {
			retrieveDatabaseRoles = false
			defaultRoles = ['ROLE_USER']
		}
	}
}
