/**=========================================================
 * Module: Service .js Class for handling all http reqeusts.
 =========================================================*/

(function(angular) {
    angular
        .module('todoapp')
        .service('httpService', ['$http','$state', function($http, $state) {		
					
        	/**
        	 * url (string) : the url of the rest-api call.
        	 * data (json) : json-payload of the data sent to the post function
        	 * isCache (boolean) : boolean value to specifiy whether to cache the resoure on the browser.
        	 * successCallback (callback function) : callback function to handle rest-api call success status.
        	 * errorCallback (callback function) : callback function to handle rest-api call failed status.
        	 */
			this.post=function(url, data, isCache, successCallback, errorCallback) {
				var request =  $http({
					method: 'POST',
					url: url,
					data: data,
					cache: isCache
				});

				return( request.then(successCallback, errorCallback) );
			}

        	/**
        	 * url (string) : the url of the rest-api call.
        	 * params (json) : json-payload of the params sent to the post function
        	 * isCache (boolean) : boolean value to specifiy whether to cache the resoure on the browser.
        	 * successCallback (callback function) : callback function to handle rest-api call success status.
        	 * errorCallback (callback function) : callback function to handle rest-api call failed status.
        	 */
			this.get = function(url, params, isCache, successCallback, errorCallback) {
				var request = $http({
					url : url,
					method: 'GET',
					params: params,
					cache: isCache
				});
				return ( request.then(successCallback, errorCallback) );
			}

        	/**
        	 * url (string) : the url of the rest-api call.
        	 * params (json) : json-payload of the params sent to the post function
        	 * isCache (boolean) : boolean value to specifiy whether to cache the resoure on the browser.
        	 * successCallback (callback function) : callback function to handle rest-api call success status.
        	 * errorCallback (callback function) : callback function to handle rest-api call failed status.
        	 */
			this.put = function(url, params, isCache, successCallback, errorCallback) {
				$http({
					url : url,
					method: 'PUT',
					params: params,
					cache: isCache
				}).success(successCallback).
				   error(errorCallback);
			}

        	/**
        	 * url (string) : the url of the rest-api call.
        	 * params (json) : json-payload of the params sent to the post function
        	 * isCache (boolean) : boolean value to specifiy whether to cache the resoure on the browser.
        	 * successCallback (callback function) : callback function to handle rest-api call success status.
        	 * errorCallback (callback function) : callback function to handle rest-api call failed status.
        	 */
			this.del = function(url, params, isCache, successCallback, errorCallback) {
				var request = $http({
					url : url,
					method: 'DELETE',
					params: params,
					cache: isCache
				});

				return ( request.then(successCallback, errorCallback) );
			}

			function handleError(response) {
				console.log(response);
				if ( ! angular.isObject( response.data ) || ! response.data.message) {
					$.smallBox({
						title : 'An unknown error occurred. <p>URL: <a href='+response.config.url+' target=_blank style="color:white;margin-left:10px;">' + response.config.url + '</a><p>',
						content : '',
						color : '#A65858',
						iconSmall : 'fa fa-times',
						timeout : 5000
					});
					return( $q.reject( 'An unknown error occurred.' ) );
				}

				return( $q.reject( response.data.message ) );
			}
			function handleSuccess(response) {
				return( response.data );
			}
        }])
}(angular));
