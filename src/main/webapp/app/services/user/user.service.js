(function () {
    'use strict';

    angular
        .module('libSupportApp')
        .factory('User', User);

    User.$inject = ['$resource'];

    function User ($resource) {
        return $resource('api/users/:login', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': {method: 'POST'},
            'update': {method: 'PUT'},
            'delete': {method: 'DELETE'}
        });
    }
})();
