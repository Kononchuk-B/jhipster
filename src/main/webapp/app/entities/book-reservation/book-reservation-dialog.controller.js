(function() {
    'use strict';

    angular
        .module('libSupportApp')
        .controller('BookReservationDialogController', BookReservationDialogController);

    BookReservationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BookReservation', 'Book'];

    function BookReservationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BookReservation, Book) {
        var vm = this;

        vm.bookReservation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.books = Book.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bookReservation.id !== null) {
                BookReservation.update(vm.bookReservation, onSaveSuccess, onSaveError);
            } else {
                BookReservation.save(vm.bookReservation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('libSupportApp:bookReservationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
