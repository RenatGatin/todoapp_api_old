(function(angular) {
	var controller = function($scope, $rootScope, AppConstants, SharingService, httpService, $httpParamSerializer, $http, CommonService, toaster) {
		CommonService.getProfile();
		
		//SharingService.set('reloadedHome', false);
		var GET_LIST_ALL = '/api/user/todo/list/all';
		var POST_LIST_RENAME = '/api/user/todo/list/rename/';
		var DELETE_LIST_RENAME = '/api/user/todo/list/delete/';
		var POST_LIST_CREATE = '/api/user/todo/list/create';
		
		
		$scope.testAngular = "CUSTOMER's DashboardController Page loaded!";
		var todoListRaw = [];
			
		$scope.securedApi = function() {
			var url = '/api/user/ping';
			httpService.get(url, null, false, function(response){
				$scope.securedApiResponse = JSON.stringify(response);
		    }, function(response){
				$scope.securedApiResponse = JSON.stringify(response);
		    });
		};
		
		$scope.listDelete = function(listItem, index) {
			swal({   
					title: "Are you sure?",   
					text: "You will not be able to recover this list with todo items!",   
					type: "warning",   
					showCancelButton: true,   
					confirmButtonColor: "#DD6B55",   
					confirmButtonText: "Yes, delete it!",   
					cancelButtonText: "No, cancel plx!",   
					closeOnConfirm: false,   
					closeOnCancel: false
				}, function(isConfirm) {   
					if (isConfirm) {
						doListDelete(listItem, index);
					} else {     
						swal("Cancelled", "Your list is safe :)", "info");   
					}
				});
		};
		
		function doListDelete(listItem, index) {
			var url = DELETE_LIST_RENAME + listItem.id + '/';
			httpService.del(url, null, false, function(response){
				if (response.status == 200) {
					var data = response.data;
					if (data.status.code == AppConstants.SUCCESS) {
						$scope.todoListObjects.splice(index, 1);
						swal("Deleted!", "Your list has been deleted.", "success");
						
					} else {
						toaster.pop('error', 'Error deleting list. Status message: ' + data.status.message);
					}
				} else {
					toaster.pop('error', 'Error deleting list. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
				}
		    }, function(response){
				toaster.pop('error', 'Error deleting list. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
		    });
		};
		
		$scope.listRename = function(listItem) {
			swal({
				  title: "Rename List",
				  text: "Write new name:",
				  type: "input",
				  showCancelButton: true,
				  closeOnConfirm: false,
				  animation: "slide-from-top",
				  inputPlaceholder: "new list name"
				},
				function(inputValue){
				  if (inputValue === false) return false;
				  
				  if (inputValue === "") {
				    swal.showInputError("You need to write something!");
				    return false
				  }
				  
				  doListRename(listItem, inputValue) 
				});
		};
		
		function doListRename(listItem, newName) {
			var url = POST_LIST_RENAME + listItem.id + '/';
			httpService.post(url, { stringVar : newName }, false, function(response){
				if (response.status == 200) {
					var data = response.data;
					if (data.status.code == AppConstants.SUCCESS) {
						listItem.name = newName;
						swal("Nice!", "List's new name is: " + newName, "success"
						, function() {
							location.reload();
						});
						
					} else {
						toaster.pop('error', 'Error renaming list. Status message: ' + data.status.message);
					}
				} else {
					toaster.pop('error', 'Error renaming list. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
				}
		    }, function(response){
				toaster.pop('error', 'Error renaming list. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
		    });
		};
			
		httpService.get(GET_LIST_ALL, null, false, function(response){
			if (response.status == 200) {
				var data = response.data;
				if (data.status.code == AppConstants.SUCCESS) {
					todoListRaw = data.entity;
					$scope.todoListObjects = getTodoListWithExtraData(todoListRaw);
					
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
					todoListObj.total = (todoListObj.todoItems) ? todoListObj.todoItems.length : 0;
					
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
		
		$scope.createList = function() {
			swal({
				  title: "Create a new List!",
				  text: "Give a meaningful name:",
				  type: "input",
				  showCancelButton: true,
				  closeOnConfirm: false,
				  animation: "slide-from-top",
				  inputPlaceholder: "type here"
			},
			function(inputValue){
			  if (inputValue === false) return false;
			  
			  if (!inputValue || inputValue.trim() == "") {
			    swal.showInputError("You need to write something!");
			    return false;
			  }
			  
			  doCreateList({name: inputValue.trim()})
			});
		};
		
		function doCreateList(bean) {
			httpService.post(POST_LIST_CREATE, bean, false, function(response){
				if (response.status == 200) {
					var data = response.data;
					if (data.status.code == AppConstants.SUCCESS) {
						todoListRaw.unshift(data.entity);
						$scope.todoListObjects = getTodoListWithExtraData(todoListRaw);
						swal("Nice!", "New list with name: '" + bean.name + "' is created.", "success");
						
					} else {
						toaster.pop('error', 'Error creating new List. Status message: ' + data.status.message);
					}
				} else {
					toaster.pop('error', 'Error creating new List. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
				}
		    }, function(response){
				toaster.pop('error', 'Error creating new List. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
		    });
		};

	};
	controller.$inject = [ '$scope', '$rootScope', 'AppConstants', 'SharingService', 'httpService', '$httpParamSerializer', '$http', 'CommonService', 'toaster'];
	angular.module('todoapp.controllers').controller('DashboardController', controller);
}(angular));