/**
 * Controller for Main Menu
 */
 function MainMenuCtrl($rootScope, $scope, $location) {
    $scope.getClass = function(path) {
        if ($location.path() == path) {
            return 'active';
        } else {
            return '';
        }
    }
}

/**
 * Controller for the Place List page
 */
 function PlaceListCtrl($rootScope, $scope, $http, $location, placeService, WebServiceAPI) {

    //Use to reset the form
    $scope.master= {};

    $scope.reset = function(){
        $scope.research = angular.copy($scope.master);
    };

    $scope.search = function(){
        // Search places from backend 
        var search_url = getUrl(CONFIG.webservice.className.place, null, null);
        WebServiceAPI.search(search_url, $scope.research, function(response) {
            $scope.places =  response.elements;
        }, function(errorData, errorStatus) {
            alert('Search Places failed. Error Status: ' + errorData);
        });
    };

    // Delete a Place
    $scope.delete = function(place){
        var resp = confirm('Delete the place ' + place.name + '?');
        if (resp == true){
            WebServiceAPI.delete(place.selfRef, function() {
                var index = $scope.places.indexOf(place);
                $scope.places.splice(index, 1); 
            }, function(errorData, errorStatus) {
                alert('Delete Place failed. Error Status: ' + errorStatus);
            });
        }
    };

    //Edit place
    $scope.edit = function(place){
        placeService.savePlace(place);
        $location.path('/place/edit');
    }

    // --------------
    // Default values
    // --------------

    // Order By option
    $scope.orderProp = 'name';
}

/**
 * Controller to Create/Edit a Place
 */
 function PlaceCreateCtrl($scope, $http, $route, $timeout, $location, placeService, WebServiceAPI) {

    // Default empty notification object
    $scope.notification = {};

    if($location.path() == '/place/edit'){

        //Edit View

        // Values use in the partial (HTML)
        $scope.buttonName = 'Update it!';
        $scope.isEditMode= true;

        //Get place object from cmsService
        $scope.place = placeService.getPlace();

        //Submit form function
        $scope.submit = function() {

            var headers = {'Content-Type': media-types.place, 'Accept': media-types.place}

            WebServiceAPI.update($scope.place.selfRef, $scope.place, headers, function() {
                $scope.notification.type = 'success';
                $scope.notification.msg = 'Place "' + $scope.place.name + '" updated.';                
                $scope.notification.show = true;
                $timeout(function(){
                    $scope.notification = {};
                }, CONFIG.notification.duration); 
            }, function(errorData, errorStatus) {
                $scope.notification.type = 'error';
                $scope.notification.msg = 'Place update failed : ' + errorStatus;
                $scope.notification.show = true;
                $timeout(function(){
                    $scope.notification = {};
                }, CONFIG.notification.duration);
            });
        }
    }else{

        //Create View

        // Values use in the partial (HTML)
        $scope.buttonName = 'Create it!';
        $scope.isEditMode= false;

        //Use to reset the form and notification
        $scope.master = {};

        // Reset the form
        $scope.reset = function(){
            $scope.place = angular.copy($scope.master);
        };

        //Submit form function to save a place
        $scope.submit = function() {

            // Default selfRef
            $scope.place.selfRef = null;

            var url = CONFIG.webservice.base_url + 'places/'
            var headers = {'Content-Type': media-types.place, 'Accept': media-types.place}

            WebServiceAPI.push(url, $scope.place, headers, function() {
                $scope.notification.type = 'success';
                $scope.notification.msg = 'Place "' + $scope.place.name + '" created';
                $scope.notification.show = true;
                $timeout(function(){
                    $scope.notification = {};                
                    $route.reload();
                }, CONFIG.notification.duration); 
            }, function(errorData, errorStatus) {
                $scope.notification.type = 'danger';
                $scope.notification.msg = 'Place creation failed. Error Status: ' + errorStatus;
                $scope.notification.show = true;
                $timeout(function(){                
                    $scope.notification = {};
                }, CONFIG.notification.duration);
            });       
        }
    }
}

