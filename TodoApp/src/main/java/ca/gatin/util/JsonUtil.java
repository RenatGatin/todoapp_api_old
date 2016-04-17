package ca.gatin.util;

import ca.gatin.api.response.ResponseStatus;

/**
 * Commonly used JSON Util class
 * 
 * @author RGatin
 * @since 17-Apr-2016
 */
public class JsonUtil {
	
	/**
	 * Return JSON String that contains code and message
	 * 
	 * @return
	 */
    public static String getJsonStringStatus(ResponseStatus responseStatus) {
        return "{\"status\":{\"code\":" + responseStatus.getCode() + ",\"message\":\"" + responseStatus.getMessage() + "\"}}";
    }
	
}
