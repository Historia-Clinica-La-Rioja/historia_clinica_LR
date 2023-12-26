const NUMERIC = 1;
const FREE_TEXT = 2;
const SNOMED_ECL = 3;
const TEXT_OPTION = 4;

export const TYPE_CHOICES = {
    "numeric": {id: NUMERIC, name: 'resources.proceduretemplateparameters.typeChoices.numeric',},
    "freeText": {id: FREE_TEXT, name: 'resources.proceduretemplateparameters.typeChoices.text'},
    "snomedEcl": {id: SNOMED_ECL, name: 'resources.proceduretemplateparameters.typeChoices.snomed'},
    "textOption": {id: TEXT_OPTION, name: 'resources.proceduretemplateparameters.typeChoices.options'}
};

export const TYPE_CHOICES_IDS = Object.values(TYPE_CHOICES)