import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';
import CustomToolbar from "../../modules/components/CustomToolbar";

const InstitutionCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar />}>
            <TextInput source="name" validate={[required()]} />
            <TextInput source="website" />
            <TextInput source="phone" validate={[required()]} />
            <TextInput source="email" type="email" validate={[required()]} />
            <TextInput source="cuit" validate={[required()]} />
            <TextInput source="sisaCode" validate={[required()]} />
        </SimpleForm>
    </Create>
);

export default InstitutionCreate;
