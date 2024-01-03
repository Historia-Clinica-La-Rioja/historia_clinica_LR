import React from 'react';
import {
    ArrayInput,
    AutocompleteInput,
    maxLength,
    minLength,
    ReferenceInput,
    required,
    SelectInput,
    SimpleFormIterator,
    TextInput
} from 'react-admin';

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

export const UnitsOfMeasure = (props) => {
    return (
        <ArrayInput source="unitsOfMeasureIds" {...props} validate={[required()]}>
        <SimpleFormIterator TransitionProps={{ enter: false }}>
            <ReferenceInput
                reference="units-of-measure"
                filter={{enabled:true}}
                validate={[required()]}
                sort={{field: "code", order: "ASC"}}
                label="resources.units-of-measure.fields.code"
                filterToQuery={searchText => ({code: searchText ? searchText : ''})}
            >
                <AutocompleteInput optionText="code" validate={[required()]} />
            </ReferenceInput>
        </SimpleFormIterator>
        </ArrayInput>
    );
}

export const SnomedECL = (props) => {
    return (
        <ReferenceInput
        source="eclId"
        reference="snomed-ecls"
        sort={{ field: 'code', order: 'ASC' }}
        label="resources.proceduretemplateparameters.fields.eclId"
        filterToQuery={searchText => ({code: searchText ? searchText : -1})}
    >
        <SelectInput optionText="code" optionValue="id" validate={[required()]} options={{ disabled: false }} />
    </ReferenceInput>
    );
}

export const Options = (props) => {
    return (
        <ArrayInput source="textOptions" {...props} validate={[required(), validateOptions]}>
            <SimpleFormIterator source="options" TransitionProps={{ enter: false }}>
                <TextInput label="resources.proceduretemplateparameters.fields.option" validate={[required(), minLength(1), maxLength(300)]} />
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