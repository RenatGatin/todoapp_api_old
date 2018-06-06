(function(angular) {
	var controller = function($scope, $rootScope, SharingService, $timeout, AppConstants, CommonService, httpService, $httpParamSerializer, $http, $state, $stateParams, toaster) {

		CommonService.checkSession();
		
		$scope.signIn = function(event) {
			var formElement = angular.element(event.target);
	        
	        formElement.addClass('was-validated');
	        if (formElement[0].checkValidity() === false) {
	            event.preventDefault();
	            event.stopPropagation();
	            return;
	        }
	        
	        doSignIn($scope.customer); 
		};
		
		function doSignIn(credentials) {
			httpService.login(credentials, function(response){
				
				if (response.status == 200) {
					httpService.setTokens(response.data);
					$state.go('dashboard');
					
				} else {
					toaster.pop('error', 'SignIn error. HTTP status: ' + response.status + '. Message: ' + resonse.data.error_description);
				}
		    }, function(response){
				if (response.data && response.data.error) {
					toaster.pop('error', 'SignIn error. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
				} else {
					toaster.pop('error', 'SignIn error. Response: ' + JSON.stringify(response));
				}
		    });
		}

	};
	controller.$inject = [ '$scope', '$rootScope','SharingService', '$timeout', 'AppConstants', 'CommonService', 'httpService', '$httpParamSerializer', '$http', '$state', '$stateParams', 'toaster'];
	angular.module('todoapp.controllers').controller('SignInController', controller);
}(angular));