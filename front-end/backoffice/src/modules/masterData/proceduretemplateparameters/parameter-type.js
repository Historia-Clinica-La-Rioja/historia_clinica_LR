const NUMERIC = 1;
const FREE_TEXT = 2;
const SNOMED_ECL = 3;
const TEXT_OPTION = 4;

const TYPE_CHOICES = {
    "numeric": {id: NUMERIC, name: 'resources.proceduretemplateparameters.typeChoices.numeric',},
    "freeText": {id: FREE_TEXT, name: 'resources.proceduretemplateparameters.typeChoices.text'},
    "snomedEcl": {id: SNOMED_ECL, name: 'resources.proceduretemplateparameters.typeChoices.snomed'},
    "textOption": {id: TEXT_OPTION, name: 'resources.proceduretemplateparameters.typeChoices.options'}
};

export const TYPE_CHOICES_IDS = Object.values(TYPE_CHOICES);

export const isNumeric = (data) => {
    return data && data.typeId === TYPE_CHOICES['numeric'].id;
}

export const isSnomed = (data) => {
    return data && data.typeId === TYPE_CHOICES['snomedEcl'].id;
}

export const isOptions = (data) => {
    return data && data.typeId === TYPE_CHOICES['textOption'].id;
}