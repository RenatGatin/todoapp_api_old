/**=========================================================
 * Module: Service .js Class for handling all http reqeusts.
 * =========================================================
*/

(function(angular) {
    angular
        .module('todoapp')
        .service('httpService', ['$http', '$httpParamSerializer', 'AppConstants', '$cookies', function($http, $httpParamSerializer, AppConstants, $cookies) {		
			
        	/**
        	 * Sets tokens to cookies and headers
        	 */
        	this.setTokens = function(loginData) {
        		$http.defaults.headers.common.Authorization = 'Bearer ' + loginData.access_token;
    			var cookieExpiresIn = new Date();
    			cookieExpiresIn.setSeconds(cookieExpiresIn.getSeconds() + loginData.expires_in);
			    $cookies.put('access_token', loginData.access_token, {expires : cookieExpiresIn });
			    $cookies.put('refresh_token', loginData.refresh_token);

			    console.log('Sucessfully authenticated. Session expires in ' + loginData.expires_in + ' sec.');
        	}
        	
        	/**
        	 * credentials (JSON object) : contains username and password
        	 * errorCallback (callback function) : callback function to handle rest-api call failed status.
        	 */
        	this.login = function(credentials, successCallback, errorCallback) {
        		credentials.grant_type = AppConstants.OAUTH2_GRANT_TYPE_PASSWORD;
        		
        		var request =  $http({
					method : 'POST',
					url    :  AppConstants.BASE_URL + AppConstants.URL_OAUTH_TOKEN,
					headers: {
		                "Authorization": AppConstants.OAUTH2_AUTHORIZATION,
		                "Content-type": "application/x-www-form-urlencoded"
		            },
		            data: $httpParamSerializer(credentials)
				});
        		
        		return( request.then(successCallback, errorCallback) );
        	}
        	
        	/**
        	 * Method refreshes access_token using refresh_token
        	 */
        	this.refresh = function(errorCallback) {
        		var token = $cookies.get('refresh_token');
        		if (token) {
	        		var body = {
	        				grant_type    : AppConstants.OAUTH2_GRANT_TYPE_REFRESH,
	        				refresh_token : token
	        		}
	        		
	        		var request =  $http({
						method : 'POST',
						url    :  AppConstants.BASE_URL + AppConstants.URL_OAUTH_TOKEN,
						headers: {
			                "Authorization": AppConstants.OAUTH2_AUTHORIZATION,
			                "Content-type": "application/x-www-form-urlencoded"
			            },
			            data: $httpParamSerializer(body)
					});
	        		
	        		return( request.then(function(data){
	        			$http.defaults.headers.common.Authorization = 'Bearer ' + data.data.access_token;
	        			var cookieExpiresIn = new Date();
	        			cookieExpiresIn.setSeconds(cookieExpiresIn.getSeconds() + data.data.expires_in);
					    $cookies.put('access_token', data.data.access_token, {expires : cookieExpiresIn });
					    $cookies.put('refresh_token', data.data.refresh_token);
	
					    console.log('Sucessfully refreshed authentication. Session expires in ' + data.data.expires_in + ' sec.');
				    }, errorCallback) );
        		} else {
        			console.log('refresh_token is null');
        		}
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
