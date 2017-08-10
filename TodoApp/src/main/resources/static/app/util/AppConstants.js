(function(angular) {
	var AppConstants = {

		UNKNOWN_ERROR                                	: -1,
		SUCCESS 										: 0,
		SYSTEM_UNAVAILABLE 								: 1001,
		SYSTEM_INTERNAL_ERROR 							: 1002,
		
		BASE_URL 										: "http://localhost:8080",
		URL_OAUTH_TOKEN									: "/oauth/token",
		URL_EMPLOYEE_PROFILE	 						: "/mobibranch/profile",
		
		TEST											: 'AppConstants are accessable'
	};

	angular.module("todoapp").constant("AppConstants", AppConstants);
}(angular));