
﻿
(function($){var registrations=new Array();function pair(selector,method){this.selector=selector;this.method=method;}
$.liveReady=function(selectorOrMethod,method){if(method){registrations.push(new pair(selectorOrMethod,method));$(function(){$(selectorOrMethod).each(function(){$.proxy(method,$(this))();});});}else{registrations.push(new pair(null,selectorOrMethod));$($.proxy(selectorOrMethod,$(document)));}}
$.fn.liveReady=function(){return this.each(function(){var $element=$(this);$.each(registrations,function(){if(this.selector){var $matching=$element.filter(this.selector).add($element.find(this.selector));var method=this.method;$matching.each(function(){$.proxy(method,$(this))($(this));});}else{$.proxy(this.method,$element)($element);}});});};})(jQuery);