(function(angular) {
	var UnauthorizedController = function($scope, $rootScope, $timeout, AppConstants, SharingService, httpService, $httpParamSerializer, $http, $state, $stateParams) {

		$scope.errorMessage = $stateParams.message


	};
	UnauthorizedController.$inject = [ '$scope', '$rootScope', '$timeout', 'AppConstants', 'SharingService', 'httpService', '$httpParamSerializer', '$http', '$state', '$stateParams'];
	angular.module('todoapp.controllers').controller('UnauthorizedController', UnauthorizedController);
}(angular));