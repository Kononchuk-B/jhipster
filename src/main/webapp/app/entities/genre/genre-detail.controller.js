(function() {
    'use strict';

    angular
        .module('libSupportApp')
        .controller('GenreDetailController', GenreDetailController);

    GenreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Genre', 'Book'];

    function GenreDetailController($scope, $rootScope, $stateParams, previousState, entity, Genre, Book) {
        var vm = this;

        vm.genre = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('libSupportApp:genreUpdate', function(event, result) {
            vm.genre = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
