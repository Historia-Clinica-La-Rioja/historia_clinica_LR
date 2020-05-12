import React from 'react';
import {
    TextInput,
    ReferenceInput,
    AutocompleteInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';

const SectorCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="description" validate={[required()]} />
            <ReferenceInput
                source="institutionId"
                reference="institutions"
                sort={{ field: 'name', order: 'ASC' }}
                filterToQuery={searchText => ({name: searchText})}                
            >
                <AutocompleteInput optionText="name" optionValue="id" options={{ disabled: true }}/>
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default SectorCreate;
