(function(angular) {
	var controller = function($scope, $rootScope, AppConstants, SharingService, httpService, $httpParamSerializer, $http, CommonService) {
		//SharingService.set('reloadedHome', false);
		
		CommonService.getProfile();
		
		$scope.testAngular = "CUSTOMER's DashboardController Page loaded!";
			
		$scope.securedApi = function() {
			var url = '/api/user/ping';
			httpService.get(url, null, false, function(response){
				$scope.securedApiResponse = JSON.stringify(response);
		    }, function(response){
				$scope.securedApiResponse = JSON.stringify(response);
		    });
		};
		
		$scope.showAlert = function() {
			swal({
				title : "Test header",
				type : "success",
				showCancelButton : true,
				confirmButtonColor : "#8FC23F",
				confirmButtonText : "Continue",
				closeOnConfirm : true
			}, function() {
				// do nothing
			});
		};
		
		//$scope.errorMessage = 'Test tttttt';

	};
	controller.$inject = [ '$scope', '$rootScope', 'AppConstants', 'SharingService', 'httpService', '$httpParamSerializer', '$http', 'CommonService'];
	angular.module('todoapp.controllers').controller('DashboardController', controller);
}(angular));