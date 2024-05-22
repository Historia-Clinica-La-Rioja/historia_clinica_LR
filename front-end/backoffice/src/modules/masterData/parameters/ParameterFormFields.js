import React, { useEffect, useRef, useState } from 'react';
import {
    ArrayInput,
    AutocompleteInput,
    Button,
    maxLength,
    minLength,
    ReferenceInput,
    required,
    SelectInput,
    SimpleFormIterator,
    TextInput,
    RadioButtonGroupInput,
} from 'react-admin';
import { useForm, useField } from 'react-final-form';


import ActionDelete from '@material-ui/icons/Delete';
import { alpha } from '@material-ui/core/styles/colorManipulator';
import { makeStyles } from "@material-ui/core/styles";

import { TYPE_CHOICES_IDS } from './ParameterTypes';

export const validateInputCount = (value, allValues) => {
    const uomCount = (allValues?.unitsOfMeasureIds && allValues.unitsOfMeasureIds.length) || 0;
    const inputCount = allValues?.inputCount || 0;
    if (inputCount <= 0)
        return 'resources.parameters.errors.inputCountLte0'
    if (uomCount < inputCount)
        return 'resources.parameters.errors.inputCountGtUomCount'
    return undefined;
}

const validateOptions = (value, allValues) => {
    return (!(value && (value.length >= 2))) ?
        'resources.parameters.errors.optionsMinLength'
        : undefined;
}

const uniqueUnitsOfMeasure = (value, allValues) => {
    if (value && allValues.unitsOfMeasureIds) {
        const values = allValues.unitsOfMeasureIds;
        const first = values.indexOf(value);
        const last = values.lastIndexOf(value);
        if (first !== last)
            return "resources.parameters.errors.uniqueUoms"
    }
    return undefined;
}

const useStyles = makeStyles(
    theme => {
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
    const {
        input: { value: typeIdValue }
    } = useField('typeId');

    const prevTypeIdValue = useRef(typeIdValue);

    useEffect(() => {
        if (typeIdValue !== prevTypeIdValue.current) {
            form.change('unitsOfMeasureIds', null);
            form.change('snomedGroupId', null);
            form.change('textOptions', null);
            form.change('options', null);
            form.change('inputCount', null);
        }
        prevTypeIdValue.current = typeIdValue;
    }, [typeIdValue, form]);

    return (
        <SelectInput
            source="typeId"
            label="resources.parameters.fields.type"
            choices={TYPE_CHOICES_IDS}
            validate={[required()]}
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
                        <ActionDelete />
                    </Button>
                }
                classes={classes}
            >
                <ReferenceInput
                    reference="units-of-measure"
                    filter={{ enabled: true }}
                    validate={[required()]}
                    sort={{ field: "code", order: "ASC" }}
                    label="resources.units-of-measure.fields.code"
                    filterToQuery={searchText => ({ code: searchText ? searchText : '' })}
                >
                    <AutocompleteInput optionText="code" validate={[required(), uniqueUnitsOfMeasure]} />
                </ReferenceInput>
            </SimpleFormIterator>
        </ArrayInput>
    );
}

export const SnomedECL = props => (
    <ReferenceInput
        reference="snomedgroups"
        source="snomedGroupId"
        sort={{ field: 'description', order: 'ASC' }}
        filterToQuery={searchText => ({ description: searchText ? searchText : '' })}
        validate={required()} >
        <AutocompleteInput optionText="description" optionValue="id" />
    </ReferenceInput>
);


export const Options = props => {
    const classes = useStyles(props);
    return (
        <ArrayInput source="textOptions" {...props} validate={[required(), validateOptions]}>
            <SimpleFormIterator
                source="textOptions"
                TransitionProps={{ enter: false }}
                removeButton={
                    <Button label='ra.action.delete'>
                        <ActionDelete />
                    </Button>
                }
                classes={classes}
            >
                <TextInput
                    label="resources.parameters.fields.option"
                    validate={[required(), minLength(1), maxLength(300)]}
                />
            </SimpleFormIterator>
        </ArrayInput>
    );
}

export const LoincCode = props => (
    <ReferenceInput
        source="loincId"
        reference="loinc-codes"
        sort={{ field: 'code', order: 'ASC' }}
        label="resources.parameters.fields.loincId"
        filterToQuery={searchText => ({ code: searchText ? searchText : -1 })}
    >
        <AutocompleteInput optionText="code" optionValue="id" validate={[required()]} options={{ disabled: false }} />
    </ReferenceInput>
);


export const LoincDescription = props => (
    <ReferenceInput
        source="loincId"
        reference="loinc-codes"
        sort={{ field: 'code', order: 'ASC' }}
        label="resources.parameters.fields.description"
        fullWidth
    >
        <AutocompleteInput
            optionText={(x) => x?.customDisplayName || x?.description || ''}
            optionValue="id"
            options={{ disabled: true }} />
    </ReferenceInput>
);

export const Description = props => (
    <TextInput source="description" label="resources.parameters.fields.description" validate={[required()]} />
);

const includeLoinc = 1;
const notIncludeLoinc = 2;

export const LoincRadioButton = (props) => {
    const { record } = props;
    const form = useForm();

    let defaultValue = includeLoinc;
    if (record && record.id) {
        defaultValue = (record.loincId !== null && record.loincId !== undefined) ? includeLoinc : notIncludeLoinc;
    }

    const [selectedRadioButton, setSelectedRadioButton] = useState(defaultValue);

    useEffect(() => {
        setSelectedRadioButton(defaultValue);
    }, [defaultValue]);

    useEffect(() => {
        if (selectedRadioButton === notIncludeLoinc) {
            form.change('loincId', null);
        } else {
            form.change('description', null);
        }
    }, [selectedRadioButton, form]);

    const handleChange = (value) => {
        if (value)
            setSelectedRadioButton(value);
    };

    const radioButtonChoices = [
        { id: includeLoinc, name: "resources.parameters.fields.loincRadioButton.option_1" },
        { id: notIncludeLoinc, name: "resources.parameters.fields.loincRadioButton.option_2" },
    ];

    return (
        <div style={{ display: 'flex', flexDirection: 'column' }}>
            <RadioButtonGroupInput
                source="radioButton"
                label="resources.parameters.fields.loincRadioButton.title"
                choices={radioButtonChoices}
                validate={required()}
                onChange={handleChange}
                defaultValue={defaultValue}
            />

            {selectedRadioButton === includeLoinc ? (
                <>
                    <LoincCode />
                    <LoincDescription />
                </>
            ) : (
                <Description />
            )}
        </div>
    );
};