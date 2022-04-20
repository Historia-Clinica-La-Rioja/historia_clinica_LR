import React from 'react';
import { AutocompleteInput, Create, maxLength, ReferenceInput, required, SimpleForm, TextInput } from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';

const redirect = (basePath, id, data) => `/medicalcoverages/${data.medicalCoverageId}/show`;

const MedicalCoveragePlanCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar/>}>
            <ReferenceInput
                source="medicalCoverageId"
                reference="medicalcoverages"
                sort={{field: 'name', order: 'ASC'}}
                label="resources.medicalcoverageplans.fields.medicalCoverageId">
                <AutocompleteInput optionText="name" optionValue="id" options={{disabled: true}}/>
            </ReferenceInput>
            <TextInput source="plan" validate={[
                maxLength(10), required()]}/>

        </SimpleForm>
    </Create>
);

export default MedicalCoveragePlanCreate;
