import React from 'react';
import { AutocompleteInput, Create, maxLength, ReferenceInput, SimpleForm, TextInput } from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';

const redirect = (basePath, id, data) => `/medicalcoverages/${data.privateHealthInsuranceId}/show`;

const PrivateHealthInsurancePlanCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar/>}>
            <ReferenceInput
                source="privateHealthInsuranceId"
                reference="medicalcoverages"
                sort={{field: 'name', order: 'ASC'}}
                label="resources.privatehealthinsuranceplans.fields.privateHealthInsuranceId">
                <AutocompleteInput optionText="name" optionValue="id" options={{disabled: true}}/>
            </ReferenceInput>
            <TextInput source="plan" validate={[
                maxLength(10)]}/>

        </SimpleForm>
    </Create>
);

export default PrivateHealthInsurancePlanCreate;
