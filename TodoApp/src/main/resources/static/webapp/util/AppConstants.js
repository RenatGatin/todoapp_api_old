(function(angular) {
	var AppConstants = {

		UNKNOWN_ERROR                                	: -1,
		SUCCESS 										: 0,
		SYSTEM_UNAVAILABLE 								: 1001,
		SYSTEM_INTERNAL_ERROR 							: 1002,
		
		BASE_URL 										: 'http://localhost:8080',
		URL_OAUTH_TOKEN									: '/oauth/token',
		URL_EMPLOYEE_PROFILE	 						: '/mobibranch/profile',
		
		OAUTH2_GRANT_TYPE								: 'password',
		OAUTH2_AUTHORIZATION							: 'Basic d2ViOnBhc3M=',
		
		LIB_DEPENDENCY_TYPE_TEXT_JS						: 'text/javascript',
		LIB_DEPENDENCY_TYPE_TEXT_CSS					: 'text/css',
		LIB_DEPENDENCY_REL_CSS							: 'stylesheet',
		
		TEST											: 'AppConstants are accessable'
	};

	angular.module('todoapp').constant('AppConstants', AppConstants);
}(angular));