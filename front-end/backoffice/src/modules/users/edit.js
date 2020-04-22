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
// import Aside from './Aside'

const UserEdit = props => (
    <Edit {...props} 
        // aside={<Aside />} 
    >
        <SimpleForm toolbar={<OnlySaveToolbar />}>
            <ReferenceField source="personId" reference="persons" validate={[required()]}>
                <TextField source="completeName" />
            </ReferenceField>
            <TextInput source="username" validate={[required()]}/>
            <BooleanInput source="enable" validate={[required()]}/>
            <DateField source="lastLogin" showTime locales="es-AR"/>
        </SimpleForm>
    </Edit>
);

export default UserEdit;