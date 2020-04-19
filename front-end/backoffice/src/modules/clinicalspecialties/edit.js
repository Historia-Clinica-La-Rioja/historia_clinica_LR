import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required
} from 'react-admin';

const ClinicalSpecialtyEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="name" validate={[required()]} />
            <TextInput source="description" validate={[required()]} />
            <TextInput source="sctidCode" validate={[required()]} />
        </SimpleForm>
    </Edit>
);

export default ClinicalSpecialtyEdit;
