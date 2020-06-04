import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required,
} from 'react-admin';

const InstitutionEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="name" validate={[required()]} />
            <TextInput source="website" />
            <TextInput source="phone" validate={[required()]} />
            <TextInput source="email" type="email" validate={[required()]} />
            <TextInput source="cuit" validate={[required()]} />
            <TextInput source="sisaCode" validate={[required()]} />
        </SimpleForm>
    </Edit>
);

export default InstitutionEdit;
