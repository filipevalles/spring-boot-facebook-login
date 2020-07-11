// Angular
var app = angular.module('FacebookApp', []);

app.config([ '$httpProvider', function($httpProvider) {
	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
} ]);

// Controller
app.controller('AppCtrl', function($http, $scope) {

	// Detalhes do usuário
	var getUser = function() {
		$http.get('/user').success(function(user) {
			$scope.user = user;
			console.log('Logged User : ', user);
		}).error(function(error) {
			$scope.resource = error;
		});
	};
	getUser();

	// Logout
	$scope.logout = function() {
		$http.post('/logout').success(function(res) {
			$scope.user = null;
		}).error(function(error) {
			console.log("Logout error : ", error);
		});
	};
});
