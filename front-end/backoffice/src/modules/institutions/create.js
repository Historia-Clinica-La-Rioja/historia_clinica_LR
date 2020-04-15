import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';

import OnlySaveToolbar from '../components/only-save-toolbar';

const InstitutionCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<OnlySaveToolbar />}>
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
