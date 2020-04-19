import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';

const ClinicalSpecialtyCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="name" validate={[required()]} />
            <TextInput source="description" validate={[required()]} />
            <TextInput source="sctidCode" validate={[required()]} />
        </SimpleForm>
    </Create>
);

export default ClinicalSpecialtyCreate;
