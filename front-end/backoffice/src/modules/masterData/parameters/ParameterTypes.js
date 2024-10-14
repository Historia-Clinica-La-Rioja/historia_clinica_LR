const NUMERIC = 1;
const FREE_TEXT = 2;
const SNOMED_ECL = 3;
const TEXT_OPTION = 4;

const TYPE_CHOICES = {
    "numeric": {id: NUMERIC, name: 'Numérico',},
    "freeText": {id: FREE_TEXT, name: 'Texto libre'},
    "snomedEcl": {id: SNOMED_ECL, name: 'SNOMED (ECL)'},
    "textOption": {id: TEXT_OPTION, name: 'Lista de opciones'}
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
