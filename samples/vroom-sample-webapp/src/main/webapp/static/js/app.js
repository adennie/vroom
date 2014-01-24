/* App Module */
angular.module('VroomSampleWebapp', ['VroomSampleService']).
config(['$routeProvider', function($routeProvider) {
	$routeProvider.
	when('/index', {templateUrl: 'partials/list-place.html',   controller: PlaceListCtrl}).
	when('/place/create', {templateUrl: 'partials/create-place.html', controller: PlaceCreateCtrl}).
	when('/place/edit', {templateUrl: 'partials/create-place.html', controller: PlaceCreateCtrl}).
	otherwise({redirectTo: '/index'});
}]);