(function() {
    'use strict';

    angular
        .module('libSupportApp')
        .directive('hasOnlyAuthority', hasOnlyAuthority);

    hasOnlyAuthority.$inject = ['Principal'];

    function hasOnlyAuthority(Principal) {
        var directive = {
            restrict: 'A',
            link: linkFunc
        };

        return directive;

        function linkFunc(scope, element, attrs) {
            var authority = attrs.hasOnlyAuthority.replace(/\s+/g, '');

            var setVisible = function () {
                    element.removeClass('hidden');
                },
                setHidden = function () {
                    element.addClass('hidden');
                },
                defineVisibility = function (reset) {

                    if (reset) {
                        setVisible();
                    }

                    Principal.hasOnlyAuthority(authority)
                        .then(function (result) {
                            if (result) {
                                setVisible();
                            } else {
                                setHidden();
                            }
                        });
                };

            if (authority.length > 0) {
                defineVisibility(true);

                scope.$watch(function() {
                    return Principal.isAuthenticated();
                }, function() {
                    defineVisibility(true);
                });
            }
        }
    }
})();
