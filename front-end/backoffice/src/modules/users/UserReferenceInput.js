import React from 'react';
import {
    ReferenceInput,
    AutocompleteInput,
} from 'react-admin';

const renderUser = (choice) => choice ? `${choice.username}` : '';

const UserReferenceInput = (props) => (
    <ReferenceInput
        reference="users"
        sort={{ field: 'username', order: 'ASC' }}
        filterToQuery={searchText => ({username: searchText})}
        {...props}
    >
        <AutocompleteInput optionText={renderUser} optionValue="id" helperText="Buscar por nombre de usuario" resettable />
    </ReferenceInput>
);

export default UserReferenceInput;
