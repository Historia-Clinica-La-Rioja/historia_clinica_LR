import React from 'react';
import {
    Edit,
    SimpleForm,
    BooleanInput,
    TextInput,
    TextField,
    ReferenceField,
    DateField,
    required,
} from 'react-admin';

import OnlySaveToolbar from '../components/only-save-toolbar';
import PeopleReferenceField from '../people/PeopleReferenceField';
import Aside from './Aside'

const UserEdit = props => (
    <Edit {...props} 
        aside={<Aside />} 
    >
        <SimpleForm toolbar={<OnlySaveToolbar />}>
            <PeopleReferenceField source="personId" />
            <TextInput source="username" validate={[required()]}/>
            <BooleanInput source="enable" validate={[required()]}/>
            <DateField source="lastLogin" showTime locales="es-AR"/>
        </SimpleForm>
    </Edit>
);

export default UserEdit;