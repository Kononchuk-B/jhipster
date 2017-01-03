(function() {
    'use strict';

    angular
        .module('libSupportApp')
        .controller('BookCopyDetailController', BookCopyDetailController);

    BookCopyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BookCopy', 'Book'];

    function BookCopyDetailController($scope, $rootScope, $stateParams, previousState, entity, BookCopy, Book) {
        var vm = this;

        vm.bookCopy = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('libSupportApp:bookCopyUpdate', function(event, result) {
            vm.bookCopy = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
