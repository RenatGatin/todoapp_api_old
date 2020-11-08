(function(angular) {
	var controller = function($scope, $location, $rootScope, SharingService, $timeout, AppConstants, CommonService, httpService, $httpParamSerializer, $http, $state, $stateParams, toaster) {

		CommonService.checkSession('sign-up');
		var isPreSignup = ($stateParams.signup != null);
		var username = $location.search().username;
		var activationKey = $location.search().key;
		var isActivation = (username != null && activationKey != null);
		$scope.data = {};
		
		$scope.step = 'create';
		if (isPreSignup) {
			$scope.data.signup = $stateParams.signup;
			
		} else if (isActivation) {
			// check if account(by email) is not activated yet and exists
			 httpService.get(AppConstants.BASE_URL + '/api/open/isPreSignupUserActivated/' + username + '/', null, false, function(response) {
				var data = response.data;
				if (data) {
					var status = data.status;
					if (status) {
						// if account does not exist show error toast and create step
						if (status.code == AppConstants.PRESIGNUP_USER_NOT_EXIST) {
							toaster.pop('error', 'Invalid request', 'User with email: ' + username + ' does not exist. Create one.');
							
						// if account is already activate show Start new ToDo list step
						} else if (status.code == AppConstants.PRESIGNUP_USER_STATUS_ACTIVATED) {
							$scope.step = 'newlist';
							toaster.pop('success', 'Activated', 'User with email: ' + username + ' was already activated.');
							
						} else if (status.code == AppConstants.PRESIGNUP_USER_STATUS_NOT_ACTIVATED) {
							$scope.step = 'confirm';
							$scope.data.activation = {
								email : username,
								key : activationKey
							}
						} else {
							toaster.pop('error', 'Error checking user status.', 'Status code: ' + status.code + ', message: ' + status.message);
						}
					} else {
						toaster.pop('error', 'Error checking user status', ' No data status returned. Data: ' + JSON.stringify(data));
					}
				} else {
					toaster.pop('error', 'Error checking user status', ' No data returned.');
				}
		    }, function(response){
				if (response.data && response.data.error) {
					toaster.pop('error', 'Error checking user status', ' HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
				} else if (response.status) {
					toaster.pop('error', 'Error checking user status', ' HTTP status: ' + response.status + '. StatusText: ' + response.statusText);
				} else {
					toaster.pop('error', 'Error checking user status', ' Response: ' + JSON.stringify(response));
				}
		    });
			
		}
				
		$scope.createAccount = function (event) {
	        var formElement = angular.element(event.target);
	        
	        //1. check HTML form validation
	        formElement.addClass('was-validated');
	        if (formElement[0].checkValidity() === false) {
	            event.preventDefault();
	            event.stopPropagation();
	            return;
	        }
	        
	        //2. try to create account
	        httpService.post(AppConstants.BASE_URL + '/api/open/preSignupUser', $scope.data.signup, false, function(response){
				var data = response.data;
				if (data) {
					var status = data.status;
					if (status) {
						if (status.code == AppConstants.SUCCESS) {
							$scope.step = 'confirm';
							$scope.data.activation = {
								email : $scope.data.signup.email,
								key : null
							}
							
						} else if (status.code == AppConstants.INVALID_DATA) {
							printInvalidFields(data.fieldErrors);
							
						} else if (status.code == AppConstants.ACCOUNT_UNIQUE_FIELD_DUPLICATION) {
							$timeout(function() {
								$scope.data.signup.email = null;
							}, 1500);
							toaster.pop('warning', 'Email is taken ', 'This email is already used, please choose different one');
							
						} else {
							toaster.pop('error', 'SignUp error.', 'Status code: ' + status.code + ', message: ' + status.message);
						}
					} else {
						toaster.pop('error', 'SignUp error.', ' No data status returned. Data: ' + JSON.stringify(data));
					}
				} else {
					toaster.pop('error', 'SignUp error.', ' No data returned.');
				}
		    }, function(response){
				if (response.data && response.data.error) {
					toaster.pop('error', 'SignUp error.', ' HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
				} else if (response.status) {
					toaster.pop('error', 'SignUp error.', ' HTTP status: ' + response.status + '. StatusText: ' + response.statusText);
				} else {
					toaster.pop('error', 'SignUp error.', ' Response: ' + JSON.stringify(response));
				}
		    });
	    }; 
	    
	    function printInvalidFields(fieldErrors) {
			for (var i = 0; i < fieldErrors.length; i++) {
				var field = fieldErrors[i];
				toaster.pop('warning', 'Invalid ' + field.field, field.message);
			}
		}
	    
	    $scope.activateAccount = function (event) {
	        var formElement = angular.element(event.target);
	        
	        //1. check HTML form validation
	        formElement.addClass('was-validated');
	        if (formElement[0].checkValidity() === false) {
	            event.preventDefault();
	            event.stopPropagation();
	            return;
	        }
	        
	        //2. activation
	        var url = AppConstants.BASE_URL + '/api/open/activatePreSignupUser/' + $scope.data.activation.email + '/' + $scope.data.activation.key + '/';
	        httpService.post(url, null, false, function(response){
				var data = response.data;
				if (data) {
					var status = data.status;
					if (status) {
						if (status.code == AppConstants.SUCCESS) {
							$scope.step = 'newlist';
							
						} else if (status.code == AppConstants.PRESIGNUP_USER_STATUS_ACTIVATED) {
							$scope.step = 'newlist';
							toaster.pop('success', 'Activated', 'User with email: ' + $scope.data.activation.email + ' was already activated.');
						
						} else if (status.code == AppConstants.PRESIGNUP_USER_INVALID_KEY) {
							toaster.pop('error', 'Try again', 'Invalid activation key.');
							
						} else {
							toaster.pop('error', 'Activation error.', 'Status code: ' + status.code + ', message: ' + status.message);
						}
					} else {
						toaster.pop('error', 'Activation error.', ' No data status returned. Data: ' + JSON.stringify(data));
					}
				} else {
					toaster.pop('error', 'Activation error.', ' No data returned.');
				}
		    }, function(response){
				if (response.data && response.data.error) {
					toaster.pop('error', 'Activation error.', ' HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
				} else if (response.status) {
					toaster.pop('error', 'Activation error.', ' HTTP status: ' + response.status + '. StatusText: ' + response.statusText);
				} else {
					toaster.pop('error', 'Activation error.', ' Response: ' + JSON.stringify(response));
				}
		    });
	    }; 
		
	};
	controller.$inject = [ '$scope', '$location', '$rootScope','SharingService', '$timeout', 'AppConstants', 'CommonService', 'httpService', '$httpParamSerializer', '$http', '$state', '$stateParams', 'toaster'];
	angular.module('todoapp.controllers').controller('SignUpController', controller);
}(angular));