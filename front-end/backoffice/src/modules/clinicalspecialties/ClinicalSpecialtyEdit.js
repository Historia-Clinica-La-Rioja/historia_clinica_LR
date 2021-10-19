import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";

const ClinicalSpecialtyEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="name" validate={[required()]} />
            <TextInput source="sctidCode" validate={[required()]} />
        </SimpleForm>
    </Edit>
);

export default ClinicalSpecialtyEdit;
