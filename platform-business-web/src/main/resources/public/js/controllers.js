/**
 * INSPINIA - Responsive Admin Theme
 *
 */

/**
 * MainCtrl - controller
 */
function MainCtrl($scope, $state) {

    this.userName = 'Example user';
    this.helloText = 'Welcome in SeedProject';
    this.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';

    $scope.$on('$stateChangeStart', function(scope, next, current) {
        console.log("stateChangeStart");
        $state.go('login');
    });
}

function LoginCtrl($scope, $http, $state) {
    $scope.user = {};
    $scope.authError = null;
    $scope.login = function() {
        $scope.authError = null;
        // Try to login
        $http.post('/auth', {username: $scope.user.username, password: $scope.user.password})
            .then(function(response) {
                if ( !response.data.token ) {
                    $scope.authError = 'Email or Password not right';
                }else{
                    $state.go('index.main');
                }
            }, function(x) {
                $scope.authError = 'Server Error';
            });
    };
}

angular
    .module('inspinia')
    .controller('MainCtrl', ['$scope', '$state', MainCtrl])
    .controller('LoginCtrl', ['$scope', '$http', '$state', LoginCtrl])
;