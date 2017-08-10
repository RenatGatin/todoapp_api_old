(function(angular) {
	
	angular.module('todoapp.routers', ['ui.router']);
	angular.module('todoapp.controllers', ['ui.bootstrap']);
	angular.module('todoapp', ['todoapp.routers','todoapp.controllers'])
	
		.run(function($rootScope, SharingService) {
			$rootScope.isLoading = false;
			
			SharingService.set('test' , 'SharingService works!')
			
		});
}(angular));