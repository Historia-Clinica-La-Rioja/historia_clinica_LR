import React from 'react';
import {
    Create,
    SimpleForm,
    TextInput,
    required,
} from 'react-admin';
import PeopleReferenceInput from '../people/PeopleReferenceInput';
import CustomToolbar from "../../modules/components/CustomToolbar";

const UserCreate = props => (
    <Create {...props}>
        <SimpleForm toolbar={<CustomToolbar />}>
            <PeopleReferenceInput source="personId" validate={[required()]} />
            <TextInput source="username" validate={[required()]}/>
        </SimpleForm>
    </Create>
);

export default UserCreate;
