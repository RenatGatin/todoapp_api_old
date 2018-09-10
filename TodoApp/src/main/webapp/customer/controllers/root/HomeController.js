(function(angular) {
	var HomeController = function($scope, $rootScope, SharingService, $timeout, AppConstants, CommonService, httpService, $httpParamSerializer, $http, $state, $stateParams, toaster) {
		$scope.errorMessage = $stateParams.message;
		
		CommonService.checkSession();
		
		$scope.signUp = function (event) {
	        var formElement = angular.element(event.target);
	        
	        formElement.addClass('was-validated');
	        if (formElement[0].checkValidity() === false) {
	            event.preventDefault();
	            event.stopPropagation();
	            return;
	        }
	        
	        $state.go('sign-up', { signup : $scope.signup });
	    }; 
	    
	};
	HomeController.$inject = [ '$scope', '$rootScope','SharingService', '$timeout', 'AppConstants', 'CommonService', 'httpService', '$httpParamSerializer', '$http', '$state', '$stateParams', 'toaster'];
	angular.module('todoapp.controllers').controller('HomeController', HomeController);
}(angular));