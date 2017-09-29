(function(angular) {
	
	var Interceptor = function($q, $window, AppConstants, $cookies, $rootScope, $injector) {
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
				case 400:
				case 401:
					/* disable refresh method for now
					$cookies.remove('access_token');
					var httpService = $injector.get('httpService');
					httpService.refresh();
					break;
					*/
				case 403:
				case 406:
					var data = rejection.data;
					if (data) {
						var desc = data.error_description;
						if (desc) {
							if (desc.contains('Invalid refresh token:') || desc.contains('Refresh token expired:')) {
								$cookies.remove('refresh_token');
							} else if (desc.contains('Invalid access token:') || desc.contains('Access token expired:')) {
								$cookies.remove('access_token');
							}
						}
					}
					
					$state = $injector.get('$state');
					//$state.go('unauthorized', {message : 'Your request is unauthorized.'});
					break;
				case 500:
					$state.go('unauthorized', {message : 'Error occurred on server.'});
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

	Interceptor.$inject = [ '$q', '$window', 'AppConstants', '$cookies', '$rootScope', '$injector' ];
	
	angular.module('todoapp').config(function($httpProvider) {
		$httpProvider.interceptors.push('Interceptor');
	}).factory('Interceptor', Interceptor);

}(angular));