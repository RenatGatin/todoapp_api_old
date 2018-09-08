(function(angular) {
	var AppConstants = {

		UNKNOWN_ERROR                                	: -1,
		SUCCESS 										: 0,
		SYSTEM_UNAVAILABLE 								: 1001,
		SYSTEM_INTERNAL_ERROR 							: 1002,
		ACCESS_TOKEN_NOT_FOUND							: 1504,
		INVALID_DATA									: 2004,
		ACCOUNT_UNIQUE_FIELD_DUPLICATION				: 2006,
		
		BASE_URL										: '/todoapp',
		
		POST_OAUTH_TOKEN								: '/oauth/token',
		POST_OAUTH_LOGOUT								: '/oauth/logout',
		GET_API_COMMON_PROFILE							: '/api/common/profile',
		
		OAUTH2_GRANT_TYPE_PASSWORD						: 'password',
		OAUTH2_GRANT_TYPE_REFRESH						: 'refresh_token',
		OAUTH2_AUTHORIZATION							: 'Basic d2ViOnBhc3M=',
		
		LIB_DEPENDENCY_TYPE_TEXT_JS						: 'text/javascript',
		LIB_DEPENDENCY_TYPE_TEXT_CSS					: 'text/css',
		LIB_DEPENDENCY_REL_CSS							: 'stylesheet',
		
		TEST											: 'AppConstants are accessable'
	};

	angular.module('todoapp').constant('AppConstants', AppConstants);
}(angular));