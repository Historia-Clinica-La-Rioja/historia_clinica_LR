import React from 'react';
import {
    Create,
    SimpleForm,
    TextInput,
    required,
} from 'react-admin';

import PersonReferenceInput from '../person/PersonReferenceInput';

const UserCreate = props => (
    <Create {...props}>
        <SimpleForm>
            <PersonReferenceInput source="personId" validate={[required()]} />
            <TextInput source="username" validate={[required()]}/>
        </SimpleForm>
    </Create>
);

export default UserCreate;
