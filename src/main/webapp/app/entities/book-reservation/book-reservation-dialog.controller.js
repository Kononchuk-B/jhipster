(function() {
    'use strict';

    angular
        .module('libSupportApp')
        .controller('BookReservationDialogController', BookReservationDialogController);

    BookReservationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BookReservation', 'Book', 'User', 'Principal'];

    function BookReservationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BookReservation, Book, User, Principal) {
        var vm = this;

        vm.bookReservation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.books = Book.query();
        vm.users = User.query();
        vm.currentUser = null;
        vm.currentBook = null;
        vm.instantiateMainData = instantiateMainData;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        Principal.identity().then(function (account) {
            User.get({login: account.login}, function (result) {
                vm.currentUser = result;
            });
        });

        Book.get({id: $stateParams.bookId}, function (result) {
            vm.currentBook = result;
        });

        function instantiateMainData () {
            vm.bookReservation.book = vm.currentBook;
            vm.bookReservation.user = vm.currentUser;
        }

        function save () {
            vm.isSaving = true;
            if (vm.bookReservation.id !== null) {
                BookReservation.update(vm.bookReservation, onSaveSuccess, onSaveError);
            } else {
                vm.instantiateMainData();
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
