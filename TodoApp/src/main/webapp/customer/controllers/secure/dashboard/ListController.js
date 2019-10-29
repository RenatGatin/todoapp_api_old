(function(angular) {
	var controller = function($scope, $rootScope, AppConstants, httpService, $stateParams, CommonService, toaster) {
		CommonService.getProfile();

		// SharingService.set('reloadedHome', false);
		var GET_LIST_ITEM = '/api/user/todo/list/';
		var CREATE_TODO_ITEM = '/api/user/todo/item/create';
		var UPDATE_TODO_ITEM = '/api/user/todo/item/update';
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
						$scope.list.todoItems.push(data.entity);
						swal("Nice!", "New task is added!", "success");
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

		$scope.listRename = function(listItem) {
			swal({
				title : "Rename List",
				text : "Write new name:",
				type : "input",
				showCancelButton : true,
				closeOnConfirm : false,
				animation : "slide-from-top",
				inputPlaceholder : "new list name"
			}, function(inputValue) {
				if (inputValue === false)
					return false;

				if (inputValue === "") {
					swal.showInputError("You need to write something!");
					return false
				}

				doListRename(listItem, inputValue)
			});
		};

		function doListRename(listItem, newName) {
			var url = POST_LIST_RENAME + listItem.id + '/';
			httpService.post(url, {
				stringVar : newName
			}, false, function(response) {
				if (response.status == 200) {
					var data = response.data;
					if (data.status.code == AppConstants.SUCCESS) {
						listItem.name = newName;
						swal("Nice!", "List's new name is: " + newName, "success", function() {
							location.reload();
						});

					} else {
						toaster.pop('error', 'Error renaming list. Status message: ' + data.status.message);
					}
				} else {
					toaster.pop('error', 'Error renaming list. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
				}
			}, function(response) {
				toaster.pop('error', 'Error renaming list. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
			});
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

		$scope.createItem = function() {
			swal({
				title : "Create a new task!",
				text : "Give a title:",
				type : "input",
				showCancelButton : true,
				closeOnConfirm : false,
				animation : "slide-from-top",
				inputPlaceholder : "type here"
			}, function(inputValue) {
				if (inputValue === false)
					return false;

				if (!inputValue || inputValue.trim() == "") {
					swal.showInputError("You need to write something!");
					return false;
				}

				doCreateList({
					name : inputValue.trim()
				})
			});
		};

	};
	controller.$inject = [ '$scope', '$rootScope', 'AppConstants', 'httpService', '$stateParams', 'CommonService', 'toaster' ];
	angular.module('todoapp.controllers').controller('ListController', controller);
}(angular));