(function(angular) {

	var SharingService = function() {
		function set(key, data) {
			sessionStorage.setItem(key, JSON.stringify(data));
		}

		function get(key) {
			return JSON.parse(sessionStorage.getItem(key));
		}

		var clearAllData = function() {
			var i = sessionStorage.length;
			while (i--) {
				var key = sessionStorage.key(i);
				sessionStorage.removeItem(key);
			}
		}

		return {
			set : set,
			get : get,
			clear : clearAllData
		};
	};

	/**
	 * Service used to make Http POST request with multipart/form-data
	 */
	var HttpMultipartFormService = function($http) {
		return this.post = function(uploadUrl, data) {
			var fd = new FormData();

			for ( var key in data)
				fd.append(key, data[key]);

			$http.post(uploadUrl, fd, {
				transformRequest : angular.identity,
				headers : {
					'Content-Type' : undefined
				}
			});
		};
	};
	HttpMultipartFormService.$inject = [ '$http' ];

	angular.module("todoapp").factory("SharingService", SharingService).factory("HttpMultipartFormService", HttpMultipartFormService);
}(angular));