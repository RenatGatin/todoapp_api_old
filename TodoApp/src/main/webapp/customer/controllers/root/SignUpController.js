(function(angular) {
	var controller = function($scope, $rootScope, SharingService, $timeout, AppConstants, CommonService, httpService, $httpParamSerializer, $http, $state, $stateParams, toaster) {

		CommonService.checkSession('sign-up');
		$scope.step = 'create';
		
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
	        httpService.post(AppConstants.BASE_URL + '/api/open/preSignupUser', $scope.signup, false, function(response){
				if (response.status == 200) {
					var data = response.data;
					if (data) {
						var status = data.status;
						if (status) {
							if (status.code == AppConstants.SUCCESS) {
								toaster.pop('success', 'Account successfully created!');
								
							} else if (status.code == AppConstants.INVALID_DATA) {
								printInvalidFields(data.fieldErrors);
								
							} else if (status.code == AppConstants.ACCOUNT_UNIQUE_FIELD_DUPLICATION) {
								$scope.signup.email = null;
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
				} else {
					toaster.pop('error', 'SignUp error.', ' HTTP status: ' + response.status + '. Message: ' + resonse.data.error_description);
				}
		    }, function(response){
				if (response.data && response.data.error) {
					toaster.pop('error', 'SignUp error.', ' HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
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
		
	};
	controller.$inject = [ '$scope', '$rootScope','SharingService', '$timeout', 'AppConstants', 'CommonService', 'httpService', '$httpParamSerializer', '$http', '$state', '$stateParams', 'toaster'];
	angular.module('todoapp.controllers').controller('SignUpController', controller);
}(angular));