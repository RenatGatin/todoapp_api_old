(function(angular) {
	
	angular.module('todoapp.routers', ['ui.router', 'uiRouterStyles']);
	angular.module('todoapp.controllers', []);
	angular.module('todoapp', ['todoapp.routers','todoapp.controllers', 'ngCookies', 'oc.lazyLoad', 'toaster', 'ngAnimate'])
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
				//SharingService.set('reloadedHome', false);
				CommonService.logout();
			}
			
			SharingService.set('test' , 'SharingService!');
		});
}(angular));