(function(angular) {
	var controller = function($scope, $rootScope, SharingService, $timeout, AppConstants, CommonService, httpService, $httpParamSerializer, $http, $state, $stateParams, toaster) {

		CommonService.checkSession();
		
		var resetPasswordKey = $stateParams.resetkey;
		if (!resetPasswordKey) {
			$location.path('/');
			return;
		}
		
		/*
		 * Validate key
		 */
		httpService.get(AppConstants.BASE_URL + '/api/open/checkPasswordResetKey/' + resetPasswordKey + '/', null, false, function(response){
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
	        
	        var changePasswordBean = {
	        		resetPasswordKey : resetPasswordKey,
	        		password : $scope.password
	        }
	        
	        doChangePassword(changePasswordBean); 
		};
		
		function doChangePassword(bean) {
			httpService.post(AppConstants.BASE_URL + '/api/open/changePasswordWithKey', bean, false, function(response){
				var data = response.data;
				if (data) {
					var status = data.status;
					if (status) {
						if (status.code == AppConstants.SUCCESS) {
							$scope.changedSuccessfully = true;
							
						} else if (status.code == AppConstants.ACCOUNT_NOT_FOUND) {
							$state.go('password-reset');
							toaster.pop('error', 'Invalid', "It looks like your password reset link is expired. Please try again.", 7000);
						
						} else if (status.code == AppConstants.INVALID_DATA) {
							toaster.pop('warning', 'Validation', data.fieldErrors[0].message);
							
						} else {
							toaster.pop('error', 'Error', 'Error changing passoword. Code: ' + status.code + ', message: ' + status.message);
						}
					} else {
						toaster.pop('error', 'Error changing passoword', ' No data status returned. Data: ' + JSON.stringify(data));
					}
				} else {
					toaster.pop('error', 'Error changing passoword', ' No data returned.');
				}
		    }, function(response){
				if (response.status) {
					toaster.pop('error', 'Error changing passoword', ' HTTP status: ' + response.status + '. StatusText: ' + response.statusText);
				} else {
					toaster.pop('error', 'Error changing passoword', ' Response: ' + JSON.stringify(response));
				}
		    });
		} 

	};
	controller.$inject = [ '$scope', '$rootScope','SharingService', '$timeout', 'AppConstants', 'CommonService', 'httpService', '$httpParamSerializer', '$http', '$state', '$stateParams', 'toaster'];
	angular.module('todoapp.controllers').controller('PasswordDoResetController', controller);
}(angular));