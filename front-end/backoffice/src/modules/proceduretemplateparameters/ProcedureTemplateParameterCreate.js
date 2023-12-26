import React from 'react';
import {
    ArrayInput,
    AutocompleteInput,
    Create,
    FormDataConsumer,
    maxLength,
    minLength,
    NumberInput,
    ReferenceInput,
    required,
    SelectInput,
    SimpleForm,
    SimpleFormIterator,
    TextInput
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';
import { TYPE_CHOICES, TYPE_CHOICES_IDS } from './parameter-type';


const isNumeric = (data) => {
    return data && data.typeId === TYPE_CHOICES['numeric'].id;
}

const isSnomed = (data) => {
    return data && data.typeId === TYPE_CHOICES['snomedEcl'].id;
}

const isOptions = (data) => {
    return data && data.typeId === TYPE_CHOICES['textOption'].id;
}

const validateInputCount = (value, allValues) => {

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

const UnitsOfMeasure = (props) => {
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

const SnomedECL = (props) => {
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

const Options = (props) => {
    return (
        <ArrayInput source="textOptions" {...props} validate={[required(), validateOptions]}>
            <SimpleFormIterator source="options" TransitionProps={{ enter: false }}>
                <TextInput label="resources.proceduretemplateparameters.fields.option" validate={[required(), minLength(1), maxLength(300)]} />
            </SimpleFormIterator>
        </ArrayInput>
    );
}

const LoincCode = (props) => {
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

const ProcedureTemplateParameterCreate = props => (
    <Create {...props}>
        <SimpleForm
            toolbar={<CustomToolbar/>}
            redirect={(basePath, id, data) => `/proceduretemplates/${data.procedureTemplateId}/show`}
        >

            {/**
             * Loinc code
             */}
            <LoincCode/>

            {/**
             * Loinc description
             */}
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

            {/**
             * Type
             */}
            <SelectInput source="typeId" choices={TYPE_CHOICES_IDS} validate={[required()]}/>

            {/**
             * Units of measure
             */}
            <FormDataConsumer>
                 {({formData, ...props}) => formData && isNumeric(formData) && <UnitsOfMeasure {...props}/> }
             </FormDataConsumer>

            {/**
             * Input count
             */}
             <FormDataConsumer>
                 {({formData, ...props}) => formData && isNumeric(formData) &&
                    <NumberInput source='inputCount' validate={[required(), validateInputCount]} step={false} min={1} {...props}/> }
             </FormDataConsumer>
             
            {/**
             * Snomed ECL
             */}
             <FormDataConsumer>
                 {({formData, ...props}) => formData && isSnomed(formData) && <SnomedECL {...props}/> }
             </FormDataConsumer>

            {/**
             * Options
             */}
             <FormDataConsumer>
                 {({formData, ...props}) => formData && isOptions(formData) && <Options {...props}/> }
             </FormDataConsumer>

        </SimpleForm>
    </Create>
);

export default ProcedureTemplateParameterCreate;
