(function(angular) {
	
	var Interceptor = function($q, $window, AppConstants, $cookies, $rootScope) {
		return {
			request: function($config) {
				$rootScope.isLoading = true;
	            if ($config.url != (AppConstants.BASE_URL + AppConstants.URL_OAUTH_TOKEN)) {
	            	var token = $cookies.get('access_token');
	            	if (token) {
	            		$config.headers['Authorization'] = 'Bearer '+ token;
	            	}
	            }
	            return $config;
	        },
	        
	        response: function(response) {
	        	$rootScope.isLoading = false;
	            //if you get a token back in your response you can use 
	            //the response interceptor to update the token in the 
	            //stored in the cookie
	            if (response.config.url === 'api/token' && response.config.data.tokenData) {
	                  //fetch token
	                  var token=response.config.data.tokenData;

	                  //set token
	                  //$window.sessionStorage.setItem('userInfo-token', token);
	            }
	            return response;
	        },
	        
			responseError : function(rejection) {
				$rootScope.isLoading = false;
				switch (rejection.status) {
				case 401:
				case 403:
				case 406:
					$cookies.remove('access_token');
					$window.location = '/webapp/unauthorized.html';
					break;
				case 500:
					$window.location = '/';
					break;
				}
				return $q.reject(rejection);
			},
			
			requestError : function(rejection) {
				console.log(rejection);
				
				return $q.reject(errorResponse);
			}
		};
	};

	Interceptor.$inject = [ '$q', '$window', 'AppConstants', '$cookies', '$rootScope' ];
	
	angular.module('todoapp').config(function($httpProvider) {
		$httpProvider.interceptors.push('Interceptor');
	}).factory('Interceptor', Interceptor);

}(angular));