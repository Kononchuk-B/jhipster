(function() {
    'use strict';

    angular
        .module('libSupportApp')
        .controller('BookCopyDeleteController',BookCopyDeleteController);

    BookCopyDeleteController.$inject = ['$uibModalInstance', 'entity', 'BookCopy'];

    function BookCopyDeleteController($uibModalInstance, entity, BookCopy) {
        var vm = this;

        vm.bookCopy = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BookCopy.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
