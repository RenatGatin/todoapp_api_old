(function(angular) {
	var UiRouting = function($urlRouterProvider, $stateProvider, $httpProvider) {

		$urlRouterProvider.otherwise('/home');

		$stateProvider

		// ///////////////////
		// ///// HOME ////////
		// ///////////////////

		.state('home', {
			url 		: '/home',
			templateUrl : './resources/pages/root/home.html',
			controller  : 'HomeController', 
			params		: {message : null}
		})
		
		.state('unauthorized', {
			url 		: '/unauthorized',
			templateUrl : './resources/pages/root/unauthorized.html',
			controller  : 'UnauthorizedController', 
			params		: {message : null}
		})
		
		.state('server-error', {
			url 		: '/server-error',
			templateUrl : './resources/pages/root/server-error.html',
			controller  : 'UnauthorizedController', 
			params		: {message : null}
		})
		
		.state('secured', {
			url : '/secured',
			templateUrl : './resources/pages/secure/secured.html',
			controller : 'SecuredController', //will be loaded lazily from 'resolve'
			resolve : {
				loadMyCtrl: ['$ocLazyLoad', '$cookies', function($ocLazyLoad, $cookies) {
					var access_token = $cookies.get('access_token');
					if (access_token) {
						return $ocLazyLoad.load({
							files: ['./controllers/secure/SecuredController.js?access_token=' + access_token]						});						
					}
				}]
			}
		})
	};

	UiRouting.$inject = [ '$urlRouterProvider', '$stateProvider', '$httpProvider'];
	angular.module('todoapp.routers', [ 'ui.router' ]).config(UiRouting);
}(angular));