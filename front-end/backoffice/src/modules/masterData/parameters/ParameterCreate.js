import React from 'react';
import {
    Create,
    FormDataConsumer,
    NumberInput,
    required,
    SimpleForm,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import { LoincRadioButton, UnitsOfMeasure, validateInputCount, SnomedECL, Options, ParameterTypeInput } from './ParameterFormFields';
import { isNumeric, isOptions, isSnomed } from './ParameterTypes';

const ParameterCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar />}>
            <LoincRadioButton />

            <ParameterTypeInput />

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
    </Create>
);

export default ParameterCreate;