(function() {
    'use strict';
    angular
        .module('libSupportApp')
        .factory('BookCopy', BookCopy);

    BookCopy.$inject = ['$resource', 'DateUtils'];

    function BookCopy ($resource, DateUtils) {
        var resourceUrl =  'api/book-copies/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateOfSupply = DateUtils.convertLocalDateFromServer(data.dateOfSupply);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateOfSupply = DateUtils.convertLocalDateToServer(copy.dateOfSupply);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateOfSupply = DateUtils.convertLocalDateToServer(copy.dateOfSupply);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
