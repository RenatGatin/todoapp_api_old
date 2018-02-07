(function(angular) {
	
	angular.module('todoapp.routers', ['ui.router', 'uiRouterStyles']);
	angular.module('todoapp.controllers', ['ui.bootstrap']);
	angular.module('todoapp', ['todoapp.routers','todoapp.controllers', 'ngCookies', 'oc.lazyLoad', 'toaster'])
		.config(['$ocLazyLoadProvider', function($ocLazyLoadProvider) {
			$ocLazyLoadProvider.config({
				debug  : true, // disable for production (it shows token in console log)
				events : true
			})
		}])
	
		.run(function($rootScope, SharingService, AppConstants, CommonService) {
			CommonService.checkSession();
			
			$rootScope.isLoading = false;
			
			$rootScope.logout = function() {
				CommonService.logout();
			}
			
			SharingService.set('test' , 'SharingService!');
		});
}(angular));