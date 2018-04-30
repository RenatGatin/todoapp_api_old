/**
 * General script for global JS functions
 * 
 * @author RGatin
 * @since 28-Sep-2017 
 * 
 */

/**
 * Add a contains method to String 
 */
if (typeof String.prototype.contains === 'undefined') { 
	String.prototype.contains = function(it) { return this.indexOf(it) != -1; }; 
}

