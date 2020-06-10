import React from 'react';
import {
    TextInput,
    ReferenceInput,
    AutocompleteInput,
    Edit,
    SimpleForm,
    required
} from 'react-admin';

const SectorEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="description" validate={[required()]} />
            <ReferenceInput
                source="institutionId"
                reference="institutions"
                sort={{ field: 'name', order: 'ASC' }}
                filterToQuery={searchText => ({name: searchText})}                
            >
                <AutocompleteInput optionText="name" optionValue="id"/>
            </ReferenceInput>
        </SimpleForm>
    </Edit>
);

export default SectorEdit;
