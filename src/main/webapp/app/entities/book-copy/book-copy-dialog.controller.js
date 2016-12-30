(function() {
    'use strict';

    angular
        .module('libSupportApp')
        .controller('BookCopyDialogController', BookCopyDialogController);

    BookCopyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BookCopy', 'Book'];

    function BookCopyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BookCopy, Book) {
        var vm = this;

        vm.bookCopy = entity;
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
            if (vm.bookCopy.id !== null) {
                BookCopy.update(vm.bookCopy, onSaveSuccess, onSaveError);
            } else {
                BookCopy.save(vm.bookCopy, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('libSupportApp:bookCopyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateOfSupply = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
