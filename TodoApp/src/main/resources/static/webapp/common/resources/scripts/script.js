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

/*
 * Make front-end to sent "Accept All" header to 
 * server to avoid getting HTTP 406 error  
 */
var o = XMLHttpRequest.prototype.open;
XMLHttpRequest.prototype.open = function(){
  var res = o.apply(this, arguments);
  this.setRequestHeader("Accept", "*/*");
  return res;
}