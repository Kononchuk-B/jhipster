(function() {
    'use strict';

    angular
        .module('libSupportApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('book-reservation', {
            parent: 'entity',
            url: '/book-reservation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'libSupportApp.bookReservation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/book-reservation/book-reservations.html',
                    controller: 'BookReservationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bookReservation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('book-reservation-detail', {
            parent: 'entity',
            url: '/book-reservation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'libSupportApp.bookReservation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/book-reservation/book-reservation-detail.html',
                    controller: 'BookReservationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bookReservation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BookReservation', function($stateParams, BookReservation) {
                    return BookReservation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'book-reservation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('book-reservation-detail.edit', {
            parent: 'book-reservation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/book-reservation/book-reservation-dialog.html',
                    controller: 'BookReservationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BookReservation', function(BookReservation) {
                            return BookReservation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('book-reservation.new', {
            parent: 'book-reservation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/book-reservation/book-reservation-dialog.html',
                    controller: 'BookReservationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startDate: null,
                                endDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('book-reservation', null, { reload: 'book-reservation' });
                }, function() {
                    $state.go('book-reservation');
                });
            }]
        })
        .state('book-reservation.edit', {
            parent: 'book-reservation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/book-reservation/book-reservation-dialog.html',
                    controller: 'BookReservationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BookReservation', function(BookReservation) {
                            return BookReservation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('book-reservation', null, { reload: 'book-reservation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('book-reservation.delete', {
            parent: 'book-reservation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/book-reservation/book-reservation-delete-dialog.html',
                    controller: 'BookReservationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BookReservation', function(BookReservation) {
                            return BookReservation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('book-reservation', null, { reload: 'book-reservation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
