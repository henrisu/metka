define(function (require) {
    'use strict';

    return function (childConstructor) {
        return function (parentOptions) {
            parentOptions = parentOptions || {};
            return function (options) {
                options = options || {};
                options.parent = parentOptions;

                // inherit readOnly option
                options.readOnly = parentOptions.readOnly || options.readOnly;

                // use parent's values, if nothing else is available
                options.$events = options.$events || parentOptions.$events;
                options.data = options.data || parentOptions.data;
                options.dataConf = options.dataConf || parentOptions.dataConf;

                return childConstructor.call(this, options);
            };
        };
    };
});
