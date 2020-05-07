import React from 'react';
import {
    ReferenceInput,
    AutocompleteInput,
} from 'react-admin';

const renderPerson = (choice) => `${choice.identificationNumber} ${choice.lastName} ${choice.firstName}`;
//Así evitamos mostrar valores cuando el input está vacío
const searchToFilter = searchText => ({identificationNumber: searchText ? searchText : -1});

const PeopleReferenceInput = (props) => (
    <ReferenceInput
        reference="people"
        sort={{ field: 'identificationNumber', order: 'ASC' }}
        filterToQuery={searchToFilter}
        {...props}
    >
        {/*TODO: traducir helperText*/}
        <AutocompleteInput optionText={renderPerson} optionValue="id" helperText="Buscar por DNI" />
    </ReferenceInput>
);

export default PeopleReferenceInput;
