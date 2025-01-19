import React from 'react';
import {
    Edit,
    FormDataConsumer,
    NumberInput,
    required,
    SimpleForm,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import { LoincRadioButton, UnitsOfMeasure, validateInputCount, SnomedECL, Options, ParameterTypeInput } from './ParameterFormFields';
import { isNumeric, isOptions, isSnomed } from './ParameterTypes';

const ParameterEdit = props => {
    return (
        <Edit {...props}
        >
            <SimpleForm toolbar={<CustomToolbar />} redirect="show">

                <LoincRadioButton {...props} />

                <ParameterTypeInput {...props} />
                <FormDataConsumer>
                    {({ formData, ...props }) => formData && isNumeric(formData) && <UnitsOfMeasure {...props} />}
                </FormDataConsumer>

                <FormDataConsumer>
                    {({ formData, ...props }) => formData && isNumeric(formData) &&
                        <NumberInput source='inputCount' validate={[required(), validateInputCount]} step={1} min={1} {...props} />}
                </FormDataConsumer>

                <FormDataConsumer>
                    {({ formData, ...props }) => formData && isSnomed(formData) && <SnomedECL {...props} />}
                </FormDataConsumer>

                <FormDataConsumer>
                    {({ formData, ...props }) => formData && isOptions(formData) && <Options {...props} />}
                </FormDataConsumer>

            </SimpleForm>
        </Edit>
    )
}

export default ParameterEdit;