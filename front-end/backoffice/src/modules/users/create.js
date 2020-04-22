import React from 'react';
import {
    Create,
    SimpleForm,
    ReferenceInput,
    TextInput,
    AutocompleteInput,
    BooleanInput,
    required,
} from 'react-admin';

const UserCreate = props => (
    <Create {...props}>
        <SimpleForm>
            <ReferenceInput source="personId" reference="persons" validate={[required()]}>
                <AutocompleteInput optionText="completeName" />
            </ReferenceInput>
            <TextInput source="username" validate={[required()]}/>
            <BooleanInput source="enable" validate={[required()]}/>
        </SimpleForm>
    </Create>
);

export default UserCreate;