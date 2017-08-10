(function(angular) {
	var HomeController = function($scope, $rootScope, AppConstants, SharingService, httpService) {

		$scope.angularWorks = "AngularJS Works!";
		
		$scope.constCheck = AppConstants.TEST;
		
		$scope.serviceTest = SharingService.get('test');
		
		$scope.doAdminLogin = function() {
			
			
			alert('Login: ' + $scope.adminLogin + ', Pass: ' + $scope.adminPass);
			
			
			$rootScope.isLoading = true;
			httpService.post(AppConstants.BASE_URL + AppConstants.URL_OAUTH_TOKEN, {}, false, function(responseObj) {
	    		  	$rootScope.isLoading = false;
	    		  	
	    		  	var status = responseObj.data.status;
	      			if (status) {
	      				var statusCode = status.code;

	      				if (statusCode == AppConstants.SUCCESS) {
	      					toaster.pop('success', "Successfully created an ID Scan request on Server", "");
	      					var reqId = responseObj.data.entity;
	      					$rootScope.loadingCancelableText = "Waiting for MobiSign App ID capturing...";
	      		        	$rootScope.isLoadingCancelable = true;
	      					callGet(reqId);
	      					
	      				} else {
	      					toaster.pop('error', "Failure creating an ID Scan request on MobiSign Server", "");
	      				}
	      			} else {
	      				toaster.pop('error', "Failure creating an ID Scan request on MobiSign Server", "");
	      			}
					
		      }, function(responseObj) {
		    	  $rootScope.isLoading = false;
		    	  toaster.pop('error', "Failure creating an ID Scan request on MobiSign Server", "");
		      });
		}

	};
	HomeController.$inject = [ '$scope', '$rootScope', 'AppConstants', 'SharingService', 'httpService'];
	angular.module('todoapp.controllers').controller('HomeController', HomeController);
}(angular));