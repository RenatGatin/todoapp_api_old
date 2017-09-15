(function(angular) {
	var UiRouting = function($urlRouterProvider, $stateProvider, $httpProvider) {

		$urlRouterProvider.otherwise('/home');

		$stateProvider

		// ///////////////////
		// ///// HOME ////////
		// ///////////////////

		.state('home', {
			url : '/home',
			templateUrl : './resources/pages/root/home.html',
			controller : 'HomeController' 
		})
		
		.state('secured', {
			url : '/secured',
			templateUrl : './resources/pages/secure/secured.html',
			controller : 'SecuredController', //will be loaded lazily from 'resolve'
			resolve : {
				loadMyCtrl: ['$ocLazyLoad', '$cookies', function($ocLazyLoad, $cookies) {
					return $ocLazyLoad.load({
						files: ['./controllers/secure/SecuredController.js?access_token=' + $cookies.get('access_token')]
					});
				}]
			}
		})
	};
	
	//TODO: move interceptor to separate file
	var ResponseObserver = function($q, $window, AppConstants, $cookies, $rootScope) {
		return {
			'responseError' : function(errorResponse) {
				$rootScope.isLoading = false;
				switch (errorResponse.status) {
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
				return $q.reject(errorResponse);
			},
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
	        }
		};
	};

	UiRouting.$inject = [ '$urlRouterProvider', '$stateProvider', '$httpProvider'];
	ResponseObserver.$inject = [ '$q', '$window', 'AppConstants', '$cookies', '$rootScope' ];
	
	angular.module('todoapp.routers', [ 'ui.router' ]).config(UiRouting).config(function($httpProvider) {
		$httpProvider.interceptors.push('responseObserver');
	}).factory('responseObserver', ResponseObserver);

}(angular));