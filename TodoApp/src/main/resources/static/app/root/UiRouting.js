(function(angular) {
	var UiRouting = function($urlRouterProvider, $stateProvider, $httpProvider) {

		$urlRouterProvider.otherwise('/web');

		$stateProvider

		// ///////////////////
		// ///// HOME ////////
		// ///////////////////

		.state('home', {
			url : '/web',
			templateUrl : './pages/root/home.html',
			controller : 'HomeController' 
		})
	};
	
	var ResponseObserver = function($q, $window) {
		return {
			'responseError' : function(errorResponse) {
				switch (errorResponse.status) {
				case 403:
					$window.location = '/';
					break;
				case 500:
					$window.location = '/';
					break;
				}
				return $q.reject(errorResponse);
			}
		};
	};

	UiRouting.$inject = [ '$urlRouterProvider', '$stateProvider', '$httpProvider' ];
	ResponseObserver.$inject = [ '$q', '$window' ];
	
	angular.module('todoapp.routers', [ 'ui.router' ]).config(UiRouting).config(function($httpProvider) {
		$httpProvider.interceptors.push('responseObserver');
	}).factory('responseObserver', ResponseObserver);

}(angular));