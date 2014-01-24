

var app = angular.module('VroomSampleService', ['ngResource']);

app.factory("WebServiceAPI", function($http) {
	return {
		search: function(url, params, callback, errorcallback) {
			$http({
				method: 'GET',
				url: url,
				params: params,
			}).success(callback).error(errorcallback);
		},
		query: function(url, callback, errorcallback) {
			$http({
				method: 'GET',
				url: url,
			}).success(callback).error(errorcallback);
		},
		delete: function(url, callback, errorcallback) {
			$http({
				method: 'DELETE',
				url: url,
			}).success(callback).error(errorcallback);
		},
		update: function(url, data, headers, callback, errorcallback) {
			$http({
				method: 'PUT',
				url: url,
				data: data,
				headers: headers,
			}).success(callback).error(errorcallback);
		},
		push: function(url, data, headers, callback, errorcallback) {
			$http({
				method: 'POST',
				url: url,
				data: data,
				headers: headers,
			}).success(callback).error(errorcallback);
		}
	}
});

app.factory('placeService', function () {
	var place = {};

	return {
		savePlace:function (data) {
			place = data;
			console.log(data);
		},
		getPlace:function () {

			if(angular.isUndefined(place) || place === null){
				return null;
			}
			return place;
		}
	};
});