import React from 'react';
import {
    Create,
    SimpleForm,
    TextInput,
    required, ReferenceField, FunctionField,
} from 'react-admin';

const renderPerson = (choice) => `${choice.identificationNumber} ${choice.lastName} ${choice.firstName}`;

const redirect = (basePath, id, data) => `/users/${data.id}/show`;

const UserCreate = props => (
    <Create {...props}>
        <SimpleForm  redirect={redirect}>
            <ReferenceField source="personId" reference="person" link={false}>
                <FunctionField render={renderPerson}/>
            </ReferenceField>
            <TextInput source="username" validate={[required()]}/>
        </SimpleForm >
    </Create>
);

export default UserCreate;
