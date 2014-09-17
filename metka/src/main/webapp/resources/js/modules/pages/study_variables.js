define(function (require) {
    'use strict';

    if (location.pathname.split('/').indexOf('search') === -1) {
        return require('./../pages/defaults');
    }

    return function (options, onLoad) {
        $.extend(options, {
            header: MetkaJS.L10N.get('type.STUDY_VARIABLES.search'),
            "fieldTitles": {
                "title": {
                    "title": "Aineisto"
                }
            },
            content: [
                {
                    "type": "COLUMN",
                    "columns": 1,
                    "rows": [
                        {
                            "type": "ROW",
                            "cells": [
                                {
                                    "type": "CELL",
                                    "title": "Aineistot",
                                    "field": {
                                        "readOnly": true,
                                        "displayType": "CONTAINER",
                                        "columnFields": [
                                            "title"
                                        ],
                                        onClick: function (transferRow) {
                                            require('./../assignUrl')('view', {
                                                id: transferRow.fields.id.values.DEFAULT.current,
                                                no: ''
                                            });
                                        }
                                    },
                                    create: function (options) {
                                        var $containerField = $(this).children().first();
                                        require('./../server')('/study/studiesWithVariables', {
                                            method: 'GET',
                                            success: function (data) {
                                                data.studies.forEach(function (data) {
                                                    $containerField.data('addRowFromDataObject')(data);
                                                });
                                            }
                                        });
                                    }
                                }
                            ]
                        }
                    ]
                }
            ],
            buttons: [],
            data: {},
            dataConf: {
                fields: {
                    title: {
                        type: 'STRING'
                    }
                }
            }
        });
        onLoad();
    }
});