(function(angular) {
	var SecuredController = function($scope, $rootScope, AppConstants, SharingService, httpService, $httpParamSerializer, $http) {

		$scope.testAngular = 'Secured CUSTOMER Page loaded!';
		

	};
	SecuredController.$inject = [ '$scope', '$rootScope', 'AppConstants', 'SharingService', 'httpService', '$httpParamSerializer', '$http'];
	angular.module('todoapp.controllers').controller('SecuredController', SecuredController);
}(angular));