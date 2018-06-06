(function(angular) {
	var HomeController = function($scope, $window, SharingService, $rootScope, $timeout, AppConstants, CommonService, httpService, $httpParamSerializer, $http, $state, $stateParams, toaster) {
		$scope.errorMessage = $stateParams.message;

	};
	HomeController.$inject = [ '$scope', '$window', 'SharingService', '$rootScope', '$timeout', 'AppConstants', 'CommonService', 'httpService', '$httpParamSerializer', '$http', '$state', '$stateParams', 'toaster'];
	angular.module('todoapp.controllers').controller('HomeController', HomeController);
}(angular));