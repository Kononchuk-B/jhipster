(function() {
    'use strict';

    angular
        .module('libSupportApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('book-copy', {
            parent: 'entity',
            url: '/book-copy',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'libSupportApp.bookCopy.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/book-copy/book-copies.html',
                    controller: 'BookCopyController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bookCopy');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('book-copy-detail', {
            parent: 'entity',
            url: '/book-copy/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'libSupportApp.bookCopy.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/book-copy/book-copy-detail.html',
                    controller: 'BookCopyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bookCopy');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BookCopy', function($stateParams, BookCopy) {
                    return BookCopy.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'book-copy',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('book-copy-detail.edit', {
            parent: 'book-copy-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/book-copy/book-copy-dialog.html',
                    controller: 'BookCopyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BookCopy', function(BookCopy) {
                            return BookCopy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('book-copy.new', {
            parent: 'book-copy',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/book-copy/book-copy-dialog.html',
                    controller: 'BookCopyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateOfSupply: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('book-copy', null, { reload: 'book-copy' });
                }, function() {
                    $state.go('book-copy');
                });
            }]
        })
        .state('book-copy.edit', {
            parent: 'book-copy',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/book-copy/book-copy-dialog.html',
                    controller: 'BookCopyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BookCopy', function(BookCopy) {
                            return BookCopy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('book-copy', null, { reload: 'book-copy' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('book-copy.delete', {
            parent: 'book-copy',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/book-copy/book-copy-delete-dialog.html',
                    controller: 'BookCopyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BookCopy', function(BookCopy) {
                            return BookCopy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('book-copy', null, { reload: 'book-copy' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
