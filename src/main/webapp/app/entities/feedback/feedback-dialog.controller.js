(function () {
    'use strict';

    angular
        .module('libSupportApp')
        .controller('FeedbackDialogController', FeedbackDialogController);

    FeedbackDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Feedback', 'Book', 'User', 'Principal'];

    function FeedbackDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Feedback, Book, User, Principal) {
        var vm = this;

        vm.feedback = entity;
        vm.clear = clear;
        vm.save = save;
        vm.books = Book.query();
        vm.users = User.query();
        vm.today = new Date();
        vm.currentUser = null;
        vm.currentBook = null;
        vm.instantiateMainData = instantiateMainData;

        function foo() {
            console.log($stateParams.foo);
        }

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
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
            vm.feedback.book = vm.currentBook;
            vm.feedback.user = vm.currentUser;
        }

        function save() {
            vm.isSaving = true;
            if (vm.feedback.id !== null) {
                Feedback.update(vm.feedback, onSaveSuccess, onSaveError);
            } else {
                vm.instantiateMainData();
                Feedback.save(vm.feedback, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('libSupportApp:feedbackUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }
})();
