(function(angular) {
	angular.module("todoapp")
	
	/**
	 * New passwords match validation
	 */
	.directive('pwdsMatchCheck', [function () {
	    return {
	      require: 'ngModel',
	      link: function (scope, elem, attrs, ctrl) {
	        var firstPassword = '#' + attrs.pwdsMatchCheck;
	        elem.add(firstPassword).on('keyup', function () {
	          scope.$apply(function () {
	            var v = elem.val()===$(firstPassword).val();
	            ctrl.$setValidity('pwmatch', v);
	            elem[0].setCustomValidity((v) ? '' : 'Passwords do not match.');
	          });
	        });
	      }
	    }
	}])

	.directive('validFile', function() {
		return {
			require : 'ngModel',
			link : function(scope, el, attrs, ngModel) {
				// change event is fired when file is selected
				el.bind('change', function() {
					scope.$apply(function() {
						ngModel.$setViewValue(el.val());
						ngModel.$render();
					});
				});
			}
		}
	})

	.directive('fileModel', [ '$parse', function($parse) {
		return {
			restrict : "A",
			link : function(scope, element, attrs) {
				var model = $parse(attrs.fileModel);
				var modelSetter = model.assign;

				element.bind('change', function() {
					scope.$apply(function() {
						modelSetter(scope, element[0].files[0]);
					})
				})
			}
		}
	} ])

	.filter('range', function() {
		return function(input, total) {
			total = parseInt(total);

			for (var i = 1; i < total; i++) {
				input.push(i);
			}
			return input;
		};
	})

	.filter('rangeMinMax', function() {
		return function(input, min, max) {
			min = parseInt(min);
			max = parseInt(max);

			for (var i = min; i < max; i++) {
				input.push(i);
			}
			return input;
		};
	})

	.filter('cmdate', [ '$filter', function($filter) {
		return function(inputDate, format) {
			var day = parseInt(inputDate.substr(2, 2));
			var month = parseInt(inputDate.substr(0, 2)) - 1;
			var year = parseInt(inputDate.substr(4, 4));
			var date = new Date(year, month, day);

			var result = $filter('date')(date, format);
			return result;
		};
	} ])

	.filter('rangeMinMaxReversed', function() {
		return function(input, min, max) {
			min = parseInt(min);
			max = parseInt(max);

			for (var i = max; i >= min; i--) {
				input.push(i);
			}
			return input;
		};
	});

}(angular));