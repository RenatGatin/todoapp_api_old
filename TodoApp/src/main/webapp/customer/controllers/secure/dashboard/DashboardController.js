(function(angular) {
	var controller = function($scope, $rootScope, AppConstants, SharingService, httpService, $httpParamSerializer, $http, CommonService) {
		//SharingService.set('reloadedHome', false);
		var GET_LIST_ALL = '/api/user/todo/list/all';
		
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
		
			
		httpService.get(GET_LIST_ALL, null, false, function(response){
			if (response.status == 200) {
				var data = response.data;
				if (data.status.code == AppConstants.SUCCESS) {
					$scope.todoListObjects = getTodoListWithExtraData(data.entity);
					
				} else {
					toaster.pop('error', 'Error fetching data. Status message: ' + data.status.message);
				}
			} else {
				toaster.pop('error', 'Error fetching data. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
			}
	    }, function(response){
			toaster.pop('error', 'Error fetching data. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
	    });
		
		/**
		 * adds "completed" and "total" properties to each todoList object
		 */
		function getTodoListWithExtraData(todoListObjects) {
			if (todoListObjects) {
				for (var i = 0; i < todoListObjects.length; i++) {
					var todoListObj = todoListObjects[i];
					todoListObj.completed = 0;
					todoListObj.total = todoListObj.todoItems.length;
					
					for (var j = 0; j < todoListObj.total; j++) {
						var todoItem = todoListObj.todoItems[j];
						if (todoItem.completed) {
							todoListObj.completed++;
						}
					}
				}
			}
			return todoListObjects;
		}
		
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
		
		

		$scope.todoList2 = [
				{ 
					name: 'Abc',
					total: 10,
					completed: 6,
					connectionIds : [ 
						{
							id : 1,
							id : 2,
							id : 3
						}
					]
				}, 
				{ 
					name: 'Def',
					total: 8,
					completed: 1,
					connectionIds : []
				}, 
				{ 	name: 'Ghi',
					total: 11,
					completed: 7,
					connectionIds : [ 
						{
							id : 1,
							id : 2
						}
					]
				}
		];
		
		//$scope.errorMessage = 'Test tttttt';

	};
	controller.$inject = [ '$scope', '$rootScope', 'AppConstants', 'SharingService', 'httpService', '$httpParamSerializer', '$http', 'CommonService'];
	angular.module('todoapp.controllers').controller('DashboardController', controller);
}(angular));