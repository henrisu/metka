define(function (require) {
    'use strict';

    return function (options) {
        var key = 'vargroups';

        var rowDialog = require('./../../containerRowDialog')(options, options.defaultLang, key, function () {
            return [{
                type: 'ROW',
                cells: [{
                    "type": "CELL",
                    "title": "Ryhmän nimi",
                    "field": {
                        "key": "vargrouptitle"
                    }
                }]
            }, {
                type: 'ROW',
                cells: [{
                    "type": "CELL",
                    "title": "Ryhmän tekstit",
                    "field": {
                        "key": "vargrouptexts",
                        "displayHeader": false,
                        "columnFields": [
                            "vargrouptext"
                        ]
                    },
                    "extraDialogConfiguration" : {
                        "vargrouptext": {
                            "field": {
                                "multiline": true
                            }
                        }
                    }
                }]
            }];
        });

        return {
            create: function create(options) {
                var key = 'variables';
                var column = 'varlabel';

                var $elem = this;

                require('./../../data')(options).onChange(function onChange() {
                    require('./../../preloader')($elem);
                    var rows = require('./../../data')(options)(key).getByLang(options.defaultLang);
                    if (rows) {
                        require('./../../server')('options', {
                            data: JSON.stringify({
                                key: key,
                                requests: rows.map(function (transferRow) {
                                    var fieldValues = {};
                                    fieldValues[key] = transferRow.value;
                                    return {
                                        key: column,
                                        container: key,
                                        confType: options.dataConf.key.type,
                                        confVersion: options.dataConf.key.version,
                                        language: options.defaultLang,
                                        fieldValues: fieldValues
                                    }
                                })
                            }),
                            success: function (data) {
                                var variables = data.responses.map(function (response) {
                                    return {
                                        text: response.options[0].title.value,
                                        value: response.fieldValues.variables
                                    };
                                });
                                var treeViewEvents = {};
                                if (!require('./../../isFieldDisabled')(options, options.defaultLang)) {
                                    treeViewEvents.onClick = function (node) {
                                        if (!node.children) {
                                            require('./../../variableModal')(options.field.key, {
                                                id: node.transferRow.value,
                                                no: ''
                                            }, onChange);
                                        } else {
                                            rowDialog('modify', 'ok')(node.transferRow, onChange);
                                        }
                                    };
                                }
                                $elem.empty().append(require('./../../treeView')((require('./../../data')(options)('vargroups').getByLang(options.defaultLang) || []).filter(function (row) {
                                    return row.fields && row.fields.vargrouptitle;
                                }).map(function (transferRow) {
                                    return require('./../../treeViewVariableGroup')(
                                        transferRow.fields.vargrouptitle.values.DEFAULT.current,
                                        transferRow.fields.vargroupvars ? transferRow.fields.vargroupvars.rows.DEFAULT.map(function (transferRow) {
                                            var groupedVariable = variables.find(function (variable) {
                                                return variable.value === transferRow.value;
                                            });
                                            if (!transferRow.removed) {
                                                variables.splice(variables.indexOf(groupedVariable), 1);
                                            }
                                            return {
                                                transferRow: transferRow,
                                                groupedVariable: groupedVariable
                                            };
                                        }).filter(function (o) {
                                            if (!o.groupedVariable || o.transferRow.removed) {
                                                o.transferRow.removed = true;
                                                return false;
                                            } else {
                                                return true;
                                            }
                                        }).map(function (o) {
                                            return {
                                                text: o.groupedVariable.text,
                                                transferRow: o.transferRow
                                            };
                                        }) : [],
                                        transferRow
                                    );

                                }), treeViewEvents));
                            }
                        });
                    }
                });
            }
        };
    };
});
