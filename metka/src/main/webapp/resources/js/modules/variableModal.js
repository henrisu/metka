define(function (require) {
    'use strict';

    return function (key, requestOptions, onSaveSuccess) {
        require('./server')('viewAjax', $.extend({
            PAGE: 'STUDY_VARIABLE'
        }, requestOptions), {
            method: 'GET',
            success: function (data) {
                var modalOptions = $.extend(data.gui, {
                    title: 'Muokkaa muuttujaa',
                    data: data.transferData,
                    dataConf: data.configuration,
                    $events: $({}),
                    defaultLang: 'DEFAULT',
                    translatableCurrentLang: MetkaJS.User.role.defaultLanguage.toUpperCase(),
                    large: true,
                    "fieldTitles": {
                        "ivuinstr": {
                            "key" : "ivuinstr",
                            "title" : "Haastattelijan ohje"
                        },
                        "postqtxt": {
                            "key" : "postqtxt",
                            "title" : "Jälkiteksti"
                        },
                        "preqtxt": {
                            "key" : "preqtxt",
                            "title" : "Esiteksti"
                        },
                        "qstnlit": {
                            "key" : "qstnlit",
                            "title" : "Kysymysteksti"
                        },
                        "varnote": {
                            "key" : "varnote",
                            "title" : "Huomiot"
                        },
                        "varsecurity": {
                            "key" : "varsecurity",
                            "title" : "Tietosuoja-asiat"
                        },
                        "vartext": {
                            "key" : "vartext",
                            "title" : "Lisätiedot"
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
                                            "title": "Nimi",
                                            "horizontal": true,
                                            "field": {
                                                "key": "varname"
                                            }
                                        }
                                    ]
                                },
                                {
                                    "type": "ROW",
                                    "cells": [
                                        {
                                            "type": "CELL",
                                            "title": "Selite",
                                            "horizontal": true,
                                            "field": {
                                                "key": "varlabel"
                                            }
                                        }
                                    ]
                                },
                                {
                                    "type": "ROW",
                                    "cells": [
                                        {
                                            "type": "CELL",
                                            "title": "Kysymystekstit",
                                            "field": {
                                                "key": "qstnlits",
                                                "displayHeader": false,
                                                "columnFields": [
                                                    "qstnlit"
                                                ]
                                            }
                                        }
                                    ]
                                },
                                {
                                    "type": "ROW",
                                    "cells": [
                                        {
                                            "type": "CELL",
                                            "title": "Esitekstit",
                                            "field": {
                                                "key": "preqtxts",
                                                "displayHeader": false,
                                                "columnFields": [
                                                    "preqtxt"
                                                ]
                                            }
                                        }
                                    ]
                                },
                                {
                                    "type": "ROW",
                                    "cells": [
                                        {
                                            "type": "CELL",
                                            "title": "Jälkitekstit",
                                            "field": {
                                                "key": "postqtxts",
                                                "displayHeader": false,
                                                "columnFields": [
                                                    "postqtxt"
                                                ]
                                            }
                                        }
                                    ]
                                },
                                {
                                    "type": "ROW",
                                    "cells": [
                                        {
                                            "type": "CELL",
                                            "title": "Haastattelijan ohjeet",
                                            "field": {
                                                "key": "ivuinstrs",
                                                "displayHeader": false,
                                                "columnFields": [
                                                    "ivuinstr"
                                                ]
                                            }
                                        }
                                    ]
                                },
                                {
                                    "type": "ROW",
                                    "cells": [
                                        {
                                            "type": "CELL",
                                            "title": "Huomiot",
                                            "field": {
                                                "key": "varnotes",
                                                "displayHeader": false,
                                                "columnFields": [
                                                    "varnote"
                                                ]
                                            }
                                        }
                                    ]
                                },
                                {
                                    "type": "ROW",
                                    "cells": [
                                        {
                                            "type": "CELL",
                                            "title": "Lisätiedot",
                                            "field": {
                                                "key": "vartexts",
                                                "displayHeader": false,
                                                "columnFields": [
                                                    "vartext"
                                                ]
                                            }
                                        }
                                    ]
                                },
                                {
                                    "type": "ROW",
                                    "cells": [
                                        {
                                            "type": "CELL",
                                            "title": "Tietosuoja-asiat",
                                            "field": {
                                                "key": "varsecurities",
                                                "displayHeader": false,
                                                "columnFields": [
                                                    "varsecurity"
                                                ]
                                            }
                                        }
                                    ]
                                },
                                {
                                    "type": "ROW",
                                    "cells": [
                                        {
                                            "type": "CELL",
                                            "title": "Arvojen selitteet",
                                            "readOnly": true,
                                            "field": {
                                                "displayHeader": false,
                                                "displayType": "CONTAINER",
                                                "key": "categories",
                                                "columnFields": [
                                                    "value",
                                                    "label",
                                                    "stat",
                                                    "missing"
                                                ]
                                            }
                                        }
                                    ]
                                },
                                {
                                    "type": "ROW",
                                    "cells": [
                                        {
                                            "type": "CELL",
                                            "title": "Statistiikka",
                                            "readOnly": true,
                                            "field": {
                                                "key": "statistics",
                                                "displayHeader": false,
                                                "columnFields": [
                                                    "statisticstype",
                                                    "statisticsvalue"
                                                ]
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                    ],
                    buttons: [
                        {
                            "&title": {
                                "default": "Tallenna"
                            },
                            "type": "SAVE",
                            "isHandler": true,
                            "states": [
                                "DRAFT"
                            ],
                            "permissions": [
                                "canEditRevision"
                            ]
                        },
                        /*{
                            "&title": {
                                "default": "Hyväksy"
                            },
                            "type": "APPROVE",
                            "isHandler": true,
                            "states": [
                                "DRAFT"
                            ],
                            "permissions": [
                                "canApproveRevision"
                            ]
                        },*/
                        /*{
                            "&title": {
                                "default": "Tee luonnos"
                            },
                            "type": "EDIT",
                            "states": [
                                "APPROVED"
                            ],
                            "permissions": [
                                "canEditRevision"
                            ]
                        },
                        {
                            "&title": {
                                "default": "Poista"
                            },
                            "type": "REMOVE",
                            "states": [
                                "DRAFT",
                                "APPROVED"
                            ],
                            "isHandler": true,
                            "permissions": [
                                "canRemoveRevision"
                            ]
                        },
                        {
                            "&title": {
                                "default": "Palauta"
                            },
                            "type": "RESTORE",
                            "states": [
                                "REMOVED"
                            ],
                            "permissions": [
                                "canRestoreRevision"
                            ]
                        },*/
                        {
                            "&title": {
                                "default": "Aloita muokkaus"
                            },
                            "type": "CLAIM",
                            "hasHandler": false,
                            "states": [
                                "DRAFT"
                            ],
                            "permissions": [
                                "canEditRevision"
                            ]
                        },
                        {
                            "&title": {
                                "default": "Ota haltuun"
                            },
                            "type": "CLAIM",
                            "hasHandler": true,
                            "isHandler": false,
                            "states": [
                                "DRAFT"
                            ],
                            "permissions": [
                                "canEditRevision",
                                "canForceClaimRevision"
                            ]
                        },
                        {
                            "&title": {
                                "default": "Lopeta muokkaus"
                            },
                            "type": "RELEASE",
                            "isHandler": true,
                            "states": [
                                "DRAFT"
                            ],
                            "permissions": [
                                "canEditRevision"
                            ]
                        },
                        {
                            "&title": {
                                "default": "Vapauta luonnos"
                            },
                            "type": "RELEASE",
                            "isHandler": false,
                            "hasHandler": true,
                            "states": [
                                "DRAFT"
                            ],
                            "permissions": [
                                "canForceReleaseRevision"
                            ]
                        },
                        /*{
                            "&title": {
                                "default": "Revisiohistoria"
                            },
                            "type": "HISTORY",
                            "permissions": [
                                "canViewRevision"
                            ]
                        },
                        {
                            "&title": {
                                "default": "Export DDI"
                            },
                            "type": "EXPORT_DDI",
                            "permissions": [
                                "canViewRevision"
                            ]
                        },*/
                        {
                            type: 'CANCEL'
                        }
                    ]
                });
                var $modal = require('./modal')(modalOptions);
            }
        });
    };
});