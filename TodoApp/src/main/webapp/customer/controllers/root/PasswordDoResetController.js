(function(angular) {
	var controller = function($scope, $rootScope, SharingService, $timeout, AppConstants, CommonService, httpService, $httpParamSerializer, $http, $state, $stateParams, toaster) {

		CommonService.checkSession();
		
		var passwordResetKey = $stateParams.resetkey;
		if (!passwordResetKey) {
			$location.path('/');
			return;
		}
		
		/*
		 * It looks like you clicked on an invalid password reset link. Please try again.
		 */
		
		httpService.post(AppConstants.BASE_URL + '/api/open/checkPasswordResetKey/' + passwordResetKey + '/', null, false, function(response){
			var data = response.data;
			if (data) {
				var status = data.status;
				if (status) {
					if (status.code == AppConstants.SUCCESS) {
						$scope.sentSuccessfully = true;
						
					} else if (status.code == AppConstants.ACCOUNT_NOT_FOUND) {
						$timeout(function() {
							$scope.email = '';
						}, 1500);
						toaster.pop('warning', 'Email not found', "Can't find that email, sorry.");
						
					} else if (status.code == AppConstants.ACCOUNT_NOT_ACTIVATED) {
						toaster.pop('warning', 'Disactivated', status.message);
						
					} else if (status.code == AppConstants.ACCOUNT_NOT_ENABLED) {
						toaster.pop('warning', 'Disabled', status.message);
						
					} else if (status.code == AppConstants.EMAIL_TRANSMISSION_ERROR) {
						toaster.pop('warning', 'Sending failed', status.message + '. Please try again later.');
						
					} else {
						toaster.pop('error', 'Reset password error.', 'Status code: ' + status.code + ', message: ' + status.message);
					}
				} else {
					toaster.pop('error', 'Reset password error.', ' No data status returned. Data: ' + JSON.stringify(data));
				}
			} else {
				toaster.pop('error', 'Reset passwordgnUp error.', ' No data returned.');
			}
	    }, function(response){
			if (response.status) {
				toaster.pop('error', 'Reset password error.', ' HTTP status: ' + response.status + '. StatusText: ' + response.statusText);
			} else {
				toaster.pop('error', 'Reset password error.', ' Response: ' + JSON.stringify(response));
			}
	    });
		
		
		
		$scope.sendPasswordReset = function(event) {
			var formElement = angular.element(event.target);
	        
	        formElement.addClass('was-validated');
	        if (formElement[0].checkValidity() === false) {
	            event.preventDefault();
	            event.stopPropagation();
	            return;
	        }
	        
	        doSend($scope.email); 
		};
		
		function doSend(email) {
			httpService.post(AppConstants.BASE_URL + '/api/open/passwordReset/' + email + '/', null, false, function(response){
				var data = response.data;
				if (data) {
					var status = data.status;
					if (status) {
						if (status.code == AppConstants.SUCCESS) {
							$scope.sentSuccessfully = true;
							
						} else if (status.code == AppConstants.ACCOUNT_NOT_FOUND) {
							$timeout(function() {
								$scope.email = '';
							}, 1500);
							toaster.pop('warning', 'Email not found', "Can't find that email, sorry.");
							
						} else if (status.code == AppConstants.ACCOUNT_NOT_ACTIVATED) {
							toaster.pop('warning', 'Disactivated', status.message);
							
						} else if (status.code == AppConstants.ACCOUNT_NOT_ENABLED) {
							toaster.pop('warning', 'Disabled', status.message);
							
						} else if (status.code == AppConstants.EMAIL_TRANSMISSION_ERROR) {
							toaster.pop('warning', 'Sending failed', status.message + '. Please try again later.');
							
						} else {
							toaster.pop('error', 'Reset password error.', 'Status code: ' + status.code + ', message: ' + status.message);
						}
					} else {
						toaster.pop('error', 'Reset password error.', ' No data status returned. Data: ' + JSON.stringify(data));
					}
				} else {
					toaster.pop('error', 'Reset passwordgnUp error.', ' No data returned.');
				}
		    }, function(response){
				if (response.status) {
					toaster.pop('error', 'Reset password error.', ' HTTP status: ' + response.status + '. StatusText: ' + response.statusText);
				} else {
					toaster.pop('error', 'Reset password error.', ' Response: ' + JSON.stringify(response));
				}
		    });
		}

	};
	controller.$inject = [ '$scope', '$rootScope','SharingService', '$timeout', 'AppConstants', 'CommonService', 'httpService', '$httpParamSerializer', '$http', '$state', '$stateParams', 'toaster'];
	angular.module('todoapp.controllers').controller('PasswordDoResetController', controller);
}(angular));