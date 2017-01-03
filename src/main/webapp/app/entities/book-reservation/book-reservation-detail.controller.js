(function() {
    'use strict';

    angular
        .module('libSupportApp')
        .controller('BookReservationDetailController', BookReservationDetailController);

    BookReservationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BookReservation', 'Book', 'User'];

    function BookReservationDetailController($scope, $rootScope, $stateParams, previousState, entity, BookReservation, Book, User) {
        var vm = this;

        vm.bookReservation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('libSupportApp:bookReservationUpdate', function(event, result) {
            vm.bookReservation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
