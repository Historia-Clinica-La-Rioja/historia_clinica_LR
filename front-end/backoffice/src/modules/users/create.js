import React from 'react';
import {
    Create,
    SimpleForm,
    ReferenceInput,
    TextInput,
    AutocompleteInput,
    required,
} from 'react-admin';

const renderPerson = (choice) => `${choice.identificationNumber} ${choice.lastName} ${choice.firstName}`;

const UserCreate = props => (
    <Create {...props}>
        <SimpleForm>
            <ReferenceInput
                source="personId"
                reference="people"
                sort={{ field: 'identificationNumber', order: 'ASC' }}
                filterToQuery={searchText => ( searchText ? { identificationNumber: searchText } : '')}
                validate={[required()]}
            >
                <AutocompleteInput optionText={renderPerson} optionValue="id"/>
            </ReferenceInput>
            <TextInput source="username" validate={[required()]}/>
        </SimpleForm>
    </Create>
);

export default UserCreate;