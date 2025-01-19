import React from 'react';
import {
    ArrayInput,
    AutocompleteInput,
    Button,
    FunctionField,
    maxLength,
    minLength,
    ReferenceField,
    ReferenceInput,
    required,
    SelectInput,
    SimpleFormIterator,
    TextInput
} from 'react-admin';
import { useForm } from 'react-final-form';


import ActionDelete from '@material-ui/icons/Delete';
import { alpha } from '@material-ui/core/styles/colorManipulator';
import {makeStyles} from "@material-ui/core/styles";

import { TYPE_CHOICES_IDS } from './parameter-type';

export const validateInputCount = (value, allValues) => {
    const uomCount = (allValues?.unitsOfMeasureIds && allValues.unitsOfMeasureIds.length) || 0;
    const inputCount = allValues?.inputCount || 0;
    if (inputCount <= 0)
        return 'resources.proceduretemplateparameters.errors.inputCountLte0'
    if (uomCount < inputCount)
        return 'resources.proceduretemplateparameters.errors.inputCountGtUomCount'
    return undefined;
}

const validateOptions = (value, allValues) => {
    if (!(value && (value.length >= 2)))
        return 'resources.proceduretemplateparameters.errors.optionsMinLength'
    return undefined;
}

const uniqueUnitsOfMeasure = (value, allValues) => {
    if (value && allValues.unitsOfMeasureIds){
        const values = allValues.unitsOfMeasureIds;
        const first = values.indexOf(value);
        const last = values.lastIndexOf(value);
        if (first !== last)
            return "resources.proceduretemplateparameters.errors.uniqueUoms"
    }
    return undefined;
}

/**
 * Overrides the delete button's color on arrayinputs.
 * Must be set as a parameter for the SimpleFormIterator
 */
const useStyles = makeStyles(
    theme => {
        // Así seleccionamos la clase button-remove hija de action
        return ({
            action: {
                '& .button-remove': {
                    color: theme.palette.error.main,
                    '&:hover': {
                        backgroundColor: alpha(theme.palette.error.main, 0.12),
                        // Reset on mouse devices
                        '@media (hover: none)': {
                            backgroundColor: 'transparent',
                        },
                    }
                }
            },
        });
    }
);

export const ParameterTypeInput = (props) => {
    const form = useForm();
    return (
        <SelectInput
            source="typeId"
            choices={TYPE_CHOICES_IDS}
            validate={[required()]}
            onChange={value => {
                form.change('unitsOfMeasureIds', null);
                form.change('snomedGroupId', null);
                form.change('textOptions', null);
                form.change('options', null);
                form.change('inputCount', null);
            }}
        />
    );
}

export const UnitsOfMeasure = (props) => {
    const classes = useStyles(props);
    return (
        <ArrayInput source="unitsOfMeasureIds" {...props} validate={[required()]}>
        <SimpleFormIterator
            TransitionProps={{ enter: false }}
            removeButton={
                <Button label='ra.action.delete'>
                    <ActionDelete/>
                </Button>
            }
            classes={classes}
            >
            <ReferenceInput
                reference="units-of-measure"
                filter={{enabled:true}}
                validate={[required()]}
                sort={{field: "code", order: "ASC"}}
                label="resources.units-of-measure.fields.code"
                filterToQuery={searchText => ({code: searchText ? searchText : ''})}
            >
                <AutocompleteInput optionText="code" validate={[required(), uniqueUnitsOfMeasure]} />
            </ReferenceInput>
        </SimpleFormIterator>
        </ArrayInput>
    );
}

export const SnomedECL = (props) => {
    return (
        <ReferenceInput
            reference="proceduretemplatesnomedgroup"
            source="snomedGroupId"
            sort={{ field: 'description', order: 'ASC' }}
            filterToQuery={searchText => ({description: searchText ? searchText : ''})}
            isRequired={true}
            validate={required()}
        >
            <AutocompleteInput optionText="description" optionValue="id"/>
        </ReferenceInput>);
};

export const Options = (props) => {
    const classes = useStyles(props);
    return (
        <ArrayInput source="textOptions" {...props} validate={[required(), validateOptions]}>
            <SimpleFormIterator
                source="textOptions"
                TransitionProps={{ enter: false }}
                removeButton={
                    <Button label='ra.action.delete'>
                        <ActionDelete/>
                    </Button>
                }
                classes={classes}
            >
                <TextInput
                    label="resources.proceduretemplateparameters.fields.option"
                    validate={[required(), minLength(1), maxLength(300)]}
                />
            </SimpleFormIterator>
        </ArrayInput>
    );
}

export const LoincCode = (props) => {
    return (
        <ReferenceInput
            source="loincId"
            reference="loinc-codes"
            sort={{ field: 'code', order: 'ASC' }}
            label="resources.proceduretemplateparameters.fields.loincId"
            filterToQuery={searchText => ({code: searchText ? searchText : -1})}
        >
            <AutocompleteInput optionText="code" optionValue="id" validate={[required()]} options={{ disabled: false }} />
        </ReferenceInput>
    );
}

export const LoincDescription = (props) => {
    return (
        <ReferenceInput
            source="loincId"
            reference="loinc-codes"
            sort={{ field: 'code', order: 'ASC' }}
            label="resources.proceduretemplateparameters.fields.description"
            fullWidth
        >
            <AutocompleteInput 
                optionText={(x) => x?.customDisplayName || x?.displayName || x?.description || ''}
                optionValue="id"
                options={{ disabled: true }} />
        </ReferenceInput>
    );
}

export const LoincCodeDescriptionShow = (props) => {
    return (
        <ReferenceField
            source="loincId"
            reference="loinc-codes"
            label="resources.proceduretemplateparameters.fields.loincId"
        >
            <FunctionField
                render={(x) => x?.customDisplayName || x?.displayName || x?.description || ''} 
                label="resources.proceduretemplateparameters.fields.loincId"/>
        </ReferenceField>
    );
}