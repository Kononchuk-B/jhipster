(function() {
    'use strict';
    angular
        .module('libSupportApp')
        .factory('Book', Book);

    Book.$inject = ['$resource', 'DateUtils'];

    function Book ($resource, DateUtils) {
        var resourceUrl =  'api/books/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.yearOfPublish = DateUtils.convertLocalDateFromServer(data.yearOfPublish);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.yearOfPublish = DateUtils.convertLocalDateToServer(copy.yearOfPublish);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.yearOfPublish = DateUtils.convertLocalDateToServer(copy.yearOfPublish);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
