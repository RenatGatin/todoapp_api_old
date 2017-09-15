/**=========================================================
 * Module: Service .js Class for handling all http reqeusts.
 =========================================================*/

(function(angular) {
    angular
        .module('todoapp')
        .service('httpService', ['$http', '$state', '$httpParamSerializer', 'AppConstants', '$cookies', function($http, $state, $httpParamSerializer, AppConstants, $cookies) {		
			
        	/*
        	// Ignore `invalid_grant` error - should be catched on `LoginController`.
		    if ('invalid_grant' === rejection.data.error) {
		    	return;
		    }

		    // Refresh token when a `invalid_token` error occurs.
		    if ('invalid_token' === rejection.data.error) {
		        return OAuth.getRefreshToken();
		    }

		    // Redirect to `/login` with the `error_reason`.
		    return console.error(rejection.data.error);
        	*/
        	
        	/**
        	 * credentials (JSON object) : contains username and password
        	 * errorCallback (callback function) : callback function to handle rest-api call failed status.
        	 */
        	this.login = function(credentials, errorCallback) {
        		credentials.grant_type = AppConstants.OAUTH2_GRANT_TYPE;
        		
        		var request =  $http({
					method : 'POST',
					url    :  AppConstants.BASE_URL + AppConstants.URL_OAUTH_TOKEN,
					headers: {
		                "Authorization": AppConstants.OAUTH2_AUTHORIZATION,
		                "Content-type": "application/x-www-form-urlencoded"
		            },
		            data: $httpParamSerializer(credentials)
				});
        		
        		return( request.then(function(data){
        			$http.defaults.headers.common.Authorization = 'Bearer ' + data.data.access_token;
        			var cookieExpiresIn = new Date();
        			cookieExpiresIn.setSeconds(cookieExpiresIn.getSeconds() + data.data.expires_in);
				    $cookies.put('access_token', data.data.access_token, {expires : cookieExpiresIn });
				    
				    console.log('sucessfully authenticated');
			    }, errorCallback) );
        	}
        	
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
