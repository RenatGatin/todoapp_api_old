(function(angular) {
	
	var Interceptor = function($q, $window, AppConstants, $cookies, $rootScope, $injector, toaster ) {
		return {
			request: function($config) {
				$rootScope.isLoading = true;
	            if ($config.url != (AppConstants.BASE_URL + AppConstants.POST_OAUTH_TOKEN)) {
	            	var token = $cookies.get('access_token');
	            	if (token) {
	            		$config.headers['Authorization'] = 'Bearer '+ token;
	            	}
	            }
	            return $config;
	        },
	        
	        response: function(response) {
	        	$rootScope.isLoading = false;
	        	
	            return response;
	        },
	        
			responseError : function(rejection) {
				$state = $injector.get('$state');
				httpService = $injector.get('httpService');
				
				$rootScope.isLoading = false;
				var url = rejection.config.url;
				switch (rejection.status) {
				case 400:
				case 401:
					/* disable refresh method for now
					$cookies.remove('access_token');
					var httpService = $injector.get('httpService');
					httpService.refresh();
					break;
					*/
				case 406:
					var data = rejection.data;
					if (data) {
						var desc = data.error_description;
						if (desc) {
							if (desc.contains('Invalid refresh token:') || desc.contains('Refresh token expired:')) {
								$cookies.remove('refresh_token');
							} else if (desc.contains('Invalid access token:') || desc.contains('Access token expired:')) {
								$cookies.remove('access_token');
								httpService.removeGlobalTokens();
								$rootScope.profile = null;
							}
						}
						
						var rejectionStatus = rejection.status == 401;
						var dataStatus = data.status && data.status == 401;
						if (rejectionStatus || dataStatus) {
							swal({
								title : (dataStatus) ? data.error : rejection.statusText,
								text: (dataStatus) ? data.message : 'Please sign in to access this resource\n' + url,
								type : "error",
								showCancelButton : false,
								confirmButtonText : "Sign in",
								closeOnConfirm : true
							}, function() {
								$state.go('home');
							});
						}
					}
					break;
				case 403:
					var error = 'Failure accessing resource: "' + url + '". ';
					var description;
					try {
						description = JSON.parse(rejection.data).error_description;
					} catch (e) {
						description = rejection.data;
					}
					error += description;
					toaster.pop('error', 'HTTP 403', error);
					break;
				case 404:
					toaster.pop('error', 'HTTP 404', 'Resource not found: "' + url + '"');
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

	Interceptor.$inject = [ '$q', '$window', 'AppConstants', '$cookies', '$rootScope', '$injector', 'toaster' ];
	
	angular.module('todoapp').config(function($httpProvider) {
		$httpProvider.interceptors.push('Interceptor');
	}).factory('Interceptor', Interceptor);

}(angular));