(function() {
    'use strict';

    angular
        .module('libSupportApp')
        .controller('BookReservationDeleteController',BookReservationDeleteController);

    BookReservationDeleteController.$inject = ['$uibModalInstance', 'entity', 'BookReservation'];

    function BookReservationDeleteController($uibModalInstance, entity, BookReservation) {
        var vm = this;

        vm.bookReservation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BookReservation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
