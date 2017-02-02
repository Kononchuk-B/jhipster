(function () {
    'use strict';
    angular
        .module('libSupportApp')
        .factory('UserBook', UserBook);

    UserBook.$inject = ['$resource', 'DateUtils', 'Principal', 'User'];

    function UserBook($resource, DateUtils, Principal, User) {

        var resourceUrlExtended = 'api/my-books/:login';

        return $resource(resourceUrlExtended, {}, {
            'query': {
                method: 'GET',
                isArray: true
            }
        });
    }
})();
