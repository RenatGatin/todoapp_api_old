(function(angular) {
	var controller = function($scope, $rootScope, SharingService, $timeout, AppConstants, CommonService, httpService, $httpParamSerializer, $http, $state, $stateParams, toaster) {

		CommonService.checkSession('sign-up');
		$scope.step = 'create';
		
		
	};
	controller.$inject = [ '$scope', '$rootScope','SharingService', '$timeout', 'AppConstants', 'CommonService', 'httpService', '$httpParamSerializer', '$http', '$state', '$stateParams', 'toaster'];
	angular.module('todoapp.controllers').controller('SignUpController', controller);
}(angular));