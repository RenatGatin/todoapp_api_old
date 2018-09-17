(function(angular) {
	var controller = function($scope, $rootScope, SharingService, $timeout, AppConstants, CommonService, httpService, $httpParamSerializer, $http, $state, $stateParams, toaster) {

		CommonService.checkSession();
		
		var passwordResetKey = $stateParams.resetkey;
		if (!passwordResetKey) {
			$location.path('/');
			return;
		}
		
		/*
		 * Validate key
		 */
		httpService.get(AppConstants.BASE_URL + '/api/open/checkPasswordResetKey/' + passwordResetKey + '/', null, false, function(response){
			var data = response.data;
			if (data) {
				var status = data.status;
				if (status) {
					if (status.code == AppConstants.SUCCESS) {
						$scope.keyVerified = true;
						
					} else if (status.code == AppConstants.ACCOUNT_NOT_FOUND) {
						$state.go('password-reset');
						toaster.pop('error', 'Invalid', "It looks like you clicked on an invalid password reset link. Please try again.", 7000);
						
					} else {
						$state.go('password-reset');
						toaster.pop('error', 'Error', 'Error validating reset passoword key. Code: ' + status.code + ', message: ' + status.message);
					}
				} else {
					toaster.pop('error', 'Error validating reset passoword key', ' No data status returned. Data: ' + JSON.stringify(data));
				}
			} else {
				toaster.pop('error', 'Error validating reset passoword key', ' No data returned.');
			}
	    }, function(response){
			if (response.status) {
				toaster.pop('error', 'Error validating reset passoword key', ' HTTP status: ' + response.status + '. StatusText: ' + response.statusText);
			} else {
				toaster.pop('error', 'Error validating reset passoword key', ' Response: ' + JSON.stringify(response));
			}
	    });
		
		$scope.changePassword = function(event) {
			var formElement = angular.element(event.target);
	        
	        formElement.addClass('was-validated');
	        if (formElement[0].checkValidity() === false) {
	            event.preventDefault();
	            event.stopPropagation();
	            return;
	        }
	        
	        // validate password and confirmPassword do match!
	        
	        var changePasswordBean = {
	        		passwordResetKey : passwordResetKey,
	        		password : $scope.password
	        }
	        
	        doChangePassword(changePasswordBean); 
		};

	};
	controller.$inject = [ '$scope', '$rootScope','SharingService', '$timeout', 'AppConstants', 'CommonService', 'httpService', '$httpParamSerializer', '$http', '$state', '$stateParams', 'toaster'];
	angular.module('todoapp.controllers').controller('PasswordDoResetController', controller);
}(angular));