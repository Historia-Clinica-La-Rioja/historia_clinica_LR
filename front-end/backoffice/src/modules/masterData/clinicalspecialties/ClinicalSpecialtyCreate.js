import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";

const ClinicalSpecialtyCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="list" toolbar={<CustomToolbar />}>
            <TextInput source="name" validate={[required()]} />
            <TextInput source="sctidCode" validate={[required()]} />
        </SimpleForm>
    </Create>
);

export default ClinicalSpecialtyCreate;
