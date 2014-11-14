define(function (require) {
    'use strict';

    return function(options) {
        $(this).toggleClass('hiddenByCustomCode', !require('./../../root')(options).isRelatedStudyDraftForCurrentUser);
        this.click(require('./../../formAction')('edit')(options, function (response) {
            $.extend(options.data, response.data);
            $(this).trigger('refresh.metka');
        }, [
            'REVISION_FOUND',
            'REVISION_CREATED'
        ]));
    };
});