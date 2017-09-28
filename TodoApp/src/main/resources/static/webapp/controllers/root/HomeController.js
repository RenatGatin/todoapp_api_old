(function(angular) {
	var HomeController = function($scope, $rootScope, $timeout, AppConstants, SharingService, httpService, $httpParamSerializer, $http, $state, $stateParams) {

		$scope.data = {username:'', password:''};
		$scope.errorMessage = $stateParams.message
		
		$scope.doAdminLogin = function() {
			httpService.login($scope.data, function(response){
				if (response.data && response.data.error)
					alert(response.data.error_description);
		    });
		}
		
		$scope.securedPage = function() {
			$state.go('secured');
		}
		
		$scope.securedApi = function() {
			var url = '/api/superadmin/getAdminProfileList';
			httpService.get(url, null, false, function(response){
				$scope.securedApiResponse = JSON.stringify(response);
		    }, function(response){
				$scope.securedApiResponse = JSON.stringify(response);
		    });
		}

	};
	HomeController.$inject = [ '$scope', '$rootScope', '$timeout', 'AppConstants', 'SharingService', 'httpService', '$httpParamSerializer', '$http', '$state', '$stateParams'];
	angular.module('todoapp.controllers').controller('HomeController', HomeController);
}(angular));