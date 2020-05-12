import React from 'react';
import {
    Create,
    TextInput,
    ReferenceInput,
    SelectInput,
    AutocompleteInput,
    SimpleForm,
    required,
    BooleanInput,
} from 'react-admin';

const BedCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="bedNumber" validate={[required()]} />
            <ReferenceInput
                source="roomId"
                reference="rooms"
                sort={{ field: 'description', order: 'ASC' }}
                validate={[required()]}
                filterToQuery={searchText => ({description: searchText ? searchText : -1})}                
            >
                <AutocompleteInput optionText="description" optionValue="id" options={{ disabled: true }} />
            </ReferenceInput>
            <ReferenceInput
                source="bedCategoryId"
                reference="bedcategories"
                sort={{ field: 'description', order: 'ASC' }}
                validate={[required()]}
            >
                <SelectInput optionText="description" optionValue="id"/>
            </ReferenceInput>
            <BooleanInput source="enabled" validate={[required()]} disabled={false} initialValue={true}/>
            <BooleanInput source="available" validate={[required()]} disabled={false} initialValue={true}/>
            <BooleanInput source="free" validate={[required()]} disabled={false} initialValue={true}/>
        </SimpleForm>
    </Create>
);

export default BedCreate;

