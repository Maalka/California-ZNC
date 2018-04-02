define(['angular', 'common'], function(angular) {
	'use strict';
	var mod = angular.module('benchmark.services', ['benchmark.common']);
	mod.service('benchmarkServices', ['playRoutes', function(playRoutes) { 
		var services = {
			'normalize': function(model) {
				return playRoutes.controllers.ParseController.normalize().post(model).then(function (response)  {
					/// handle errors (500 etc)
					return response.data;
				});
			},
			'getZNCMetrics': function(model) {
				return playRoutes.controllers.BaselineController.getZNCMetrics().post(model).then(function (response)  {
					/// handle errors (500 etc)
					return response.data;
				});
			}
		};
		return services;
	}]);
	return mod;
});