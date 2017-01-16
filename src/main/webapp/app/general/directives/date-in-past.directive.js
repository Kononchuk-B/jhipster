(function () {
    'use strict';

    angular
        .module('libSupportApp')
        .directive('dateInPast', dateInPast);

    function dateInPast () {
        return {
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$asyncValidators.dateInPast = function(modelValue, viewValue) {

                    if (ctrl.$isEmpty(modelValue)) {
                        return true;
                    }

                    var def = $q.defer();
                    if(modelValue < new Date()) {
                        def.resolve();
                    } else {
                        def.reject();
                    }
                    return def.promise;
                };
            }
        };
    }

})();
