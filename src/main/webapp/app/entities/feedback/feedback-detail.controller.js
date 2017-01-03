(function() {
    'use strict';

    angular
        .module('libSupportApp')
        .controller('FeedbackDetailController', FeedbackDetailController);

    FeedbackDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Feedback', 'Book', 'User'];

    function FeedbackDetailController($scope, $rootScope, $stateParams, previousState, entity, Feedback, Book, User) {
        var vm = this;

        vm.feedback = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('libSupportApp:feedbackUpdate', function(event, result) {
            vm.feedback = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
