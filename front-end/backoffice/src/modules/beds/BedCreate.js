import React from 'react';
import {
    Create,
    TextInput,
    ReferenceInput,
    AutocompleteInput,
    SimpleForm,
    required,
    BooleanInput,
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";

const redirect = (basePath, id, data) => `/rooms/${data.roomId}/show`;

const BedCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
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
            <BooleanInput source="enabled" validate={[required()]} disabled={false} initialValue={true}/>
            <BooleanInput source="available" validate={[required()]} disabled={false} initialValue={true}/>
            <BooleanInput source="free" validate={[required()]} disabled={false} initialValue={true}/>
        </SimpleForm>
    </Create>
);

export default BedCreate;

