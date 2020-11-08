(function(angular) {
	var controller = function($scope, $rootScope, AppConstants, httpService, $stateParams, CommonService, toaster) {
		CommonService.getProfile();

		// SharingService.set('reloadedHome', false);
		var GET_LIST_ITEM = '/api/user/todo/list/';
		var CREATE_TODO_ITEM = '/api/user/todo/item/create';
		var UPDATE_TODO_ITEM = '/api/user/todo/item/update';
		var DELETE_TODO_ITEM = '/api/user/todo/item/delete/';
		var CHANGE_HIDE_COMPLETED = '/api/user/todo/list/hide-completed/';
		$scope.totalIncompleteItems = 0;
		
		var listId = $stateParams.id;
		if (!listId) {
			$location.path('/');
			return;
		}
		
		function checkIncompleteItemsAreLeft() {
			var totalIncompleteItems = 0;
			$scope.list.todoItems.forEach(function(item) {
				totalIncompleteItems += (item.completed) ? 0 : 1;
			})
			$scope.totalIncompleteItems = totalIncompleteItems;
		}

		httpService.get(GET_LIST_ITEM + listId, null, false, function(response) {
			if (response.status == 200) {
				var data = response.data;
				if (data.status.code == AppConstants.SUCCESS) {
					$scope.list = data.entity;
					checkIncompleteItemsAreLeft();
					
				} else {
					toaster.pop('error', 'Error fetching data. Status message: ' + data.status.message);
				}
			} else {
				toaster.pop('error', 'Error fetching data. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
			}
		}, function(response) {
			toaster.pop('error', 'Error fetching data. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
		});

		$scope.clickTodoItem = function(item) {
			item.clicked = true;
			$scope.itemBeforeQuickEdit = JSON.parse(JSON.stringify(item)); // deep-copy
		};

		$scope.blurTodoItem = function(item) {
			item.clicked = false;
			if ($scope.itemBeforeQuickEdit.title != item.title) {
				if (item.title.length < 3) {
					toaster.pop('warning', 'Too small', 'Please use min 3 characters for title');
					item.title = $scope.itemBeforeQuickEdit.title;
				} else {
					doModifyTodoItem(item);
				}
			}
		};

		$scope.checkTodoItem = function(item) {
			doModifyTodoItem(item);
		};

		$scope.markComplete = function(item, isCompleted) {
			item.completed = isCompleted;
			doModifyTodoItem(item);
		};

		$scope.showAddNewItemModal = function() {
			$scope.newItem = {
				listId : listId
			};
			var addNewItemForm = document.getElementById('addNewItemForm');
			addNewItemForm.classList.remove('was-validated');
		}

		$scope.addTodoItem = function(event) {
			var form = document.getElementById('addNewItemForm');
			form.classList.add('was-validated');
			if ($scope.newItem.title) {
				$scope.newItem.title = $scope.newItem.title.trim();
			}
			if (form.checkValidity() === false) {
				event.preventDefault();
				event.stopPropagation();
				return;
			}
			$('#addTaskModal').modal('hide');
			doAddTodoItem($scope.newItem);
		};

		function doAddTodoItem(newItem) {
			httpService.post(CREATE_TODO_ITEM, newItem, false, function(response) {
				if (response.status == 200) {
					var data = response.data;
					if (data.status.code == AppConstants.SUCCESS) {
						$scope.list.todoItems.unshift(data.entity);
						swal("Nice!", "New task is added!", "success");
						resetFilter()
						checkIncompleteItemsAreLeft();

					} else {
						toaster.pop('error', 'Error adding task. Status message: ' + data.status.message);
					}
				} else {
					toaster.pop('error', 'Error adding task. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
				}
			}, function(response) {
				toaster.pop('error', 'Error adding task. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
			});
		}

		$scope.changeHideCompletedFlag = function(list) {
			var url = CHANGE_HIDE_COMPLETED + list.id + '/' + list.hideCompleted + '/'
			httpService.post(url, null, false, function(response) {
				if (response.status == 200) {
					var data = response.data;
					if (data.status.code == AppConstants.SUCCESS) {
						var message = "Completed task will be " + ((list.hideCompleted) ? "hidden" : "shown");
						toaster.pop("success", "", message);
						resetFilter()
						/*
						 * show empty list message if no more item to show
						 */
						if (list.hideCompleted) {
							checkIncompleteItemsAreLeft();
						}

					} else {
						toaster.pop('error', 'Error updating settings. Status message: ' + data.status.message);
					}
				} else {
					toaster.pop('error', 'Error updating settings. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
				}
			}, function(response) {
				toaster.pop('error', 'Error updating settings. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
			});
		}

		$scope.showModifyItemModal = function(item) {
			$scope.modifyItem = JSON.parse(JSON.stringify(item)); // deep-copy
			$scope.origModifyItem = item;
			var modifyItemForm = document.getElementById('modifyItemForm');
			modifyItemForm.classList.remove('was-validated');
		}

		$scope.modifyTodoItem = function(event) {
			var form = document.getElementById('modifyItemForm');
			form.classList.add('was-validated');
			if ($scope.modifyItem.title) {
				$scope.modifyItem.title = $scope.modifyItem.title.trim();
			}
			if (form.checkValidity() === false) {
				event.preventDefault();
				event.stopPropagation();
				return;
			}
			if ($scope.modifyItem.title == $scope.origModifyItem.title && $scope.modifyItem.notes == $scope.origModifyItem.notes) {
				toaster.pop('warning', 'Change something', 'No changes detected');
				return;
			}

			$('#modifyTaskModal').modal('hide');
			doModifyTodoItem($scope.modifyItem);
		};

		function doModifyTodoItem(modifyItem) {
			httpService.post(UPDATE_TODO_ITEM, modifyItem, false, function(response) {
				if (response.status == 200) {
					var data = response.data;
					if (data.status.code == AppConstants.SUCCESS) {
						swal("Updated!", "Changes are saved!", "success");
						resetFilter()
						$scope.list.todoItems.forEach(function(item) {
							if (item.id == modifyItem.id) {
								item.title = modifyItem.title;
								item.notes = modifyItem.notes;
								return;
							}
						});
						checkIncompleteItemsAreLeft();

					} else {
						toaster.pop('error', 'Error updating task. Status message: ' + data.status.message);
					}
				} else {
					toaster.pop('error', 'Error updating task. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
				}
			}, function(response) {
				toaster.pop('error', 'Error updating task. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
			});
		}
		
		$scope.itemDelete = function(id) {
			swal({   
					title: "Are you sure?",   
					text: "You will not be able to recover this task!",   
					type: "warning",   
					showCancelButton: true,   
					confirmButtonColor: "#DD6B55",   
					confirmButtonText: "Yes, delete it!",   
					cancelButtonText: "No, cancel plx!",   
					closeOnConfirm: false,   
					closeOnCancel: false
				}, function(isConfirm) {   
					if (isConfirm) {
						doDeleteItem(listId, id);
					} else {     
						swal("Cancelled", "Your task is safe :)", "info");   
					}
				});
		};
		
		function doDeleteItem(listId, id) {
			var url = DELETE_TODO_ITEM + listId + '/' + id;
			httpService.del(url , null, false, function(response) {
				if (response.status == 200) {
					var data = response.data;
					if (data.status.code == AppConstants.SUCCESS) {
						swal("Deleted!", "", "success");
						var index = $scope.list.todoItems.map(x => { 
							return x.id;
						}).indexOf(id);
						$scope.list.todoItems.splice(index, 1);
					} else {
						toaster.pop('error', 'Error deleting task. Status message: ' + data.status.message);
					}
				} else {
					toaster.pop('error', 'Error deleting task. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
				}
			}, function(response) {
				toaster.pop('error', 'Error deleting task. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
			});
		}
		
		$scope.filterItems = function() {
			var countVisible = 0;
			for (var i = 0; i < $scope.list.todoItems.length; i++) {
				var todoObj = $scope.list.todoItems[i];
				todoObj.filteredout = !todoObj.title.includes($scope.filterText || '');
				countVisible += (todoObj.filteredout) ? 0 : 1;
			};
			$scope.noFilterResults = countVisible == 0;
		};
		
		function resetFilter() {
			for (var i = 0; i < $scope.list.todoItems.length; i++) {
				$scope.list.todoItems[i].filteredout = false;
			};
			$scope.filterText = null;
		}

	};
	controller.$inject = [ '$scope', '$rootScope', 'AppConstants', 'httpService', '$stateParams', 'CommonService', 'toaster' ];
	angular.module('todoapp.controllers').controller('ListController', controller);
}(angular));