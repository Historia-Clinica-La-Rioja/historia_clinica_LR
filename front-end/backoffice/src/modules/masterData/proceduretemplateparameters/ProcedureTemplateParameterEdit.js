import React from 'react';
import {
    Edit,
    FormDataConsumer,
    NumberInput,
    required,
    SimpleForm,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import { LoincCode, LoincDescription, UnitsOfMeasure, validateInputCount, SnomedECL, Options, ParameterTypeInput } from './fields';
import {  isNumeric, isOptions, isSnomed } from './parameter-type';

const ProcedureTemplateParameterEdit = props => (
    <Edit {...props}>
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
            <LoincDescription/>

            {/**
             * Type
             */}
            <ParameterTypeInput/>

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
                    <NumberInput source='inputCount' validate={[required(), validateInputCount]} step={1} min={1} {...props}/> }
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
    </Edit>
);

export default ProcedureTemplateParameterEdit;