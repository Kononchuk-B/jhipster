(function() {
    'use strict';
    angular
        .module('libSupportApp')
        .factory('Feedback', Feedback);

    Feedback.$inject = ['$resource', 'DateUtils'];

    function Feedback ($resource, DateUtils) {
        var resourceUrl =  'api/feedbacks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateOfPublish = DateUtils.convertLocalDateFromServer(data.dateOfPublish);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateOfPublish = DateUtils.convertLocalDateToServer(copy.dateOfPublish);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateOfPublish = DateUtils.convertLocalDateToServer(copy.dateOfPublish);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
