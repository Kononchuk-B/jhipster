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
        vm.currentUser = load;

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function load(login) {
            User.get({login: login}, function (result) {
                vm.user = result;
            });
        }

        Principal.identity().then(function (account) {
            User.get({login: account.login}, function (result) {
                vm.currentUser = result;
            });
        });

        function save() {
            console.log(vm.feedback.user);
            console.log(vm.currentUser);
            vm.feedback.user = vm.currentUser;
            vm.isSaving = true;
            if (vm.feedback.id !== null) {
                Feedback.update(vm.feedback, onSaveSuccess, onSaveError);
            } else {
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
