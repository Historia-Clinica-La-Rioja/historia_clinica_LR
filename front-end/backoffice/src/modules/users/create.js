import React from 'react';
import {
    Create,
    SimpleForm,
    TextInput,
    required,
} from 'react-admin';
import PeopleReferenceInput from '../people/PeopleReferenceInput';
import SaveCancelToolbar from "../../modules/components/save-cancel-toolbar";

const UserCreate = props => (
    <Create {...props}>
        <SimpleForm toolbar={<SaveCancelToolbar />}>
            <PeopleReferenceInput source="personId" validate={[required()]} />
            <TextInput source="username" validate={[required()]}/>
        </SimpleForm>
    </Create>
);

export default UserCreate;
