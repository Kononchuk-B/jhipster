(function() {
    'use strict';

    angular
        .module('libSupportApp')
        .controller('BookDetailController', BookDetailController);

    BookDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Book', 'BookReservation', 'BookCopy', 'Feedback'];

    function BookDetailController($scope, $rootScope, $stateParams, previousState, entity, Book, BookReservation, BookCopy, Feedback) {
        var vm = this;

        vm.book = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('libSupportApp:bookUpdate', function(event, result) {
            vm.book = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
